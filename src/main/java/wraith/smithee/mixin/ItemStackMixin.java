package wraith.smithee.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.Utils;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.registry.ItemRegistry;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Item getItem();

    @Shadow
    @Nullable
    public abstract CompoundTag getSubTag(String key);

    @Shadow
    public abstract boolean isEffectiveOn(BlockState state);

    @Shadow
    private CompoundTag tag;

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        if (Utils.isSmitheeTool(((ItemStack)(Object)this)) && tag != null && tag.contains("Parts")) {
            CompoundTag tag = getSubTag("Parts");
            int maxDamage = 0;
            maxDamage += ItemRegistry.MATERIALS.get(tag.getString("HeadPart")).getDurability();
            maxDamage += ItemRegistry.MATERIALS.get(tag.getString("BindingPart")).getDurability();
            maxDamage += ItemRegistry.MATERIALS.get(tag.getString("HandlePart")).getDurability();
            cir.setReturnValue(maxDamage);
            cir.cancel();
        }
    }

    @Inject(method = "getTranslationKey", at = @At("HEAD"), cancellable = true)
    public void getTranslationKey(CallbackInfoReturnable<String> cir) {
        if (getItem() instanceof BaseSmitheeTool && tag != null && tag.contains("Parts")) {
            CompoundTag tag = getSubTag("Parts");
            ToolMaterial head = ItemRegistry.MATERIALS.get(tag.getString("HeadPart"));
            System.out.println(head);
            cir.setReturnValue("items.smithee.tools." + head + "_" + Utils.getToolType(getItem()));
        }
    }

    @Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
    public void getMiningSpeedMultiplier(BlockState state, CallbackInfoReturnable<Float> cir) {
        if (getItem() instanceof BaseSmitheeTool && tag != null && tag.contains("Parts")) {
            CompoundTag tag = getSubTag("Parts");
            cir.setReturnValue(isEffectiveOn(state) ? ItemRegistry.MATERIALS.get(tag.getString("HeadPart")).getMiningSpeedMultiplier() : 1.0F);
            cir.cancel();
        }
    }

    @Inject(method = "isEffectiveOn", at = @At("HEAD"), cancellable = true)
    public void isEffectiveOn(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (getItem() instanceof BaseSmitheeTool && tag != null && tag.contains("Parts")) {
            CompoundTag tag = getSubTag("Parts");
            int mineLevel = ItemRegistry.MATERIALS.get(tag.getString("HeadPart")).getMiningLevel();
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
        }
    }
}