package wraith.smithee.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wraith.smithee.tools.SmitheePickaxe;
import wraith.smithee.tools.SmitheeTool;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V", shift = At.Shift.AFTER), cancellable = true
    )
    private void renderItem(ItemStack stack, Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (stack.getItem() instanceof SmitheePickaxe) {
            VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(getTextureForStack(stack)));
            MatrixStack.Entry tos = matrices.peek();
            Matrix3f normal = tos.getNormal();
            Matrix4f tr = tos.getModel();

            buffer.vertex(tr, 0.7f, 0.9f, 0.44f).color(1f, 1f, 1f, 1f).texture(0f, 0f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
            buffer.vertex(tr, 0.2f, 0.9f, 0.44f).color(1f, 1f, 1f, 1f).texture(1f, 0f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
            buffer.vertex(tr, 0.2f, 0.1f, 0.44f).color(1f, 1f, 1f, 1f).texture(1f, 1f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
            buffer.vertex(tr, 0.7f, 0.1f, 0.44f).color(1f, 1f, 1f, 1f).texture(0f, 1f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();

            buffer.vertex(tr, 0.2f, 0.9f, 0.44f).color(1f, 1f, 1f, 1f).texture(0f, 0f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
            buffer.vertex(tr, 0.7f, 0.9f, 0.44f).color(1f, 1f, 1f, 1f).texture(1f, 0f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
            buffer.vertex(tr, 0.7f, 0.1f, 0.44f).color(1f, 1f, 1f, 1f).texture(1f, 1f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
            buffer.vertex(tr, 0.2f, 0.1f, 0.44f).color(1f, 1f, 1f, 1f).texture(0f, 1f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
            ci.cancel();
            matrices.pop();
        }
    }

    private final HashMap<byte[],Identifier> memory = new HashMap<>();

    private Identifier getTextureForStack(ItemStack stack) {

        //if the stack has been assigned a texture it will have a tag, and at no other time.
        if(stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            //if the stack has been assigned a texture, but its not in memory
            if(!memory.containsKey(tag.getByteArray("tex"))) {
                try {
                    InputStream is = new ByteArrayInputStream(tag.getByteArray("tex"));
                    Identifier id = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("smithee", new NativeImageBackedTexture(NativeImage.read(is)));
                    memory.put(tag.getByteArray("tex"),id);
                    return id;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //basically any frame after the first one
            else
            {
                return memory.get(tag.getByteArray("tex"));
            }
        }
        else {
            //if the stack does not have a texture assigned, the texture cannot be in memory
            CompoundTag tag = new CompoundTag();
            try {

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(((SmitheeTool)stack.getItem()).getImage(), "PNG" , os);
                tag.putByteArray("tex", os.toByteArray());

                InputStream is = new ByteArrayInputStream(os.toByteArray());
                Identifier id = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("smithee",new NativeImageBackedTexture(NativeImage.read(is)));
                memory.put(os.toByteArray(),id);
                stack.setTag(tag);

                return id;

            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}