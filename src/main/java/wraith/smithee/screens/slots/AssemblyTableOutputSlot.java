package wraith.smithee.screens.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class AssemblyTableOutputSlot extends Slot {

    private final boolean insert;

    public AssemblyTableOutputSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.insert = false;
    }

    public AssemblyTableOutputSlot(Inventory inventory, int index, int x, int y, boolean insert) {
        super(inventory, index, x, y);
        this.insert = insert;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.insert;
    }

}
