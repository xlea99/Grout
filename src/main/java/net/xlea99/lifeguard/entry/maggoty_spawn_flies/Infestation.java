package net.xlea99.lifeguard.entry.maggoty_spawn_flies;

import com.ninni.spawn.registry.SpawnAttachments;
import com.ninni.spawn.registry.SpawnSoundEvents;
import com.ninni.spawn.server.gui.fly.FlyCapabilityProvider;
import com.ninni.spawn.server.gui.fly.FlyData;
import com.ninni.spawn.server.network.FlySyncPacket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.xlea99.guppylib.sync.Payloads;
import net.xlea99.lifeguard.entry.spawn_stew_repels_flies.Repellent;

/**
 * What flies and maggots do to one player ({@link MaggotySpawnFlies}). How many maggots there are never changes how often they eat, so flies grow slowly
 * however bad it gets. It only ever touches the 36 slots of the inventory, so anything in a chest, or a bundle or backpack, is safe.
 *
 * <p>Those slots are treated as the grid they look like on screen, with the hotbar as its bottom row, so the bottom row of the inventory borders the hotbar.
 */
public final class Infestation {
    /** How often this looks at a player, in ticks. */
    private static final int CHECK_EVERY = 20;
    /** Minutes per maggot are {@code LEAST + MORE / flies ^ POWER}: 21 for 1 fly, about 5 for 5, and it levels off at 3. */
    private static final double MINUTES_LEAST = 3D;
    private static final double MINUTES_MORE = 18D;
    private static final double MINUTES_POWER = 1.4D;
    /** How long between tries at a meal, in ticks: 3 to 7 minutes. */
    private static final int MEAL_LEAST = 3 * 60 * 20;
    private static final int MEAL_MOST = 7 * 60 * 20;
    /** The chance a meal makes a fly. */
    private static final float MEAL_MAKES_FLY = 0.5F;

    private static final int COLUMNS = 9;
    private static final int ROWS = 4;
    private static final int HOTBAR_ROW = 3;
    private static final int SLOTS = COLUMNS * ROWS;

