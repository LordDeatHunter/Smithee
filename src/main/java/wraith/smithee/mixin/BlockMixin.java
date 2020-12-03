package wraith.smithee.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.properties.Trait;
import wraith.smithee.registry.ItemRegistry;

import java.util.HashSet;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "afterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V"), cancellable = true)
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        ItemStack tool = player.getMainHandStack();
        if (tool.getItem() instanceof BaseSmitheeTool && tool.hasTag() && tool.getTag().contains("Parts")) {
            CompoundTag tag = stack.getTag().getCompound("Parts");
            Pair<Boolean, Boolean> cancel;
            Pair<Boolean, Boolean> result = evaluateTrait(tag.getString("HeadPart"), "head", tool, world, player, pos, state, blockEntity);
            cancel = result;
            result = evaluateTrait(tag.getString("BindingPart"), "binding", tool, world, player, pos, state, blockEntity);
            cancel = new Pair<>(cancel.getLeft() || result.getLeft(), cancel.getRight() || result.getRight());
            result = evaluateTrait(tag.getString("HandlePart"), "handle", tool, world, player, pos, state, blockEntity);
            cancel = new Pair<>(cancel.getLeft() || result.getLeft(), cancel.getRight() || result.getRight());
            if (cancel.getLeft() && cancel.getRight()) {
                ci.cancel();
            } else if (!cancel.getLeft() && cancel.getRight()) {
                player.addExhaustion(0.005F);
                ci.cancel();
            } else if (cancel.getLeft() && !cancel.getRight()) {
                Block.dropStacks(state, world, pos, blockEntity, player, stack);
                ci.cancel();
            }
        }
    }

    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private static void getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (stack.getItem() instanceof BaseSmitheeTool && stack.hasTag() && stack.getTag().contains("Parts")) {
            CompoundTag tag = stack.getTag().getCompound("Parts");
            ItemStack fortuneStack = stack.copy();
            int level = 0;
            level += Trait.evaluateFortune(tag.getString("HeadPart"), "head");
            level += Trait.evaluateFortune(tag.getString("BindingPart"), "binding");
            level += Trait.evaluateFortune(tag.getString("HandlePart"), "handle");
            if (level > 0) {
                fortuneStack.addEnchantment(Enchantments.FORTUNE, level);
                cir.setReturnValue(state.getDroppedStacks(new LootContext.Builder(world).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, fortuneStack).optionalParameter(LootContextParameters.THIS_ENTITY, entity).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity)));
                cir.cancel();
            }
        }
    }


    private Pair<Boolean, Boolean> evaluateTrait(String material, String part, ItemStack stack, World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        HashSet<Trait> traits = ItemRegistry.PROPERTIES.get(material).traits.get(part);
        for (Trait trait : traits) {
            if ("brittle".equals(trait.traitName)) {
                Trait.evaluateBrittle(trait, stack);
                return new Pair<>(false, false);
            } else if ("midas_touch".equals(trait.traitName)) {
                Trait.evaluateMediasTouch(trait, world, pos, blockEntity);
                return new Pair<>(false, false);
            } else if ("magnetic".equals(trait.traitName)) {
                Trait.evaluateMagnetic(trait, state, world, pos, blockEntity, player, stack);
                return new Pair<>(false, true);
            }
        }
        return new Pair<>(false, false);
    }

}
