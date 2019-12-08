package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.enums.Diagonals;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;

public class BlockTFSpiralBrick extends Block implements ModelRegisterCallback {

    public static final IProperty<Diagonals> DIAGONAL = PropertyEnum.create("diagonal", Diagonals.class);
    public static final IProperty<Direction.Axis> AXIS_FACING = PropertyEnum.create("axis", Direction.Axis.class);

    public BlockTFSpiralBrick() {
        super(Material.ROCK, MaterialColor.STONE);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setLightOpacity(255);
        this.useNeighborBrightness = true;
        this.setCreativeTab(TFItems.creativeTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DIAGONAL, Diagonals.TOP_RIGHT).withProperty(AXIS_FACING, Direction.Axis.X));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS_FACING, DIAGONAL);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return (state.getValue(AXIS_FACING).ordinal() << 2) | (state.getValue(DIAGONAL).ordinal());
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DIAGONAL, Diagonals.values()[meta & 0b0011]).withProperty(AXIS_FACING, Direction.Axis.values()[(meta & 0b1100) >> 2]);
    }

    @Override
    public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        BlockState state = worldIn.getBlockState(pos.offset(facing.getOpposite()));

        if (!placer.isSneaking() && worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock() instanceof BlockTFSpiralBrick) {
            Direction.Axis axis = state.getValue(AXIS_FACING);

            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
                    .withProperty(AXIS_FACING, axis)
                    .withProperty(DIAGONAL, Diagonals.mirror(state.getValue(DIAGONAL), facing.getAxis() == Direction.Axis.X ? Mirror.LEFT_RIGHT : Mirror.FRONT_BACK));
        }

        Direction playerFacing = Direction.getDirectionFromEntityLiving(pos, placer);

        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
                .withProperty(AXIS_FACING, playerFacing.getAxis())
                .withProperty(DIAGONAL, getDiagonalFromPlayerPlacement(placer, facing));
    }

    private static Diagonals getDiagonalFromPlayerPlacement(EntityLivingBase placer, Direction facing) {
        int angleX = (int) ((placer.rotationPitch + 180f) / 180f) & 1;
        int angleY = (int) ((placer.rotationYaw + 180f) / 90f) & 3;

        switch (facing) {
            case DOWN:
            case UP:
                switch (angleY) {
                    default: return Diagonals.TOP_RIGHT; // NORTH EAST
                    case 1: return Diagonals.BOTTOM_RIGHT; // SOUTH EAST
                    case 2: return Diagonals.BOTTOM_LEFT; // SOUTH WEST
                    case 3: return Diagonals.TOP_LEFT; // NORTH WEST
                }

                /*
                NORTH
                3   0

                2   1
                */

            case NORTH:
                return Diagonals.getDiagonalFromUpDownLeftRight(  isEast(angleY), angleX < 1);
            case SOUTH:
                return Diagonals.getDiagonalFromUpDownLeftRight( !isEast(angleY), angleX < 1);
            case EAST:
                return Diagonals.getDiagonalFromUpDownLeftRight( isNorth(angleY), angleX < 1);
            case WEST:
                return Diagonals.getDiagonalFromUpDownLeftRight(!isNorth(angleY), angleX < 1);
        }

        return Diagonals.TOP_RIGHT;
    }

    private static boolean isNorth(int intIn) {
        return intIn == 0 || intIn == 3;
    }

    private static boolean isEast(int intIn) {
        return intIn == 0 || intIn == 1;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void registerModel() {
        IStateMapper stateMapper = new StateMap.Builder().withName(AXIS_FACING).withSuffix("_spiral_bricks").build();
        ModelLoader.setCustomStateMapper(this, stateMapper);
        ModelUtils.registerToState(this, 0, this.getDefaultState().withProperty(DIAGONAL, Diagonals.BOTTOM_LEFT), stateMapper);
    }

    @Override
    public BlockState withRotation(BlockState state, Rotation rot) {
        if (rot == Rotation.NONE) return state;

        Direction.Axis axis = state.getValue(AXIS_FACING);

        if (axis == Direction.Axis.Y) {
            return state.withProperty(DIAGONAL, Diagonals.rotate(state.getValue(DIAGONAL), rot));
        } else {
            if (rot == Rotation.CLOCKWISE_180 || (axis == Direction.Axis.X && rot == Rotation.COUNTERCLOCKWISE_90) || (axis == Direction.Axis.Z && rot == Rotation.CLOCKWISE_90))
                state = state.withProperty(DIAGONAL, Diagonals.mirror(state.getValue(DIAGONAL), Mirror.LEFT_RIGHT));

            return rot.ordinal() % 2 == 0 ? state : state.withProperty(AXIS_FACING, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
        }
    }

    @Override
    public BlockState withMirror(BlockState state, Mirror mirrorIn) {
        return state.withProperty(DIAGONAL, Diagonals.mirrorOn(state.getValue(AXIS_FACING), state.getValue(DIAGONAL), mirrorIn));
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, Direction facing) {
        BlockState state = world.getBlockState(pos);

        if (facing.getAxis() == state.getValue(AXIS_FACING)) {
            state = state.cycleProperty(DIAGONAL);
        } else {
            switch (facing.getAxis()) {
                case X:
                    state = state.withProperty(AXIS_FACING, state.getValue(AXIS_FACING) == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y);
                    break;
                case Y:
                    state = state.withProperty(AXIS_FACING, state.getValue(AXIS_FACING) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
                    break;
                case Z:
                    state = state.withProperty(AXIS_FACING, state.getValue(AXIS_FACING) == Direction.Axis.Y ? Direction.Axis.X : Direction.Axis.Y);
                    break;
            }
        }

        world.setBlockState(pos, state);
        return true;
    }

    @Override
    @Nullable
    public Direction[] getValidRotations(World world, BlockPos pos) {
        return Direction.values();
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {

        Direction.Axis axis = state.getValue(AXIS_FACING);
        if (face.getAxis() == axis) {
            return BlockFaceShape.UNDEFINED;
        }

        Direction top  = axis == Direction.Axis.Y ? Direction.NORTH : Direction.UP;
        Direction left = axis == Direction.Axis.X ? Direction.SOUTH : Direction.WEST;

        Diagonals diagonal = state.getValue(DIAGONAL);
        if (face == (diagonal.isLeft() ? left : left.getOpposite()) || face == (diagonal.isTop() ? top : top.getOpposite())) {
            return BlockFaceShape.SOLID;
        } else {
            return BlockFaceShape.UNDEFINED;
        }
    }

    @Override
    public boolean doesSideBlockRendering(BlockState state, IBlockAccess world, BlockPos pos, Direction face) {
        return getBlockFaceShape(world, state, pos, face) == BlockFaceShape.SOLID;
    }

    @Override
    protected ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(Item.getItemFromBlock(this));
    }
}
