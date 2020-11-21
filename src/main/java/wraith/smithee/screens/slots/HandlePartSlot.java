package wraith.smithee.screens.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import wraith.smithee.items.tool_parts.ToolPartItem;

public class HandlePartSlot extends Slot {

    public HandlePartSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof ToolPartItem && "handle".equals(((ToolPartItem)stack.getItem()).part.partType);
    }

}
