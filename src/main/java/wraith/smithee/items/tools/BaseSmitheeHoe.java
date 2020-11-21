package wraith.smithee.items.tools;

import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;

public class BaseSmitheeHoe extends HoeItem implements BaseSmitheeTool {

    public BaseSmitheeHoe(Item.Settings settings) {
        super(ToolMaterials.WOOD, 0, 0, settings);
    }

}
