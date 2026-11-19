package net.xlea99.grout.mixin.cauldron_fourths_supplementaries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** A cauldron moved into water (by rope or piston, with Supplementaries' moved_cauldron_filling) fills to four levels. */
@Mixin(targets = "net.mehvahdjukaar.supplementaries.common.block.cauldron.MovedFluidFiller")
public abstract class MovedFluidFillerMixin {

    @ModifyConstant(method = "getStateMovedInto", constant = @Constant(intValue = 3))
    private static int grout$fullAtFour(int full) {
        return 4;
    }
}
