package wraith.smithee.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.smithee.blocks.ChiselingTableBlockEntity;
import wraith.smithee.items.Chisel;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.screens.slots.ToolOutputSlot;
import wraith.smithee.screens.slots.ToolSlot;

import java.util.HashMap;
import java.util.Random;

public class ChiselingTableScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate delegate;

    public ChiselingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(10), new ArrayPropertyDelegate(1));
    }

    public ChiselingTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ScreenHandlerRegistry.SCREEN_HANDLERS.get("chiseling_table"), syncId);
        this.delegate = delegate;
        this.addProperties(this.delegate);
        this.inventory = inventory;
        if (inventory instanceof ChiselingTableBlockEntity) {
            ((ChiselingTableBlockEntity) inventory).setHandler(this);
        }
        this.addSlot(new ToolSlot(inventory, 0, 17, 44, ItemRegistry.ITEMS.get("flint_chisel"))); //Chisel

        this.addSlot(new Slot(inventory, 1, 51, 20)); //Material 1
        this.addSlot(new Slot(inventory, 2, 50, 44)); //Material 2
        this.addSlot(new Slot(inventory, 3, 50, 68)); //Material 3

        this.addSlot(new ToolOutputSlot(inventory, 4, 109, 20)); //Head
        this.addSlot(new ToolOutputSlot(inventory, 5, 109, 44)); //Binding
        this.addSlot(new ToolOutputSlot(inventory, 6, 109, 68)); //Handle

        this.addSlot(new ToolOutputSlot(inventory, 7, 130, 20)); //Fragments 1
        this.addSlot(new ToolOutputSlot(inventory, 8, 130, 44)); //Fragments 2
        this.addSlot(new ToolOutputSlot(inventory, 9, 130, 68)); //Fragments 3

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 123 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 181));
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
            if (invSlot < 10) { //If the click is inside the tool station, transfer it to the player
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() instanceof Chisel) {
                if (!this.insertItem(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 1, 4, false)) {
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
        if (id > 2) {
            this.delegate.set(0, id - 3);
            return true;
        }
        if (!inventory.getStack(1 + id).isEmpty()) {
            ServerPlayerEntity serverPlayerEntity;
            if (player instanceof ServerPlayerEntity) {
                serverPlayerEntity = (ServerPlayerEntity) player;
            } else {
                return false;
            }

            if (!(inventory.getStack(0).getItem() instanceof Chisel)) {
                return false;
            }
            ItemStack materialStack = inventory.getStack(1 + id);
            if (!ItemRegistry.TOOL_PART_RECIPES.containsKey(materialStack.getItem())) {
                return false;
            }

            String type;
            switch(id) {
                case 0:
                    type = "head";
                    break;
                case 1:
                    type = "binding";
                    break;
                case 2:
                    type = "handle";
                    break;
                default:
                    return false;
            }

            HashMap<String, ToolPartRecipe> recipes = ItemRegistry.TOOL_PART_RECIPES.get(materialStack.getItem());
            if (!(materialStack.getCount() >= recipes.get(type).requiredAmount)) {
                return false;
            }

            Item output = ItemRegistry.ITEMS.get(recipes.get(type).outputMaterial + "_" + pageToString() + "_" + type);
            ItemStack prevSlot = inventory.getStack(4 + id);

            if (!prevSlot.isEmpty()) {
                return false;
            }

            if (!"".equals(recipes.get(type).remains)) {
                ItemStack shardSlot = inventory.getStack(7 + id);
                Item shard = recipes.get(type).getRemains();
                int shardAmount = recipes.get(type).remainsAmount;
                if (shardSlot.isEmpty()) {
                    inventory.setStack(7 + id, new ItemStack(shard, shardAmount));
                } else if (shardSlot.getItem() == shard && shardSlot.getCount() + shardAmount <= shardSlot.getMaxCount()) {
                    shardSlot.increment(shardAmount);
                } else {
                    return false;
                }
            }

            ItemStack outputStack = new ItemStack(output);
            outputStack.getOrCreateTag().putDouble("PartDamage", 0);
            inventory.setStack(4 + id, outputStack);
            inventory.getStack(1 + id).decrement(recipes.get(type).requiredAmount);

            if (inventory.getStack(0).damage(1, new Random(), serverPlayerEntity)) {
                Item chisel = inventory.getStack(0).getItem();
                inventory.getStack(0).decrement(1);
                player.incrementStat(Stats.BROKEN.getOrCreateStat(chisel));
            }

            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, inventory.getStack(0)));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 1 + id, inventory.getStack(1 + id)));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 4 + id, inventory.getStack(4 + id)));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 7 + id, inventory.getStack(7 + id)));

        }
        return true;
    }

    public String pageToString() {
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

    public void setPage(int id) {
        this.delegate.set(0, id);
    }

    public int getPage() {
        return this.delegate.get(0);
    }

}