package wraith.smithee.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.items.tools.BaseSmitheeItem;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        CompoundTag tag = stack.getSubTag("SmitheeProperties");
        if (this instanceof BaseSmitheeItem && tag != null && tag.contains("isBroken") && tag.getBoolean("isBroken")) {
            cir.setReturnValue(TypedActionResult.pass(stack));
            cir.cancel();
        }
    }

}
