package wraith.smithee;

import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.smithee.registry.BlockEntityRegistry;
import wraith.smithee.registry.BlockRegistry;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.utils.Utils;

public class Smithee implements ModInitializer {

    public static final String MOD_ID = "smithee";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("[Smithee] is loading.");

        JsonObject json = Config.loadConfig();

        ItemRegistry.addItems();
        ItemRegistry.registerItems();
        BlockRegistry.registerBlocks();
        BlockEntityRegistry.addBlockEntities();
        BlockEntityRegistry.registerBlockEntities();
        ScreenHandlerRegistry.registerScreenHandlers();

        if (!json.has("regenerate_deleted_files") || json.get("regenerate_deleted_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/", json.has("replace_old_files_when_regenerating") && json.get("replace_old_files_when_regenerating").getAsBoolean());
            ItemRegistry.generateProperties();
        }

        LOGGER.info("[Smithee] has successfully been loaded.");
    }


}
