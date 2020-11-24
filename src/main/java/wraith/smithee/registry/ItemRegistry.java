package wraith.smithee.registry;

import com.google.gson.JsonObject;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.registry.Registry;
import wraith.smithee.Config;
import wraith.smithee.ItemGroups;
import wraith.smithee.utils.JsonParser;
import wraith.smithee.utils.Utils;
import wraith.smithee.items.tool_parts.Part;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.items.tools.*;
import wraith.smithee.properties.Properties;

import java.io.File;
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
    public static final HashMap<String, Properties> PROPERTIES = new HashMap<>();

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

    public static void generateProperties() {
        File[] files = Config.getFiles("config/smithee/parts/");
        if (files == null) {
            return ;
        }
        for(File file : files) {
            JsonObject json = Config.getJsonObject(Config.readFile(file));
            try {
                String[] segments = file.getName().split("/");
                String filename = segments[segments.length - 1].split("\\.")[0];

                PROPERTIES.put(filename, new Properties());

                JsonObject parts = json.get("individual_parts").getAsJsonObject();
                JsonParser.parseIndividualPart(parts.get("head").getAsJsonObject(), PROPERTIES.get(filename), "head");
                JsonParser.parseIndividualPart(parts.get("binding").getAsJsonObject(), PROPERTIES.get(filename), "binding");
                JsonParser.parseIndividualPart(parts.get("handle").getAsJsonObject(), PROPERTIES.get(filename), "handle");

                if (json.has("two_parts")) {
                    if (json.get("two_parts").getAsJsonObject().has("combinations")) {
                        JsonParser.parseTwoParts(json.get("two_parts").getAsJsonObject().get("combinations").getAsJsonArray(), PROPERTIES.get(filename));
                    }
                }
                if (json.has("two_parts")) {
                    JsonParser.parseFullTool(json.get("full_tool").getAsJsonObject(), PROPERTIES.get(filename));
                }
            }
            catch(Exception e) {
                System.out.println("Found error with file '" + file.getName() + "'");
            }
        }
    }

}
