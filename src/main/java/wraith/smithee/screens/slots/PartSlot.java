package wraith.smithee.screens.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import wraith.smithee.items.tool_parts.ToolPartItem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class PartSlot extends Slot {

    private final HashSet<String> types = new HashSet<>();

    public PartSlot(Inventory inventory, int index, int x, int y, String... types) {
        super(inventory, index, x, y);
        Collections.addAll(this.types, types);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof ToolPartItem && types.contains(((ToolPartItem)stack.getItem()).part.partType);
    }

}
