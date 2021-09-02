package twilightforest.client;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.client.renderer.BiomeColors;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.block.*;
import twilightforest.item.ArcticArmorItem;
import twilightforest.item.TFItems;

import java.awt.Color;

@Environment(EnvType.CLIENT)
public final class ColorHandler {

	public static void registerBlockColors(BlockColors blockColors) {

		blockColors.register((state, worldIn, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : Color.HSBtoRGB(worldIn == null ? 0.45F : AuroraBrickBlock.rippleFractialNoise(2, 128.0f, pos != null ? pos.above(128) : new BlockPos(0, 0, 0), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f), TFBlocks.aurora_block);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			int normalColor = blockColors.getColor(TFBlocks.aurora_block.defaultBlockState(), worldIn, pos, tintIndex);

			int red = (normalColor >> 16) & 255;
			int blue = normalColor & 255;
			int green = (normalColor >> 8) & 255;

			float[] hsb = Color.RGBtoHSB(red, blue, green, null);

			return Color.HSBtoRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
		}, TFBlocks.aurora_pillar, TFBlocks.aurora_slab, TFBlocks.auroralized_glass);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return FoliageColor.getDefaultColor();
			}

			int red = 0;
			int grn = 0;
			int blu = 0;

			for (int dz = -1; dz <= 1; ++dz) {
				for (int dx = -1; dx <= 1; ++dx) {
					//int i2 = worldIn.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos.add(dx, 0, dz));
					int i2 = BiomeColors.getAverageFoliageColor(worldIn, pos.offset(dx, 0, dz));
					red += (i2 & 16711680) >> 16;
					grn += (i2 & 65280) >> 8;
					blu += i2 & 255;
				}
			}

