package wraith.smithee;

import net.minecraft.item.ToolMaterial;
import wraith.smithee.registry.ItemRegistry;

import java.util.HashMap;

public class ToolMaterials {

    public static ToolMaterial createToolMaterial(String handle, String binding, String head) {
        RepairType handleType = ItemRegistry.MATERIALS.get(head);
        RepairType bindingType = ItemRegistry.MATERIALS.get(head);
        RepairType headType = ItemRegistry.MATERIALS.get(head);
        return new CustomToolMaterial();
    }

}
