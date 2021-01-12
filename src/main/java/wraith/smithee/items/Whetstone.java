package wraith.smithee.items;

import net.minecraft.item.Item;

public class Whetstone extends Item {

    String material;

    public Whetstone(Settings settings, String material) {
        super(settings);
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }
}
