package wraith.smithee.mixin;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.Smithee;
import wraith.smithee.utils.Utils;

//ItemModels

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        boolean isSmithee = id.getNamespace().equals(Smithee.MOD_ID);
        if (Utils.isToolPart(id.getPath()) || (isSmithee && (id.getPath().endsWith("_chisel") || id.getPath().endsWith("_embossment") || id.getPath().endsWith("_shard")))) {
            JsonUnbakedModel model = JsonUnbakedModel.deserialize(Utils.createModelJson(id.getPath(), id.getPath().endsWith("_chisel")?"handheld":"generated"));
            model.id = id.toString();
            cir.setReturnValue(model);
            cir.cancel();
        }
    }

}
