package wraith.smithee.registry;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import wraith.smithee.screens.AssemblyTableScreenHandler;
import wraith.smithee.screens.ChiselingTableScreenHandler;
import wraith.smithee.screens.DisassemblyTableScreenHandler;
import wraith.smithee.utils.Utils;

import java.util.HashMap;

public class ScreenHandlerRegistry {

    public static HashMap<String, ScreenHandlerType<? extends ScreenHandler>> SCREEN_HANDLERS = new HashMap<>();

    public static void registerScreenHandlers() {
        SCREEN_HANDLERS.put("assembly_table", net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerSimple(Utils.ID("assembly_table"), AssemblyTableScreenHandler::new));
        SCREEN_HANDLERS.put("disassembly_table", net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerSimple(Utils.ID("disassembly_table"), DisassemblyTableScreenHandler::new));
        SCREEN_HANDLERS.put("chiseling_table", net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerSimple(Utils.ID("chiseling_table"), ChiselingTableScreenHandler::new));
    }

}
