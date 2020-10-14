package wraith.smithee.registry;

import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import wraith.smithee.ItemGroups;
import wraith.smithee.Utils;

import java.util.HashMap;
import java.util.HashSet;

public class ItemRegistry {

    public static HashMap<String, Item> ITEMS = new HashMap<>();

    public static HashSet<String> MATERIALS = new HashSet<String>() {{
        add("wooden");
        add("stone");
        add("iron");
        add("golden");
        add("diamond");
        add("netherite");
    }};
    public static HashSet<String> TOOL_TYPES = new HashSet<String>() {{
        add("pickaxe");
        add("axe");
        add("shovel");
        add("sword");
        add("hoe");
    }};
    public static HashSet<String> PART_TYPES = new HashSet<String>() {{
        add("head");
        add("binding");
        add("handle");
    }};

    public static void addItems() {
        for (String material : MATERIALS) {
            for (String tool : TOOL_TYPES) {
                for (String part : PART_TYPES) {
                    ITEMS.put(material + "_" + tool + "_" + part, new Item(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
                }
            }
        }
    }

    public static void registerItems() {
        for (String id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, Utils.ID(id), ITEMS.get(id));
        }
    }

}
