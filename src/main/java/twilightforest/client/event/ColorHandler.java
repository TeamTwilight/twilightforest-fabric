package twilightforest.client.event;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import twilightforest.block.ClimbableHollowLogBlock;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFSpawnEggItem;
import twilightforest.item.ArcticArmorItem;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

public class ColorHandler {
	public static final Int2IntFunction CANOPY_COLORIZER = color -> 0xFF000000 | (((color & 0xFEFEFE) + 0x469A66) / 2);
	public static final Int2IntFunction MANGROVE_COLORIZER = color -> 0xFF000000 | (((color & 0xFEFEFE) + 0xC0E694) / 2);

	public static void registerBlockColors() {
		// Aurora block - animated color based on position
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFF000000 | ColorUtil.hsvToRGB(world == null ? 0.45F : SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos != null ? pos.above(128) : new BlockPos(0, 0, 0), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f),
			TFBlocks.AURORA_BLOCK.get()
		);

		// Aurora pillar/slab/glass - darker variant of aurora block color
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				// Get the aurora block's color at this position
				int normalColor = world != null && pos != null
					? 0xFF000000 | ColorUtil.hsvToRGB(SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos.above(128), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f)
					: 0xFF000000 | ColorUtil.hsvToRGB(0.45F, 1.0f, 1.0f);

				int red = (normalColor >> 16) & 255;
				int blue = normalColor & 255;
				int green = (normalColor >> 8) & 255;

				float[] hsb = ColorUtil.rgbToHSV(red, green, blue);

