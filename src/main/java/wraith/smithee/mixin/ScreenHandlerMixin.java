package wraith.smithee.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.items.Whetstone;
import wraith.smithee.items.tools.BaseSmitheeItem;
import wraith.smithee.utils.Utils;

import java.util.List;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow
    @Final
    public List<Slot> slots;

    @Shadow
    public abstract void sendContentUpdates();

    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    public void onSlotClick(int screenStackIndex, int playerStackIndex, SlotActionType actionType, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (screenStackIndex < 0 || playerStackIndex < 0) {
            return;
        }
        ItemStack playerStack = player.inventory.getCursorStack();
        CompoundTag tag = playerStack.getTag();
        if (tag == null) {
            return;
        }
        CompoundTag smitheeTag = tag.getCompound("SmitheeProperties");
        if (playerStack.isEmpty() || !(playerStack.getItem() instanceof BaseSmitheeItem) || smitheeTag == null || ((!smitheeTag.contains("isBroken") || !smitheeTag.getBoolean("isBroken")) && playerStack.getDamage() == 0)) {
            return;
        }
        String material = tag.getCompound("Parts").getString("HeadPart");
        if (actionType == SlotActionType.PICKUP && !slots.get(screenStackIndex).getStack().isEmpty() && slots.get(screenStackIndex).getStack().getItem() instanceof Whetstone && ((Whetstone) slots.get(screenStackIndex).getStack().getItem()).getMaterial().equals(material)) {
            ItemStack whetstoneStack = slots.get(screenStackIndex).getStack();
            int toolDamage = player.inventory.getCursorStack().getDamage();
            if (smitheeTag.contains("isBroken") && smitheeTag.getBoolean("isBroken")) {
                toolDamage = whetstoneStack.getMaxDamage();
            }
            int repairAmount = toolDamage;
            if (whetstoneStack.getDamage() + toolDamage > whetstoneStack.getMaxDamage()) {
                repairAmount = whetstoneStack.getMaxDamage() - whetstoneStack.getDamage();
            }
            Utils.damage(slots.get(screenStackIndex).getStack(), repairAmount);
            if (smitheeTag.contains("isBroken") && smitheeTag.getBoolean("isBroken")) {
                smitheeTag.putBoolean("isBroken", false);
            }
            player.inventory.getCursorStack().setDamage(toolDamage - repairAmount);
            sendContentUpdates();
            cir.setReturnValue(player.inventory.getCursorStack());
            cir.cancel();
        }
    }

}
