package wraith.smithee.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.smithee.Config;
import wraith.smithee.ItemGroups;
import wraith.smithee.Smithee;
import wraith.smithee.items.Chisel;
import wraith.smithee.items.Whetstone;
import wraith.smithee.items.tool_parts.Part;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.items.tools.*;
import wraith.smithee.properties.ChiselingRecipe;
import wraith.smithee.properties.Properties;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.recipes.EmbossRecipe;
import wraith.smithee.utils.JsonParser;
import wraith.smithee.utils.Utils;

import java.io.File;
import java.util.*;

public class ItemRegistry {

    //TODO: Move this out of here
    public static final HashMap<String, String> MODELS = new HashMap<>();

    public static final HashSet<Item> DISABLED_ITEMS = new HashSet<>();
    public static HashMap<String, Item> ITEMS = new HashMap<>();
    public static HashSet<String> MATERIALS = new HashSet<>();
    public static final HashSet<String> EMBOSS_MATERIALS = new HashSet<>();
    public static final HashMap<String, Identifier> SHARDS = new HashMap<>();
    public static final ArrayList<ChiselingRecipe> CHISELING_RECIPES = new ArrayList<>();

    public static HashSet<String> TOOL_TYPES = new HashSet<String>() {{
        add("pickaxe");
        add("axe");
        add("shovel");
        add("sword");
        add("hoe");
    }};
    public static HashSet<String> BINDING_TYPES = new HashSet<String>() {{
        add("binding");
        add("sword_guard");
    }};
    public static HashSet<String> HANDLE_TYPES = new HashSet<String>() {{
        add("handle");
    }};

    //Material -> Properties
    public static final HashMap<String, Properties> PROPERTIES = new HashMap<>();

    //Material -> [Tool_Type__Part_Type] -> Recipe
    public static final HashMap<Item, HashMap<String, ToolPartRecipe>> TOOL_PART_RECIPES = new HashMap<>();

    public static final HashMap<String, EmbossRecipe> EMBOSS_RECIPES = new HashMap<>();

    //InputMaterial -> OutputItem -> Cost
    public static final HashMap<String, HashMap<Identifier, Integer>> REMAINS = new HashMap<>();

    public static HashMap<String, Integer> BASE_RECIPE_VALUES = new HashMap<String, Integer>(){{
        put("pickaxe_head", 27);
        put("hoe_head", 18);
        put("axe_head", 27);
        put("shovel_head", 9);
        put("sword_head", 18);

        put("embossment", 405);
        put("shard", 1);
        put("whetstone", 27);

        put("binding", 9);
        put("handle", 18);
        put("sword_guard", 18);
    }};

