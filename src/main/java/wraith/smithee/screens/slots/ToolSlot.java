package wraith.smithee.screens.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import wraith.smithee.items.tools.BaseSmitheeTool;

public class ToolSlot extends Slot {

    public ToolSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof BaseSmitheeTool;
    }

}
