package wraith.smithee.items.tool_parts;

import net.minecraft.item.Item;
import wraith.smithee.utils.Utils;

public class ToolPartItem extends Item{

    public Part part;

    public ToolPartItem(Part part, Item.Settings settings) {
        super(settings);
        this.part = part;
    }

    @Override
    public String toString() {
        return Utils.capitalize(this.part.materialName.split("_")) + " " + ("any".equals(this.part.toolType)?"":(Utils.capitalize(this.part.toolType.split("_")) + " "))  + Utils.capitalize(this.part.partType.split("_"));
    }

}
