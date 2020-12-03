package wraith.smithee;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import org.jetbrains.annotations.Nullable;
import wraith.smithee.utils.Utils;

public class ItemModelProvider implements ModelVariantProvider {

    @Override
    public @Nullable UnbakedModel loadModelVariant(ModelIdentifier modelIdentifier, ModelProviderContext modelProviderContext) {
        if(modelIdentifier.getNamespace().equals(Smithee.MOD_ID) && Utils.isSmitheeTool(modelIdentifier.getPath())) {
            return new ItemBakedModel();
        } else {
            return null;
        }
    }

}
