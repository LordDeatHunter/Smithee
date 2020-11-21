package wraith.smithee.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;

public class BaseSmitheePickaxe extends PickaxeItem implements BaseSmitheeTool {

    public BaseSmitheePickaxe(Item.Settings settings) {
        super(ToolMaterials.WOOD, 0, 0, settings);
    }

}
