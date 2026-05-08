package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.entity.SlideBlock;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

public class SliderBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {

    public static final IntegerProperty DELAY = IntegerProperty.create("delay", 0, 3);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final int TICK_TIME = 80;
    private static final int OFFSET_TIME = 20;
    private static final int PLAYER_RANGE = 32;
    private static final float BLOCK_DAMAGE = 5.0F;
    private static final VoxelShape Y_BB = Shapes.create(new AABB(0.3125D, 0.0D, 0.3125D, 0.6875D, 1.0D, 0.6875D));
    private static final VoxelShape Z_BB = Shapes.create(new AABB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 1.0D));
    private static final VoxelShape X_BB = Shapes.create(new AABB(0.0D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D));

    public SliderBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(DELAY, 0)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return super.getStateForPlacement(context).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
        }

        return super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(DELAY, WATERLOGGED));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> X_BB;
            case Z -> Z_BB;
            default -> Y_BB;
        };
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide() && this.isConnectedInRange(level, pos)) {
            SlideBlock slideBlock = new SlideBlock(TFEntities.SLIDER.get(), level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state);
            level.addFreshEntity(slideBlock);
        }

        this.scheduleBlockUpdate(level, pos);
    }

    public boolean isConnectedInRange(Level level, BlockPos pos) {
        Direction.Axis axis = level.getBlockState(pos).getValue(AXIS);

        return switch (axis) {
            case Y -> this.anyPlayerInRange(level, pos) || this.isConnectedInRangeRecursive(level, pos, Direction.UP) || this.isConnectedInRangeRecursive(level, pos, Direction.DOWN);
            case X -> this.anyPlayerInRange(level, pos) || this.isConnectedInRangeRecursive(level, pos, Direction.WEST) || this.isConnectedInRangeRecursive(level, pos, Direction.EAST);
            case Z -> this.anyPlayerInRange(level, pos) || this.isConnectedInRangeRecursive(level, pos, Direction.NORTH) || this.isConnectedInRangeRecursive(level, pos, Direction.SOUTH);
        };
    }

    private boolean isConnectedInRangeRecursive(Level level, BlockPos pos, Direction direction) {
        BlockPos nextPos = pos.relative(direction);

        if (level.getBlockState(pos) == level.getBlockState(nextPos)) {
            return this.anyPlayerInRange(level, nextPos) || this.isConnectedInRangeRecursive(level, nextPos, direction);
        }

        return false;
    }

    private boolean anyPlayerInRange(Level level, BlockPos pos) {
        return level.getNearestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, PLAYER_RANGE, false) != null;
    }

    public void scheduleBlockUpdate(Level level, BlockPos pos) {
        int offset = level.getBlockState(pos).getValue(DELAY);
        int update = TICK_TIME - ((int) (level.getGameTime() - offset * OFFSET_TIME) % TICK_TIME);
        level.scheduleTick(pos, this, update);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        this.scheduleBlockUpdate(level, pos);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.hurt(TFDamageTypes.source(level, TFDamageTypes.SLIDER), BLOCK_DAMAGE);
        if (entity instanceof LivingEntity living) {
            double xKnockback = (pos.getX() + 0.5D - entity.getX()) * 2.0D;
            double zKnockback = (pos.getZ() + 0.5D - entity.getZ()) * 2.0D;

            living.knockback(2.0D, xKnockback, zKnockback);
        }
    }
}