				return 0xFF000000 | ColorUtil.hsvToRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
			},
			TFBlocks.AURORA_PILLAR.get(),
			TFBlocks.AURORA_SLAB.get(),
			TFBlocks.AURORALIZED_GLASS.get()
		);

		// Smoker / Fire jet - grass-like tint
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor(),
			TFBlocks.SMOKER.get(),
			TFBlocks.FIRE_JET.get()
		);

		// Huge lily pad - slightly tinted green (matches vanilla lily pad)
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> world != null && pos != null ? 0xFF000000 | 2129968 : 0xFF000000 | 7455580,
			TFBlocks.HUGE_LILY_PAD.get()
		);

		// Time leaves - position-based green/brown fade
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (world == null || pos == null) {
					return 0xFF000000 | (106 << 16) | (156 << 8) | 23;
				} else {
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

					return 0xFF000000 | (red << 16) | (green << 8) | blue;
				}
			},
			TFBlocks.TIME_LEAVES.get()
		);

		// Transformation leaves - position-based blue/green fade
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (world == null || pos == null) {
					return 0xFF000000 | (108 << 16) | (204 << 8) | 234;
				} else {
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

					return 0xFF000000 | (red << 16) | (green << 8) | blue;
				}
			},
			TFBlocks.TRANSFORMATION_LEAVES.get()
		);

		// Mining leaves - position-based yellow/gold fade
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (world == null || pos == null) {
					return 0xFF000000 | (252 << 16) | (241 << 8) | 68;
				} else {
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

					return 0xFF000000 | (red << 16) | (green << 8) | blue;
				}
			},
			TFBlocks.MINING_LEAVES.get()
		);

		// Sorting leaves - position-based dark green fade
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (world == null || pos == null) {
					return 0xFF000000 | (54 << 16) | (76 << 8) | 3;
				} else {
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

					return 0xFF000000 | (red << 16) | (green << 8) | blue;
				}
			},
			TFBlocks.SORTING_LEAVES.get()
		);

		// Towerwood - animated brownish color based on noise
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (world == null || pos == null) {
					return -1;
				} else {
					float f = SimplexNoiseHelper.rippleFractalNoise(2, 32.0f, pos, 0.4f, 1.0f, 2f);
					return 0xFF000000 | ColorUtil.hsvToRGB(0.1f, 1f - f, (f + 2f) / 3f);
				}
			},
			TFBlocks.TOWERWOOD.get(),
			TFBlocks.CRACKED_TOWERWOOD.get(),
			TFBlocks.INFESTED_TOWERWOOD.get(),
			TFBlocks.MOSSY_TOWERWOOD.get()
		);

		// Biome-tinted leaves (like vanilla leaves)
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.getDefaultColor(),
			TFBlocks.TWILIGHT_OAK_LEAVES.get(),
			TFBlocks.DARK_LEAVES.get(),
			TFBlocks.HARDENED_DARK_LEAVES.get(),
			TFBlocks.GIANT_LEAVES.get(),
			TFBlocks.FALLEN_LEAVES.get()
		);

		// Canopy leaves - modified biome foliage (evergreen-ish)
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> world != null && pos != null
				? CANOPY_COLORIZER.applyAsInt(BiomeColors.getAverageFoliageColor(world, pos))
				: FoliageColor.getEvergreenColor(),
			TFBlocks.CANOPY_LEAVES.get()
		);

		// Mangrove leaves - modified biome foliage (birch-ish)
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> world != null && pos != null
				? MANGROVE_COLORIZER.applyAsInt(BiomeColors.getAverageFoliageColor(world, pos))
				: FoliageColor.getBirchColor(),
			TFBlocks.MANGROVE_LEAVES.get()
		);

		// Rainbow oak leaves - rainbow color based on position
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (world == null || pos == null) {
					return FoliageColor.getDefaultColor();
				} else {
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

					return 0xFF000000 | (red << 16) | (green << 8) | blue;
				}
			},
			TFBlocks.RAINBOW_OAK_LEAVES.get()
		);

		// Beanstalk and thorn leaves - evergreen color
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> FoliageColor.getEvergreenColor(),
			TFBlocks.BEANSTALK_LEAVES.get(),
			TFBlocks.THORN_LEAVES.get()
		);

		// Fiddlehead - grass color
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor(),
			TFBlocks.FIDDLEHEAD.get(),
			TFBlocks.POTTED_FIDDLEHEAD.get()
		);

		// Hollow horizontal logs - grass color for inside tint (tintIndex 0)
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (tintIndex != 0) {
					return world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor();
				} else {
					return -1;
				}
			},
			TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get()
		);

		// Twilight Forest hollow horizontal logs - grass color
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (tintIndex != 0) {
					return world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor();
				} else {
					return -1;
				}
			},
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get()
		);

		// Hollow climbable logs - foliage color for vine variant (tintIndex 1)
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (state.getValue(ClimbableHollowLogBlock.VARIANT) != HollowLogVariants.Climbable.VINE || tintIndex != 1) {
					return -1;
				} else {
					if (world != null && pos != null) {
						return BiomeColors.getAverageFoliageColor(world, pos);
					} else {
						return FoliageColor.getDefaultColor();
					}
				}
			},
			TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get()
		);

		// Miniature structures - grass color for tintIndex 1
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> {
				if (tintIndex != 1) {
					return -1;
				} else {
					if (world != null && pos != null) {
						return BiomeColors.getAverageGrassColor(world, pos);
					} else {
						return GrassColor.getDefaultColor();
					}
				}
			},
			TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(),
			TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get()
		);

		// Castle rune bricks and doors - vibrant dye colors
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFFFF00FF, // Magenta
			TFBlocks.PINK_CASTLE_RUNE_BRICK.get(),
			TFBlocks.PINK_CASTLE_DOOR.get()
		);
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFF00FFFF, // Cyan
			TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(),
			TFBlocks.BLUE_CASTLE_DOOR.get()
		);
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFFFFFF00, // Yellow
			TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(),
			TFBlocks.YELLOW_CASTLE_DOOR.get()
		);
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFF4B0082, // Indigo
			TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(),
			TFBlocks.VIOLET_CASTLE_DOOR.get()
		);

		// Force fields - fixed colors
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFF5C1074,
			TFBlocks.VIOLET_FORCE_FIELD.get()
		);
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFFFA057E,
			TFBlocks.PINK_FORCE_FIELD.get()
		);
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFFFF5B02,
			TFBlocks.ORANGE_FORCE_FIELD.get()
		);
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFF89E701,
			TFBlocks.GREEN_FORCE_FIELD.get()
		);
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> 0xFF0DDEFF,
			TFBlocks.BLUE_FORCE_FIELD.get()
		);
	}

	public static void registerItemColors() {
		// All blocks that have block color handlers - pass through using BlockItem lookup
		ColorProviderRegistry.ITEM.register(
			(stack, tintIndex) -> stack.getItem() instanceof BlockItem blocc ? blocc.getBlock().defaultBlockState().getBlock() == Blocks.AIR ? -1 : getBlockDefaultColor(blocc.getBlock()) : -1,
			TFBlocks.AURORA_BLOCK.get().asItem(),
			TFBlocks.AURORA_PILLAR.get().asItem(),
			TFBlocks.AURORA_SLAB.get().asItem(),
			TFBlocks.AURORALIZED_GLASS.get().asItem(),
			TFBlocks.DARK_LEAVES.get().asItem(),
			TFBlocks.GIANT_LEAVES.get().asItem(),
			TFBlocks.SMOKER.get().asItem(),
			TFBlocks.FIRE_JET.get().asItem(),
			TFBlocks.TIME_LEAVES.get().asItem(),
			TFBlocks.TRANSFORMATION_LEAVES.get().asItem(),
			TFBlocks.MINING_LEAVES.get().asItem(),
			TFBlocks.SORTING_LEAVES.get().asItem(),
			TFBlocks.TWILIGHT_OAK_LEAVES.get().asItem(),
			TFBlocks.CANOPY_LEAVES.get().asItem(),
			TFBlocks.MANGROVE_LEAVES.get().asItem(),
			TFBlocks.RAINBOW_OAK_LEAVES.get().asItem(),
			TFBlocks.THORN_LEAVES.get().asItem(),
			TFBlocks.BEANSTALK_LEAVES.get().asItem(),
			TFBlocks.FALLEN_LEAVES.get().asItem(),
			TFBlocks.FIDDLEHEAD.get().asItem(),
			TFBlocks.POTTED_FIDDLEHEAD.get().asItem(),
			TFBlocks.PINK_CASTLE_RUNE_BRICK.get().asItem(),
			TFBlocks.BLUE_CASTLE_RUNE_BRICK.get().asItem(),
			TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get().asItem(),
			TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get().asItem(),
			TFBlocks.YELLOW_CASTLE_DOOR.get().asItem(),
			TFBlocks.BLUE_CASTLE_DOOR.get().asItem(),
			TFBlocks.PINK_CASTLE_DOOR.get().asItem(),
			TFBlocks.VIOLET_CASTLE_DOOR.get().asItem(),
			TFBlocks.PINK_FORCE_FIELD.get().asItem(),
			TFBlocks.BLUE_FORCE_FIELD.get().asItem(),
			TFBlocks.GREEN_FORCE_FIELD.get().asItem(),
			TFBlocks.ORANGE_FORCE_FIELD.get().asItem(),
			TFBlocks.VIOLET_FORCE_FIELD.get().asItem(),
			TFBlocks.HUGE_LILY_PAD.get().asItem(),
			TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get().asItem(),
			TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get().asItem()
		);

		// Arctic armor - dye color (tint index 1)
		ColorProviderRegistry.ITEM.register(
			(stack, index) -> index != 1 ? -1 : DyedItemColor.getOrDefault(stack, ArcticArmorItem.DEFAULT_COLOR),
			TFItems.ARCTIC_HELMET.get(),
			TFItems.ARCTIC_CHESTPLATE.get(),
			TFItems.ARCTIC_LEGGINGS.get(),
			TFItems.ARCTIC_BOOTS.get()
		);

		// Potion flasks - tinted based on potion color
		ColorProviderRegistry.ITEM.register(
			(stack, index) -> {
				if (index > 0) return -1;
				var contents = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS.get(), PotionFlaskComponent.EMPTY);
				if (contents.potion().potion().isEmpty()) return -1;
				return contents.potion().getColor();
			},
			TFItems.BRITTLE_FLASK.get(),
			TFItems.GREATER_FLASK.get()
		);

		// Spawn eggs - pass through to SpawnEggItem.getColor()
		for (var holder : TFEntities.SPAWN_EGGS.getEntries()) {
			var item = holder.get();
			if (item instanceof TFSpawnEggItem egg) {
				ColorProviderRegistry.ITEM.register(
					(stack, index) -> FastColor.ARGB32.opaque(egg.getColor(index)),
					item
				);
			}
		}
	}

	private static int getBlockDefaultColor(Block block) {
		if (block == TFBlocks.AURORA_BLOCK.get()) {
			return 0xFF000000 | ColorUtil.hsvToRGB(0.45F, 1.0f, 1.0f);
		}
		if (block == TFBlocks.AURORA_PILLAR.get() || block == TFBlocks.AURORA_SLAB.get() || block == TFBlocks.AURORALIZED_GLASS.get()) {
			int normalColor = 0xFF000000 | ColorUtil.hsvToRGB(0.45F, 1.0f, 1.0f);
			int red = (normalColor >> 16) & 255;
			int blue = normalColor & 255;
			int green = (normalColor >> 8) & 255;
			float[] hsb = ColorUtil.rgbToHSV(red, green, blue);
			return 0xFF000000 | ColorUtil.hsvToRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
		}
		if (block == TFBlocks.SMOKER.get() || block == TFBlocks.FIRE_JET.get()) {
			return GrassColor.getDefaultColor();
		}
		if (block == TFBlocks.TIME_LEAVES.get()) {
			return 0xFF000000 | (106 << 16) | (156 << 8) | 23;
		}
		if (block == TFBlocks.TRANSFORMATION_LEAVES.get()) {
			return 0xFF000000 | (108 << 16) | (204 << 8) | 234;
		}
		if (block == TFBlocks.MINING_LEAVES.get()) {
			return 0xFF000000 | (252 << 16) | (241 << 8) | 68;
		}
		if (block == TFBlocks.SORTING_LEAVES.get()) {
			return 0xFF000000 | (54 << 16) | (76 << 8) | 3;
		}
		if (block == TFBlocks.TOWERWOOD.get() || block == TFBlocks.CRACKED_TOWERWOOD.get() || block == TFBlocks.INFESTED_TOWERWOOD.get() || block == TFBlocks.MOSSY_TOWERWOOD.get()) {
			return -1;
		}
		if (block == TFBlocks.TWILIGHT_OAK_LEAVES.get() || block == TFBlocks.DARK_LEAVES.get() || block == TFBlocks.HARDENED_DARK_LEAVES.get() || block == TFBlocks.GIANT_LEAVES.get() || block == TFBlocks.FALLEN_LEAVES.get()) {
			return FoliageColor.getDefaultColor();
		}
		if (block == TFBlocks.CANOPY_LEAVES.get()) {
			return FoliageColor.getEvergreenColor();
		}
		if (block == TFBlocks.MANGROVE_LEAVES.get()) {
			return FoliageColor.getBirchColor();
		}
		if (block == TFBlocks.RAINBOW_OAK_LEAVES.get()) {
			return FoliageColor.getDefaultColor();
		}
		if (block == TFBlocks.BEANSTALK_LEAVES.get() || block == TFBlocks.THORN_LEAVES.get()) {
			return FoliageColor.getEvergreenColor();
		}
		if (block == TFBlocks.FIDDLEHEAD.get() || block == TFBlocks.POTTED_FIDDLEHEAD.get()) {
			return GrassColor.getDefaultColor();
		}
		if (block == TFBlocks.HUGE_LILY_PAD.get()) {
			return 0xFF000000 | 7455580;
		}
		if (block == TFBlocks.PINK_CASTLE_RUNE_BRICK.get() || block == TFBlocks.PINK_CASTLE_DOOR.get()) {
			return 0xFFFF00FF;
		}
		if (block == TFBlocks.BLUE_CASTLE_RUNE_BRICK.get() || block == TFBlocks.BLUE_CASTLE_DOOR.get()) {
			return 0xFF00FFFF;
		}
		if (block == TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get() || block == TFBlocks.YELLOW_CASTLE_DOOR.get()) {
			return 0xFFFFFF00;
		}
		if (block == TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get() || block == TFBlocks.VIOLET_CASTLE_DOOR.get()) {
			return 0xFF4B0082;
		}
		if (block == TFBlocks.VIOLET_FORCE_FIELD.get()) {
			return 0xFF5C1074;
		}
		if (block == TFBlocks.PINK_FORCE_FIELD.get()) {
			return 0xFFFA057E;
		}
		if (block == TFBlocks.ORANGE_FORCE_FIELD.get()) {
			return 0xFFFF5B02;
		}
		if (block == TFBlocks.GREEN_FORCE_FIELD.get()) {
			return 0xFF89E701;
		}
		if (block == TFBlocks.BLUE_FORCE_FIELD.get()) {
			return 0xFF0DDEFF;
		}
		if (block == TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get() || block == TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get()) {
			return GrassColor.getDefaultColor();
		}
		return -1;
	}
}
