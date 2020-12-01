package wraith.smithee.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import wraith.smithee.Smithee;
import wraith.smithee.screens.AssemblyTableScreenHandler;

public class AssemblyTable extends BlockWithEntity {

    private static final Text TITLE = new TranslatableText("container." + Smithee.MOD_ID + ".assembly_table");

    protected static final VoxelShape vs1 = Block.createCuboidShape(0f, 12f, 0f, 16f, 16f, 16f);
    protected static final VoxelShape vs2 = Block.createCuboidShape(1f, 0f, 1f, 4f, 12f, 4f);
    protected static final VoxelShape vs3 = Block.createCuboidShape(12f, 0f, 1f, 15f, 12f, 4f);
    protected static final VoxelShape vs4 = Block.createCuboidShape(1f, 0f, 12f, 4f, 12f, 15f);
    protected static final VoxelShape vs5 = Block.createCuboidShape(12f, 0f, 12f, 15f, 12f, 15f);
    protected static final VoxelShape vs6 = Block.createCuboidShape(2f, 5f, 2f, 14f, 7f, 14f);
    protected static final VoxelShape VOXEL_SHAPE = VoxelShapes.union(vs1, vs2, vs3, vs4, vs5, vs6).simplify();

    public AssemblyTable(Settings settings) {
        super(settings);
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
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new AssemblyTableScreenHandler(i, playerInventory, (AassemblyTableBlockEntity)world.getBlockEntity(pos), ScreenHandlerContext.create(world, pos)), TITLE);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new AassemblyTableBlockEntity();
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof AassemblyTableBlockEntity) {
                ItemScatterer.spawn(world, pos, (AassemblyTableBlockEntity)entity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
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
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

}
