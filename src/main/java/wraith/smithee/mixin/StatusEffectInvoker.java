package wraith.smithee.mixin;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StatusEffect.class)
public interface StatusEffectInvoker {

    @Invoker("<init>")
    static StatusEffect init(StatusEffectType type, int color) {
        throw new RuntimeException("Error while creating new TextColor");
    }

}
