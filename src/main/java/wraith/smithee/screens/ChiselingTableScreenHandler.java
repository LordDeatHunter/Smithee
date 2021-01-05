package wraith.smithee.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.smithee.items.Chisel;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.screens.slots.ToolOutputSlot;
import wraith.smithee.screens.slots.ToolSlot;
import wraith.smithee.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        this.addSlot(new ToolSlot(inventory, 0, 81, 41, Chisel.class)); //Chisel
        this.addSlot(new ToolSlot(inventory, 1, 115, 41, ItemRegistry.TOOL_PART_RECIPES.keySet())); //Material
        this.addSlot(new ToolOutputSlot(inventory, 2, 98, 66)); //Output

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 28 + x * 18, 90 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 28 + x * 18, 148));
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
        } else if (id == 5 || id == 6) {
            this.delegate.set(0, id);
            this.delegate.set(1, 0);
        } else if (id < 10) {
            this.delegate.set(1, id - 7);
            return true;
        } else if (!inventory.getStack(1).isEmpty()) {
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
            boolean isShard = "shard".equals(type);
            if (isShard) {
                type = "pickaxe_head";
            } else if ("head".equals(type)) {
                type = toolPageToString() + "_head";
            }
            boolean isRecipe = recipes.containsKey(type);
            if (!(inventory.getStack(0).getItem() instanceof Chisel)) {
                return false;
            }
            if (!isRecipe && this.delegate.get(0) != 6) {
                return false;
            }
            if (isRecipe && ((Chisel)inventory.getStack(0).getItem()).getChiselingLevel() < recipes.get(type).chiselingLevel) {
                return false;
            }
            int worth = ItemRegistry.REMAINS.get(recipes.get(type).outputMaterial).get(Registry.ITEM.getId(materialStack.getItem()));
            int requiredAmount = 1;
            if (!isShard) {
                requiredAmount = recipes.get(type).requiredAmount;
            }
            if (!isRecipe && materialStack.getCount() * worth < requiredAmount) {
                return false;
            }
            if (!inventory.getStack(2).isEmpty() && inventory.getStack(2).getCount() >= inventory.getStack(2).getMaxCount()) {
                return false;
            }
            int decAmount = (int) Math.ceil((float)requiredAmount / worth);
            int remains = decAmount * worth - requiredAmount;

            ItemStack outputStack = inventory.getStack(2);
            if (outputStack.isEmpty()) {
                if (isShard) {
                    outputStack = new ItemStack(Registry.ITEM.get(ItemRegistry.SHARDS.get(recipes.get(type).outputMaterial)), worth);
                } else {
                    outputStack = new ItemStack(ItemRegistry.ITEMS.get(recipes.get(type).outputMaterial + "_" + type));
                }
                if (!"embossment".equals(type) && !isShard) {
                    outputStack.getOrCreateTag().putDouble("PartDamage", 0);
                }
            } else {
                int difference = (outputStack.getCount() + (isShard ? worth : 1)) - outputStack.getMaxCount();
                if (difference > 0) {
                    player.inventory.offerOrDrop(player.world, new ItemStack(outputStack.getItem(), difference));
                }
                outputStack.increment(isShard ? worth : 1);
            }

            inventory.setStack(2, outputStack);
            inventory.getStack(1).decrement(decAmount);

            Utils.damage(inventory.getStack(0), 1);

            if (!isShard) {
                HashMap<Identifier, Integer> remainders = ItemRegistry.REMAINS.get(recipes.get(type).outputMaterial);
                ArrayList<Map.Entry<Identifier, Integer>> mapValues = new ArrayList<>(remainders.entrySet());
                mapValues.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

                for (Map.Entry<Identifier, Integer> entry : mapValues) {
                    if (entry.getValue() <= remains) {
                        int amount = remains / entry.getValue();
                        amount = Math.min(Registry.ITEM.get(entry.getKey()).getMaxCount(), amount);
                        remains -= amount * entry.getValue();
                        serverPlayerEntity.inventory.offerOrDrop(serverPlayerEntity.world, new ItemStack(Registry.ITEM.get(entry.getKey()), amount));
                    }
                    if (remains <= 0) {
                        break;
                    }
                }
            }

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
                if (getToolPage() == 5) {
                    return "embossment";
                } else if (getToolPage() == 6) {
                    return "shard";
                } else {
                    return "head";
                }
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
            case 5:
                return "embossment";
            case 6:
                return "shard";
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