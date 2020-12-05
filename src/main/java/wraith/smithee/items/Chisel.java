package wraith.smithee.items;

import net.minecraft.item.Item;

public class Chisel extends Item {

    private final int chiselingLevel;

    public Chisel(Settings settings, int chiselingLevel) {
        super(settings);
        this.chiselingLevel = chiselingLevel;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    public int getChiselingLevel() {
        return chiselingLevel;
    }

}
