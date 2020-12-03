package wraith.smithee.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.properties.Trait;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();

    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (getStack().getItem() instanceof BaseSmitheeTool && getStack().hasTag() && getStack().getTag().contains("Parts")) {
            CompoundTag tag = getStack().getSubTag("Parts");
            boolean immune = Trait.evaluateSuperheated(tag.getString("HeadPart"), "head");
            immune = immune || Trait.evaluateSuperheated(tag.getString("BindingPart"), "binding");
            immune = immune || Trait.evaluateSuperheated(tag.getString("HandlePart"), "handle");
            cir.setReturnValue(immune);
            cir.cancel();
        }
    }

}
