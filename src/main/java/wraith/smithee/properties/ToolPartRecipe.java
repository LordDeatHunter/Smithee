package wraith.smithee.properties;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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

    public Item getRemains() {
        if (remains == null || "".equals(remains)) {
            return null;
        }
        if (remains.startsWith("#")) {
            return TagRegistry.item(new Identifier(remains.substring(1))).values().get(0);
        }
        return Registry.ITEM.get(new Identifier(remains));
    }
}
