package wraith.smithee.screens.slots;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import wraith.smithee.items.tools.BaseSmitheeTool;

import java.util.Collections;
import java.util.HashSet;

public class ToolSlot extends Slot {

    private final boolean smithy;
    private final HashSet<Item> compatibleTools = new HashSet<>();

    public ToolSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.smithy = true;
    }

    public ToolSlot(Inventory inventory, int index, int x, int y, Item... items) {
        super(inventory, index, x, y);
        Collections.addAll(compatibleTools, items);
        this.smithy = false;
    }

    public ToolSlot(Inventory inventory, int index, int x, int y, Identifier id) {
        super(inventory, index, x, y);
        compatibleTools.addAll(TagRegistry.item(id).values());
        this.smithy = false;
    }
    @Override
    public boolean canInsert(ItemStack stack) {
        return (smithy && stack.getItem() instanceof BaseSmitheeTool) || (compatibleTools.contains(stack.getItem()));
    }

}
