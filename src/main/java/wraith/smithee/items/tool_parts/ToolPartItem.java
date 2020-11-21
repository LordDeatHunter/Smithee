package wraith.smithee.items.tool_parts;

import net.minecraft.item.Item;

public class ToolPartItem extends Item{

    public Part part;

    public ToolPartItem(Part part, Item.Settings settings) {
        super(settings);
        this.part = part;
    }


}
