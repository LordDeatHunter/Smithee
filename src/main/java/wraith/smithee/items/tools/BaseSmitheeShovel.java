package wraith.smithee.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterials;

public class BaseSmitheeShovel extends ShovelItem implements BaseSmitheeTool {

    public BaseSmitheeShovel(Item.Settings settings) {
        super(ToolMaterials.WOOD, 0, 0, settings);
    }

}
