package wraith.smithee;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Config {

    public static JsonObject loadConfig() {
        String defaultConfig =
                "{\n" +
                        "  \"regenerate_deleted_stat_files\": true,\n" +
                        "  \"replace_old_stat_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_recipe_files\": true,\n" +
                        "  \"replace_old_recipe_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_smithing_files\": true,\n" +
                        "  \"replace_old_smithing_files_when_regenerating\": false,\n" +
                        "  \"regenerate_deleted_combination_files\": true,\n" +
                        "  \"replace_old_combination_files_when_regenerating\": false,\n" +
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
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public static File[] getFiles(String path) {
        return new File(path).listFiles();
    }

}