    public static void addItems() {
        for (String material : MATERIALS) {
            try {
                int durability;
                int dura = PROPERTIES.get(material).partProperties.get("head").durability * 3;
                ITEMS.put(material + "_whetstone", new Whetstone(new Item.Settings().maxCount(1).maxDamage(dura).group(ItemGroups.SMITHEE_ITEMS), material));
                for (String tool : TOOL_TYPES) {
                    durability = PROPERTIES.get(material).partProperties.get("head").durability;
                    ITEMS.put(material + "_" + tool + "_head", new ToolPartItem(new Part(material, "head", "head", tool), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
                }
                for (String binding : BINDING_TYPES) {
                    durability = PROPERTIES.get(material).partProperties.get("binding").durability;
                    ITEMS.put(material + "_" + binding, new ToolPartItem(new Part(material, binding, "binding", "any"), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
                }
                for (String handle : HANDLE_TYPES) {
                    durability = PROPERTIES.get(material).partProperties.get("handle").durability;
                    ITEMS.put(material + "_" + handle, new ToolPartItem(new Part(material, handle, "handle", "any"), new Item.Settings().maxDamage(durability).group(ItemGroups.SMITHEE_PARTS)));
                }
            } catch (Exception e) {
                Smithee.LOGGER.error("Error with material " + material);
            }
        }
        for (String material : EMBOSS_MATERIALS) {
            ITEMS.put(material + "_embossment", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        }
        for (ChiselingRecipe recipe : CHISELING_RECIPES) {
            ITEMS.put(recipe.material + "_chisel", new Chisel(new Item.Settings().maxDamage(recipe.durability).group(ItemGroups.SMITHEE_ITEMS), recipe.level));
        }

        ITEMS.put("silky_jewel", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));
        ITEMS.put("silky_cloth", new Item(new Item.Settings().group(ItemGroups.SMITHEE_ITEMS)));

        HashSet<String> woods = new HashSet<String>(){{
            add("oak");
            add("dark_oak");
            add("spruce");
            add("birch");
            add("acacia");
            add("jungle");
        }};
        for (String wood : woods) {
            ITEMS.put(wood + "_assembly_table", new BlockItem(BlockRegistry.BLOCKS.get(wood + "_assembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
            ITEMS.put(wood + "_chiseling_table", new BlockItem(BlockRegistry.BLOCKS.get(wood + "_chiseling_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        }

        ITEMS.put("stone_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("stone_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("cobblestone_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("cobblestone_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("mossy_cobblestone_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("mossy_cobblestone_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("diorite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("diorite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("andesite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("andesite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("granite_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("granite_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));
        ITEMS.put("netherrack_disassembly_table", new BlockItem(BlockRegistry.BLOCKS.get("netherrack_disassembly_table"), new Item.Settings().group(ItemGroups.SMITHEE_BLOCKS)));

        ITEMS.put("base_smithee_pickaxe", new BaseSmitheePickaxe(new Item.Settings()));
        ITEMS.put("base_smithee_axe", new BaseSmitheeAxe(new Item.Settings()));
        ITEMS.put("base_smithee_shovel", new BaseSmitheeShovel(new Item.Settings()));
        ITEMS.put("base_smithee_hoe", new BaseSmitheeHoe(new Item.Settings()));
        ITEMS.put("base_smithee_sword", new BaseSmitheeSword(new Item.Settings()));
        for (String id : BASE_RECIPE_VALUES.keySet()) {
            ITEMS.put("base_smithee_" + id, new Item(new Item.Settings()));
        }

        ITEMS.put("items_creative_icon", new Item(new Item.Settings()));
        ITEMS.put("parts_creative_icon", new Item(new Item.Settings()));
    }

    public static void setDisabledItems() {
        DISABLED_ITEMS.clear();

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

    public static void registerItems() {
        ArrayList<String> IDs = new ArrayList<>(ITEMS.keySet());
        Collections.sort(IDs);
        for (String id : IDs) {
            Registry.register(Registry.ITEM, Utils.ID(id), ITEMS.get(id));
        }
    }

    public static void addMaterials() {
        JsonArray array = Config.getJsonObject(Config.readFile(new File("config/smithee/materials.json"))).get("materials").getAsJsonArray();
        for (JsonElement element : array) {
            MATERIALS.add(element.getAsString());
        }
        array = Config.getJsonObject(Config.readFile(new File("config/smithee/emboss_materials.json"))).get("materials").getAsJsonArray();
        for (JsonElement element : array) {
            EMBOSS_MATERIALS.add(element.getAsString());
        }
    }
    public static void addMaterials(String materials, String emboss) {
        JsonArray array = Config.getJsonObject(materials).get("materials").getAsJsonArray();
        for (JsonElement element : array) {
            MATERIALS.add(element.getAsString());
        }
        array = Config.getJsonObject(emboss).get("materials").getAsJsonArray();
        for (JsonElement element : array) {
            EMBOSS_MATERIALS.add(element.getAsString());
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
                Smithee.LOGGER.error("Found error with stats file '" + file.getName() + "'");
            }
        }
    }
    public static void generateProperties(HashMap<String, String> contents) {
        if (contents == null || contents.size() == 0) {
            return;
        }
        for(Map.Entry<String, String> content : contents.entrySet()) {
            JsonObject json = Config.getJsonObject(content.getValue());

            String name = content.getKey();
            PROPERTIES.put(name, new Properties());

            JsonObject parts = json.get("individual_parts").getAsJsonObject();
            PROPERTIES.get(name).traits.put("head", new HashSet<>());
            PROPERTIES.get(name).traits.put("binding", new HashSet<>());
            PROPERTIES.get(name).traits.put("handle", new HashSet<>());
            JsonParser.parseIndividualPart(parts.get("head").getAsJsonObject(), PROPERTIES.get(name), "head");
            JsonParser.parseIndividualPart(parts.get("binding").getAsJsonObject(), PROPERTIES.get(name), "binding");
            JsonParser.parseIndividualPart(parts.get("handle").getAsJsonObject(), PROPERTIES.get(name), "handle");
        }
    }

    public static void generateChiselingStats() {
        File[] files = Config.getFiles("config/smithee/chisels/");
        if (files == null) {
            return;
        }
        for(File file : files) {
            JsonObject obj = Config.getJsonObject(Config.readFile(file));
            if (obj.has("requires_mod") && !FabricLoader.getInstance().isModLoaded(obj.get("requires_mod").getAsString())) {
                continue;
            }
            JsonArray json = obj.get("chisels").getAsJsonArray();
            try {
                JsonParser.parseChiselingStats(json, CHISELING_RECIPES);
            }
            catch(Exception e) {
                Smithee.LOGGER.warn("Found error with chiseling file '" + file.getName() + "'");
            }
        }
    }
    public static void generateChiselingStats(String[] contents) {
        if (contents == null || contents.length == 0) {
            return;
        }
        for (String content : contents) {
            JsonObject obj = Config.getJsonObject(content);
            if (obj.has("requires_mod") && !FabricLoader.getInstance().isModLoaded(obj.get("requires_mod").getAsString())) {
                continue;
            }
            JsonArray json = obj.get("chisels").getAsJsonArray();
            JsonParser.parseChiselingStats(json, CHISELING_RECIPES);
        }
    }

    public static void generateRecipes() {
        File[] files = Config.getFiles("config/smithee/recipes/");
        if (files == null) {
            return;
        }
        TOOL_PART_RECIPES.clear();
        REMAINS.clear();
        for(File file : files) {
            JsonObject json = Config.getJsonObject(Config.readFile(file));
            try {
                Set<Map.Entry<String, JsonElement>> recipes = json.entrySet();
                JsonParser.parseRecipes(recipes, TOOL_PART_RECIPES, REMAINS);
            }
            catch(Exception e) {
                Smithee.LOGGER.warn("Found error with recipes file '" + file.getName() + "'");
            }
        }
    }
    public static void generateRecipes(String[] contents) {
        if (contents == null || contents.length == 0) {
            return;
        }
        for (String content : contents) {
            JsonObject json = Config.getJsonObject(content);
            Set<Map.Entry<String, JsonElement>> recipes = json.entrySet();
            JsonParser.parseRecipes(recipes, TOOL_PART_RECIPES, REMAINS);
        }
    }

    public static void generateModifiers() {
        File[] files = Config.getFiles("config/smithee/modifiers/");
        if (files == null) {
            return;
        }
        EMBOSS_RECIPES.clear();
        for (File file : files) {
            JsonParser.parseModifiers(Config.getJsonObject(Config.readFile(file)), EMBOSS_RECIPES);
        }
    }
    public static void generateModifiers(String[] contents) {
        if (contents == null || contents.length == 0) {
            return;
        }
        EMBOSS_RECIPES.clear();
        for (String content : contents) {
            JsonParser.parseModifiers(Config.getJsonObject(content), EMBOSS_RECIPES);
        }
    }

    public static void generateShards() {
        File[] files = Config.getFiles("config/smithee/shards/");
        if (files == null) {
            return;
        }
        SHARDS.clear();
        for (File file : files) {
            JsonObject obj = Config.getJsonObject(Config.readFile(file));
            if (obj.has("requires_mod") && !FabricLoader.getInstance().isModLoaded(obj.get("requires_mod").getAsString())) {
                continue;
            }
            JsonParser.parseShards(obj, SHARDS);
        }
    }
    public static void generateShards(String[] contents) {
        if (contents == null || contents.length == 0) {
            return;
        }
        SHARDS.clear();
        for (String content : contents) {
            JsonObject obj = Config.getJsonObject(content);
            if (obj.has("requires_mod") && !FabricLoader.getInstance().isModLoaded(obj.get("requires_mod").getAsString())) {
                continue;
            }
            JsonParser.parseShards(obj, SHARDS);
        }
    }

    public static void generateModels() {
        File[] files = Config.getFiles("config/smithee/models/");
        if (files == null) {
            return;
        }
        MODELS.clear();
        for (File file : files) {
            JsonObject obj = Config.getJsonObject(Config.readFile(file));
            JsonParser.parseModels(obj, MODELS);
        }
    }

}
