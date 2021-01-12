package wraith.smithee.mixin;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.Smithee;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        boolean isSmithee = id.getNamespace().equals(Smithee.MOD_ID);
        if (Utils.isToolPart(id.getPath()) || (isSmithee && (id.getPath().endsWith("_whetstone") || id.getPath().endsWith("_chisel") || id.getPath().endsWith("_embossment") || id.getPath().endsWith("_shard"))) || (id.getPath().startsWith("item/base_smithee_") && (ItemRegistry.BASE_RECIPE_VALUES.containsKey(id.getPath().substring("item/base_smithee_".length()))))) {
            String modelParent = "minecraft:item/generated";
            if (Utils.isToolPart(id.getPath())) {
                modelParent = "minecraft:item/handheld";
            }
            String json = Utils.createModelJson(id.getPath(), modelParent);
            for (String model : ItemRegistry.MODELS.keySet()) {
                if (id.getNamespace().equals(model.split(":")[0]) && id.getPath().endsWith(model)) {
                    json = ItemRegistry.MODELS.get(model);
                    break;
                }
            }
            JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
            model.id = id.toString();
            cir.setReturnValue(model);
            cir.cancel();
        }
    }
}
