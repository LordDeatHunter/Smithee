package wraith.smithee.items.tool_parts;

public class Part {

    public String materialName;
    public String partType;
    public String globalPartType;
    public String toolType;

    public Part(String materialName, String partType, String globalPartType, String toolType) {
        this.materialName = materialName;
        this.partType = partType;
        this.toolType = toolType;
        this.globalPartType = globalPartType;
    }

    @Override
    public String toString() {
        return materialName + "_" + ("any".equals(toolType) ? "" : toolType + "_") + partType;
    }

    public String recipeString() {
        return ("any".equals(toolType) ? "" : toolType + "_") + partType;
    }

}
