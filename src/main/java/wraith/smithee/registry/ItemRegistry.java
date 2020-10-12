package wraith.smithee.registry;

import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import wraith.smithee.ItemGroups;
import wraith.smithee.RepairType;
import wraith.smithee.Utils;

import java.util.HashMap;
import java.util.HashSet;

public class ItemRegistry {

    public static HashMap<String, Item> ITEMS = new HashMap<>();
    public static HashMap<String, RepairType> MATERIALS = new HashMap<String, RepairType>() {{
        put("wooden", new RepairType("tag", "minecraft", "planks"));
        put("stone", new RepairType("item", "minecraft", "cobblestone"));
        put("iron", new RepairType("item", "minecraft", "iron_ingot"));
        put("golden", new RepairType("item", "minecraft", "gold_ingot"));
        put("diamond", new RepairType("item", "minecraft", "diamond"));
        put("netherite", new RepairType("item", "minecraft", "netherite_ingot"));
    }};
    public static HashSet<String> TOOL_TYPES = new HashSet<String>() {{
        add("pickaxe");
        add("axe");
        add("shovel");
        add("sword");
        add("hoe");
    }};

    public static void addItems() {
        for (String material : MATERIALS.keySet()) {
            for (String tool : TOOL_TYPES) {
                ITEMS.put(material + "_" + tool, new Item(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
            }
        }
    }

    public static void registerItem() {
        for (String id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, Utils.ID(id), ITEMS.get(id));
        }
    }

}
