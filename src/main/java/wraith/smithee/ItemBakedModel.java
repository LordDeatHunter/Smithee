package wraith.smithee;

import com.google.common.base.Charsets;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.GeometryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resource.Resource;
import net.minecraft.screen.PlayerScreenHandler;
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

    static HashMap<String, BakedModel> models = new HashMap<>();
    //static HashMap<String, JsonUnbakedModel> models = new HashMap<>();
    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext context) {

        String tool = Utils.getToolType(itemStack.getItem());

        String head = "iron_" + tool + "_head";
        String binding = "iron_" + tool + "_binding";
        String handle = "iron_" + tool + "_handle";
        if (itemStack.hasTag() && itemStack.getTag().contains("Parts")) {
            CompoundTag tag = itemStack.getSubTag("Parts");
            head = tag.getString("HeadPart") + "_" + tool + "_head";
            binding = tag.getString("BindingPart") + "_" + tool + "_binding";
            handle = tag.getString("HandlePart") + "_" + tool + "_handle";
        }

        QuadEmitter emitter = context.getEmitter();


        BakedModelManager bakedModelManager = MinecraftClient.getInstance().getBakedModelManager();
        context.fallbackConsumer().accept(bakedModelManager.getModel(getModel(head)));
        context.fallbackConsumer().accept(bakedModelManager.getModel(getModel(binding)));
        context.fallbackConsumer().accept(bakedModelManager.getModel(getModel(handle)));

        models.get(handle).getQuads(null, null, supplier.get()).forEach(q -> {
            emitter.fromVanilla(q.getVertexData(), 0, false);
            emitter.emit();
        });
        models.get(binding).getQuads(null, null, supplier.get()).forEach(q -> {
            emitter.fromVanilla(q.getVertexData(), 0, false);
            emitter.emit();
        });
        models.get(head).getQuads(null, null, supplier.get()).forEach(q -> {
            emitter.fromVanilla(q.getVertexData(), 0, false);
            emitter.emit();
        });
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

    private static ModelIdentifier getModel(String type) {
        return new ModelIdentifier(Utils.ID(type), "inventory");
    }

    @Override
    public @Nullable BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        for (Identifier id : IDS) {
            models.put(id.getPath(), loader.getOrLoadModel(id).bake(loader, textureGetter, rotationContainer, id));
        }
        return this;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    static final HashSet<Identifier> IDS = new HashSet<Identifier>(){{
        for (String material : ItemRegistry.MATERIALS) {
            for (String tool : ItemRegistry.TOOL_TYPES) {
                add(Utils.ID(material + "_" + tool + "_head"));
                add(Utils.ID(material + "_" + tool + "_binding"));
                add(Utils.ID(material + "_" + tool + "_handle"));
            }
        }
    }};

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
