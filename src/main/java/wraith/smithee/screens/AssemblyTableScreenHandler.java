package wraith.smithee.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;
import wraith.smithee.Smithee;
import wraith.smithee.blocks.AassemblyTableBlockEntity;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.properties.Properties;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.recipes.EmbossRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.screens.slots.AssemblyTableOutputSlot;
import wraith.smithee.screens.slots.PartSlot;
import wraith.smithee.screens.slots.ToolSlot;

import java.util.HashSet;

public class AssemblyTableScreenHandler extends ScreenHandler {

    private Inventory inventory;
    private String toolName = "";

    public AssemblyTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5));
    }

    public AssemblyTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerRegistry.SCREEN_HANDLERS.get("assembly_table"), syncId);
        this.inventory = inventory;
        if (inventory instanceof AassemblyTableBlockEntity) {
            ((AassemblyTableBlockEntity)inventory).setHandler(this);
        }
        this.addSlot(new PartSlot(inventory, 0, 35, 57, "handle")); //Handle
        this.addSlot(new PartSlot(inventory, 1, 47, 37, "binding", "sword_guard")); //Binding
        this.addSlot(new PartSlot(inventory, 2, 59, 17, "head")); //Head
        this.addSlot(new AssemblyTableOutputSlot(inventory, 3, 113, 43, true)); //Output
        HashSet<Item> itemSet = new HashSet<>();
        for (String item : ItemRegistry.EMBOSS_MATERIALS) {
            itemSet.add(ItemRegistry.ITEMS.get(item + "_embossment"));
        }
        this.addSlot(new ToolSlot(inventory, 4, 113, 11, itemSet)); //Embossment

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 102 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 160));
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
            if (invSlot < 5) { //If the click is inside the tool station, transfer it to the player
                onContentChanged(inventory);
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() instanceof ToolPartItem) {
                ToolPartItem tool = (ToolPartItem) originalStack.getItem();
                if (("handle".equals(tool.part.partType) && !this.insertItem(originalStack, 0, 1, false)) ||
                        (("binding".equals(tool.part.partType) || "sword_guard".equals(tool.part.partType)) && !this.insertItem(originalStack, 1, 2, false)) ||
                        ("head".equals(tool.part.partType) && !this.insertItem(originalStack, 2, 3, false))) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() instanceof BaseSmitheeTool) {
                if(!this.insertItem(originalStack, 3, 4, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (Registry.ITEM.getId(originalStack.getItem()).getPath().endsWith("_embossment")) {
                if(!this.insertItem(originalStack, 4, 5, false)) {
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
        if (!(player instanceof ServerPlayerEntity)) {
            return false;
        }
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;

        if (id == 1) {
            if (inventory.getStack(3) == ItemStack.EMPTY || inventory.getStack(4) == ItemStack.EMPTY) {
                return false;
            }
            ItemStack emboss = inventory.getStack(4).copy();

            ItemStack tool = inventory.getStack(3);

            String[] segments = Registry.ITEM.getId(emboss.getItem()).getPath().split("/");
            segments = segments[segments.length - 1].split("_");
            String material = "";
            for (int i = 0; i < segments.length - 1; ++i) {
                material += "_" + segments[i];
            }
            material = material.substring(1);
            if (!ItemRegistry.EMBOSS_RECIPES.containsKey(material)) {
                Smithee.LOGGER.info("CONTAINS " + material);
                return false;
            }
            Smithee.LOGGER.info("TRUE");
            EmbossRecipe recipe = ItemRegistry.EMBOSS_RECIPES.get(material);
            CompoundTag mainTag = tool.getSubTag("SmitheeProperties");
            if (!mainTag.contains("Modifiers")) {
                return false;
            }
            CompoundTag tag = mainTag.getCompound("Modifiers");
            int traitSlots = tag.getInt("TraitSlots");
            int enchantmentSlots = tag.getInt("EnchantmentSlots");
            if (("enchant".equals(recipe.type) && enchantmentSlots > 0) || ("trait".equals(recipe.type) && traitSlots > 0)) {
                CompoundTag slots = tag.getCompound("Slots");
                for (String key : slots.getKeys()) {
                    if (recipe.incompatible.contains(key)) {
                        return false;
                    }
                }
                if (!recipe.stackable && slots.getKeys().contains(material)) {
                    return false;
                }

                slots.putInt(material, slots.contains(material) ? slots.getInt(material) + 1 : 1);

                emboss.decrement(1);
                if ("enchant".equals(recipe.type)) {
                    tag.putInt("EnchantmentSlots", enchantmentSlots - 1);
                } else {
                    tag.putInt("TraitSlots", traitSlots - 1);
                }
                tag.put("Slots", slots);
            } else {
                return false;
            }
            mainTag.put("Modifiers", tag);
            tool.putSubTag("SmitheeProperties", mainTag);
            inventory.setStack(3, tool);
            inventory.setStack(4, emboss);

            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 3, tool));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 4, emboss));
        } else if (id == 0) {

            if (inventory.getStack(3) != ItemStack.EMPTY) {
                return false;
            }

            if (inventory.getStack(0).isEmpty() ||
                inventory.getStack(1).isEmpty() ||
                inventory.getStack(2).isEmpty()) {
                    return false;
            }

            ItemStack handle = inventory.getStack(0);
            ItemStack binding = inventory.getStack(1);
            ItemStack head = inventory.getStack(2);

            HashSet<String> recipe = new HashSet<String>(){{
                add(((ToolPartItem)handle.getItem()).part.recipeString());
                add(((ToolPartItem)binding.getItem()).part.recipeString());
                add(((ToolPartItem)head.getItem()).part.recipeString());
            }};

            if (!ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.contains(recipe)){
                return false;
            }

            ItemStack itemStack = new ItemStack(ItemRegistry.ITEMS.get("base_smithee_" + ((ToolPartItem)head.getItem()).part.toolType));
            CompoundTag tag = itemStack.getOrCreateSubTag("Parts");
            tag.putString("HeadPart", ((ToolPartItem)head.getItem()).part.materialName);
            tag.putString("BindingPart", ((ToolPartItem)binding.getItem()).part.materialName);
            tag.putString("HandlePart", ((ToolPartItem)handle.getItem()).part.materialName);
            itemStack.putSubTag("Parts", tag);

            Properties.setProperties(itemStack, Properties.getProperties(itemStack));

            double headDamage = 0;
            if (head.hasTag() && head.getTag().contains("PartDamage")) {
                headDamage = head.getTag().getDouble("PartDamage");
            }
            double bindingDamage = 0;
            if (binding.hasTag() && binding.getTag().contains("PartDamage")) {
                bindingDamage = binding.getTag().getDouble("PartDamage");
            }
            double handleDamage = 0;
            if (handle.hasTag() && handle.getTag().contains("PartDamage")) {
                handleDamage = handle.getTag().getDouble("PartDamage");
            }
            double totalDamage = headDamage + bindingDamage + handleDamage;
            int totalDurability = ItemRegistry.PROPERTIES.get(((ToolPartItem)head.getItem()).part.materialName).partProperties.get("head").durability;
            totalDurability += ItemRegistry.PROPERTIES.get(((ToolPartItem)binding.getItem()).part.materialName).partProperties.get("binding").durability;
            totalDurability += ItemRegistry.PROPERTIES.get(((ToolPartItem)handle.getItem()).part.materialName).partProperties.get("handle").durability;

            itemStack.setDamage((int) Math.ceil((totalDamage * itemStack.getMaxDamage())/totalDurability));
            if (itemStack.getDamage() >= itemStack.getMaxDamage()) {
                itemStack = ItemStack.EMPTY;
            }

            inventory.setStack(3, itemStack);
            inventory.getStack(0).decrement(1);
            inventory.getStack(1).decrement(1);
            inventory.getStack(2).decrement(1);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, inventory.getStack(0)));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 1, inventory.getStack(1)));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 2, inventory.getStack(2)));
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 3, itemStack));
            return true;
        }
        return false;
    }

    public void setName(String toolName) {
        this.toolName = toolName;
    }

    public String getName() {
        return this.toolName;
    }
}