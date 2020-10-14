package wraith.smithee.items;

import net.minecraft.item.Item;
import wraith.smithee.parts.BindingPart;

public class BindingItem extends Item {

    public BindingPart part;

    public BindingItem(BindingPart part, Item.Settings settings) {
        super(settings);
        this.part = part;
    }

}
