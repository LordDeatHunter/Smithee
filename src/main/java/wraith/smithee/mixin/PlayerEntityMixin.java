package wraith.smithee.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import wraith.smithee.properties.Trait;

import java.util.HashMap;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @ModifyArgs(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private void attack(Args args, Entity target) {
        HashMap<String, Object> result = Trait.evaluateTraits(((LivingEntity)(Object)this).getMainHandStack(), null, null, null, null, null, "MobEntity#tryAttack");
        if (result.containsKey("Attack Damage Amount")) {
            args.set(1, (float)args.get(1) + (float)result.get("Attack Damage Amount"));
        }
    }

    @ModifyArgs(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private void sweepAttack(Args args, Entity target) {
        HashMap<String, Object> result = Trait.evaluateTraits(((LivingEntity)(Object)this).getMainHandStack(), null, null, null, null, null, "MobEntity#tryAttack");
        if (result.containsKey("Attack Damage Amount")) {
            args.set(1, (float)args.get(1) + (float)result.get("Attack Damage Amount"));
        }
    }

}
