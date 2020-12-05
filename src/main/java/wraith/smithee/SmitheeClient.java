package wraith.smithee;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashSet;

public class SmitheeClient implements ClientModInitializer {

    public static final HashSet<String> RENDERING_TOOL_PARTS = new HashSet<>();

    @Override
    public void onInitializeClient() {
        ScreenRegistry.registerScreens();
        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
            for (String material : ItemRegistry.MATERIALS) {
                for (String tool : ItemRegistry.TOOL_TYPES) {
                    String id;

                    id = material + "_" + tool + "_head";
                    RENDERING_TOOL_PARTS.add(id);
                    out.accept(Utils.inventoryModelID(id));

                    id = material + "_" + tool + "_binding";
                    RENDERING_TOOL_PARTS.add(id);
                    out.accept(Utils.inventoryModelID(id));

                    id = material + "_" + tool + "_handle";
                    RENDERING_TOOL_PARTS.add(id);
                    out.accept(Utils.inventoryModelID(id));
                }
            }
        });
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> new ItemModelProvider());
    }

}
