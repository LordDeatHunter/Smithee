package wraith.smithee.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.smithee.Config;
import wraith.smithee.properties.*;
import wraith.smithee.properties.Properties;

import java.io.File;
import java.util.*;

public class JsonParser {

    public static void parseIndividualPart(JsonObject part, Properties properties, String type) {
        int mineLevel = part.get("mining_level").getAsInt();
        int durability = part.get("durability").getAsInt();
        float mineSpeed = part.get("mining_speed").getAsFloat();
        float attackDamage = part.get("attack_damage").getAsFloat();
        float attackSpeed = part.get("attack_speed").getAsFloat();

        properties.partProperties.put(type, new Property(mineSpeed, mineLevel, durability, attackDamage, attackSpeed));
        if (!part.has("traits")) {
            return;
        }
        for (JsonElement trait : part.get("traits").getAsJsonArray()) {
            JsonObject traitObject = trait.getAsJsonObject();
            String traitName = traitObject.get("trait").getAsString();
            int minLevel = traitObject.get("min_level").getAsInt();
            int maxLevel = traitObject.get("max_level").getAsInt();
            double chance = traitObject.get("chance").getAsDouble();
            properties.traits.get(type).add(new Trait(traitName, minLevel, maxLevel, chance));
        }

    }

    public static void parseRecipes(Set<Map.Entry<String, JsonElement>> recipes, HashMap<Item, HashMap<String, ToolPartRecipe>> properties) {
        for (Map.Entry<String, JsonElement> entry : recipes) {
            JsonObject recipe = entry.getValue().getAsJsonObject();
            String material = entry.getKey();

            HashSet<Item> items = new HashSet<>();
            if (material.startsWith("#")) {
                items.addAll(TagRegistry.item(new Identifier(material.substring(1))).values());
            } else {
                items.add(Registry.ITEM.get(new Identifier(material)));
            }
            for (Item item : items) {
                properties.put(item, new HashMap<>());
                parseRecipeType(recipe.getAsJsonObject("head"), properties.get(item), "head");
                parseRecipeType(recipe.getAsJsonObject("binding"), properties.get(item), "binding");
                parseRecipeType(recipe.getAsJsonObject("handle"), properties.get(item), "handle");
            }
        }
    }

    private static void parseRecipeType(JsonObject recipe, HashMap<String, ToolPartRecipe> properties, String type) {
        String outputMaterial = recipe.get("output_material").getAsString();
        int requiredMaterialAmount = recipe.get("required_material_amount").getAsInt();
        if (!recipe.get("drop_remains").getAsBoolean()) {
            properties.put(type, new ToolPartRecipe(outputMaterial, requiredMaterialAmount));
        } else {
            String remains = recipe.get("remains").getAsString();
            int remainAmount = recipe.get("remains_amount").getAsInt();
            properties.put(type, new ToolPartRecipe(outputMaterial, requiredMaterialAmount, remains, remainAmount));
        }
    }

    public static void parseCombinations() {
        File[] files = Config.getFiles("config/smithee/combinations");
        for (File file : files){
            JsonObject json = Config.getJsonObject(Config.readFile(file));
            for (JsonElement combination : json.get("combinations").getAsJsonArray()) {

                HashMap<String, HashSet<String>> includes = new HashMap<>();
                includes.put("head", new HashSet<>());
                includes.put("binding", new HashSet<>());
                includes.put("handle", new HashSet<>());

                for (JsonElement element : combination.getAsJsonObject().get("includes").getAsJsonArray()) {
                    String[] segments = element.getAsString().split("_");
                    String material = "";
                    for (int i = 0; i < segments.length - 1; ++i) {
                        material += segments[i];
                    }
                    includes.get(segments[segments.length - 1]).add(material);
                }

                HashMap<String, HashSet<String>> excludes = new HashMap<>();
                excludes.put("head", new HashSet<>());
                excludes.put("binding", new HashSet<>());
                excludes.put("handle", new HashSet<>());

                for (JsonElement element : combination.getAsJsonObject().get("excludes").getAsJsonArray()) {
                    String[] segments = element.getAsString().split("_");
                    String material = "";
                    for (int i = 0; i < segments.length - 1; ++i) {
                        material += segments[i];
                    }
                    excludes.get(segments[segments.length - 1]).add(material);
                }

                HashMap<String, String> properties = new HashMap<>();
                JsonObject obj = combination.getAsJsonObject();

                if (obj.has("miningSpeed")) {
                    String miningSpeed = obj.get("mining_speed").getAsString();
                    properties.put("mining_speed", miningSpeed);
                } else if (obj.has("miningLevel")) {
                    String miningLevel = obj.get("mining_level").getAsString();
                    properties.put("mining_level", miningLevel);
                } else if (obj.has("durability")) {
                    String durability = obj.get("durability").getAsString();
                    properties.put("durability", durability);
                } else if (obj.has("attackDamage")) {
                    String attackDamage = obj.get("attack_damage").getAsString();
                    properties.put("attack_damage", attackDamage);
                } else if (obj.has("attackSpeed")) {
                    String attackSpeed = obj.get("attack_speed").getAsString();
                    properties.put("attack_speed", attackSpeed);
                }
                PartCombination.COMBINATIONS.add(new PartCombination(includes, excludes, properties));
            }
        }
    }

}