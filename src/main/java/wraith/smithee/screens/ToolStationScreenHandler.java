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
import wraith.smithee.blocks.ToolStationBlockEntity;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.screens.slots.BindingPartSlot;
import wraith.smithee.screens.slots.HandlePartSlot;
import wraith.smithee.screens.slots.HeadPartSlot;
import wraith.smithee.screens.slots.ToolStationOutputSlot;

public class ToolStationScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    public ToolStationScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(4), ScreenHandlerContext.EMPTY);
    }

    public ToolStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.SCREEN_HANDLERS.get("tool_station"), syncId);
        this.inventory = inventory;
        this.context = context;
        this.player = playerInventory.player;
        if (inventory instanceof ToolStationBlockEntity) {
            ((ToolStationBlockEntity)inventory).setHandler(this);
        }
        this.addSlot(new HandlePartSlot(inventory, 0, 26, 57)); //Handle
        this.addSlot(new BindingPartSlot(inventory, 1, 48, 35)); //Binding
        this.addSlot(new HeadPartSlot(inventory, 2, 70, 13)); //Head
        this.addSlot(new ToolStationOutputSlot(inventory, 3, 124, 35)); //Output

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
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

    protected static void updateResult(int syncId, World world, PlayerEntity player, Inventory inventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            ItemStack itemStack;
            if (inventory.getStack(0).isEmpty() ||
                inventory.getStack(1).isEmpty() ||
                inventory.getStack(2).isEmpty()) {
                return;
            }

            ToolPartItem handle = (ToolPartItem)inventory.getStack(0).getItem();
            ToolPartItem binding = (ToolPartItem)inventory.getStack(1).getItem();
            ToolPartItem head = (ToolPartItem)inventory.getStack(2).getItem();

            CompoundTag tag;

            switch(head.part.toolType) {
                case "pickaxe":
                    itemStack = new ItemStack(ItemRegistry.ITEMS.get("base_smithee_pickaxe"));
                    tag = itemStack.getOrCreateSubTag("Parts");
                    tag.putString("HeadPart", head.part.materialName);
                    tag.putString("BindingPart", binding.part.materialName);
                    tag.putString("HandlePart", handle.part.materialName);
                    itemStack.putSubTag("Parts", tag);
                    break;
                case "shovel":
                    itemStack = new ItemStack(ItemRegistry.ITEMS.get("base_smithee_shovel"));
                    tag = itemStack.getOrCreateSubTag("Parts");
                    tag.putString("HeadPart", head.part.materialName);
                    tag.putString("BindingPart", binding.part.materialName);
                    tag.putString("HandlePart", handle.part.materialName);
                    itemStack.putSubTag("Parts", tag);
                    break;
                case "axe":
                    itemStack = new ItemStack(ItemRegistry.ITEMS.get("base_smithee_axe"));
                    tag = itemStack.getOrCreateSubTag("Parts");
                    tag.putString("HeadPart", head.part.materialName);
                    tag.putString("BindingPart", binding.part.materialName);
                    tag.putString("HandlePart", handle.part.materialName);
                    itemStack.putSubTag("Parts", tag);
                    break;
                case "sword":
                    itemStack = new ItemStack(ItemRegistry.ITEMS.get("base_smithee_sword"));
                    tag = itemStack.getOrCreateSubTag("Parts");
                    tag.putString("HeadPart", head.part.materialName);
                    tag.putString("BindingPart", binding.part.materialName);
                    tag.putString("HandlePart", handle.part.materialName);
                    itemStack.putSubTag("Parts", tag);
                    break;
                case "hoe":
                    itemStack = new ItemStack(ItemRegistry.ITEMS.get("base_smithee_hoe"));
                    tag = itemStack.getOrCreateSubTag("Parts");
                    tag.putString("HeadPart", head.part.materialName);
                    tag.putString("BindingPart", binding.part.materialName);
                    tag.putString("HandlePart", handle.part.materialName);
                    itemStack.putSubTag("Parts", tag);
                    break;
                default:
                    itemStack = ItemStack.EMPTY;
                    break;
            }
            inventory.setStack(3, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 3, itemStack));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, blockPos) -> {
            updateResult(this.syncId, world, this.player, this.inventory);
        });
        super.onContentChanged(inventory);
    }



}