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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;

@Mixin(NamespaceResourceManager.class)
public class NamespaceResourceManagerMixin {

    @Inject(method = "getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;", at = @At("HEAD"), cancellable = true)
    public void getResource(Identifier id, CallbackInfoReturnable<Resource> cir) {
        String[] segments = id.getPath().split("/");
        String path = segments[segments.length - 1];
        segments = path.split("_");
        HashSet<String> parts = new HashSet<>();
        parts.add("head");
        parts.add("binding");
        parts.add("handle");
        if (id.getNamespace().equals(Smithee.MOD_ID) && path.endsWith(".png") && segments.length == 3 && parts.contains(segments[2].split("\\.")[0])) {
            try {
                File file = new File("config/smithee/items/" + path);
                cir.setReturnValue(new ResourceImpl(Smithee.MOD_ID, id, new FileInputStream(file), null));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            cir.cancel();
        }
    }

}
