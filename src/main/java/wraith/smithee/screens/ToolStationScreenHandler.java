package wraith.smithee.screens;

import jdk.javadoc.internal.doclets.formats.html.markup.Head;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import wraith.smithee.Utils;
import wraith.smithee.items.BindingItem;
import wraith.smithee.items.HandleItem;
import wraith.smithee.items.HeadItem;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.tools.*;

import java.util.Optional;

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
        int m;
        int l;
        this.addSlot(new Slot(inventory, 0, 0, 0)); //Handle
        this.addSlot(new Slot(inventory, 1, 0, 0)); //Binding
        this.addSlot(new Slot(inventory, 2, 0, 0)); //Head
        this.addSlot(new Slot(inventory, 3, 0, 0)); //Output
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
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
            } else {
                if (originalStack.getItem() instanceof HandleItem && !this.insertItem(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
                if (originalStack.getItem() instanceof BindingItem && !this.insertItem(originalStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
                if (originalStack.getItem() instanceof HeadItem && !this.insertItem(originalStack, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
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
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.getStack().equals(this.inventory.getStack(3)) && super.canInsertIntoSlot(stack, slot);
    }

    protected static void updateResult(int syncId, World world, PlayerEntity player, Inventory inventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            ItemStack itemStack;
            if (inventory.getStack(0).isEmpty() || !(inventory.getStack(0).getItem() instanceof HandleItem) ||
                inventory.getStack(1).isEmpty() || !(inventory.getStack(0).getItem() instanceof BindingItem) ||
                inventory.getStack(2).isEmpty() || !(inventory.getStack(0).getItem() instanceof HeadItem))
                return;

            HandleItem handle = (HandleItem)inventory.getStack(0).getItem();
            BindingItem binding = (BindingItem)inventory.getStack(1).getItem();
            HeadItem head = (HeadItem)inventory.getStack(2).getItem();

            switch(head.part.type) {
                case "pickaxe":
                    itemStack = new ItemStack(new SmitheePickaxe(Utils.createToolMaterial(head.part, binding.part, handle.part)));
                    break;
                case "shovel":
                    itemStack = new ItemStack(new SmitheeShovel(Utils.createToolMaterial(head.part, binding.part, handle.part)));
                    break;
                case "axe":
                    itemStack = new ItemStack(new SmitheeAxe(Utils.createToolMaterial(head.part, binding.part, handle.part)));
                    break;
                case "sword":
                    itemStack = new ItemStack(new SmitheeSword(Utils.createToolMaterial(head.part, binding.part, handle.part)));
                    break;
                case "hoe":
                    itemStack = new ItemStack(new SmitheeHoe(Utils.createToolMaterial(head.part, binding.part, handle.part), 5, 5, Settings));
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
    }



}