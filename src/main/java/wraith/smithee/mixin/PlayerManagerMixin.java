package wraith.smithee.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wraith.smithee.Config;
import wraith.smithee.Smithee;
import wraith.smithee.utils.Utils;

import java.io.File;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("disable_tools", Smithee.DISABLE_TOOLS);
        String contents = Config.readFile(new File("config/smithee/materials.json"));
        tag.putString("materials", contents);

        contents = Config.readFile(new File("config/smithee/emboss_materials.json"));
        tag.putString("emboss_materials", contents);

        File[] files = Config.getFiles("config/smithee/chisels/");
        CompoundTag subtag = new CompoundTag();
        for (File file : files) {
            contents = Config.readFile(file);
            subtag.putString(file.getName(), contents);
        }
        tag.put("chisels", subtag);

        files = Config.getFiles("config/smithee/combinations/");
        subtag = new CompoundTag();
        for (File file : files) {
            contents = Config.readFile(file);
            subtag.putString(file.getName(), contents);
        }
        tag.put("combinations", subtag);

        files = Config.getFiles("config/smithee/modifiers/");
        subtag = new CompoundTag();
        for (File file : files) {
            contents = Config.readFile(file);
            subtag.putString(file.getName(), contents);
        }
        tag.put("modifiers", subtag);

        files = Config.getFiles("config/smithee/recipes/");
        subtag = new CompoundTag();
        for (File file : files) {
            contents = Config.readFile(file);
            subtag.putString(file.getName(), contents);
        }
        tag.put("recipes", subtag);

        files = Config.getFiles("config/smithee/shards/");
        subtag = new CompoundTag();
        for (File file : files) {
            contents = Config.readFile(file);
            subtag.putString(file.getName(), contents);
        }
        tag.put("shards", subtag);

        files = Config.getFiles("config/smithee/smithing/");
        subtag = new CompoundTag();
        for (File file : files) {
            contents = Config.readFile(file);
            subtag.putString(file.getName(), contents);
        }
        tag.put("smithing", subtag);

        files = Config.getFiles("config/smithee/stats/");
        subtag = new CompoundTag();
        for (File file : files) {
            contents = Config.readFile(file);
            subtag.putString(file.getName(), contents);
        }
        tag.put("stats", subtag);

        data.writeCompoundTag(tag);
        ServerPlayNetworking.send(player, Utils.ID("connect_packet"), data);
    }

}
