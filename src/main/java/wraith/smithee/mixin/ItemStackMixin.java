package wraith.smithee.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.items.Chisel;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.items.tools.BaseSmitheePickaxe;
import wraith.smithee.items.tools.BaseSmitheeSword;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.properties.Modifier;
import wraith.smithee.properties.Properties;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.properties.Trait;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Item getItem();

    @Shadow
    @Nullable
    public abstract CompoundTag getSubTag(String key);

    @Shadow
    private CompoundTag tag;

    @Shadow public abstract boolean hasTag();

    @Shadow public abstract int getCount();

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        if (getItem() instanceof BaseSmitheeTool && tag != null && tag.contains("SmitheeProperties")) {
            CompoundTag tag = getSubTag("SmitheeProperties");
            cir.setReturnValue(tag.getInt("Durability"));
            cir.cancel();
        }
    }

    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    public void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        if (getItem() instanceof BaseSmitheeTool) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "hasEnchantments", at = @At("HEAD"), cancellable = true)
    public void hasEnchantments(CallbackInfoReturnable<Boolean> cir) {
        if (getItem() instanceof BaseSmitheeTool) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
    public void addEnchantment(Enchantment enchantment, int level, CallbackInfo ci) {
        if (getItem() instanceof BaseSmitheeTool) {
            ci.cancel();
        }
    }

    /*
    @Inject(method = "getTranslationKey", at = @At("HEAD"), cancellable = true)
    public void getTranslationKey(CallbackInfoReturnable<String> cir) {
        if (getItem() instanceof BaseSmitheeTool && tag != null && tag.contains("Parts")) {
            CompoundTag tag = getSubTag("Parts");
            String head = tag.getString("HeadPart");
            cir.setReturnValue("items.smithee.tools." + head + "_" + Utils.getToolType(getItem()));
        }
    }
     */

    @Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
    public void getMiningSpeedMultiplier(BlockState state, CallbackInfoReturnable<Float> cir) {
        if (getItem() instanceof BaseSmitheeTool && !(getItem() instanceof BaseSmitheeSword) && tag != null && tag.contains("SmitheeProperties")) {
            CompoundTag tag = getSubTag("SmitheeProperties");
            float mineSpeed = ((MiningToolItemAccessor) getItem()).getEffectiveBlocks().contains(state.getBlock()) ? tag.getFloat("MiningSpeed") : 1.0F;
            if (getItem() instanceof PickaxeItem) {
                Material material = state.getMaterial();
                mineSpeed = material != Material.METAL && material != Material.REPAIR_STATION && material != Material.STONE ? mineSpeed : tag.getFloat("MiningSpeed");
            } else if (getItem() instanceof AxeItem) {
                mineSpeed = ((AxeItemAccessor) getItem()).getEffectiveMaterials().contains(state.getMaterial()) ? tag.getFloat("MiningSpeed") : mineSpeed;
            } else if (getItem() instanceof SwordItem) {
                mineSpeed = (getItem().isEffectiveOn(state)) ? tag.getFloat("MiningSpeed") : mineSpeed;
            }
            cir.setReturnValue(mineSpeed);
            cir.cancel();
        }
    }

    @Inject(method = "isEffectiveOn", at = @At("HEAD"), cancellable = true)
    public void isEffectiveOn(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (getItem() instanceof BaseSmitheeTool) {
            if (getItem() instanceof BaseSmitheePickaxe && tag != null && tag.contains("SmitheeProperties")) {
                CompoundTag tag = getSubTag("SmitheeProperties");
                int mineLevel = tag.getInt("MiningLevel");
                if (!state.isOf(Blocks.OBSIDIAN) && !state.isOf(Blocks.CRYING_OBSIDIAN) && !state.isOf(Blocks.NETHERITE_BLOCK) && !state.isOf(Blocks.RESPAWN_ANCHOR) && !state.isOf(Blocks.ANCIENT_DEBRIS)) {
                    if (!state.isOf(Blocks.DIAMOND_BLOCK) && !state.isOf(Blocks.DIAMOND_ORE) && !state.isOf(Blocks.EMERALD_ORE) && !state.isOf(Blocks.EMERALD_BLOCK) && !state.isOf(Blocks.GOLD_BLOCK) && !state.isOf(Blocks.GOLD_ORE) && !state.isOf(Blocks.REDSTONE_ORE)) {
                        if (!state.isOf(Blocks.IRON_BLOCK) && !state.isOf(Blocks.IRON_ORE) && !state.isOf(Blocks.LAPIS_BLOCK) && !state.isOf(Blocks.LAPIS_ORE)) {
                            Material material = state.getMaterial();
                            cir.setReturnValue(material == Material.STONE || material == Material.METAL || material == Material.REPAIR_STATION || state.isOf(Blocks.NETHER_GOLD_ORE));
                        } else {
                            cir.setReturnValue(mineLevel >= 1);
                        }
                    } else {
                        cir.setReturnValue(mineLevel >= 2);
                    }
                } else {
                    cir.setReturnValue(mineLevel >= 3);
                }
                cir.cancel();
            } else if (getItem() instanceof MiningToolItem) {
                cir.setReturnValue(((MiningToolItemAccessor) getItem()).getEffectiveBlocks().contains(state.getBlock()));
                cir.cancel();
            }
        }
    }

    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    public void getAttributeModifiers(EquipmentSlot equipmentSlot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (getItem() instanceof BaseSmitheeTool && tag != null && tag.contains("SmitheeProperties") && equipmentSlot == EquipmentSlot.MAINHAND) {
            CompoundTag tag = getSubTag("SmitheeProperties");
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(((ItemAccessor) getItem()).getAttackDamageModifierId(), getItem() instanceof BaseSmitheeSword ? "Tool modifier" : "Weapon modifier", tag.getFloat("AttackDamage") + Properties.getExtraDamage(Utils.getToolType(getItem())), EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(((ItemAccessor) getItem()).getAttackSpeedModifierId(), getItem() instanceof BaseSmitheeSword ? "Tool modifier" : "Weapon modifier", -4 + tag.getFloat("AttackSpeed") + Properties.getExtraAttackSpeed(Utils.getToolType(getItem())), EntityAttributeModifier.Operation.ADDITION));
            cir.setReturnValue(builder.build());
            cir.cancel();
        }
    }

    @Inject(method = "hasCustomName", at = @At("HEAD"), cancellable = true)
    public void hasCustomName(CallbackInfoReturnable<Boolean> cir) {
        if (getItem() instanceof BaseSmitheeTool || getItem() instanceof Chisel || Registry.ITEM.getId(getItem()).getPath().endsWith("_embossment")) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    public void getName(CallbackInfoReturnable<Text> cir) {
        if (getItem() instanceof BaseSmitheeTool) {
            if (tag != null && tag.contains("SmitheeProperties") && tag.getCompound("SmitheeProperties").contains("CustomName")) {
                cir.setReturnValue(new LiteralText(tag.getCompound("SmitheeProperties").getString("CustomName")));
            } else if (tag != null && tag.contains("Parts")) {
                cir.setReturnValue(new LiteralText(Utils.capitalize(tag.getCompound("Parts").getString("HeadPart").split("_")) + " " + Utils.capitalize(Utils.getToolType(getItem()))));
            } else {
                cir.setReturnValue(new LiteralText("Base Smithee " + Utils.capitalize(Utils.getToolType(getItem()))));
            }
        } else if (getItem() instanceof ToolPartItem) {
            ToolPartItem part = (ToolPartItem)getItem();
            cir.setReturnValue(new LiteralText(part.toString()));
        } else if (getItem() instanceof Chisel || Registry.ITEM.getId(getItem()).getPath().endsWith("_embossment")) {
            cir.setReturnValue(new LiteralText(Utils.capitalize(Registry.ITEM.getId(getItem()).getPath().split("/")[0].split("_"))));
        }
    }

    @Environment(EnvType.CLIENT)
    @ModifyVariable(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isSectionHidden(ILnet/minecraft/item/ItemStack$TooltipSection;)Z", ordinal = 3))
    public List<Text> getTooltip(List<Text> list, PlayerEntity player, TooltipContext context) {
        if (ItemRegistry.TOOL_PART_RECIPES.containsKey(getItem()) && ItemRegistry.TOOL_PART_RECIPES.get(getItem()).containsKey("pickaxe_head")) {
            list.add(new LiteralText("§1[§5Tool material§1]"));
            if (Screen.hasShiftDown() && ItemRegistry.REMAINS.containsKey(ItemRegistry.TOOL_PART_RECIPES.get(getItem()).get("pickaxe_head").outputMaterial)) {
                HashMap<String, ToolPartRecipe> recipes = ItemRegistry.TOOL_PART_RECIPES.get(getItem());
                int worth = ItemRegistry.REMAINS.get(recipes.get("pickaxe_head").outputMaterial).get(getItem());
                list.add(new LiteralText("§9[§dIndividual Worth: §b" + worth + "§d]"));
                list.add(new LiteralText("§9[§dTotal Worth: §b" + (worth * getCount()) + "§d]"));
            } else {
                list.add(new LiteralText("§3[§bSHIFT§3] for info."));
            }
        }
        if (getItem() instanceof BaseSmitheeTool) {
            CompoundTag tag = getSubTag("SmitheeProperties");
            if (tag != null && tag.contains("Experience")) {
                list.add(new LiteralText("§2Level §a" + tag.getInt("Level") + "."));
                list.add(new LiteralText("§5Progress " + BaseSmitheeTool.getProgressString(tag.getInt("Experience"), tag.getInt("Level"))));
            }
            list.add(new LiteralText(""));
            list.addAll(Trait.getTooltip(((ItemStack)(Object)this)));
            list.add(new LiteralText(""));
            list.addAll(Modifier.getTooltip(((ItemStack)(Object)this)));
        }
        return list;
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    public void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        Trait.evaluateTraits(((ItemStack)(Object)this), world, null, null, null, entity, "ItemStack#inventoryTick");
    }

    @Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
    public <T extends LivingEntity> void damage(int amount, T entity, Consumer<T> breakCallback, CallbackInfo ci) {
        HashMap<String, Object> result = Trait.evaluateTraits(((ItemStack)(Object)this), null, null, null, null, null, "ItemStack#damage");
        if (result.containsKey("Cancel Item Damage") && (boolean)result.get("Cancel Item Damage")) {
            ci.cancel();
        }
    }

}