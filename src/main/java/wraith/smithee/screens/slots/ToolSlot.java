package wraith.smithee.screens.slots;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import wraith.smithee.items.tools.BaseSmitheeItem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ToolSlot extends Slot {

    private final boolean smithy;
    private final Class<?> useClass;
    private Set<Item> compatibleTools = new HashSet<>();

    public ToolSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.useClass = null;
        this.smithy = true;
    }

    public ToolSlot(Inventory inventory, int index, int x, int y, Set<Item> items) {
        super(inventory, index, x, y);
        this.smithy = false;
        this.useClass = null;
        this.compatibleTools = items;
    }

    public ToolSlot(Inventory inventory, int index, int x, int y, Item... items) {
        super(inventory, index, x, y);
        this.useClass = null;
        Collections.addAll(compatibleTools, items);
        this.smithy = false;
    }

    public ToolSlot(Inventory inventory, int index, int x, int y, Identifier id) {
        super(inventory, index, x, y);
        this.useClass = null;
        compatibleTools.addAll(TagRegistry.item(id).values());
        this.smithy = false;
    }

    public <T> ToolSlot(Inventory inventory, int index, int x, int y, Class<T> tClass) {
        super(inventory, index, x, y);
        this.useClass = tClass;
        this.smithy = false;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return (smithy && stack.getItem() instanceof BaseSmitheeItem) || (compatibleTools.contains(stack.getItem()) || useClass == null || useClass.isInstance(stack.getItem()));
    }

}
