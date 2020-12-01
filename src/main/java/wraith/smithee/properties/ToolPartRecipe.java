package wraith.smithee.properties;

public class ToolPartRecipe {

    public int requiredAmount;
    public String outputMaterial;
    public String remains;
    public int remainsAmount;

    public ToolPartRecipe(String outputMaterial, int requiredAmount) {
        this.outputMaterial = outputMaterial;
        this.requiredAmount = requiredAmount;
        this.remains = "";
        this.remainsAmount = 0;
    }

    public ToolPartRecipe(String outputMaterial, int requiredAmount, String remains, int remainsAmount) {
        this(outputMaterial, requiredAmount);
        this.remains = remains;
        this.remainsAmount = remainsAmount;
    }

}
