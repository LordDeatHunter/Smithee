package wraith.smithee.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.registry.StatusEffectRegistry;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {

    @Inject(method = "canApplyUpdateEffect", at = @At("HEAD"), cancellable = true)
    public void canApplyUpdateEffect(int duration, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        int k;
        StatusEffect effect = ((StatusEffect) (Object) this);
        /*if (effect == StatusEffectRegistry.STATUS_EFFECTS.get("bleeding")) {
            k = 50 >> amplifier;
            if (k > 0) {
                cir.setReturnValue(duration % k == 0);
            } else {
                cir.setReturnValue(true);
            }
            cir.cancel();
        } else */
        if (effect == StatusEffectRegistry.STATUS_EFFECTS.get("frostbite")) {
            k = 40 >> amplifier;
            if (k > 0) {
                cir.setReturnValue(duration % k == 0);
            } else {
                cir.setReturnValue(true);
            }
            cir.cancel();
        }
    }

    @Inject(method = "applyUpdateEffect", at = @At("HEAD"))
    public void applyUpdateEffect(LivingEntity entity, int amplifier, CallbackInfo ci) {
        StatusEffect effect = ((StatusEffect) (Object) this);
        if (effect == StatusEffectRegistry.STATUS_EFFECTS.get("frostbite")) {
            entity.damage(DamageSource.MAGIC, 0.5F);
        }
    }

}
