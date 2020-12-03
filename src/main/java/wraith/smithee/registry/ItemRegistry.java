package wraith.smithee.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.icu.impl.USerializedSet;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.smithee.Config;
import wraith.smithee.ItemGroups;
import wraith.smithee.Smithee;
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

    public static final HashSet<Item> DISABLED_ITEMS = new HashSet<>();
    public static HashMap<String, Item> ITEMS = new HashMap<>();
    public static HashSet<String> MATERIALS = new HashSet<>();

    public static HashSet<String> TOOL_TYPES = new HashSet<String>() {{
        add("pickaxe");
        add("axe");
        add("shovel");
        add("sword");
        add("hoe");
    }};

    //Material -> Properties
    public static final HashMap<String, Properties> PROPERTIES = new HashMap<>();

    //Material -> [Part_Type --> Recipe]
    public static HashMap<Item, HashMap<String, ToolPartRecipe>> TOOL_PART_RECIPES = new HashMap<>();

    public static void addItems() {
        for (String material : MATERIALS) {
            for (String tool : TOOL_TYPES) {
                try {
                    int durability = PROPERTIES.get(material).partProperties.get("head").durability;
                    ITEMS.put(material + "_" + tool + "_head", new ToolPartItem(new Part(material, "head", tool), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
                    durability = PROPERTIES.get(material).partProperties.get("binding").durability;
                    ITEMS.put(material + "_" + tool + "_binding", new ToolPartItem(new Part(material, "binding", tool), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
                    durability = PROPERTIES.get(material).partProperties.get("handle").durability;
                    ITEMS.put(material + "_" + tool + "_handle", new ToolPartItem(new Part(material, "handle", tool), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
                } catch (Exception e) {
                    Smithee.LOGGER.error("Error with material " + material);
                }
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
        ITEMS.put("netherrack_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("netherrack_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));

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

        ITEMS.put("oak_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("dark_oak_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("birch_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("acacia_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("jungle_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("spruce_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("stone_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("andesite_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("diorite_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("granite_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("netherrack_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("diamond_shard", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
    }

    public static void setDisabledItems() {
        DISABLED_ITEMS.add(Items.WOODEN_AXE);
        DISABLED_ITEMS.add(Items.WOODEN_PICKAXE);
        DISABLED_ITEMS.add(Items.WOODEN_SHOVEL);
        DISABLED_ITEMS.add(Items.WOODEN_SWORD);
        DISABLED_ITEMS.add(Items.WOODEN_HOE);

        DISABLED_ITEMS.add(Items.GOLDEN_AXE);
        DISABLED_ITEMS.add(Items.GOLDEN_PICKAXE);
        DISABLED_ITEMS.add(Items.GOLDEN_SHOVEL);
        DISABLED_ITEMS.add(Items.GOLDEN_SWORD);
        DISABLED_ITEMS.add(Items.GOLDEN_HOE);

        DISABLED_ITEMS.add(Items.STONE_AXE);
        DISABLED_ITEMS.add(Items.STONE_PICKAXE);
        DISABLED_ITEMS.add(Items.STONE_SHOVEL);
        DISABLED_ITEMS.add(Items.STONE_SWORD);
        DISABLED_ITEMS.add(Items.STONE_HOE);

        DISABLED_ITEMS.add(Items.IRON_AXE);
        DISABLED_ITEMS.add(Items.IRON_PICKAXE);
        DISABLED_ITEMS.add(Items.IRON_SHOVEL);
        DISABLED_ITEMS.add(Items.IRON_SWORD);
        DISABLED_ITEMS.add(Items.IRON_HOE);

        DISABLED_ITEMS.add(Items.DIAMOND_AXE);
        DISABLED_ITEMS.add(Items.DIAMOND_PICKAXE);
        DISABLED_ITEMS.add(Items.DIAMOND_SHOVEL);
        DISABLED_ITEMS.add(Items.DIAMOND_SWORD);
        DISABLED_ITEMS.add(Items.DIAMOND_HOE);

        DISABLED_ITEMS.add(Items.NETHERITE_AXE);
        DISABLED_ITEMS.add(Items.NETHERITE_PICKAXE);
        DISABLED_ITEMS.add(Items.NETHERITE_SHOVEL);
        DISABLED_ITEMS.add(Items.NETHERITE_SWORD);
        DISABLED_ITEMS.add(Items.NETHERITE_HOE);
    }

    public static void addMaterials() {
        JsonArray array = Config.getJsonObject(Config.readFile(new File("config/smithee/materials.json"))).get("materials").getAsJsonArray();
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
                PROPERTIES.get(filename).traits.put("head", new HashSet<>());
                PROPERTIES.get(filename).traits.put("binding", new HashSet<>());
                PROPERTIES.get(filename).traits.put("handle", new HashSet<>());
                JsonParser.parseIndividualPart(parts.get("head").getAsJsonObject(), PROPERTIES.get(filename), "head");
                JsonParser.parseIndividualPart(parts.get("binding").getAsJsonObject(), PROPERTIES.get(filename), "binding");
                JsonParser.parseIndividualPart(parts.get("handle").getAsJsonObject(), PROPERTIES.get(filename), "handle");

            }
            catch(Exception e) {
                System.out.println("Found error with stats file '" + file.getName() + "'");
            }
        }
    }

    public static void generateRecipes() {
        File[] files = Config.getFiles("config/smithee/recipes/");
        if (files == null) {
            return ;
        }
        TOOL_PART_RECIPES.clear();
        for(File file : files) {
            JsonObject json = Config.getJsonObject(Config.readFile(file));
            try {
                Set<Map.Entry<String, JsonElement>> recipes = json.entrySet();
                JsonParser.parseRecipes(recipes, TOOL_PART_RECIPES);
            }
            catch(Exception e) {
                Smithee.LOGGER.warn("Found error with recipes file '" + file.getName() + "'");
            }
        }
    }
}
