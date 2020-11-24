package wraith.smithee.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import wraith.smithee.blocks.AssemblyTableEntity;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.properties.Properties;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.screens.slots.PartSlot;
import wraith.smithee.screens.slots.AssemblyTableOutputSlot;

public class AssemblyTableScreenHandler extends ScreenHandler {

    private Inventory inventory;
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private ItemStack result;

    public AssemblyTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(4), ScreenHandlerContext.EMPTY);
    }

    public AssemblyTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.SCREEN_HANDLERS.get("assembly_table"), syncId);
        this.inventory = inventory;
        this.context = context;
        this.player = playerInventory.player;
        if (inventory instanceof AssemblyTableEntity) {
            ((AssemblyTableEntity)inventory).setHandler(this);
        }
        this.addSlot(new PartSlot(inventory, 0, 43, 57, "handle")); //Handle
        this.addSlot(new PartSlot(inventory, 1, 55, 37, "binding")); //Binding
        this.addSlot(new PartSlot(inventory, 2, 67, 17, "head")); //Head
        this.addSlot(new AssemblyTableOutputSlot(inventory, 3, 113, 38)); //Output

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
        this.result = inventory.getStack(3);
    }

    @Override
    public boolean canUse(PlayerEntity playerEntity) {
        return this.inventory.canPlayerUse(playerEntity);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < 4) { //If the click is inside the tool station, transfer it to the player
                onContentChanged(inventory);
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() instanceof ToolPartItem) {
                ToolPartItem tool = (ToolPartItem) originalStack.getItem();
                if (("handle".equals(tool.part.partType) && !this.insertItem(originalStack, 0, 1, false)) ||
                    ("binding".equals(tool.part.partType) && !this.insertItem(originalStack, 1, 2, false)) ||
                    ("head".equals(tool.part.partType) && !this.insertItem(originalStack, 2, 3, false))) {
                        return ItemStack.EMPTY;
                }
            } else if (invSlot < this.slots.size() - 9) {
                //Inventory to hotbar
                if (!this.insertItem(originalStack, this.slots.size() - 9, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size() - 4 - 9, false)) {
                //Hotbar to inventory
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    public void updateResult(int syncId, World world, PlayerEntity player, Inventory inventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            if (inventory.getStack(3) == ItemStack.EMPTY && result != ItemStack.EMPTY) {
                result = inventory.getStack(3).copy();
                for (int i = 0; i < 3; i++) {
                    ItemStack itemStack = inventory.removeStack(i, 1);
                    serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, i, itemStack));
                }
            }

            if (inventory.getStack(0).isEmpty() ||
                inventory.getStack(1).isEmpty() ||
                inventory.getStack(2).isEmpty()) {
                    result = ItemStack.EMPTY;
                    inventory.setStack(3, ItemStack.EMPTY);
                    serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 3, ItemStack.EMPTY));
                    return;
            }

            ToolPartItem handle = (ToolPartItem)inventory.getStack(0).getItem();
            ToolPartItem binding = (ToolPartItem)inventory.getStack(1).getItem();
            ToolPartItem head = (ToolPartItem)inventory.getStack(2).getItem();

            ItemStack itemStack = new ItemStack(ItemRegistry.ITEMS.get("base_smithee_" + head.part.toolType));
            CompoundTag tag = itemStack.getOrCreateSubTag("Parts");
            tag.putString("HeadPart", head.part.materialName);
            tag.putString("BindingPart", binding.part.materialName);
            tag.putString("HandlePart", handle.part.materialName);
            itemStack.putSubTag("Parts", tag);

            Properties.setProperties(itemStack, Properties.getProperties(itemStack));

            result = itemStack.copy();
            inventory.setStack(3, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 3, itemStack));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.inventory = inventory;
        this.context.run((world, blockPos) -> {
            updateResult(this.syncId, world, this.player, inventory);
        });
        super.onContentChanged(inventory);
    }
}