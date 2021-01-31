package wraith.smithee.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wraith.smithee.properties.Trait;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "afterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V"), cancellable = true)
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack, CallbackInfo ci) {

        ItemStack handStack = player.getMainHandStack();
        boolean cancelExhaustion = false; // TODO: add a exhaustion canceller
        boolean magnetic = Trait.hasTrait(handStack, Trait.Traits.MAGNETIC);

        List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) world, pos, entity, player, stack);
        drops = Trait.hasTrait(stack, Trait.Traits.MIDAS_TOUCH) ? Collections.singletonList(Trait.getMidas(handStack)) : drops;

        if (magnetic) {
            Block.dropStacks(state, world, pos, entity, player, stack);
            for (ItemStack drop : drops) {
                player.inventory.offerOrDrop(world, drop);
            }
        }

        if (cancelExhaustion && magnetic) {
            ci.cancel();
        } else if (!cancelExhaustion && magnetic) {
            player.addExhaustion(0.005F);
            ci.cancel();
        } else {
            Block.dropStacks(state, world, pos, entity, player, stack);
            for (ItemStack drop : drops) {
                Block.dropStack(world, pos, drop);
            }
            ci.cancel();
        }

    }

}
