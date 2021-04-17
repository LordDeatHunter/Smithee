package wraith.smithee.items.tools;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import wraith.harvest_scythes.ScytheTool;

public class BaseSmitheeHarvestScythe extends ScytheTool implements BaseSmitheeMeleeWeapon {

    public BaseSmitheeHarvestScythe(Settings settings) {
        super(ToolMaterials.WOOD, 0, 0, 0, false, settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        super.use(world, user, hand);
        return boostXp(world, user, hand);
    }

    @Override
    public String getToolType() {
        return "harvest_scythe";
    }

}
