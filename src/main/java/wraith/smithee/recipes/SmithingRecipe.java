package wraith.smithee.recipes;

import net.minecraft.util.Identifier;

public class SmithingRecipe {

    public String inputMaterial;
    public String addition;
    public int additionAmount;
    public String outputMaterial;

    public SmithingRecipe(String inputMaterial, String addition, int additionAmount, String outputMaterial) {
        this.inputMaterial = inputMaterial;
        this.addition = addition;
        this.additionAmount = additionAmount;
        this.outputMaterial = outputMaterial;
    }

}
