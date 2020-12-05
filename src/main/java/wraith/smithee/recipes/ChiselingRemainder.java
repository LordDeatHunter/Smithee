package wraith.smithee.recipes;

import net.minecraft.item.Item;

import java.util.HashSet;

public class ChiselingRemainder {
    public HashSet<Item> remainders;
    public int worth;

    public ChiselingRemainder(HashSet<Item> remainders, int worth) {
        this.remainders = remainders;
        this.worth = worth;
    }

}
