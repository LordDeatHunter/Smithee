package wraith.smithee.items;

import net.minecraft.item.Item;

public class Chisel extends Item {
    public Chisel(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }



}
