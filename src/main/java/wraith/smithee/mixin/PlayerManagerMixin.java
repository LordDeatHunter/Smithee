package wraith.smithee.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        CompoundTag tag = new CompoundTag();
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

        data.writeCompoundTag(tag);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, Utils.ID("connect_packet"), data);
    }

}