    private static final DeferredHolder<Item, Item> MAGGOT = DeferredHolder.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("alexsmobs", "maggot"));
    private static final TagKey<Item> CANT_EAT = ItemTags.create(ResourceLocation.fromNamespaceAndPath("lifeguard", "flies_cant_eat"));
    private static final SoundEvent PLOP = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("lifeguard", "maggot.plop"));
    private static final SoundEvent SQUIRM = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("lifeguard", "maggot.squirm"));

    /** When each player's maggots next try for a meal. Only the server thread uses it, and starting over after a relog does no harm. */
    private static final Map<ServerPlayer, Long> NEXT_MEAL = new WeakHashMap<>();

    private Infestation() {
    }

    public static void tick(ServerPlayer player) {
        if (player.tickCount % CHECK_EVERY != 0 || !player.isAlive() || player.isSpectator())
            return;
        int flies = flies(player);
        if (flies > 0 && !Repellent.on(player) && player.getRandom().nextDouble() < CHECK_EVERY / (minutesPerMaggot(flies) * 60D * 20D))
            layMaggot(player);
        long now = player.level().getGameTime();
        Long next = NEXT_MEAL.get(player);
        if (next == null || now >= next) {
            if (next != null)
                tryMeal(player);
            NEXT_MEAL.put(player, now + Mth.nextInt(player.getRandom(), MEAL_LEAST, MEAL_MOST));
        }
    }

    public static double minutesPerMaggot(int flies) {
        return MINUTES_LEAST + MINUTES_MORE / Math.pow(flies, MINUTES_POWER);
    }

    /** Every 5 flies on a mob that dies is a maggot, and what's left over is the chance of one more: 7 flies are 1, and 40% of a second. So on average a fifth as many maggots as flies. */
    private static final int FLIES_PER_MAGGOT = 5;

    /** Mobs only: a player's unnamed flies are already lost when they die. Each maggot is its own item, so they spill out. */
    public static void dropMaggots(LivingEntity mob, Collection<ItemEntity> drops) {
        if (mob instanceof Player || !(mob.level() instanceof ServerLevel level) || !level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
            return;
        int flies = flies(mob);
        int maggots = flies / FLIES_PER_MAGGOT + (mob.getRandom().nextInt(FLIES_PER_MAGGOT) < flies % FLIES_PER_MAGGOT ? 1 : 0);
        for (int i = 0; i < maggots; i++)
            drops.add(new ItemEntity(level, mob.getX(), mob.getY() + mob.getBbHeight() * 0.5D, mob.getZ(), new ItemStack(MAGGOT.value())));
    }

    /** Flies that have been swatted stay in Spawn's list for a moment. */
    private static int flies(LivingEntity carrier) {
        if (!carrier.hasData(SpawnAttachments.FLIES))
            return 0;
        int alive = 0;
        for (FlyData fly : carrier.getData(SpawnAttachments.FLIES).getFlies()) {
            if (!fly.dead)
                alive++;
        }
        return alive;
    }

    /** Puts one maggot beside food if it can, in an empty slot or on a stack with room, or else in any such slot. With no room anywhere, nothing happens. */
    public static void layMaggot(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        List<Integer> besideFood = new ArrayList<>();
        List<Integer> anywhere = new ArrayList<>();
        for (int slot = 0; slot < SLOTS; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && !(stack.is(MAGGOT.value()) && stack.getCount() < stack.getMaxStackSize()))
                continue;
            anywhere.add(slot);
            for (int neighbor : neighbors(slot)) {
                if (edible(inventory.getItem(neighbor), player)) {
                    besideFood.add(slot);
                    break;
                }
            }
        }
        List<Integer> choices = besideFood.isEmpty() ? anywhere : besideFood;
        if (choices.isEmpty())
            return;
        int slot = choices.get(player.getRandom().nextInt(choices.size()));
        ItemStack stack = inventory.getItem(slot);
        if (stack.isEmpty())
            inventory.setItem(slot, new ItemStack(MAGGOT.value()));
        else
            stack.grow(1);
        inventory.setChanged();
        player.level().playSound(null, player.blockPosition(), PLOP, SoundSource.PLAYERS, 1F, 1F);
    }

    /** Eats one food that borders a maggot, if any does, and sometimes makes a fly of it. */
    public static void tryMeal(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        List<Integer> meals = new ArrayList<>();
        for (int slot = 0; slot < SLOTS; slot++) {
            if (!inventory.getItem(slot).is(MAGGOT.value()))
                continue;
            for (int neighbor : neighbors(slot)) {
                if (edible(inventory.getItem(neighbor), player) && !meals.contains(neighbor))
                    meals.add(neighbor);
            }
        }
        if (meals.isEmpty())
            return;
        eat(player, meals.get(player.getRandom().nextInt(meals.size())));
        player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.4F, 1.6F);
        if (!Repellent.on(player) && player.getRandom().nextFloat() < MEAL_MAKES_FLY)
            addFly(player);
    }

    /** Food that leaves something behind, a stew's bowl, does here too: in its slot if that's now empty. */
    private static void eat(ServerPlayer player, int slot) {
        Inventory inventory = player.getInventory();
        ItemStack food = inventory.getItem(slot);
        FoodProperties properties = food.getFoodProperties(player);
        ItemStack left = properties != null && properties.usingConvertsTo().isPresent() ? properties.usingConvertsTo().get().copy() : food.getCraftingRemainingItem();
        food.shrink(1);
        if (food.isEmpty())
            inventory.setItem(slot, left);
        else if (!left.isEmpty() && !inventory.add(left))
            player.drop(left, false);
        inventory.setChanged();
    }

    private static void addFly(ServerPlayer player) {
        FlyCapabilityProvider flies = player.getData(SpawnAttachments.FLIES);
        flies.getFlies().add(new FlyData());
        Payloads.send(player, new FlySyncPacket(flies.getFlies()));
        player.level().playSound(null, player.blockPosition(), SQUIRM, SoundSource.PLAYERS, 1F, 1F);
        player.level().playSound(null, player.blockPosition(), SpawnSoundEvents.FLY_BUZZ.get(), SoundSource.PLAYERS, 0.5F, 1F);
    }

    private static boolean edible(ItemStack stack, ServerPlayer player) {
        return !stack.isEmpty() && !stack.is(CANT_EAT) && stack.getFoodProperties(player) != null;
    }

    /** The slots directly above, below and beside one, never around the edges. */
    public static List<Integer> neighbors(int slot) {
        int row = slot < COLUMNS ? HOTBAR_ROW : (slot - COLUMNS) / COLUMNS;
        int column = slot % COLUMNS;
        List<Integer> neighbors = new ArrayList<>(4);
        if (row > 0)
            neighbors.add(slotAt(row - 1, column));
        if (row < HOTBAR_ROW)
            neighbors.add(slotAt(row + 1, column));
        if (column > 0)
            neighbors.add(slotAt(row, column - 1));
        if (column < COLUMNS - 1)
            neighbors.add(slotAt(row, column + 1));
        return neighbors;
    }

    private static int slotAt(int row, int column) {
        return row == HOTBAR_ROW ? column : COLUMNS + row * COLUMNS + column;
    }
}
