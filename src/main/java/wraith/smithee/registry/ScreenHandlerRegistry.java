package wraith.smithee.registry;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import wraith.smithee.Utils;
import wraith.smithee.screens.ToolStationScreenHandler;

import java.util.HashMap;

public class ScreenHandlerRegistry {

    public static HashMap<String, ScreenHandlerType<? extends ScreenHandler>> SCREEN_HANDLERS = new HashMap<>();

    public static void registerScreenHandlers() {
        SCREEN_HANDLERS.put("tool_station", net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerSimple(Utils.ID("tool_station"), ToolStationScreenHandler::new));
    }

}
