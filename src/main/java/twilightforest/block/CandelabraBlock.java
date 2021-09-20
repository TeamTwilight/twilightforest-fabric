package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

// The code is flexible to allow colors but I'm not sure if they'd look good on Candelabra
public class CandelabraBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty ON_WALL = BooleanProperty.create("on_wall");
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final VoxelShape CANDLES_NORTH = Shapes.or(Block.box(1, 7, 2, 15, 15, 6), Block.box(1, 1, 3.5, 15, 7,4.5), Block.box(7.5, 1, 1, 8.5, 7,7), Block.box(6, 2, 0, 10, 6, 1));
    public static final VoxelShape CANDLES_SOUTH = Shapes.or(Block.box(1, 7, 10, 15, 15, 14), Block.box(1, 1, 11.5, 15, 7,12.5), Block.box(7.5, 1, 9, 8.5, 7,15), Block.box(6, 2, 15, 10, 6, 16));

    public static final VoxelShape CANDLES_WEST = Shapes.or(Block.box(2, 7, 1, 6, 15, 15), Block.box(3.5, 1, 1, 4.5, 7,15), Block.box(1, 1, 7.5, 7, 7,8.5), Block.box(0, 2, 6, 1, 6, 10));
    public static final VoxelShape CANDLES_EAST = Shapes.or(Block.box(10, 7, 1, 14, 15, 15), Block.box(11.5, 1, 1, 12.5, 7,15), Block.box(9, 1, 7.5, 15, 7,8.5), Block.box(15, 2, 6, 16, 6, 10));

    public static final VoxelShape CANDLES_X = Shapes.or(Block.box(6, 7, 1, 10, 15, 15), Block.box(7.5, 1, 1, 8.5, 7,15), Block.box(5, 1, 7.5, 11, 7,8.5), Block.box(6, 0, 6, 10, 1, 10));
    public static final VoxelShape CANDLES_Z = Shapes.or(Block.box(1, 7, 6, 15, 15, 10), Block.box(1, 1, 7.5, 15, 7,8.5), Block.box(7.5, 1, 5, 8.5, 7,11), Block.box(6, 0, 6, 10, 1, 10));

    protected CandelabraBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ON_WALL, false).setValue(LIT, false));
    }

    @Override
    public int getLightBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.getLightBlock(pState, pLevel, pPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(ON_WALL)) {
            return switch (state.getValue(FACING)) {
                case SOUTH -> CANDLES_SOUTH;
                case WEST -> CANDLES_WEST;
                case EAST -> CANDLES_EAST;
                default -> CANDLES_NORTH;
            };
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? CANDLES_X : CANDLES_Z;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickDirection = context.getClickedFace();
        boolean onBottomBlock = clickDirection == Direction.DOWN;
        Direction[] placements = context.getNearestLookingDirections();
        BlockPos placePos = context.getClickedPos();
        Level level = context.getLevel();

        // If placer is clicking the bottom block, then we want to test for the bottom block first
        //  before we cycle the walls for possible placements
        // Otherwise we test wall placements before testing the bottom block
        if (onBottomBlock) {
            if (canSurvive(level, placePos, false, context.getHorizontalDirection()))
                return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, false);

            for (Direction nextSide : placements)
                if (nextSide.getAxis().isHorizontal() && canSurvive(level, placePos, true, nextSide))
                    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, true);
        } else {
            for (Direction nextSide : placements)
                if (nextSide.getAxis().isHorizontal() && canSurvive(level, placePos, true, nextSide))
                    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, true);

            if (canSurvive(level, placePos, false, context.getHorizontalDirection()))
                return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(ON_WALL, false);
        }

        // Fail
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, ON_WALL, LIT);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        return canSurvive(levelReader, pos, state.getValue(ON_WALL), state.getValue(FACING));
    }

    public static boolean canSurvive(LevelReader levelReader, BlockPos pos, boolean onWall, Direction facing) {
        return canSupportCenter(levelReader, onWall ? pos.relative(facing) : pos.below(), Direction.UP);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        if (state.getValue(LIT)) {
            if (state.getValue(ON_WALL)) {
                switch (state.getValue(FACING)) {
                    case SOUTH -> {
                        addParticlesAndSound(level, pos, 0.1875, 0.875, 0.75, rand);
                        addParticlesAndSound(level, pos, 0.5, 0.875, 0.75, rand);
                        addParticlesAndSound(level, pos, 0.8125, 0.875, 0.75, rand);
                    }
                    case WEST -> {
                        addParticlesAndSound(level, pos, 0.25, 0.875, 0.1875, rand);
                        addParticlesAndSound(level, pos, 0.25, 0.875, 0.5, rand);
                        addParticlesAndSound(level, pos, 0.25, 0.875, 0.8125, rand);
                    }
                    case EAST -> {
                        addParticlesAndSound(level, pos, 0.75, 0.875, 0.1875, rand);
                        addParticlesAndSound(level, pos, 0.75, 0.875, 0.5, rand);
                        addParticlesAndSound(level, pos, 0.75, 0.875, 0.8125, rand);
                    }
                    default -> {
                        addParticlesAndSound(level, pos, 0.1875, 0.875, 0.25, rand);
                        addParticlesAndSound(level, pos, 0.5, 0.875, 0.25, rand);
                        addParticlesAndSound(level, pos, 0.8125, 0.875, 0.25, rand);
                    }
                }
            } else {
                if (state.getValue(FACING).getAxis() == Direction.Axis.X) {
                    addParticlesAndSound(level, pos, 0.5, 0.875, 0.1875, rand);
                    addParticlesAndSound(level, pos, 0.5, 0.875, 0.5, rand);
                    addParticlesAndSound(level, pos, 0.5, 0.875, 0.8125, rand);
                } else {
                    addParticlesAndSound(level, pos, 0.1875, 0.875, 0.5, rand);
                    addParticlesAndSound(level, pos, 0.5, 0.875, 0.5, rand);
                    addParticlesAndSound(level, pos, 0.8125, 0.875, 0.5, rand);
                }
            }
        }
    }

    // Below: Code copied from AbstractSkullCandleBlock. If you thought that mash-up was bad, brace yourself

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (player.getAbilities().mayBuild && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {
            extinguish(player, state, level, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);

        } else if (!state.getValue(LIT)){
            if(player.getItemInHand(hand).is(Items.FLINT_AND_STEEL)) {
                setLit(level, state, pos, true);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if(!player.getAbilities().instabuild) player.getItemInHand(hand).hurtAndBreak(1, player, (res) -> {
                    res.broadcastBreakEvent(hand);
                });
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (player.getItemInHand(hand).is(Items.FIRE_CHARGE)) {
                setLit(level, state, pos, true);
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if(!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult result, Projectile projectile) {
        if (!level.isClientSide && projectile.isOnFire() && this.canBeLit(state)) {
            setLit(level, state, result.getBlockPos(), true);
        }
    }

    protected boolean canBeLit(BlockState state) {
        return !state.getValue(LIT);
    }

    // Original methods used Vec3 but here we can avoid creation of extraneous vectors
    protected static void addParticlesAndSound(Level level, BlockPos pos, double xFraction, double yFraction, double zFraction, Random rand) {
        addParticlesAndSound(level, pos.getX() + xFraction, pos.getY() + yFraction, pos.getZ() + zFraction, rand);
    }

    protected static void addParticlesAndSound(Level level, double x, double y, double z, Random rand) {
        float var3 = rand.nextFloat();
        if (var3 < 0.3F) {
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            if (var3 < 0.17F) {
                level.playLocalSound(x + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
            }
        }

        level.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    public static void extinguish(@Nullable Player player, BlockState state, LevelAccessor accessor, BlockPos pos) {
        setLit(accessor, state, pos, false);
        if (state.getBlock() instanceof AbstractCandleBlock) {
            ((AbstractSkullCandleBlock)state.getBlock()).getParticleOffsets(state).forEach((p_151926_) ->
                    accessor.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + p_151926_.x(), (double)pos.getY() + p_151926_.y(), (double)pos.getZ() + p_151926_.z(), 0.0D, 0.1D, 0.0D));
        }

        accessor.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        accessor.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    }

    private static void setLit(LevelAccessor accessor, BlockState state, BlockPos pos, boolean lit) {
        accessor.setBlock(pos, state.setValue(LIT, lit), 11);
    }
}
