package wraith.smithee;

import wraith.smithee.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class TextureDatabase {

    public static final HashMap<String, InputStream> TEXTURES = new HashMap<>();

    public static void generateTextures() {
        File[] palettes = Config.getFiles("config/smithee/palettes/");
        File[] templates = Config.getFiles("config/smithee/templates/");
        File[] textures = Config.getFiles("config/smithee/textures/");

        for (File texture : textures) {
            String[] segments = texture.getName().split("/");
            String name = segments[segments.length - 1].split("\\.")[0];
            try {
                TEXTURES.put(name, new FileInputStream(texture));
            } catch (FileNotFoundException e) {
                Smithee.LOGGER.error("Failed to load texture " + name + ".png");
                e.printStackTrace();
            }
        }
        File templatePalette = new File("config/smithee/templates/template_palette.png");
        for (File template : templates) {
            String[] segments = template.getName().split("/");
            String templateName = segments[segments.length - 1].split("\\.")[0];
            if ("template_palette".equals(templateName)) {
                continue;
            }
            for (File palette : palettes) {
                segments = palette.getName().split("/");
                String paletteName = segments[segments.length - 1].split("\\.")[0];
                String textureName = paletteName + "_" + templateName;
                if (TEXTURES.containsKey(textureName)) {
                    continue;
                }
                TEXTURES.put(textureName, Utils.recolor(template, templatePalette, palette, textureName));
            }
        }
    }

}
