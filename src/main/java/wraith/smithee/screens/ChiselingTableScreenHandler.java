package wraith.smithee.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import wraith.smithee.items.Chisel;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.screens.slots.ToolOutputSlot;
import wraith.smithee.screens.slots.ToolSlot;
import wraith.smithee.utils.Utils;

import java.util.HashMap;

public class ChiselingTableScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate delegate;

    public ChiselingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(2));
    }

    public ChiselingTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ScreenHandlerRegistry.SCREEN_HANDLERS.get("chiseling_table"), syncId);
        this.delegate = delegate;
        this.addProperties(this.delegate);
        this.inventory = inventory;
        this.addSlot(new ToolSlot(inventory, 0, 61, 41, new Identifier("c", "part_chisels"))); //Chisel
        this.addSlot(new ToolSlot(inventory, 1, 95, 41, ItemRegistry.TOOL_PART_RECIPES.keySet())); //Material
        this.addSlot(new ToolOutputSlot(inventory, 2, 78, 66)); //Output

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 90 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 148));
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
            if (invSlot < 3) { //If the click is inside the tool station, transfer it to the player
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() instanceof Chisel) {
                if (!this.insertItem(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 1, 2, false)) {
                return ItemStack.EMPTY;
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
        if (id < 5) {
            this.delegate.set(0, id);
            return true;
        } else if (id < 9) {
            this.delegate.set(1, id - 5);
            return true;
        } if (!inventory.getStack(1).isEmpty()) {
            ServerPlayerEntity serverPlayerEntity;
            if (player instanceof ServerPlayerEntity) {
                serverPlayerEntity = (ServerPlayerEntity) player;
            } else {
                return false;
            }

            ItemStack materialStack = inventory.getStack(1);
            if (!ItemRegistry.TOOL_PART_RECIPES.containsKey(materialStack.getItem())) {
                return false;
            }
            HashMap<String, ToolPartRecipe> recipes = ItemRegistry.TOOL_PART_RECIPES.get(materialStack.getItem());

            String type = partPageToString();
            if ("head".equals(type)) {
                type = toolPageToString() + "_head";
            }

            if (!(inventory.getStack(0).getItem() instanceof Chisel) || ((Chisel)inventory.getStack(0).getItem()).getChiselingLevel() < recipes.get(type).chiselingLevel) {
                return false;
            }

            if (materialStack.getCount() < recipes.get(type).requiredAmount) {
                return false;
            }

            if (!inventory.getStack(2).isEmpty()) {
                return false;
            }

            ItemStack outputStack = new ItemStack(ItemRegistry.ITEMS.get(recipes.get(type).outputMaterial + "_" + type));
            outputStack.getOrCreateTag().putDouble("PartDamage", 0);
            inventory.setStack(2, outputStack);
            inventory.getStack(1).decrement(recipes.get(type).requiredAmount);

            Utils.damage(inventory.getStack(0), 1);

            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, inventory.getStack(0)));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 1, inventory.getStack(1)));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 2, inventory.getStack(2)));
            return true;
        }
        return false;
    }

    public String partPageToString() {
        switch (this.delegate.get(1)) {
            case 1:
                return this.delegate.get(0) == 3 ? "sword_guard" : "binding";
            case 2:
                return "handle";
            default:
                return "head";
        }
    }

    public String toolPageToString() {
        switch(this.delegate.get(0)) {
            case 1:
                return "shovel";
            case 2:
                return "axe";
            case 3:
                return "sword";
            case 4:
                return "hoe";
            default:
                return "pickaxe";
        }
    }



    public void setToolPage(int id) {
        this.delegate.set(0, id);
    }

    public int getToolPage() {
        return this.delegate.get(0);
    }

    public int getPartPage() {
        return this.delegate.get(1);
    }

    public void setPartPage(int id) {
        this.delegate.set(1, id);
    }
}