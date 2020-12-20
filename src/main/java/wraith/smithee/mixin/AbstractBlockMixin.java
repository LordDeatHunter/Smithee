package wraith.smithee.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.registry.ItemRegistry;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (state.getBlock() == Blocks.BOOKSHELF && player.getStackInHand(hand).getItem() == ItemRegistry.ITEMS.get("mossy_cobblestone_embossment") && player.experienceLevel >= 1) {
            player.getStackInHand(hand).decrement(1);
            player.inventory.offerOrDrop(world, new ItemStack(ItemRegistry.ITEMS.get("mending_moss_embossment")));
            player.addExperienceLevels(-1);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }


}
