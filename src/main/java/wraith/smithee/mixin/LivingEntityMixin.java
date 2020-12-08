package wraith.smithee.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import wraith.smithee.properties.Trait;
import wraith.smithee.registry.StatusEffectRegistry;

import java.util.HashMap;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @ModifyArgs(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void applyDamage(Args args, DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getAttacker();
            HashMap<String, Object> results = Trait.evaluateTraits(player.getMainHandStack(), null, null, null, null, player, "LivingEntity#damage");
            if (results.containsKey("Damage Entity Amount")) {
                args.set(1, results.get("Damage Entity Amount"));
            }
            if (results.containsKey("Damage Entity Effect Type") && results.containsKey("Damage Entity Effect Duration")) {
                addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.STATUS_EFFECTS.get(results.get("Damage Entity Effect Type")), (Integer) results.get("Damage Entity Effect Duration")));
            }
        }
    }

}
