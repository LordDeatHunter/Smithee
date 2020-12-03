package wraith.smithee;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import wraith.smithee.registry.ScreenRegistry;

public class SmitheeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.registerScreens();
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> new ItemModelProvider());
    }

}
