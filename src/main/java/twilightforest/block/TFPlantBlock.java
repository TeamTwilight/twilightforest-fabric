package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.util.PlantType;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import twilightforest.init.TFBlocks;

public abstract class TFPlantBlock extends BushBlock implements BonemealableBlock {

	protected TFPlantBlock(BlockBehaviour.Properties properties) {
		super(properties);
		FlammableBlockRegistry.getDefaultInstance().add(this, getFlammability(), getFireSpreadSpeed());
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		BlockState soil = reader.getBlockState(pos.below());
		return (reader.getMaxLocalRawBrightness(pos) >= 3 || reader.canSeeSkyFromBelowWater(pos)) && soil.canSustainPlant(reader, pos.below(), Direction.UP, this);
	}

	public static boolean canPlaceRootAt(LevelReader reader, BlockPos pos) {
		BlockState state = reader.getBlockState(pos.above());
		if (state.getMaterial() == Material.DIRT || state.getMaterial() == Material.GRASS) {
			// can always hang below dirt blocks
			return true;
		} else {
			return (state.getBlock() == TFBlocks.ROOT_STRAND.get()
					|| state.is(TFBlocks.ROOT_BLOCK.get())
					|| state.is(TFBlocks.LIVEROOT_BLOCK.get())
					|| state.is(TFBlocks.MANGROVE_ROOT.get()));
		}
	}

	@Override
	public PlantType getPlantType(BlockGetter getter, BlockPos pos) {
		return PlantType.PLAINS;
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter getter, BlockPos pos, BlockState state, boolean client) {
		return false;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return false;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos pos, BlockState state) {
	}

	public int getFlammability() {
		return 100;
	}

	public int getFireSpreadSpeed() {
		return 60;
	}
}
