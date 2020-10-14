package wraith.smithee;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import wraith.smithee.mod_support.AstromineSupport;
import wraith.smithee.registry.BlockEntityRegistry;
import wraith.smithee.registry.BlockRegistry;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;

public class Smithee implements ModInitializer {

    public static final String MOD_ID = "smithee";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("[Smithee] is loading.");

        ItemRegistry.addItems();

        if (FabricLoader.getInstance().isModLoaded("astromine-foundations")) {
            AstromineSupport.load();
        }

        ItemRegistry.registerItems();
        BlockRegistry.registerBlocks();
        BlockEntityRegistry.registerBlockEntities();
        ScreenHandlerRegistry.registerScreenHandlers();

        LOGGER.info("[Smithee] has successfully been loaded.");
    }


}
