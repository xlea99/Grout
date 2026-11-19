package net.xlea99.grout.mixin.neapolitan_plantain_spider_bundles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "com.teamabnormals.neapolitan.common.block.BananaBundleBlock")
public abstract class BananaBundleBlockMixin {
    /** The enchantment tag is "prevents spider spawns", and the bundle spawns one only with it. */
    @ModifyExpressionValue(method = "spawnAfterBreak", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;hasTag(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/tags/TagKey;)Z"))
    private boolean grout$preventsMeansPrevents(boolean hasTag) {
        return !hasTag;
    }

    /** The second and third spiders are the first one added again: each gets its own entity. */
    @WrapOperation(method = "spawnSpider", at = {
        @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 1),
        @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 2)})
    private boolean grout$anotherSpider(ServerLevel level, Entity spider, Operation<Boolean> original) {
        Entity another = spider.getType().create(level);
        if (another == null)
            return false;
        another.moveTo(spider.getX(), spider.getY(), spider.getZ(), 0.0F, 0.0F);
        return original.call(level, another);
    }
}
