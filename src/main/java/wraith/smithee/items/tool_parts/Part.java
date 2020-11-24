package wraith.smithee.items.tool_parts;

import net.minecraft.item.ToolMaterial;

public class Part {

    public ToolMaterial material;
    public String materialName;
    public String partType;
    public String toolType;

    public Part(String materialName, String partType, String toolType, ToolMaterial material) {
        this.material = material;
        this.materialName = materialName;
        this.partType = partType;
        this.toolType = toolType;
    }

}
