package wraith.smithee.items;

import net.minecraft.item.Item;
import wraith.smithee.parts.HeadPart;

public class HeadItem extends Item {

    public HeadPart part;

    public HeadItem(HeadPart part, Item.Settings settings) {
        super(settings);
        this.part = part;
    }

}
