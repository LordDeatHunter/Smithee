package wraith.smithee;

import com.google.common.base.Charsets;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemBakedModel implements FabricBakedModel, BakedModel, UnbakedModel {

    private static final HashMap<String, FabricBakedModel> PART_MODELS = new HashMap<>();

    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext context) {
        String tool = Utils.getToolType(itemStack.getItem());

        String head = "iron_" + tool + "_head";
        String binding = "iron_" + tool + "_binding";
        String handle = "iron_" + tool + "_handle";

        CompoundTag tag = itemStack.getSubTag("Parts");
        if (tag != null) {
            head = tag.getString("HeadPart") + "_" + tool + "_head";
            binding = tag.getString("BindingPart") + "_" + tool + "_binding";
            handle = tag.getString("HandlePart") + "_" + tool + "_handle";
        }

        PART_MODELS.get(head).emitItemQuads(null, null, context);
        PART_MODELS.get(binding).emitItemQuads(null, null, context);
        PART_MODELS.get(handle).emitItemQuads(null, null, context);
    }

    public static ModelTransformation loadTransformFromJson(Identifier location) {
        try {
            return JsonUnbakedModel.deserialize(getReaderForResource(location)).getTransformations();
        } catch (IOException exception) {
            Smithee.LOGGER.warn("Can't load resource " + location);
            exception.printStackTrace();
            return null;
        }
    }

    public static Reader getReaderForResource(Identifier location) throws IOException {
        Identifier file = new Identifier(location.getNamespace(), location.getPath() + ".json");
        Resource resource = MinecraftClient.getInstance().getResourceManager().getResource(file);
        return new BufferedReader(new InputStreamReader(resource.getInputStream(), Charsets.UTF_8));
    }

    @Override
    public @Nullable BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        if (PART_MODELS.isEmpty()) {
            for(String id : SmitheeClient.RENDERING_TOOL_PARTS) {
                PART_MODELS.put(id, (FabricBakedModel) loader.bake(Utils.inventoryModelID(id), ModelRotation.X0_Y0));
            }
        }
        return this;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {}

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return null;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public ModelTransformation getTransformation() {
        return loadTransformFromJson(new Identifier("minecraft:models/item/handheld"));
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }
}
