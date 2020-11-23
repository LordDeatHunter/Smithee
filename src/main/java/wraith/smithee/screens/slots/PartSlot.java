package wraith.smithee.screens.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import wraith.smithee.items.tool_parts.ToolPartItem;

public class PartSlot extends Slot {

    private final String type;

    public PartSlot(Inventory inventory, int index, int x, int y, String type) {
        super(inventory, index, x, y);
        this.type = type;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof ToolPartItem && type.equals(((ToolPartItem)stack.getItem()).part.partType);
    }

}
