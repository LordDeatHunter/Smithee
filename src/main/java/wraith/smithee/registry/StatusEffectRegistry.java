package wraith.smithee.registry;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;
import wraith.smithee.mixin.StatusEffectInvoker;
import wraith.smithee.utils.Utils;

import java.util.HashMap;

public class StatusEffectRegistry {

    public static final HashMap<String, StatusEffect> STATUS_EFFECTS = new HashMap<>();

    public static void registerStatusEffects() {
        //STATUS_EFFECTS.put("bleeding", Registry.register(Registry.STATUS_EFFECT, Utils.ID("bleeding"), StatusEffectInvoker.init(StatusEffectType.HARMFUL, 0xAE0000)));
        STATUS_EFFECTS.put("frostbite", Registry.register(Registry.STATUS_EFFECT, Utils.ID("frostbite"), StatusEffectInvoker.init(StatusEffectType.HARMFUL, 0x08CDFD)).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.4D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    }

}
