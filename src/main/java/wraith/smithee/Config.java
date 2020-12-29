package wraith.smithee;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Config {

    public static void createMaterials(boolean overwrite) {
        HashSet<String> materials = new HashSet<>();
        HashSet<String> embossMaterials = new HashSet<>();
        materials.add("oak");
        materials.add("spruce");
        materials.add("acacia");
        materials.add("dark_oak");
        materials.add("jungle");
        materials.add("birch");
        materials.add("warped");
        materials.add("crimson");
        materials.add("stone");
        materials.add("granite");
        materials.add("andesite");
        materials.add("diorite");
        materials.add("basalt");
        materials.add("blackstone");
        materials.add("end_stone");
        materials.add("prismarine");
        materials.add("bamboo");
        materials.add("bone");
        materials.add("flint");
        materials.add("ice");
        materials.add("cactus");
        materials.add("netherrack");
        materials.add("golden");
        materials.add("iron");
        materials.add("diamond");
        materials.add("netherite");
        materials.add("mossy_cobblestone");

        embossMaterials.add("lapis_lazuli");
        embossMaterials.add("redstone");
        embossMaterials.add("quartz");
        embossMaterials.add("silky_jewel");
        embossMaterials.add("mossy_cobblestone");
        embossMaterials.add("mending_moss");

        if (FabricLoader.getInstance().isModLoaded("mythicmetals")) {
            materials.add("adamantite");
            materials.add("aetherium");
            materials.add("aquarium");
            materials.add("argonium");
            materials.add("banglum");
            materials.add("brass");
            materials.add("bronze");
            materials.add("carmot");
            materials.add("celestium");
            materials.add("copper");
            materials.add("discordium");
            materials.add("durasteel");
            materials.add("electrum");
            materials.add("etherite");
            materials.add("kyber");
            materials.add("metallurgium");
            materials.add("midas_gold");
            materials.add("mythril");
            materials.add("orichalcum");
            materials.add("osmium");
            materials.add("platinum");
            materials.add("prometheum");
            materials.add("quadrillum");
            materials.add("quicksilver");
            materials.add("runite");
            materials.add("silver");
            materials.add("slowsilver");
            materials.add("starrite");
            materials.add("steel");
            materials.add("stormyx");
            materials.add("tantalite");
            materials.add("tin");
            materials.add("truesilver");
            materials.add("ur");
        }
        if (FabricLoader.getInstance().isModLoaded("astromine-foundations")) {
            materials.add("copper");
            materials.add("tin");
            materials.add("silver");
            materials.add("lead");
            materials.add("bronze");
            materials.add("steel");
            materials.add("electrum");
            materials.add("rose_gold");
            materials.add("sterling_silver");
            materials.add("fools_gold");
            materials.add("metite");
            materials.add("asterite");
            materials.add("stellum");
            materials.add("galaxium");
            materials.add("univite");
            materials.add("lunum");
            materials.add("meteoric_steel");
        }

        Config.createFile("config/smithee/materials.json", getMaterials(materials), overwrite);
        Config.createFile("config/smithee/emboss_materials.json", getMaterials(embossMaterials), overwrite);
    }

    private static String getMaterials(HashSet<String> materials) {
        String defaultMaterials =
                "{\n" +
                        "  \"materials\": [\n" ;

        Iterator<String> it = materials.iterator();
        while (it.hasNext()) {
            String material = it.next();
            defaultMaterials += "    \"" + material + "\"";
            if (it.hasNext()) {
                defaultMaterials += ",";
            }
            defaultMaterials += "\n";
        }
        defaultMaterials +=
                "  ]\n" +
                        "}";
        return defaultMaterials;
    }

    public static JsonObject loadConfig() {
        String defaultConfig =
                        "{\n" +
                        "  \"disable_vanilla_tools\": false,\n" +
                        "\n" +
                        "  \"regenerate_deleted_stat_files\": true,\n" +
                        "  \"replace_old_stat_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_recipe_files\": true,\n" +
                        "  \"replace_old_recipe_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_smithing_files\": true,\n" +
                        "  \"replace_old_smithing_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_combination_files\": true,\n" +
                        "  \"replace_old_combination_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_palettes\": true,\n" +
                        "  \"replace_old_palettes_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_texture_files\": true,\n" +
                        "  \"replace_old_texture_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_modifier_files\": true,\n" +
                        "  \"replace_old_modifier_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_chisel_files\": true,\n" +
                        "  \"replace_old_chisel_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_shard_files\": true,\n" +
                        "  \"replace_old_shard_files_when_regenerating\": false,\n" +
                        "  \"regenerate_material_list\": true,\n" +
                        "  \"replace_material_list_when_regenerating\": false\n" +
                        "}";
        String path = "config/smithee/config.json";
        Config.createFile(path, defaultConfig, false);
        return getJsonObject(readFile(new File(path)));
    }

    public static void createFile(String path, String contents, boolean overwrite) {
        File file = new File(path);
        if (file.exists() && !overwrite) {
            return;
        }
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.setReadable(true);
        file.setWritable(true);
        file.setExecutable(true);
        if (contents == null || "".equals(contents)) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(File file) {
        String output = "";
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\Z");
            output = scanner.next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static JsonObject getJsonObject(String json) {
        try {
            return new JsonParser().parse(json).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            Smithee.LOGGER.error("Error while parsing following json:\n\n" + json);
            return null;
        }
    }

    public static File[] getFiles(String path) {
        return new File(path).listFiles();
    }

}
