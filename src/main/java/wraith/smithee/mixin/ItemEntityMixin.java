package wraith.smithee.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.properties.Trait;

import java.util.HashMap;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();

    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        HashMap<String, Object> result = Trait.evaluateTraits(getStack(), null, null, null, null, null, "ItemEntity#isFireImmune");
        if (result.containsKey("Fire Immunity")) {
            cir.setReturnValue((Boolean) result.get("Fire Immunity"));
            cir.cancel();
        }
    }

}
