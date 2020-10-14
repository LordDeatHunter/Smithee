package wraith.smithee.parts;

import net.minecraft.item.ToolMaterial;

public abstract class Part {

    public ToolMaterial material;
    public String materialName;
    public String type;

    public Part(String materialName, String type, ToolMaterial material) {
        this.material = material;
        this.materialName = materialName;
    }

}
