package twilightforest.client.event;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.block.ClimbableHollowLogBlock;
import twilightforest.client.FoliageColorHandler;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

public final class ColorHandler {
	public static final Int2IntFunction CANOPY_COLORIZER = color -> 0xFF000000 | (((color & 0xFEFEFE) + 0x469A66) / 2);
	public static final Int2IntFunction MANGROVE_COLORIZER = color -> 0xFF000000 | (((color & 0xFEFEFE) + 0xC0E694) / 2);

	private ColorHandler() {
	}

	public static void bootstrap() {
		ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, level) -> FoliageColorHandler.clearCache());
		registerBlockColors();
		registerItemColors();
	}

	private static void registerBlockColors() {
		BlockColors blockColors = net.minecraft.client.Minecraft.getInstance().getBlockColors();
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) ->
				0xFF000000 | ColorUtil.hsvToRGB(getter == null ? 0.45F : SimplexNoiseHelper.rippleFractalNoise(2, 128.0F, pos != null ? pos.above(128) : new BlockPos(0, 0, 0), 0.37F, 0.67F, 1.5F), 1.0F, 1.0F),
				TFBlocks.AURORA_BLOCK.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> {
			int normalColor = blockColors.getColor(TFBlocks.AURORA_BLOCK.get().defaultBlockState(), getter, pos, tintIndex);
			int red = (normalColor >> 16) & 255;
			int green = (normalColor >> 8) & 255;
			int blue = normalColor & 255;
			float[] hsb = ColorUtil.rgbToHSV(red, green, blue);
			return 0xFF000000 | ColorUtil.hsvToRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
		}, TFBlocks.AURORA_PILLAR.get(), TFBlocks.AURORA_SLAB.get(), TFBlocks.AURORALIZED_GLASS.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) ->
				blockColors.getColor(Blocks.SHORT_GRASS.defaultBlockState(), getter, pos, tintIndex),
				TFBlocks.SMOKER.get(), TFBlocks.FIRE_JET.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) ->
				getter != null && pos != null ? 0xFF000000 | 2129968 : 0xFF000000 | 7455580,
				TFBlocks.HUGE_LILY_PAD.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> seasonal(pos, 106, 156, 23, 251, 108, 27, 16, 16, 16), TFBlocks.TIME_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> seasonal(pos, 108, 204, 234, 96, 107, 121, 27, 63, 39), TFBlocks.TRANSFORMATION_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> seasonal(pos, 252, 241, 68, 237, 172, 9, 31, 33, 32), TFBlocks.MINING_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> seasonal(pos, 54, 76, 3, 168, 199, 43, 63, 63, 63), TFBlocks.SORTING_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> {
			if (getter == null || pos == null) return -1;
			float f = SimplexNoiseHelper.rippleFractalNoise(2, 32.0F, pos, 0.4F, 1.0F, 2.0F);
			return 0xFF000000 | ColorUtil.hsvToRGB(0.1F, 1.0F - f, (f + 2.0F) / 3.0F);
		}, TFBlocks.TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(), TFBlocks.INFESTED_TOWERWOOD.get(), TFBlocks.MOSSY_TOWERWOOD.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> {
			if (getter == null || pos == null) return FoliageColor.getDefaultColor();
			var level = net.minecraft.client.Minecraft.getInstance().level;
			int original = BiomeColors.getAverageFoliageColor(getter, pos);
			return level != null ? FoliageColorHandler.get(original, level.getBiome(pos).value(), pos.getX(), pos.getZ()) : original;
		}, TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.DARK_LEAVES.get(), TFBlocks.HARDENED_DARK_LEAVES.get(), TFBlocks.GIANT_LEAVES.get(), TFBlocks.FALLEN_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) ->
				getter != null && pos != null ? CANOPY_COLORIZER.applyAsInt(BiomeColors.getAverageFoliageColor(getter, pos)) : FoliageColor.getEvergreenColor(),
				TFBlocks.CANOPY_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) ->
				getter != null && pos != null ? MANGROVE_COLORIZER.applyAsInt(BiomeColors.getAverageFoliageColor(getter, pos)) : FoliageColor.getBirchColor(),
				TFBlocks.MANGROVE_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> rainbow(pos), TFBlocks.RAINBOW_OAK_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> FoliageColor.getEvergreenColor(), TFBlocks.BEANSTALK_LEAVES.get(), TFBlocks.THORN_LEAVES.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) ->
				getter != null && pos != null ? BiomeColors.getAverageGrassColor(getter, pos) : GrassColor.getDefaultColor(),
				TFBlocks.FIDDLEHEAD.get(), TFBlocks.POTTED_FIDDLEHEAD.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) ->
				tintIndex != 0 && getter != null && pos != null ? BiomeColors.getAverageGrassColor(getter, pos) : tintIndex != 0 ? GrassColor.getDefaultColor() : -1,
				horizontalHollowLogs());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> {
			if (state.getValue(ClimbableHollowLogBlock.VARIANT) != HollowLogVariants.Climbable.VINE || tintIndex != 1) return -1;
			return getter != null && pos != null ? BiomeColors.getAverageFoliageColor(getter, pos) : FoliageColor.getDefaultColor();
		}, climbableHollowLogs());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> tintIndex == 1 && getter != null && pos != null ? BiomeColors.getAverageFoliageColor(getter, pos) : tintIndex == 1 ? FoliageColor.getDefaultColor() : -1,
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(), TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFFFF00FF, TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), TFBlocks.PINK_CASTLE_DOOR.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFF00FFFF, TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.BLUE_CASTLE_DOOR.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFFFFFF00, TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_DOOR.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFF4B0082, TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_DOOR.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFF5C1074, TFBlocks.VIOLET_FORCE_FIELD.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFFFA057E, TFBlocks.PINK_FORCE_FIELD.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFFFF5B02, TFBlocks.ORANGE_FORCE_FIELD.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFF89E701, TFBlocks.GREEN_FORCE_FIELD.get());
		ColorProviderRegistry.BLOCK.register((state, getter, pos, tintIndex) -> 0xFF0DDEFF, TFBlocks.BLUE_FORCE_FIELD.get());
	}

	private static void registerItemColors() {
		BlockColors blockColors = net.minecraft.client.Minecraft.getInstance().getBlockColors();
		ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
				stack.getItem() instanceof BlockItem blockItem ? blockColors.getColor(blockItem.getBlock().defaultBlockState(), null, null, tintIndex) : -1,
				itemTintBlocks());
	}

	private static int seasonal(BlockPos pos, int sr, int sg, int sb, int fr, int fg, int fb, int mx, int my, int mz) {
		if (pos == null) return 0xFF000000 | sr << 16 | sg << 8 | sb;
		int fade = pos.getX() * mx + pos.getY() * my + pos.getZ() * mz;
		if ((fade & 256) != 0) fade = 255 - (fade & 255);
		fade &= 255;
		float spring = (255 - fade) / 255.0F;
		float fall = fade / 255.0F;
		int red = (int) (spring * sr + fall * fr);
		int green = (int) (spring * sg + fall * fg);
		int blue = (int) (spring * sb + fall * fb);
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}

	private static int rainbow(BlockPos pos) {
		if (pos == null) return FoliageColor.getDefaultColor();
		int red = pos.getX() * 32 + pos.getY() * 16;
		if ((red & 256) != 0) red = 255 - (red & 255);
		red &= 255;
		int green = pos.getY() * 32 + pos.getZ() * 16;
		if ((green & 256) != 0) green = 255 - (green & 255);
		green ^= 255;
		int blue = pos.getX() * 16 + pos.getZ() * 32;
		if ((blue & 256) != 0) blue = 255 - (blue & 255);
		blue &= 255;
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}

	private static Block[] horizontalHollowLogs() {
		return new Block[] {
				TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(),
				TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get()
		};
	}

	private static Block[] climbableHollowLogs() {
		return new Block[] {
				TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(), TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(),
				TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get()
		};
	}

	private static Block[] itemTintBlocks() {
		return new Block[] {
				TFBlocks.AURORA_BLOCK.get(), TFBlocks.AURORA_PILLAR.get(), TFBlocks.AURORA_SLAB.get(), TFBlocks.AURORALIZED_GLASS.get(), TFBlocks.DARK_LEAVES.get(), TFBlocks.GIANT_LEAVES.get(),
				TFBlocks.SMOKER.get(), TFBlocks.FIRE_JET.get(), TFBlocks.TIME_LEAVES.get(), TFBlocks.TRANSFORMATION_LEAVES.get(), TFBlocks.MINING_LEAVES.get(), TFBlocks.SORTING_LEAVES.get(),
				TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.CANOPY_LEAVES.get(), TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.THORN_LEAVES.get(),
				TFBlocks.BEANSTALK_LEAVES.get(), TFBlocks.FALLEN_LEAVES.get(), TFBlocks.FIDDLEHEAD.get(), TFBlocks.POTTED_FIDDLEHEAD.get(), TFBlocks.PINK_CASTLE_RUNE_BRICK.get(),
				TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_DOOR.get(),
				TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get(), TFBlocks.PINK_FORCE_FIELD.get(), TFBlocks.BLUE_FORCE_FIELD.get(),
				TFBlocks.GREEN_FORCE_FIELD.get(), TFBlocks.ORANGE_FORCE_FIELD.get(), TFBlocks.VIOLET_FORCE_FIELD.get(), TFBlocks.HUGE_LILY_PAD.get(), TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(),
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get()
		};
	}
}
