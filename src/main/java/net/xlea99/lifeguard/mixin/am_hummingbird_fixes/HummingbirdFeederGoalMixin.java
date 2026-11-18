package net.xlea99.lifeguard.mixin.am_hummingbird_fixes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * {@code canUse} counts {@code runCooldown} down, then falls through to the line that sets it back to 400-999, so it never
 * reaches zero. Returning right after the countdown step lets it run out. In {@code canUse} the field is read twice before
 * that step writes it ({@code runCooldown > 0}, then {@code runCooldown--}), so the step's write is the third use; the reset
 * is the fourth. Targeted by name: Lifeguard isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityHummingbird$AIUseFeeder")
public abstract class HummingbirdFeederGoalMixin {
    @Inject(method = "canUse", cancellable = true, at = @At(value = "FIELD", shift = At.Shift.AFTER, ordinal = 2,
        target = "Lcom/github/alexthe666/alexsmobs/entity/EntityHummingbird$AIUseFeeder;runCooldown:I"))
    private void lifeguard$keepCountingDown(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
