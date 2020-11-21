package wraith.smithee;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.registry.ItemRegistry;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Utils {
    public static Identifier ID(String id) {
        return new Identifier(Smithee.MOD_ID, id);
    }

    public static String createModelJson(Identifier id) {
        String[] split = id.getPath().split("/")[1].split("_");
        if (id.getNamespace().equals(Smithee.MOD_ID) && split.length == 3) {
            //Tool Part
            if (ItemRegistry.MATERIALS.containsKey(split[0]) && ItemRegistry.TOOL_TYPES.contains(split[1])) {
                return "{\n" +
                        "  \"parent\": \"item/generated\",\n" +
                        "  \"textures\": {\n" +
                        "    \"layer0\": \"" + id + "\"\n" +
                        "  }\n" +
                        "}";
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
}
