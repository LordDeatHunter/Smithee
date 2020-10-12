package wraith.smithee;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Smithee implements ModInitializer {

    public static final String MOD_ID = "smithee";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("[Smithee] is loading.");

        

        LOGGER.info("[Smithee] has successfully been loaded.");
    }


}
