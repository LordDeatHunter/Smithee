package wraith.smithee.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import wraith.smithee.properties.Modifier;
import wraith.smithee.properties.Trait;

import java.util.HashMap;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "hasAquaAffinity", at = @At("HEAD"), cancellable = true)
    private static void hasAquaAffinity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (Trait.hasTrait(entity.getMainHandStack(), Trait.Traits.AQUADYNAMIC)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

/*
    @Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
    private static void getAttackDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir) {
        HashMap<String, Object> result = Modifier.evaluateModifiers(stack);
        if (result.containsKey("Quartz")) {
            float dmg = 1.0F + Math.max(0, (float) result.get("Quartz") - 1) * 0.5F;
            cir.setReturnValue(dmg);
            cir.cancel();
        }
    }

    @Inject(method = "getEfficiency", at = @At("HEAD"), cancellable = true)
    private static void getEfficiency(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        HashMap<String, Object> result = Modifier.evaluateModifiers(entity.getMainHandStack());
        if (result.containsKey("Redstone")) {
            cir.setReturnValue((Integer) result.get("Redstone"));
            cir.cancel();
        }
    }
*/

    @Inject(method = "getLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getEnchantments()Lnet/minecraft/nbt/ListTag;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void getLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir, Identifier id) {
        HashMap<String, Modifier> result = Modifier.evaluateModifiers(stack);
        int level = 0;
        for (Modifier modifiers : result.values()) {
            for (String modifier : modifiers.modifiers) {
                if (modifier.equals(id.toString())) {
                    level += modifiers.level;
                }
            }
        }
        if (level > 0) {
            cir.setReturnValue(level);
            cir.cancel();
        }
    }

    @ModifyVariable(method = "forEachEnchantment(Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getEnchantments()Lnet/minecraft/nbt/ListTag;"))
    private static ListTag forEachEnchantment(ListTag listTag, EnchantmentHelper.Consumer consumer, ItemStack stack) {
        HashMap<String, Modifier> result = Modifier.evaluateModifiers(stack);
        if (result.isEmpty()) {
            return listTag;
        }
        ListTag newListTag = listTag.copy();
        for (Modifier modifiers : result.values()) {
            if (!"enchant".equals(modifiers.type)) {
                continue;
            }
            for (String modifier : modifiers.modifiers) {
                CompoundTag tag = new CompoundTag();
                tag.putString("id", modifier);
                tag.putInt("lvl", modifiers.level);
                newListTag.add(tag);
            }
        }
        return newListTag;
    }

}