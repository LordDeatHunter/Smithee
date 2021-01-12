package wraith.smithee.items.tools;

import chronosacaria.mcdw.bases.McdwScythe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BaseSmitheeBattleScythe extends McdwScythe implements BaseSmitheeMeleeWeapon {

    public BaseSmitheeBattleScythe(Settings settings) {
        super(ToolMaterials.WOOD, 0, 0, settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return boostXp(world, user, hand);
    }

    @Override
    public String getToolType() {
        return "battle_scythe";
    }
}
