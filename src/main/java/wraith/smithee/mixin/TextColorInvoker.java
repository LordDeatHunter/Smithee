package wraith.smithee.mixin;

import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextColor.class)
public interface TextColorInvoker {

    @Invoker("<init>")
    static TextColor init(int rgb) {
        throw new RuntimeException("Error while creating new TextColor");
    }


}
