package wraith.smithee.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.registry.Registry;
import wraith.smithee.ItemGroups;
import wraith.smithee.Utils;
import wraith.smithee.items.BindingItem;
import wraith.smithee.items.HandleItem;
import wraith.smithee.items.HeadItem;
import wraith.smithee.parts.BindingPart;
import wraith.smithee.parts.HandlePart;
import wraith.smithee.parts.HeadPart;

import java.util.HashMap;
import java.util.HashSet;

public class ItemRegistry {

    public static HashMap<String, Item> ITEMS = new HashMap<>();

    public static HashMap<String, ToolMaterial> MATERIALS = new HashMap<String, ToolMaterial>() {{
        put("wooden", ToolMaterials.WOOD);
        put("stone", ToolMaterials.STONE);
        put("iron", ToolMaterials.IRON);
        put("golden", ToolMaterials.GOLD);
        put("diamond", ToolMaterials.DIAMOND);
        put("netherite", ToolMaterials.NETHERITE);
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
                ITEMS.put(material + "_" + tool + "_head", new HeadItem(new HeadPart(material, tool, MATERIALS.get(material)), new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
                ITEMS.put(material + "_" + tool + "_binding", new BindingItem(new BindingPart(material, tool, MATERIALS.get(material)), new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
                ITEMS.put(material + "_" + tool + "_handle", new HandleItem(new HandlePart(material, tool, MATERIALS.get(material)), new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
            }
        }
        ITEMS.put("oak_tool_station", new BlockItem(BlockRegistry.BLOCKS.get("oak_tool_station"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("dark_oak_tool_station", new BlockItem(BlockRegistry.BLOCKS.get("dark_oak_tool_station"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("spruce_tool_station", new BlockItem(BlockRegistry.BLOCKS.get("spruce_tool_station"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("birch_tool_station", new BlockItem(BlockRegistry.BLOCKS.get("birch_tool_station"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("jungle_tool_station", new BlockItem(BlockRegistry.BLOCKS.get("jungle_tool_station"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
    }

    public static void registerItems() {
        for (String id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, Utils.ID(id), ITEMS.get(id));
        }
    }

}
