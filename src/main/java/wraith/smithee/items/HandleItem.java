package wraith.smithee.items;

import net.minecraft.item.Item;
import wraith.smithee.parts.HandlePart;

public class HandleItem extends Item {

    public HandlePart part;

    public HandleItem(HandlePart part, Item.Settings settings) {
        super(settings);
        this.part = part;
    }
}
