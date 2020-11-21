package wraith.smithee.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class BaseSmitheeSword extends SwordItem implements BaseSmitheeTool {

    public BaseSmitheeSword(Item.Settings settings) {
        super(ToolMaterials.WOOD, 0, 0, settings);
    }

}
