package wraith.smithee.utils;

import com.udojava.evalex.Expression;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;
import wraith.smithee.Config;
import wraith.smithee.Smithee;
import wraith.smithee.SmitheeClient;
import wraith.smithee.registry.ItemRegistry;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Utils {

    public static final Random random = new Random(Calendar.getInstance().getTimeInMillis());

    public static Identifier ID(String id) {
        return new Identifier(Smithee.MOD_ID, id);
    }

    public static ModelIdentifier inventoryModelID(String id) {
        return new ModelIdentifier(new Identifier(Smithee.MOD_ID, id), "inventory");
    }

    public static String createModelJson(String path, String type) {
        String[] segments = path.split("/");
        path = segments[segments.length - 1];
        return "{\n" +
                "  \"parent\": \"item/" + type + "\",\n" +
                "  \"textures\": {\n" +
                "    \"layer0\": \"" + Smithee.MOD_ID + ":item/" + path + "\"\n" +
                "  }\n" +
                "}";
    }
    /*
    public static String createModelJson(Identifier id) {

        String path = id.getPath();
        String[] split = id.getPath().split("/");
        String itemString;
        if (split.length < 2){
            split = path.split("_");
            itemString = path;
        } else {
            itemString = split[1];
            split = split[1].split("_");
        }

        if (id.getNamespace().equals(Smithee.MOD_ID) && split.length >= 3) {
            Item item = ItemRegistry.ITEMS.get(itemString);
            if (item instanceof ToolPartItem) {
                //Tool Part
                if (ItemRegistry.MATERIALS.contains(((ToolPartItem)item).part.materialName) && ItemRegistry.TOOL_TYPES.contains(split[split.length - 2])) {
                    return "{\n" +
                            "  \"parent\": \"item/generated\",\n" +
                            "  \"textures\": {\n" +
                            "    \"layer0\": \"" + id + "\"\n" +
                            "  }\n" +
                            "}";
                }
            }
            //Tool
            //else if ((split[0] + "_" + split[1]).equals("base_" + Smithee.MOD_ID) && ItemRegistry.TOOL_TYPES.contains(split[2])) {
            //    return "{\n" +
            //            "  \"parent\": \"item/handheld\",\n" +
            //            "  \"textures\": {\n" +
            //            "    \"layer0\": \"" + id + "\"\n" +
            //            "  }\n" +
            //            "}";
            //}
        }
        return "";
    }
     */

    public static String getToolType(Item item) {
        if (item instanceof PickaxeItem) {
            return "pickaxe";
        }
        else if (item instanceof AxeItem) {
            return "axe";
        }
        else if (item instanceof ShovelItem) {
            return "shovel";
        }
        else if (item instanceof HoeItem) {
            return "hoe";
        }
        else if (item instanceof SwordItem) {
            return "sword";
        }
        else {
            return "";
        }
    }

    public static void saveFilesFromJar(String dir, String outputDir, boolean overwrite) {
        JarFile jar = null;
        try {
            jar = new JarFile(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (IOException | URISyntaxException ignored) {}

        if (jar != null) {
            Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.getName().startsWith(dir) || (!entry.getName().endsWith(".json") && !entry.getName().endsWith(".png") && !entry.getName().endsWith(".mcmeta"))) {
                    continue;
                }
                String[] segments = entry.getName().split("/");
                String filename = segments[segments.length - 1];
                if (entry.isDirectory()) {
                    continue;
                }
                InputStream is = Utils.class.getResourceAsStream("/" + entry.getName());
                String path = "config/smithee/" + outputDir + ("".equals(outputDir) ? "" : File.separator) + filename;
                if (filename.endsWith(".png")) {
                    if (Files.exists(new File(path).toPath()) && overwrite) {
                        try {
                            Files.delete(new File(path).toPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!Files.exists(new File(path).toPath())) {
                        try {
                            new File(path).getParentFile().mkdirs();
                            Files.copy(is, new File(path).toPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        inputStreamToFile(is, new File(path), overwrite);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Launched from IDE.");
            File[] files = FabricLoader.getInstance().getModContainer(Smithee.MOD_ID).get().getPath(dir).toFile().listFiles();
            for(File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                String[] segments = file.getName().split("/");
                String filename = segments[segments.length - 1];
                String path = "config/smithee/" + outputDir + ("".equals(outputDir) ? "" : File.separator) + filename;
                if (filename.endsWith(".png")) {
                    if (Files.exists(new File(path).toPath()) && overwrite) {
                        try {
                            Files.delete(new File(path).toPath());
                        } catch (IOException e) {
                            Smithee.LOGGER.warn("ERROR OCCURRED WHILE DELETING OLD TEXTURES FOR " + filename);
                            e.printStackTrace();
                        }
                    }
                    if (!Files.exists(new File(path).toPath())) {
                        try {
                            Smithee.LOGGER.info("Regenerating " + filename);
                            new File(path).getParentFile().mkdirs();
                            Files.copy(file.toPath(), new File(path).toPath());
                        } catch (IOException e) {
                            Smithee.LOGGER.warn("ERROR OCCURRED WHILE REGENERATING " + filename + " TEXTURE");
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Config.createFile(path, FileUtils.readFileToString(file, StandardCharsets.UTF_8), overwrite);
                    } catch (IOException e) {
                        Smithee.LOGGER.warn("ERROR OCCURRED WHILE REGENERATING " + filename + " TEXTURE");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void inputStreamToFile(InputStream inputStream, File file, boolean overwrite) throws IOException{
        if (!file.exists() || overwrite) {
            FileUtils.copyInputStreamToFile(inputStream, file);
        }
    }
    public static double evaluateExpression(String stringExpression) {
        return new Expression(stringExpression).eval().doubleValue();
    }

    public static String capitalize(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static void setDamage(ItemStack stack, int damage) {
        stack.setDamage(damage);
        if (stack.getDamage() >= stack.getMaxDamage()) {
            stack.setCount(0);
        }
    }

    public static void damage(ItemStack stack, int damage) {
        setDamage(stack, stack.getDamage() + damage);
    }

    public static void repair(ItemStack stack, int repairAmount) {
        setDamage(stack, Math.max(0, stack.getDamage() - repairAmount));
    }

    public static String stripToolType(String part) {
        String[] parts = part.split("_");
        String stripped = "";
        for (int i = 0; i < parts.length - 1; ++i) {
            stripped += parts[i];
        }
        stripped += parts[parts.length - 1];
        return stripped;
    }

    public static int getRandomIntInRange(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static double getRandomDoubleInRange(int min, int max) {
        return min + random.nextDouble() * max;
    }

    public static String capitalize(String[] split) {
        String result = "";
        Iterator<String> it = Arrays.asList(split).iterator();
        while (it.hasNext()) {
            result += capitalize(it.next());
            if (it.hasNext()) {
                result += " ";
            }
        }
        return result;
    }

    public static boolean isToolPart(String path) {
        /*
        for (String part : ItemRegistry.BASE_RECIPE_VALUES.keySet()) {
            if (path.endsWith("_" + part)) {
                return true;
            }
        }
        return false;
        */
        String[] segments = path.split("/");
        path = segments[segments.length - 1];
        int maxI = Utils.getMaterialFromPathIndex(path);
        segments = path.split("_");
        if (maxI >= segments.length) {
            return false;
        }
        String part = "";
        for (int i = maxI; i < segments.length; i++) {
            part += "_" + segments[i];
        }
        part = part.substring(1);
        return ItemRegistry.BASE_RECIPE_VALUES.containsKey(part) || SmitheeClient.RENDERING_TOOL_PARTS.contains(path);
    }

    public static boolean isSmitheeTool(String path) {
        HashSet<String> baseTools = new HashSet<>();
        for(String tool : ItemRegistry.TOOL_TYPES) {
            baseTools.add("base_smithee_" + tool);
        }
        return baseTools.contains(path);
    }

    public static InputStream recolor(File template, File templatePalette, File palette, String textureName) {
        BufferedImage templateImage;
        BufferedImage paletteImage;
        BufferedImage templatePaletteImage;
        try {
            templateImage = ImageIO.read(template);
            paletteImage = ImageIO.read(palette);
            templatePaletteImage = ImageIO.read(templatePalette);
        } catch (IOException e) {
            Smithee.LOGGER.warn("Error while creating texture " + textureName);
            e.printStackTrace();
            return null;
        }
        ArrayList<Integer> templateColors = new ArrayList<>();
        for (int x = 0; x < templatePaletteImage.getWidth(); ++x) {
            templateColors.add(templatePaletteImage.getRGB(x, 0));
        }
        ArrayList<Integer> paletteColors = new ArrayList<>();
        for (int x = 0; x < paletteImage.getWidth(); ++x) {
            paletteColors.add(paletteImage.getRGB(x, 0));
        }

        for (int y = 0; y < templateImage.getHeight(); ++y){
            for (int x = 0; x < templateImage.getWidth(); ++x){
                for (int i = 0; i < templateColors.size(); ++i) {
                    if (templateImage.getRGB(x, y) == templateColors.get(i)) {
                        templateImage.setRGB(x, y, paletteColors.get(i));
                        break;
                    }
                }
            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(templateImage, "PNG", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            Smithee.LOGGER.warn("Error while creating texture " + textureName);
            e.printStackTrace();
        }
        return null;
    }

    public static int getMaterialFromPathIndex(String path) {
        String material = "";
        String[] segments = path.split("_");
        int i = 0;
        while (!ItemRegistry.MATERIALS.contains(material) && !ItemRegistry.EMBOSS_MATERIALS.contains(material) && i < segments.length) {
            if (i > 0) {
                material += "_";
            }
            material += segments[i];
            ++i;
        }
        return i;
    }
}
