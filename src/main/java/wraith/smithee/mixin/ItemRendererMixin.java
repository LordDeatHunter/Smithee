package wraith.smithee.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wraith.smithee.Utils;
import wraith.smithee.registry.ItemRegistry;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow @Final private ItemModels models;

    @Inject(
            /*
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V", shift = At.Shift.AFTER), cancellable = true
             */
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"), cancellable = true
    )
    private void renderItem(ItemStack stack, Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (!stack.isEmpty() && Utils.isSmitheeTool(stack)) {
            CompoundTag tag = stack.getTag();
            String tool = Utils.getToolType(stack.getItem());

            String headString = "iron_" + tool + "_head";
            String bindingString = "iron_" + tool + "_binding";
            String handleString = "iron_" + tool + "_handle";
            if (tag != null && tag.contains("Parts")) {
                headString = tag.getCompound("Parts").getString("HeadPart") + "_" + tool + "_head";
                bindingString = tag.getCompound("Parts").getString("BindingPart") + "_" + tool + "_binding";
                handleString = tag.getCompound("Parts").getString("HandlePart") + "_" + tool + "_handle";
            }
            Item head = ItemRegistry.ITEMS.get(headString);
            Item binding = ItemRegistry.ITEMS.get(bindingString);
            Item handle = ItemRegistry.ITEMS.get(handleString);

            BakedModel handleModel = models.getModelManager().getModel(new ModelIdentifier(Utils.ID(handleString), "inventory"));
            BakedModel bindingModel = models.getModelManager().getModel(new ModelIdentifier(Utils.ID(bindingString), "inventory"));
            BakedModel headModel = models.getModelManager().getModel(new ModelIdentifier(Utils.ID(headString), "inventory"));
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(handle), renderMode, leftHanded, matrices, vertexConsumers, light, overlay, handleModel);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(binding), renderMode, leftHanded, matrices, vertexConsumers, light, overlay, bindingModel);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(head), renderMode, leftHanded, matrices, vertexConsumers, light, overlay, headModel);
            ci.cancel();

            //Broken code, honestly...
            /*
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(id));
            model = models.getModelManager().getModel(new ModelIdentifier(id, ""));
            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);

            //Tried this:
            //renderBakedItemModel(mdl, stack, light, overlay, matrices, vertexConsumer);
            //And this:
            //renderBakedItemQuads(matrices, vertexConsumer, model.getQuads(null, null, new Random()), stack, light, overlay);

            //matrices.pop();
            //ci.cancel();
             */
        }
    }

    //No longer needed
    /*
    private static final HashMap<ArrayList<String>, Identifier> textures = new HashMap<>();

    private Identifier getTexture(ItemStack stack) {
        ArrayList<String> parts = new ArrayList<>();
        if (stack.getTag().contains("Parts")) {
            CompoundTag tag = stack.getSubTag("Parts");
            parts.add(tag.getString("HeadPart"));
            parts.add(tag.getString("BindingPart"));
            parts.add(tag.getString("HandlePart"));
        } else {
            parts.add("iron");
            parts.add("iron");
            parts.add("iron");
        }
        if (!textures.containsKey(parts)) {
            textures.put(parts, Utils.generateTexture(stack));
        }
        return textures.get(parts);
    }
     */

}
