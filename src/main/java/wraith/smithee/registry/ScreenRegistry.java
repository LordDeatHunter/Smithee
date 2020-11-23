package wraith.smithee.registry;

import wraith.smithee.screens.AssemblyTableScreen;
import wraith.smithee.screens.DisassemblyTableScreen;

public class ScreenRegistry {

    public static void registerScreens() {
        net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.register(ScreenHandlerRegistry.SCREEN_HANDLERS.get("assembly_table"), AssemblyTableScreen::new);
        net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.register(ScreenHandlerRegistry.SCREEN_HANDLERS.get("disassembly_table"), DisassemblyTableScreen::new);
    }

}
