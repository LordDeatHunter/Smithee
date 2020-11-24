package wraith.smithee.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import wraith.smithee.properties.Trait;
import wraith.smithee.properties.Properties;
import wraith.smithee.properties.Property;

import java.util.HashMap;
import java.util.HashSet;

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
            String activateOn = traitObject.get("activate_on").getAsString();
            double chance = traitObject.get("chance").getAsDouble();
            properties.traits.put(traitName, new Trait(traitName, minLevel, maxLevel, activateOn, chance));
        }
    }

    public static void parseTwoParts(JsonArray combinations, Properties properties) {
        for (JsonElement combination : combinations) {
            JsonObject combinationObject = combination.getAsJsonObject();
            String[] parts = combinationObject.get("parts").getAsString().split("\\+");
            HashSet<String> partsKey = new HashSet<>();
            partsKey.add(parts[0]);
            partsKey.add(parts[1]);
            properties.combinations.put(partsKey, new HashMap<>());
            if (combinationObject.has("mineLevel")) {
                properties.combinations.get(partsKey).put("mineLevel", combinationObject.get("mineLevel").getAsString());
            } else if (combinationObject.has("durability")) {
                properties.combinations.get(partsKey).put("durability", combinationObject.get("durability").getAsString());
            } else if (combinationObject.has("mineSpeed")) {
                properties.combinations.get(partsKey).put("mineSpeed", combinationObject.get("mineSpeed").getAsString());
            } else if (combinationObject.has("attackDamage")) {
                properties.combinations.get(partsKey).put("attackDamage", combinationObject.get("attackDamage").getAsString());
            } else if (combinationObject.has("attackSpeed")) {
                properties.combinations.get(partsKey).put("attackSpeed", combinationObject.get("attackSpeed").getAsString());
            }
        }
    }

    public static void parseFullTool(JsonObject combinationObject, Properties properties) {
        if (combinationObject.has("mineLevel")) {
            properties.fullToolProperties.put("mineLevel", combinationObject.get("mineLevel").getAsString());
        } else if (combinationObject.has("durability")) {
            properties.fullToolProperties.put("durability", combinationObject.get("durability").getAsString());
        } else if (combinationObject.has("mineSpeed")) {
            properties.fullToolProperties.put("mineSpeed", combinationObject.get("mineSpeed").getAsString());
        } else if (combinationObject.has("attackDamage")) {
            properties.fullToolProperties.put("attackDamage", combinationObject.get("attackDamage").getAsString());
        } else if (combinationObject.has("attackSpeed")) {
            properties.fullToolProperties.put("attackSpeed", combinationObject.get("attackSpeed").getAsString());
        }

    }
}