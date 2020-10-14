package wraith.smithee.parts;

import net.minecraft.item.ToolMaterial;

public abstract class Part {

    public ToolMaterial material;
    public String materialName;

    public Part(String materialName, ToolMaterial material) {
        this.material = material;
        this.materialName = materialName;
    }

}