			return (red / 9 & 255) << 16 | (grn / 9 & 255) << 8 | blu / 9 & 255;
		}, TFBlocks.dark_leaves, TFBlocks.giant_leaves);
		blockColors.register((state, worldIn, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : blockColors.getColor(Blocks.GRASS.defaultBlockState(), worldIn, pos, tintIndex), TFBlocks.smoker, TFBlocks.fire_jet);
		blockColors.register((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? 2129968 : 7455580, TFBlocks.huge_lilypad);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return 106 << 16 | 156 << 8 | 23;
			} else {
				int red, green, blue;

				int fade = pos.getX() * 16 + pos.getY() * 16 + pos.getZ() * 16;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 106 + fall * 251);
				green = (int) (spring * 156 + fall * 108);
				blue = (int) (spring * 23 + fall * 27);

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.time_leaves);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return 108 << 16 | 204 << 8 | 234;
			} else {
				int red, green, blue;

				int fade = pos.getX() * 27 + pos.getY() * 63 + pos.getZ() * 39;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 108 + fall * 96);
				green = (int) (spring * 204 + fall * 107);
				blue = (int) (spring * 234 + fall * 121);

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.transformation_leaves);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return 252 << 16 | 241 << 8 | 68;
			} else {
				int red, green, blue;

				int fade = pos.getX() * 31 + pos.getY() * 33 + pos.getZ() * 32;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 252 + fall * 237);
				green = (int) (spring * 241 + fall * 172);
				blue = (int) (spring * 68 + fall * 9);

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.mining_leaves);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return 54 << 16 | 76 << 8 | 3;
			} else {
				int red, green, blue;

				int fade = pos.getX() * 63 + pos.getY() * 63 + pos.getZ() * 63;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 54 + fall * 168);
				green = (int) (spring * 76 + fall * 199);
				blue = (int) (spring * 3 + fall * 43);

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.sorting_leaves);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return -1;
			} else {
				float f = AuroraBrickBlock.rippleFractialNoise(2, 32.0f, pos, 0.4f, 1.0f, 2f);
				return Color.HSBtoRGB(0.1f, 1f - f, (f + 2f) / 3f);
			}
		}, TFBlocks.tower_wood, TFBlocks.tower_wood_cracked, TFBlocks.tower_wood_infested, TFBlocks.tower_wood_mossy);
		blockColors.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x48B518;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						//int color = world.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos);
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				return (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
			}
		}, TFBlocks.oak_leaves);
		blockColors.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x609860;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						//int color = world.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos);
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				int normalColor = (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
				// canopy colorizer
				return ((normalColor & 0xFEFEFE) + 0x469A66) / 2;
				//return ((normalColor & 0xFEFEFE) + 0x009822) / 2;
			}
		}, TFBlocks.canopy_leaves);
		blockColors.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x80A755;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						//int color = world.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos);
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				int normalColor = (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
				// mangrove colors
				return ((normalColor & 0xFEFEFE) + 0xC0E694) / 2;
			}
		}, TFBlocks.mangrove_leaves);
		blockColors.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x48B518;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						//int color = world.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos);
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				// RAINBOW!
				red = pos.getX() * 32 + pos.getY() * 16;
				if ((red & 256) != 0) {
					red = 255 - (red & 255);
				}
				red &= 255;

				blue = pos.getY() * 32 + pos.getZ() * 16;
				if ((blue & 256) != 0) {
					blue = 255 - (blue & 255);
				}
				blue ^= 255;

				green = pos.getX() * 16 + pos.getZ() * 32;
				if ((green & 256) != 0) {
					green = 255 - (green & 255);
				}
				green &= 255;


				return red << 16 | blue << 8 | green;
			}
		}, TFBlocks.rainboak_leaves);
		blockColors.register((state, worldIn, pos, tintIndex) -> FoliageColor.getEvergreenColor(), TFBlocks.beanstalk_leaves, TFBlocks.thorn_leaves);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex != 0) {
				return 0xFFFFFF;
			} else {
				if (worldIn != null && pos != null) {
					return BiomeColors.getAverageFoliageColor(worldIn, pos);
				} else {
					return FoliageColor.getDefaultColor();
				}
			}
		}, TFBlocks.fallen_leaves);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex != 0) {
				return 0xFFFFFF;
			} else {
				if (worldIn != null && pos != null) {
					return BiomeColors.getAverageGrassColor(worldIn, pos);
				} else {
					return GrassColor.get(0.5D, 1.0D);
				}
			}
		}, TFBlocks.fiddlehead, TFBlocks.potted_fiddlehead);
		blockColors.register((state, worldIn, pos, tintIndex) -> GrassColor.get(0.5D, 1.0D),
				TFBlocks.twilight_portal_miniature_structure, /*TFBlocks.hedge_maze_miniature_structure, TFBlocks.hollow_hill_miniature_structure, TFBlocks.quest_grove_miniature_structure, TFBlocks.mushroom_tower_miniature_structure,*/ TFBlocks.naga_courtyard_miniature_structure, TFBlocks.lich_tower_miniature_structure //TFBlocks.minotaur_labyrinth_miniature_structure,
				/*TFBlocks.hydra_lair_miniature_structure, TFBlocks.goblin_stronghold_miniature_structure, TFBlocks.dark_tower_miniature_structure, TFBlocks.yeti_cave_miniature_structure, TFBlocks.aurora_palace_miniature_structure, TFBlocks.troll_cave_cottage_miniature_structure, TFBlocks.final_castle_miniature_structure*/);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFF00FF;
		}, TFBlocks.castle_rune_brick_pink, TFBlocks.castle_door_pink);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x00FFFF;
		}, TFBlocks.castle_rune_brick_blue, TFBlocks.castle_door_blue);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFFFF00;
		}, TFBlocks.castle_rune_brick_yellow, TFBlocks.castle_door_yellow);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x4B0082;
		}, TFBlocks.castle_rune_brick_purple, TFBlocks.castle_door_purple);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x5C1074;
		}, TFBlocks.force_field_purple);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFA057E;
		}, TFBlocks.force_field_pink);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFF5B02;
		}, TFBlocks.force_field_orange);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x89E701;
		}, TFBlocks.force_field_green);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x0DDEFF;
		}, TFBlocks.force_field_blue);
	}


	public static void registerItemColors(ItemColors itemColors, BlockColors blockColors) {
		itemColors.register((stack, tintIndex) -> blockColors.getColor(((BlockItem)stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex),
				TFBlocks.aurora_block, TFBlocks.aurora_pillar, TFBlocks.aurora_slab, TFBlocks.auroralized_glass, TFBlocks.dark_leaves, TFBlocks.giant_leaves, TFBlocks.smoker, TFBlocks.fire_jet,
				TFBlocks.time_leaves, TFBlocks.transformation_leaves, TFBlocks.mining_leaves, TFBlocks.sorting_leaves, TFBlocks.oak_leaves, TFBlocks.canopy_leaves, TFBlocks.mangrove_leaves, TFBlocks.rainboak_leaves, TFBlocks.thorn_leaves, TFBlocks.beanstalk_leaves,
				TFBlocks.fallen_leaves, TFBlocks.fiddlehead, TFBlocks.potted_fiddlehead, TFBlocks.castle_rune_brick_pink, TFBlocks.castle_rune_brick_blue, TFBlocks.castle_rune_brick_yellow, TFBlocks.castle_rune_brick_purple,
				TFBlocks.castle_door_yellow, TFBlocks.castle_door_blue, TFBlocks.castle_door_pink, TFBlocks.castle_door_purple, TFBlocks.force_field_pink, TFBlocks.force_field_blue, TFBlocks.force_field_green, TFBlocks.force_field_orange, TFBlocks.force_field_purple, TFBlocks.huge_lilypad,
				TFBlocks.twilight_portal_miniature_structure, /*TFBlocks.hedge_maze_miniature_structure, TFBlocks.hollow_hill_miniature_structure, TFBlocks.quest_grove_miniature_structure, TFBlocks.mushroom_tower_miniature_structure,*/ TFBlocks.naga_courtyard_miniature_structure, TFBlocks.lich_tower_miniature_structure//, TFBlocks.minotaur_labyrinth_miniature_structure,
				/*TFBlocks.hydra_lair_miniature_structure, TFBlocks.goblin_stronghold_miniature_structure, TFBlocks.dark_tower_miniature_structure, TFBlocks.yeti_cave_miniature_structure, TFBlocks.aurora_palace_miniature_structure, TFBlocks.troll_cave_cottage_miniature_structure, TFBlocks.final_castle_miniature_structure*/);

		itemColors.register((stack, tintIndex) ->
				stack.getItem() instanceof ArcticArmorItem
						? ((ArcticArmorItem) stack.getItem()).getColor(stack, tintIndex)
						: 0xFFFFFF,
				TFItems.arctic_helmet, TFItems.arctic_chestplate, TFItems.arctic_leggings, TFItems.arctic_boots);

		//FIXME IE Compat
		/*if (ModList.isLoaded("immersiveengineering")) {
			itemColors.register(TFShaderItem::getShaderColors, ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")));
			for(Rarity r: ShaderRegistry.rarityWeightMap.keySet()) {
				itemColors.register((stack, tintIndex) -> {
					int c = r.color.getColor();

					float d = tintIndex + 1;

					return (int) ((c >> 16 & 0xFF) / d) << 16
							| (int) ((c >> 8 & 0xFF) / d) << 8
							| (int) ((c & 0xFF) / d);
				}, ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_" + r.name().toLowerCase(Locale.US).replace(':', '_'))));
			}
		}*/
	}

	private ColorHandler() {}
}
