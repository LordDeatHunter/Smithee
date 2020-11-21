package wraith.smithee.screens.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ToolStationOutputSlot extends Slot {

    public ToolStationOutputSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(int amount) {
        inventory.removeStack(0, 1);
        inventory.removeStack(1, 1);
        inventory.removeStack(2, 1);
    }

}
