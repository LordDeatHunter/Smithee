package wraith.smithee.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.registry.Registry;
import wraith.smithee.ItemGroups;
import wraith.smithee.Utils;
import wraith.smithee.items.tool_parts.Part;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.items.tools.*;

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
                ITEMS.put(material + "_" + tool + "_head", new ToolPartItem(new Part(material, "head", tool, MATERIALS.get(material)), new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
                ITEMS.put(material + "_" + tool + "_binding", new ToolPartItem(new Part(material, "binding", tool, MATERIALS.get(material)), new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
                ITEMS.put(material + "_" + tool + "_handle", new ToolPartItem(new Part(material, "handle", tool, MATERIALS.get(material)), new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
            }
        }
        ITEMS.put("oak_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("oak_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("dark_oak_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("dark_oak_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("spruce_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("spruce_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("birch_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("birch_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("jungle_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("jungle_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("acacia_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("acacia_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));

        ITEMS.put("stone_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("stone_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("diorite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("diorite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("andesite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("andesite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("granite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("granite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));

        ITEMS.put("base_smithee_pickaxe", new BaseSmitheePickaxe(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ITEMS.put("base_smithee_axe", new BaseSmitheeAxe(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ITEMS.put("base_smithee_shovel", new BaseSmitheeShovel(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ITEMS.put("base_smithee_hoe", new BaseSmitheeHoe(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ITEMS.put("base_smithee_sword", new BaseSmitheeSword(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
    }

    public static void registerItems() {
        for (String id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, Utils.ID(id), ITEMS.get(id));
        }
    }

}
