package wraith.smithee.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.*;
import wraith.smithee.Utils;
import wraith.smithee.parts.BindingPart;
import wraith.smithee.parts.HandlePart;
import wraith.smithee.parts.HeadPart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SmitheeSword extends AxeItem implements SmitheeTool {

    protected HeadPart head;
    protected BindingPart binding;
    protected HandlePart handle;

    public SmitheeSword(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public BufferedImage getImage() {
        BufferedImage toolImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = toolImage.getGraphics();

        BufferedImage headImage = null;
        BufferedImage bindingImage = null;
        BufferedImage handleImage = null;

        try {
            headImage = ImageIO.read(MinecraftClient.getInstance().getResourceManager().getResource(Utils.ID("textures/tools/" + head.materialName + "_sword_head")).getInputStream());
            bindingImage = ImageIO.read(MinecraftClient.getInstance().getResourceManager().getResource(Utils.ID("textures/tools/" + binding.materialName + "_sword_binding")).getInputStream());
            handleImage = ImageIO.read(MinecraftClient.getInstance().getResourceManager().getResource(Utils.ID("textures/tools/" + handle.materialName + "_sword_handle")).getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.drawImage(handleImage, 0, 0, null);
        g.drawImage(bindingImage, 0, 0, null);
        g.drawImage(headImage, 0, 0, null);
        g.dispose();

        return toolImage;
    }

}
