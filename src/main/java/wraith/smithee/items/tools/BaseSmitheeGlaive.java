package wraith.smithee.items.tools;

import chronosacaria.mcdw.bases.McdwGlaive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BaseSmitheeGlaive extends McdwGlaive implements BaseSmitheeMeleeWeapon {

    public BaseSmitheeGlaive(Settings settings) {
        super(ToolMaterials.IRON, 0, 0, settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return boostXp(world, user, hand);
    }
    @Override
    public String getToolType() {
        return "glaive";
    }

    @Override
    public String getBindingType() {
        return "binding";
    }

}
