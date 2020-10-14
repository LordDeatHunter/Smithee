package wraith.smithee;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import wraith.smithee.parts.BindingPart;
import wraith.smithee.parts.HandlePart;
import wraith.smithee.parts.HeadPart;

public class Utils {

    public static Identifier ID(String id) {
        return new Identifier(Smithee.MOD_ID, id);
    }

    //Durability calculation:
    //  30% original = HEAD
    //  20% original = BINDING
    //  50% original = HANDLE
    //-=-=-=-=-=-=-=-=-=-=-=-=-

    //Mining Speed Multiplier calculation:
    //  50% original = HEAD
    //  20% original = BINDING
    //  30% original = HANDLE
    //-=-=-=-=-=-=-=-=-=-=-=-=-
    public ToolMaterial createToolMaterial(HeadPart head, BindingPart binding, HandlePart handle) {
        int durability = (int) (Math.floor(head.material.getDurability() * 0.3F) + Math.floor(binding.material.getDurability() * 0.2F) + Math.floor(handle.material.getDurability() * 0.5F));
        int mineSpeed = (int) (Math.floor(head.material.getMiningSpeedMultiplier() * 0.5F) + Math.floor(binding.material.getMiningSpeedMultiplier() * 0.2F) + Math.floor(handle.material.getMiningSpeedMultiplier() * 0.3F));
        return new CustomToolMaterial(head.material.getMiningLevel(), durability, mineSpeed, head.material.getAttackDamage(), 0, () -> head.material.getRepairIngredient());
    }

}
