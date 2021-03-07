package wraith.smithee.mixin;

import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceImpl;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.Smithee;
import wraith.smithee.SmitheeClient;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Mixin(NamespaceResourceManager.class)
public class NamespaceResourceManagerMixin {

    @Inject(method = "getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;", at = @At("HEAD"), cancellable = true)
    public void getResource(Identifier id, CallbackInfoReturnable<Resource> cir) {
        String[] segments = id.getPath().split("/");
        String path = segments[segments.length - 1];

        if (!id.getNamespace().equals(Smithee.MOD_ID) || !path.endsWith(".png")) {
            return;
        }
        String pathWithoutExtension = path.split("\\.")[0];
        segments = pathWithoutExtension.split("_");
        int maxI = Utils.getMaterialFromPathIndex(pathWithoutExtension);
        if (maxI >= segments.length) {
            return;
        }
        StringBuilder material = new StringBuilder();
        int i = 0;
        for (; i < maxI; ++i) {
            if (i > 0) {
                material.append("_");
            }
            material.append(segments[i]);
        }
        StringBuilder part = new StringBuilder();
        for (; i < segments.length; ++i) {
            part.append("_").append(segments[i]);
        }
        part = new StringBuilder(part.substring(1));
        if ((!ItemRegistry.MATERIALS.contains(material.toString()) && !ItemRegistry.EMBOSS_MATERIALS.contains(material.toString())) || (!ItemRegistry.BASE_RECIPE_VALUES.containsKey(part.toString()) && !SmitheeClient.RENDERING_TOOL_PARTS.contains(pathWithoutExtension) && !"whetstone".equals(part.toString()) && !"embossment".equals(part.toString()) && !"chisel".equals(part.toString()) && !"shard".equals(part.toString()))) {
            return;
        }
        File texture = new File("config/smithee/textures/" + path);
        File metadata = new File("config/smithee/textures/" + path + ".mcmeta");
        if (texture.exists()) {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(texture));
                cir.setReturnValue(new ResourceImpl(Smithee.MOD_ID, id, bis, metadata.exists() ? new FileInputStream(metadata) : null));
                cir.cancel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Generating new texture: " + id);
            try {
                BufferedInputStream templatePalette = new BufferedInputStream(new FileInputStream("config/smithee/templates/template_palette.png"));
                BufferedInputStream palette = new BufferedInputStream(new FileInputStream("config/smithee/palettes/" + material + ".png"));
                BufferedInputStream template = new BufferedInputStream(new FileInputStream("config/smithee/templates/" + part + ".png"));
                cir.setReturnValue(new ResourceImpl(Smithee.MOD_ID, id, Utils.recolor(template, templatePalette, palette, pathWithoutExtension, texture), null));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            cir.cancel();
        }
    }
}
