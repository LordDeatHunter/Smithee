package wraith.smithee.utils;

import com.udojava.evalex.Expression;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;
import wraith.smithee.Config;
import wraith.smithee.Smithee;
import wraith.smithee.items.tool_parts.ToolPartItem;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.registry.ItemRegistry;

import javax.imageio.ImageIO;
import java.awt.*;
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

    public static String createModelJson(Identifier id) {
        String[] split = id.getPath().split("/")[1].split("_");
        if (id.getNamespace().equals(Smithee.MOD_ID) && split.length >= 3) {
            Item item = ItemRegistry.ITEMS.get(id.getPath().split("/")[1]);
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
            else if ((split[0] + "_" + split[1]).equals("base_" + Smithee.MOD_ID) && ItemRegistry.TOOL_TYPES.contains(split[2])) {
                return "{\n" +
                        "  \"parent\": \"item/handheld\",\n" +
                        "  \"textures\": {\n" +
                        "    \"layer0\": \"" + id + "\"\n" +
                        "  }\n" +
                        "}";
            }
        }
        return "";
    }

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

    public static boolean isSmitheeTool(ItemStack stack) {
        return stack.getItem() instanceof BaseSmitheeTool;
    }

    public static BufferedImage getImage(ItemStack stack) {
        BufferedImage toolImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = toolImage.getGraphics();

        BufferedImage headImage = null;
        BufferedImage bindingImage = null;
        BufferedImage handleImage = null;

        CompoundTag tag = stack.getSubTag("Parts");

        String head = "iron";
        String binding = "iron";
        String handle = "iron";

        if (tag != null) {
            head = tag.getString("HeadPart");
            binding = tag.getString("BindingPart");
            handle = tag.getString("HandlePart");
        }

        String type = getToolType(stack.getItem());

        try {
            headImage = ImageIO.read(MinecraftClient.getInstance().getResourceManager().getResource(Utils.ID("textures/item/" + head + "_" + type + "_head.png")).getInputStream());
            bindingImage = ImageIO.read(MinecraftClient.getInstance().getResourceManager().getResource(Utils.ID("textures/item/" + binding + "_" + type + "_binding.png")).getInputStream());
            handleImage = ImageIO.read(MinecraftClient.getInstance().getResourceManager().getResource(Utils.ID("textures/item/" + handle + "_" + type + "_handle.png")).getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.drawImage(handleImage, 0, 0, null);
        g.drawImage(bindingImage, 0, 0, null);
        g.drawImage(headImage, 0, 0, null);
        g.dispose();

        return toolImage;
    }

    public static NativeImage getNativeImage(ItemStack itemStack){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(getImage(itemStack), "PNG", os);
            return NativeImage.read(new ByteArrayInputStream(os.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Identifier generateTexture(ItemStack itemStack) {
        MinecraftClient client = MinecraftClient.getInstance();

        NativeImage img = Utils.getNativeImage(itemStack);
        NativeImageBackedTexture nIBT = new NativeImageBackedTexture(img);

        Identifier dynamicTexture = client.getTextureManager().registerDynamicTexture(Smithee.MOD_ID, nIBT);
        Objects.requireNonNull(client.getTextureManager().getTexture(dynamicTexture)).bindTexture();
        return dynamicTexture;

    }

    public static void saveFilesFromJar(String dir, String outputDir, boolean overwrite) {
        JarFile jar = null;
        try {
            jar = new JarFile(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        if (jar != null) {
            Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.getName().startsWith(dir) || (!entry.getName().endsWith(".json") && !entry.getName().endsWith(".png"))) {
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

}
