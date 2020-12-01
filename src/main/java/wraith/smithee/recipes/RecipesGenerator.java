package wraith.smithee.recipes;

import com.google.gson.JsonObject;
import wraith.smithee.Config;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class RecipesGenerator {

    public static HashMap<HashSet<String>, SmithingRecipe> RECIPES = new HashMap<>();

    public static void generateRecipes() {
        File[] files = Config.getFiles("config/smithee/smithing/");
        for (File file : files) {
            JsonObject json = Config.getJsonObject(Config.readFile(file));
            String input = json.get("input_material").getAsString();
            String addition = json.get("addition_item").getAsString();
            int additionAmount = json.get("addition_mount").getAsInt();
            String output = json.get("output_material").getAsString();

            HashSet<String> key = new HashSet<>();
            key.add(input);
            key.add(addition);

            RECIPES.put(key, new SmithingRecipe(input, addition, additionAmount, output));
        }
    }

    //public static HashMap<Identifier, JsonObject> RECIPES = new HashMap<>();

    /*
    public static void generateRecipes() {
        File[] files = Config.getFiles("config/smithee/smithing/");
        for (File file : files) {
            JsonObject json = Config.getJsonObject(Config.readFile(file));
            for (String tool : ItemRegistry.TOOL_TYPES) {
                RECIPES.put(Utils.ID(json.get("output_material").getAsString() + "_" + tool + "_head"), generateSmithingJson(Utils.ID(json.get("input_material").getAsString() + "_" + tool + "_head"), new Identifier(json.get("addition_material").getAsString()), Utils.ID(json.get("output_material").getAsString() + "_" + tool + "_head")));
                RECIPES.put(Utils.ID(json.get("output_material").getAsString() + "_" + tool + "_binding"), generateSmithingJson(Utils.ID(json.get("input_material").getAsString() + "_" + tool + "_binding"), new Identifier(json.get("addition_material").getAsString()), Utils.ID(json.get("output_material").getAsString() + "_" + tool + "_binding")));
                RECIPES.put(Utils.ID(json.get("output_material").getAsString() + "_" + tool + "_handle"), generateSmithingJson(Utils.ID(json.get("input_material").getAsString() + "_" + tool + "_handle"), new Identifier(json.get("addition_material").getAsString()), Utils.ID(json.get("output_material").getAsString() + "_" + tool + "_handle")));
            }
        }
    }

    public static JsonObject generateSmithingJson(Identifier base, Identifier addition, Identifier output) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:smithing");

        JsonObject obj = new JsonObject();
        obj.addProperty("item", base.toString());
        json.add("base", obj);

        obj = new JsonObject();
        obj.addProperty("item", addition.toString());
        json.add("addition", obj);

        obj = new JsonObject();
        obj.addProperty("item", output.toString());
        json.add("result", obj);

        return json;
    }
     */

}
