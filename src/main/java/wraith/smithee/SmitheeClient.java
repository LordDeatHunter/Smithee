package wraith.smithee;

import net.fabricmc.api.ClientModInitializer;
import wraith.smithee.registry.ScreenRegistry;

public class SmitheeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.registerScreens();
    }
}
