package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.WeighedRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.IMCHandler;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.config.CaveStalactiteConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TFGenCaveStalactite extends Feature<CaveStalactiteConfig> {

	private static final List<StalactiteEntry> largeHillStalactites = new ArrayList<>();
	private static final List<StalactiteEntry> mediumHillStalactites = new ArrayList<>();
	private static final List<StalactiteEntry> smallHillStalactites = new ArrayList<>();

	/**
	 * Initializes a stalactite builder.  Actually also makes stalagmites
	 */
//	public TFGenCaveStalactite(Function<Dynamic<?>, ? extends NoFeatureConfig> configIn, Block block, float size, boolean down) {
//		this(configIn, block.getDefaultState(), size, down);
//	}

//	public TFGenCaveStalactite(Function<Dynamic<?>, ? extends NoFeatureConfig> configIn, BlockState blockState, float size, boolean down) {
//		super(configIn);
//		this.blockState = blockState;
//		this.sizeFactor = size;
//		this.maxLength = -1;
//		this.minHeight = -1;
//		this.hang = down;
//	}

	/**
	 * Initializes a stalactite builder
	 */
//	public TFGenCaveStalactite(Function<Dynamic<?>, ? extends NoFeatureConfig> configIn, BlockState blockState, float size, int maxLength, int minHeight) {
//		super(configIn);
//		this.blockState = blockState;
//		this.sizeFactor = size;
//		this.maxLength = maxLength;
//		this.minHeight = minHeight;
//		this.hang = true;
//	}

	public TFGenCaveStalactite(Codec<CaveStalactiteConfig> configIn) {
		super(configIn);
	}

	/**
	 * Makes a random stalactite appropriate to the cave size
	 * <p>
	 * All include iron, coal and glowstone.
	 * <p>
	 * Gold and redstone appears in size 2 and larger caves.
	 * <p>
	 * Diamonds and lapis only appear in size 3 and larger caves.
	 */
	public static CaveStalactiteConfig makeRandomOreStalactite(Random rand, int hillSize) {
		if (hillSize >= 3 || hillSize >= 2 && rand.nextInt(5) == 0) {
			return WeighedRandom.getRandomItem(rand, largeHillStalactites).get().stalactite;
		}
		if (hillSize >= 2 || hillSize >= 1 && rand.nextInt(5) == 0) {
			return WeighedRandom.getRandomItem(rand, mediumHillStalactites).get().stalactite;
		}
		return WeighedRandom.getRandomItem(rand, smallHillStalactites).get().stalactite;
	}

	/**
	 * Generates a stalactite at the specified location.
	 * The coordinates should be inside a cave.
	 * This will return false if it can't find a valid ceiling and floor, or if there are other errors.
	 * @param ctx
	 */
	@Override
	public boolean place(FeaturePlaceContext<CaveStalactiteConfig> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos placementPos = ctx.origin();
		Random random = ctx.random();
		CaveStalactiteConfig config = ctx.config();

		return startStalactite(world, placementPos, random, config.blockState, config.sizeFactor, config.maxLength, config.minHeight, config.hang);
	}

	public static boolean startStalactite(WorldGenLevel world, BlockPos placementPos, Random random, BlockState blockState, float sizeFactor, int maxLength, int minHeight, boolean hang) {
		return startStalactite(world, placementPos, random, blockState, sizeFactor, maxLength, minHeight, hang, world.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, placementPos.getX(), placementPos.getZ()) - 1);
	}

	// Abstracted out so various Structures can use this without creating a new Feature Configuration
	public static boolean startStalactite(WorldGenLevel world, BlockPos placementPos, Random random, BlockState blockState, float sizeFactor, int maxLength, int minHeight, boolean hang, final int startHeight) {
		int ceiling = startHeight;
		int floor = world.getMinBuildHeight();

		if (ceiling <= floor) return false; // Failed: Ceiling is lower or same level as floor

		BlockPos.MutableBlockPos movingPos = placementPos.mutable();
		// find a ceiling
		for (int ty = startHeight; ty > floor; ty--) {
			movingPos.setY(ty);

			BlockState state = world.getBlockState(movingPos);

			// Keep scanning until we find air
			if (!state.isAir())
				continue;

			// okay, we found a valid ceiling.
			ceiling = ty;
			break;
		}

		if (ceiling >= startHeight) return false; // Failed: No ceiling found

		// find a floor
		for (int ty = ceiling - 1; ty > floor; ty--) {
			movingPos.setY(ty);

			BlockState state = world.getBlockState(movingPos);

			// if we're in air, continue
			if (state.isAir())
				continue;

			// okay, we found a valid floor.
			floor = ty;
			break;
		}

		int length = (int) ((ceiling - floor) * sizeFactor * random.nextFloat());

		// check max length
		if (maxLength > -1 && length > maxLength) {
			length = maxLength;
		}

		// check minimum height
		if (minHeight > -1 && ceiling - floor - length < minHeight) {
			return false;
		}

		return makeSpike(world, random, new BlockPos(placementPos.getX(), hang ? ceiling : floor, placementPos.getZ()), length, hang, blockState);
	}

	public static boolean makeSpike(LevelAccessor world, Random random, BlockPos pos, int maxLength, boolean hang, BlockState state) {
		int diameter = (int) (maxLength / 4.5F); // diameter of the base

		// let's see...
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				// determine how long this spike will be.
				int absx = Math.abs(dx);
				int absz = Math.abs(dz);
				int dist = (int) (Math.max(absx, absz) + Math.min(absx, absz) * 0.5F);
				int spikeLength = 0;

				if (dist == 0) {
					spikeLength = maxLength;
				}

				if (dist > 0) {
					spikeLength = random.nextInt((int) (maxLength / (dist + 0.25F)));
				}

				int dir = hang ? -1 : 1;

				// check if we're generating over anything
				if (!world.getBlockState(pos.offset(dx, -dir, dz)).getMaterial().isSolid()) {
					spikeLength = 0;
				}

				for (int dy = 0; dy != spikeLength * dir; dy += dir) {
					world.setBlock(pos.offset(dx, dy, dz), state, 3);
				}
			}
		}

		return true;
	}

	public static class StalactiteEntry extends WeighedRandom.WeighedRandomItem {

		final CaveStalactiteConfig stalactite;

		StalactiteEntry(CaveStalactiteConfig stalactite, int itemWeight) {
			super(itemWeight);
			this.stalactite = stalactite;
		}

		public StalactiteEntry(BlockState blockState, float size, int maxLength, int minHeight, int itemWeight) {
			this(new CaveStalactiteConfig(blockState, size, maxLength, minHeight, true), itemWeight);
		}
	}

	public static void registerStalactite(int hillSize, BlockState blockState, float size, int maxLength, int minHeight, int itemWeight) {
		if (itemWeight > 0) {
			registerStalactite(hillSize, new StalactiteEntry(blockState, size, maxLength, minHeight, itemWeight));
		}
	}

	private static void registerStalactite(int hillSize, StalactiteEntry entry) {
		if (hillSize <= 1)
			smallHillStalactites.add(entry);
		if (hillSize <= 2)
			mediumHillStalactites.add(entry);
		largeHillStalactites.add(entry);
	}

	/*
	 * Current default weights are as follows:
	 *
	 * Large (total 195 = 13*15):
	 * 2/13 diamond
	 * 2/13 lapis
	 * 1/13 emerald
	 * 8/13 [medium pool]
	 *
	 * Medium (total 120 = 6*20):
	 * 1/6 gold
	 * 1/6 redstone
	 * 3/6 [small pool]
	 *
	 * Small (total 60 = 5*12):
	 * 2/5 iron
	 * 2/5 coal
	 * 1/5 glowstone
	 */
	private static void addDefaultStalactites() {
		registerStalactite(3, Blocks.DIAMOND_ORE.defaultBlockState(), 0.5F, 4, 16, 30);
		registerStalactite(3, Blocks.LAPIS_ORE.defaultBlockState(), 0.8F, 8, 1, 30);
		registerStalactite(3, Blocks.EMERALD_ORE.defaultBlockState(), 0.5F, 3, 12, 15);

		registerStalactite(2, Blocks.GOLD_ORE.defaultBlockState(), 0.6F, 6, 1, 20);
		registerStalactite(2, Blocks.REDSTONE_ORE.defaultBlockState(), 0.8F, 8, 1, 40);

		registerStalactite(1, Blocks.IRON_ORE.defaultBlockState(), 0.7F, 8, 1, 24);
		registerStalactite(1, Blocks.COAL_ORE.defaultBlockState(), 0.8F, 12, 1, 24);
		registerStalactite(1, Blocks.GLOWSTONE.defaultBlockState(), 0.5F, 8, 1, 12);
	}

	public static void loadStalactites() {
		smallHillStalactites.clear();
		mediumHillStalactites.clear();
		largeHillStalactites.clear();

		TFConfig.COMMON_CONFIG.DIMENSION.hollowHillStalactites.load();
		if (TFConfig.COMMON_CONFIG.DIMENSION.hollowHillStalactites.useConfigOnly) {
			if (smallHillStalactites.isEmpty()) {
				TwilightForestMod.LOGGER.info("Not all hollow hills are populated with the config, adding fallback");
				registerStalactite(1, Blocks.STONE.defaultBlockState(), 0.7F, 8, 1, 1);
			}
			return;
		}
		addDefaultStalactites();
		IMCHandler.getStalactites().forEach(TFGenCaveStalactite::registerStalactite);
	}
}
