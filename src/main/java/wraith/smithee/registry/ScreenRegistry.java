package wraith.smithee.registry;

import wraith.smithee.screens.ToolStationScreen;

public class ScreenRegistry {

    public static void registerScreens() {
        net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.register(ScreenHandlerRegistry.SCREEN_HANDLERS.get("tool_station"), ToolStationScreen::new);
    }

}
