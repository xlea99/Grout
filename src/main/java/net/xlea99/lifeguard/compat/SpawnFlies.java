package net.xlea99.lifeguard.compat;

import com.ninni.spawn.registry.SpawnAttachments;
import com.ninni.spawn.server.gui.fly.FlyData;
import com.ninni.spawn.server.network.FlySyncPacket;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.xlea99.guppylib.sync.Payloads;

/**
 * Taking one of Spawn's flies off something, the one way every entry does it, so that frogs and flytraps can't come to disagree about the rules. Spawn's flies are records attached to a
 * mob or a player, with no body of their own. Only loaded by entries that need Spawn.
 */
public final class SpawnFlies {
    private SpawnFlies() {
    }

    /** What can be eaten: never named ones, as Spawn spares them from fire and drowning, or swatted ones, which are only still listed for a moment. */
    public static List<FlyData> edible(LivingEntity carrier) {
        List<FlyData> edible = new ArrayList<>();
        if (carrier.hasData(SpawnAttachments.FLIES)) {
            for (FlyData fly : carrier.getData(SpawnAttachments.FLIES).getFlies()) {
                if (!fly.dead && fly.customName == null)
                    edible.add(fly);
            }
        }
        return edible;
    }

    /** @return whether one went: a player's own screen is told, since their flies are drawn there */
    public static boolean eatOne(LivingEntity carrier, RandomSource random) {
        List<FlyData> edible = edible(carrier);
        if (edible.isEmpty())
            return false;
        List<FlyData> flies = carrier.getData(SpawnAttachments.FLIES).getFlies();
        flies.remove(edible.get(random.nextInt(edible.size())));
        if (carrier instanceof ServerPlayer player)
            Payloads.send(player, new FlySyncPacket(flies));
        return true;
    }
}
