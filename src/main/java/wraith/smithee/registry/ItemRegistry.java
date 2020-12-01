package wraith.smithee.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.registry.Registry;
import wraith.smithee.Config;
import wraith.smithee.ItemGroups;
import wraith.smithee.items.Chisel;
import wraith.smithee.items.tool_parts.Part;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.items.tools.*;
import wraith.smithee.properties.Properties;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.utils.JsonParser;
import wraith.smithee.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemRegistry {

    public static HashMap<String, Item> ITEMS = new HashMap<>();
    public static HashSet<String> MATERIALS = new HashSet<>();

    public static HashSet<String> TOOL_TYPES = new HashSet<String>() {{
        add("pickaxe");
        add("axe");
        add("shovel");
        add("sword");
        add("hoe");
    }};

    public static final HashMap<String, Properties> PROPERTIES = new HashMap<>();

    //Material -> [Part_Type --> Recipe]
    public static HashMap<Item, HashMap<String, ToolPartRecipe>> TOOL_PART_RECIPES = new HashMap<>();

    public static void addItems() {
        for (String material : MATERIALS) {
            for (String tool : TOOL_TYPES) {
                int durability = PROPERTIES.get(material).partProperties.get("head").durability;
                ITEMS.put(material + "_" + tool + "_head", new ToolPartItem(new Part(material, "head", tool), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
                durability = PROPERTIES.get(material).partProperties.get("binding").durability;
                ITEMS.put(material + "_" + tool + "_binding", new ToolPartItem(new Part(material, "binding", tool), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
                durability = PROPERTIES.get(material).partProperties.get("handle").durability;
                ITEMS.put(material + "_" + tool + "_handle", new ToolPartItem(new Part(material, "handle", tool), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
            }
        }
        ITEMS.put("oak_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("oak_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("dark_oak_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("dark_oak_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("spruce_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("spruce_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("birch_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("birch_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("jungle_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("jungle_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("acacia_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get("acacia_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));

        ITEMS.put("stone_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("stone_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("cobblestone_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("cobblestone_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("mossy_cobblestone_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("mossy_cobblestone_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("diorite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("diorite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("andesite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("andesite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("granite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("granite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));

        ITEMS.put("oak_chiseling_table", new BlockItem(BlockRegistry.BLOCKS.get("oak_chiseling_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("dark_oak_chiseling_table", new BlockItem(BlockRegistry.BLOCKS.get("dark_oak_chiseling_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("spruce_chiseling_table", new BlockItem(BlockRegistry.BLOCKS.get("spruce_chiseling_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("birch_chiseling_table", new BlockItem(BlockRegistry.BLOCKS.get("birch_chiseling_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("jungle_chiseling_table", new BlockItem(BlockRegistry.BLOCKS.get("jungle_chiseling_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("acacia_chiseling_table", new BlockItem(BlockRegistry.BLOCKS.get("acacia_chiseling_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));

        ITEMS.put("base_smithee_pickaxe", new BaseSmitheePickaxe(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ITEMS.put("base_smithee_axe", new BaseSmitheeAxe(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ITEMS.put("base_smithee_shovel", new BaseSmitheeShovel(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ITEMS.put("base_smithee_hoe", new BaseSmitheeHoe(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ITEMS.put("base_smithee_sword", new BaseSmitheeSword(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));

        ITEMS.put("flint_chisel", new Chisel(new Item.Settings().maxDamage(6).group(ItemGroups.SMITHEE_ITEMS)));

        ITEMS.put("wooden_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("stone_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("diamond_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
    }

    public static void addMaterials() {
        JsonArray array = Config.getJsonObject(Config.readFile(new File("config/smithee/items/list.json"))).get("materials").getAsJsonArray();
        for (JsonElement element : array) {
            MATERIALS.add(element.getAsString());
        }
    }

    public static void registerItems() {
        for (String id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, Utils.ID(id), ITEMS.get(id));
        }
    }

    public static void generateProperties() {
        File[] files = Config.getFiles("config/smithee/stats/");
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

            }
            catch(Exception e) {
                System.out.println("Found error with file '" + file.getName() + "'");
            }
        }
    }

    public static void generateRecipes() {
        File[] files = Config.getFiles("config/smithee/recipes/");
        if (files == null) {
            return ;
        }
        for(File file : files) {
            JsonObject json = Config.getJsonObject(Config.readFile(file));
            try {
                Set<Map.Entry<String, JsonElement>> recipes = json.entrySet();
                JsonParser.parseRecipes(recipes, TOOL_PART_RECIPES);
            }
            catch(Exception e) {
                System.out.println("Found error with file '" + file.getName() + "'");
            }
        }
    }
}
