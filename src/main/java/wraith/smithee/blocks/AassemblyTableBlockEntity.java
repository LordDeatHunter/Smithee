package wraith.smithee.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import wraith.smithee.Smithee;
import wraith.smithee.registry.BlockEntityRegistry;
import wraith.smithee.screens.ImplementedInventory;
import wraith.smithee.screens.AssemblyTableScreenHandler;

public class AassemblyTableBlockEntity extends LockableContainerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    private AssemblyTableScreenHandler handler;

    public AassemblyTableBlockEntity() {
        super(BlockEntityRegistry.BLOCK_ENTITIES.get("assembly_table"));
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        this.handler = new AssemblyTableScreenHandler(syncId, playerInventory, this, ScreenHandlerContext.EMPTY);
        return handler;
    }

    @Override
    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return this.createMenu(syncId, playerInventory, playerInventory.player);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("container." + Smithee.MOD_ID + ".assembly_table");
    }

    @Override
    public Text getContainerName() {
        return this.getDisplayName();
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, this.inventory);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        boolean update = false;
        if (slot < 3) {
            if (!result.isEmpty()) {
                markDirty();
            }
            else {
                update = true;
            }
        }
        if (update || slot == 3) {
            handler.onContentChanged(this);
        }
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack oldStack = getItems().get(slot).copy();
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        boolean oldIsEmpty = oldStack == ItemStack.EMPTY;
        boolean newIsEmpty = stack == ItemStack.EMPTY;

        if (slot != 3 || (!oldIsEmpty && newIsEmpty)) {
            handler.onContentChanged(this);
        }
        markDirty();
    }

    public void setHandler(AssemblyTableScreenHandler handler) {
        this.handler = handler;
    }

}
