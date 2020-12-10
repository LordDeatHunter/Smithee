package wraith.smithee.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.properties.Trait;

import java.util.HashMap;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "hasAquaAffinity", at = @At("HEAD"), cancellable = true)
    private static void hasAquaAffinity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        HashMap<String, Object> result = Trait.evaluateTraits(entity.getMainHandStack(), null, null, null, null, null, "EnchantmentHelper#hasAquaAffinity");
        if (result.containsKey("Has Aqua Affinity")) {
            cir.setReturnValue((Boolean) result.get("Has Aqua Affinity"));
            cir.cancel();
        }
    }

}
