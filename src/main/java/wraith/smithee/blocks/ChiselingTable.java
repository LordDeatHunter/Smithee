package wraith.smithee.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class ChiselingTable extends BlockWithEntity {

    protected static final VoxelShape VOXEL_SHAPE = Stream.of(
            VoxelShapes.combineAndSimplify(Block.createCuboidShape(14, 7, 1, 15, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(1, 7, 1, 2, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(2, 7, 10, 3, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(13, 7, 7, 14, 7.75, 9), VoxelShapes.combineAndSimplify(Block.createCuboidShape(9, 7, 14, 10, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(6, 7, 14, 7, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(7, 7, 13, 9, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(7, 7, 1, 9, 7.75, 3), VoxelShapes.combineAndSimplify(Block.createCuboidShape(9, 7, 1, 10, 7.75, 2), VoxelShapes.combineAndSimplify(Block.createCuboidShape(6, 7, 1, 7, 7.75, 2), VoxelShapes.combineAndSimplify(Block.createCuboidShape(10, 7, 13, 13, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(3, 7, 1, 6, 7.75, 3), VoxelShapes.combineAndSimplify(Block.createCuboidShape(4, 7, 4, 6, 7.75, 6), VoxelShapes.combineAndSimplify(Block.createCuboidShape(7, 7, 10, 9, 7.75, 12), VoxelShapes.combineAndSimplify(Block.createCuboidShape(10, 7, 10, 12, 7.75, 12), VoxelShapes.combineAndSimplify(Block.createCuboidShape(10, 7, 7, 12, 7.75, 9), VoxelShapes.combineAndSimplify(Block.createCuboidShape(10, 7, 4, 12, 7.75, 6), VoxelShapes.combineAndSimplify(Block.createCuboidShape(7, 7, 7, 9, 7.75, 9), VoxelShapes.combineAndSimplify(Block.createCuboidShape(7, 7, 4, 9, 7.75, 6), VoxelShapes.combineAndSimplify(Block.createCuboidShape(4, 7, 7, 6, 7.75, 9), VoxelShapes.combineAndSimplify(Block.createCuboidShape(4, 7, 10, 6, 7.75, 12), VoxelShapes.combineAndSimplify(Block.createCuboidShape(3, 7, 13, 6, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(10, 7, 1, 13, 7.75, 3), VoxelShapes.combineAndSimplify(Block.createCuboidShape(13, 7, 1, 14, 7.75, 6), VoxelShapes.combineAndSimplify(Block.createCuboidShape(13, 7, 10, 14, 7.75, 15), VoxelShapes.combineAndSimplify(Block.createCuboidShape(2, 7, 1, 3, 7.75, 6), Block.createCuboidShape(2, 7, 7, 3, 7.75, 9), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR), BooleanBiFunction.OR),
            Block.createCuboidShape(1, 7, 15, 15, 10, 16),
            Block.createCuboidShape(0, 0, 0, 16, 7, 16),
            Block.createCuboidShape(15, 7, 0, 16, 10, 16),
            Block.createCuboidShape(0, 7, 0, 1, 10, 16),
            Block.createCuboidShape(1, 7, 0, 15, 10, 1)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public ChiselingTable(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        world.getBlockEntity(pos).markDirty();
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ChiselingTableBlockEntity();
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof ChiselingTableBlockEntity) {
                ItemScatterer.spawn(world, pos, (ChiselingTableBlockEntity) entity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

}
