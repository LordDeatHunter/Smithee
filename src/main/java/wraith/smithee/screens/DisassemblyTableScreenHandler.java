package wraith.smithee.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import wraith.smithee.items.tools.BaseSmitheeSword;
import wraith.smithee.utils.Utils;
import wraith.smithee.blocks.DisassemblyTableBlockEntity;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.screens.slots.ToolOutputSlot;
import wraith.smithee.screens.slots.ToolSlot;

public class DisassemblyTableScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public DisassemblyTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(4));
    }

    public DisassemblyTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerRegistry.SCREEN_HANDLERS.get("disassembly_table"), syncId);
        this.inventory = inventory;
        if (inventory instanceof DisassemblyTableBlockEntity) {
            ((DisassemblyTableBlockEntity) inventory).setHandler(this);
        }
        this.addSlot(new ToolSlot(inventory, 0, 80, 49)); //Output
        this.addSlot(new ToolOutputSlot(inventory, 1, 80, 19)); //Head
        this.addSlot(new ToolOutputSlot(inventory, 2, 51, 63)); //Binding
        this.addSlot(new ToolOutputSlot(inventory, 3, 109, 63)); //Handle

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 94 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 152));
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
                onContentChanged(inventory);
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() instanceof BaseSmitheeTool) {
                if (!this.insertItem(originalStack, 0, 1, false)) {
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

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (inventory.getStack(0).getItem() instanceof BaseSmitheeTool) {
            ServerPlayerEntity serverPlayerEntity;
            if (player instanceof ServerPlayerEntity) {
                serverPlayerEntity = (ServerPlayerEntity) player;
            } else {
                return false;
            }

            if (!inventory.getStack(1).isEmpty() || !inventory.getStack(2).isEmpty() || !inventory.getStack(3).isEmpty()) {
                return false;
            }

            ItemStack stack = inventory.getStack(0);
            CompoundTag tag = stack.getTag();
            if (!tag.contains("Parts")) {
                return false;
            }
            tag = tag.getCompound("Parts");
            String tool = Utils.getToolType(stack.getItem());
            String bindingType = stack.getItem() instanceof BaseSmitheeSword ? "_sword_guard" : "_binding";
            ItemStack head = new ItemStack(ItemRegistry.ITEMS.get(tag.getString("HeadPart") + "_" + tool + "_head"));
            ItemStack binding = new ItemStack(ItemRegistry.ITEMS.get(tag.getString("BindingPart") + bindingType));
            ItemStack handle = new ItemStack(ItemRegistry.ITEMS.get(tag.getString("HandlePart") + "_handle"));

            int headDurability = ItemRegistry.PROPERTIES.get(tag.getString("HeadPart")).partProperties.get("head").durability;
            int bindingDurability = ItemRegistry.PROPERTIES.get(tag.getString("BindingPart")).partProperties.get("binding").durability;
            int handleDurability = ItemRegistry.PROPERTIES.get(tag.getString("HandlePart")).partProperties.get("handle").durability;
            int maxDurability = stack.getMaxDamage();
            int summedDurability = headDurability + bindingDurability + handleDurability;
            int currentDamage = stack.getDamage();

            double adjustedHeadDurability = (double)(headDurability * maxDurability) / (double)summedDurability;
            double adjustedBindingDurability = (double)(bindingDurability * maxDurability) / (double)summedDurability;
            double adjustedHandleDurability = (double)(handleDurability * maxDurability) / (double)summedDurability;

            double damage;

            damage = (double)currentDamage / (double)maxDurability;
            head.getTag().putDouble("PartDamage", damage);
            Utils.setDamage(head, (int) (damage * adjustedHeadDurability));

            damage = (double)currentDamage / (double)maxDurability;
            binding.getTag().putDouble("PartDamage", damage);
            Utils.setDamage(binding, (int) (damage * adjustedBindingDurability));

            damage = (double)currentDamage / (double)maxDurability;
            handle.getTag().putDouble("PartDamage", damage);
            Utils.setDamage(handle, (int) (damage * adjustedHandleDurability));

            inventory.setStack(0, ItemStack.EMPTY);
            inventory.setStack(1, head);
            inventory.setStack(2, binding);
            inventory.setStack(3, handle);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, ItemStack.EMPTY));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 1, head));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 2, binding));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 3, handle));
        }
        return true;
    }

}