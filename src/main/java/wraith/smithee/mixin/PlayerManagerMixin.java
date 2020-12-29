package wraith.smithee.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
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
        /*
        CompoundTag recipesTag = new CompoundTag();
        for (Item item : ItemRegistry.TOOL_PART_RECIPES.keySet()) {
            String id = Registry.ITEM.getId(item).toString();
            CompoundTag recipes = new CompoundTag();
            for (String part : ItemRegistry.TOOL_PART_RECIPES.get(item).keySet()) {
                CompoundTag recipe = new CompoundTag();
                ToolPartRecipe partRecipe = ItemRegistry.TOOL_PART_RECIPES.get(item).get(part);
                recipe.putInt("requiredAmount", partRecipe.requiredAmount);
                recipe.putString("outputMaterial", partRecipe.outputMaterial);
                recipe.putInt("chiselingLevel", partRecipe.chiselingLevel);
                recipes.put(part, recipe);
            }
            recipesTag.put(id, recipes);
        }
        tag.put("recipes", recipesTag);

        CompoundTag remainsTag  = new CompoundTag();
        for (String input : ItemRegistry.REMAINS.keySet()) {
            CompoundTag remains = new CompoundTag();
            for (Identifier id : ItemRegistry.REMAINS.get(input).keySet()) {
                int worth = ItemRegistry.REMAINS.get(input).get(id);
                remains.putInt(id.toString(), worth);
            }
            remainsTag.put(input, remains);
        }
        tag.put("remains", remainsTag);
         */

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
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, Utils.ID("connect_packet"), data);
    }

}
