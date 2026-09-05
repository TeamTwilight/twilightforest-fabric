package twilightforest.init;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import twilightforest.TFMain;
import twilightforest.block.ClimbableHollowLogBlock;
import twilightforest.client.properties.PotionFlaskTintSource;
import twilightforest.enums.HollowLogVariants;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

import java.util.List;

public class TFTintSources {
	public static final Int2IntFunction CANOPY_COLORIZER = color -> 0xFF000000 | (((color & 0xFEFEFE) + 0x469A66) / 2);
	public static final Int2IntFunction MANGROVE_COLORIZER = color -> 0xFF000000 | (((color & 0xFEFEFE) + 0xC0E694) / 2);

	private static final BlockTintSource AURORA = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return auroraTint(null, null);
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			return auroraTint(level, pos);
		}
	};

	private static final BlockTintSource AURORA_STRUCTURE = state -> {
		// TODO: Verify this. It's kind of how you get the color
		BlockColors blockColors = Minecraft.getInstance().getBlockColors();

		int normalColor = blockColors.getTintSource(TFBlocks.AURORA_BLOCK.defaultBlockState(), 0).color(TFBlocks.AURORA_BLOCK.defaultBlockState());
		int red = (normalColor >> 16) & 255;
		int green = (normalColor >> 8) & 255;
		int blue = normalColor & 255;

		float[] hsb = ColorUtil.rgbToHSV(red, green, blue);

		return 0xFF000000 | ColorUtil.hsvToRGB(
			hsb[0],
			hsb[1] * 0.5F,
			Math.min(hsb[2] + 0.4F, 0.9F)
		);
	};

	private static final BlockTintSource HUGE_LILY_PAD = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return 0xFF000000 | 2129968;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			return 0xFF000000 | 7455580;
		}
	};

	private static final BlockTintSource TIME_LEAVES = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return 0xFF000000 | 106 << 16 | 156 << 8 | 23;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			int fade = pos.getX() * 16 + pos.getY() * 16 + pos.getZ() * 16;

			if ((fade & 256) != 0) {
				fade = 255 - (fade & 255);
			}
			fade &= 255;

			float spring = (255 - fade) / 255F;
			float fall = fade / 255F;

			int red = (int) (spring * 106 + fall * 251);
			int green = (int) (spring * 156 + fall * 108);
			int blue = (int) (spring * 23 + fall * 27);

			return 0xFF000000 | red << 16 | green << 8 | blue;
		}
	};

	private static final BlockTintSource TRANSFORMATION_LEAVES = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return 0xFF000000 | 108 << 16 | 204 << 8 | 234;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			int fade = pos.getX() * 27 + pos.getY() * 63 + pos.getZ() * 39;

			if ((fade & 256) != 0) {
				fade = 255 - (fade & 255);
			}
			fade &= 255;

			float spring = (255 - fade) / 255F;
			float fall = fade / 255F;

			int red = (int) (spring * 108 + fall * 96);
			int green = (int) (spring * 204 + fall * 107);
			int blue = (int) (spring * 234 + fall * 121);

			return 0xFF000000 | red << 16 | green << 8 | blue;
		}
	};

	private static final BlockTintSource MINING_LEAVES = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return 0xFF000000 | 252 << 16 | 241 << 8 | 68;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			int fade = pos.getX() * 31 + pos.getY() * 33 + pos.getZ() * 32;

			if ((fade & 256) != 0) {
				fade = 255 - (fade & 255);
			}
			fade &= 255;

			float spring = (255 - fade) / 255F;
			float fall = fade / 255F;

			int red = (int) (spring * 252 + fall * 237);
			int green = (int) (spring * 241 + fall * 172);
			int blue = (int) (spring * 68 + fall * 9);

			return 0xFF000000 | red << 16 | green << 8 | blue;
		}
	};

	private static final BlockTintSource SORTING_LEAVES = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return 0xFF000000 | 54 << 16 | 76 << 8 | 3;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			int fade = pos.getX() * 63 + pos.getY() * 63 + pos.getZ() * 63;
			if ((fade & 256) != 0) {
				fade = 255 - (fade & 255);
			}
			fade &= 255;

			float spring = (255 - fade) / 255F;
			float fall = fade / 255F;

			int red = (int) (spring * 54 + fall * 168);
			int green = (int) (spring * 76 + fall * 199);
			int blue = (int) (spring * 3 + fall * 43);

			return 0xFF000000 | red << 16 | green << 8 | blue;
		}
	};

	private static final BlockTintSource TOWERWOOD = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return -1;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			float f = SimplexNoiseHelper.rippleFractalNoise(
				2,
				32.0f,
				pos,
				0.4f,
				1.0f,
				2f
			);

			return 0xFF000000 | ColorUtil.hsvToRGB(
				0.1f,
				1f - f,
				(f + 2f) / 3f
			);
		}
	};

	private static final BlockTintSource FOLIAGE_DEFAULT = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return FoliageColor.FOLIAGE_DEFAULT;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			return BiomeColors.getAverageFoliageColor(level, pos);
		}
	};

	private static final BlockTintSource CANOPY = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return FoliageColor.FOLIAGE_EVERGREEN;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			return CANOPY_COLORIZER.applyAsInt(
				BiomeColors.getAverageFoliageColor(level, pos)
			);
		}
	};

	private static final BlockTintSource MANGROVE = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return FoliageColor.FOLIAGE_BIRCH;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			return MANGROVE_COLORIZER.applyAsInt(
				BiomeColors.getAverageFoliageColor(level, pos)
			);
		}
	};

	private static final BlockTintSource RAINBOW_OAK = new BlockTintSource() {
		@Override
		public int color(BlockState state) {
			return FoliageColor.FOLIAGE_DEFAULT;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			// RAINBOW!
			int red = pos.getX() * 32 + pos.getY() * 16;
			if ((red & 256) != 0) {
				red = 255 - (red & 255);
			}
			red &= 255;

			int green = pos.getY() * 32 + pos.getZ() * 16;
			if ((green & 256) != 0) {
				green = 255 - (green & 255);
			}
			green ^= 255;

			int blue = pos.getX() * 16 + pos.getZ() * 32;
			if ((blue & 256) != 0) {
				blue = 255 - (blue & 255);
			}
			blue &= 255;

			return 0xFF000000 | red << 16 | green << 8 | blue;
		}
	};

	private static final BlockTintSource HOLLOW_LOG_CLIMBABLE = new BlockTintSource() {
		//TODO: For datagen: apply to correct layer
		@Override
		public int color(BlockState state) {
			return FoliageColor.FOLIAGE_DEFAULT;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			if (state.getValue(ClimbableHollowLogBlock.VARIANT) == HollowLogVariants.Climbable.VINE) {
				return BiomeColors.getAverageFoliageColor(level, pos);
			}
			return BlockTintSource.super.colorInWorld(state, level, pos);
		}
	};

	public static void init() {
		TFMain.LOGGER.info("Initializing tint sources...");
		registerBlocks();
		registerItems();
	}

	private static void registerBlocks() {
		BlockColorRegistry.register(List.of(AURORA), TFBlocks.AURORA_BLOCK);
		BlockColorRegistry.register(List.of(AURORA_STRUCTURE), TFBlocks.AURORA_PILLAR, TFBlocks.AURORA_SLAB, TFBlocks.AURORALIZED_GLASS);
		BlockColorRegistry.register(List.of(BlockTintSources.grass()), TFBlocks.SMOKER, TFBlocks.FIRE_JET); //TODO: This got the block tint from Grass, but this is about the same?
		BlockColorRegistry.register(List.of(HUGE_LILY_PAD), TFBlocks.HUGE_LILY_PAD);
		BlockColorRegistry.register(List.of(TIME_LEAVES), TFBlocks.TIME_LEAVES);
		BlockColorRegistry.register(List.of(TRANSFORMATION_LEAVES), TFBlocks.TRANSFORMATION_LEAVES);
		BlockColorRegistry.register(List.of(MINING_LEAVES), TFBlocks.MINING_LEAVES);
		BlockColorRegistry.register(List.of(SORTING_LEAVES), TFBlocks.SORTING_LEAVES);
		BlockColorRegistry.register(List.of(TOWERWOOD), TFBlocks.TOWERWOOD, TFBlocks.CRACKED_TOWERWOOD, TFBlocks.INFESTED_TOWERWOOD, TFBlocks.MOSSY_TOWERWOOD);
		BlockColorRegistry.register(List.of(FOLIAGE_DEFAULT), TFBlocks.TWILIGHT_OAK_LEAVES, TFBlocks.DARK_LEAVES, TFBlocks.HARDENED_DARK_LEAVES, TFBlocks.GIANT_LEAVES, TFBlocks.FALLEN_LEAVES);
		BlockColorRegistry.register(List.of(CANOPY), TFBlocks.CANOPY_LEAVES);
		BlockColorRegistry.register(List.of(MANGROVE), TFBlocks.MANGROVE_LEAVES);
		BlockColorRegistry.register(List.of(RAINBOW_OAK), TFBlocks.RAINBOW_OAK_LEAVES);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(FoliageColor.FOLIAGE_EVERGREEN)), TFBlocks.BEANSTALK_LEAVES, TFBlocks.THORN_LEAVES);
		BlockColorRegistry.register(List.of(BlockTintSources.grass()), TFBlocks.FIDDLEHEAD, TFBlocks.POTTED_FIDDLEHEAD);
		BlockColorRegistry.register(List.of(BlockTintSources.grass()), TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, //TODO: For datagen: apply to correct layer
			TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL,
			TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL);
		BlockColorRegistry.register(List.of(HOLLOW_LOG_CLIMBABLE), TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE,
			TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE, TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE);
		BlockColorRegistry.register(List.of(BlockTintSources.foliage()), TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE, TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE); //TODO: For datagen: apply to correct layer
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFFFF00FF)), TFBlocks.PINK_CASTLE_RUNE_BRICK, TFBlocks.PINK_CASTLE_DOOR);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFF00FFFF)), TFBlocks.BLUE_CASTLE_RUNE_BRICK, TFBlocks.BLUE_CASTLE_DOOR);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFFFFFF00)), TFBlocks.YELLOW_CASTLE_RUNE_BRICK, TFBlocks.YELLOW_CASTLE_DOOR);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFF4B0082)), TFBlocks.VIOLET_CASTLE_RUNE_BRICK, TFBlocks.VIOLET_CASTLE_DOOR);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFF5C1074)), TFBlocks.VIOLET_FORCE_FIELD);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFFFA057E)), TFBlocks.PINK_FORCE_FIELD);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFFFF5B02)), TFBlocks.ORANGE_FORCE_FIELD);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFF89E701)), TFBlocks.GREEN_FORCE_FIELD);
		BlockColorRegistry.register(List.of(BlockTintSources.constant(0xFF0DDEFF)), TFBlocks.BLUE_FORCE_FIELD);
	}

	private static void registerItems() {
		ItemTintSources.ID_MAPPER.put(TFMain.prefix("potion_flask"), PotionFlaskTintSource.TYPE);
	}

	private static int auroraTint(@Nullable BlockAndTintGetter getter, @Nullable BlockPos pos) {
		return 0xFF000000 | ColorUtil.hsvToRGB(getter == null ? 0.45F : SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos != null ? pos.above(128) : new BlockPos(0, 0, 0), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f);
	}
}