package twilightforest.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import twilightforest.TFMazeMapData;
import twilightforest.network.MazeMapPacket;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

// [VanillaCopy] super everything, but with appropriate redirections to our own datastructures. finer details noted
// FIXME: Map does not display data. Investigate

public class MazeMapItem extends MapItem {

	public static final String STR_ID = "mazemap";
	private static final int YSEARCH = 3;

	protected final boolean mapOres;

	protected MazeMapItem(boolean mapOres, Properties props) {
		super(props);
		this.mapOres = mapOres;
	}

	// [VanillaCopy] super with own item and methods, plus y and mapOres
	public static ItemStack setupNewMap(Level world, int worldX, int worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking, int worldY, boolean mapOres) {
		ItemStack itemstack = new ItemStack(mapOres ? TFItems.ore_map : TFItems.maze_map);
		createMapData(itemstack, world, worldX, worldZ, scale, trackingPosition, unlimitedTracking, world.dimension(), worldY);
		return itemstack;
	}

	@Nullable
	public static TFMazeMapData getData(ItemStack stack, Level world) {
		return TFMazeMapData.getMazeMapData(world, getMapName(getMapId(stack)));
	}

	//@Override
	public static MapItemSavedData getSavedData(ItemStack pStack, Level pLevel) {
		// Forge: Add instance method so that mods can override
		Item map = pStack.getItem();
		if(map instanceof MapItem) {
			return ((MazeMapItem)map).getCustomMapData(pStack, pLevel);
		}
		return null;
	}

	@Nullable
	protected TFMazeMapData getCustomMapData(ItemStack stack, Level world) {
		TFMazeMapData mapdata = getData(stack, world);
		if (mapdata == null && !world.isClientSide) {
			mapdata = MazeMapItem.createMapData(stack, world, world.getLevelData().getXSpawn(), world.getLevelData().getZSpawn(), 0, false, false, world.dimension(), world.getLevelData().getYSpawn());
		}

		return mapdata;
	}

	// [VanillaCopy] super with own item and id, and y parameter
	private static TFMazeMapData createMapData(ItemStack stack, Level world, int x, int z, int scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension, int y) {
		int i = world.getFreeMapId();
//		TFMazeMapData mapdata = new TFMazeMapData(getMapName(i));
		TFMazeMapData mapdata = new TFMazeMapData(x, z, (byte)scale, trackingPosition, unlimitedTracking, false, dimension);
		mapdata.calculateMapCenter(world, x, y, z, scale); // call our own map center calculation
		TFMazeMapData.registerMazeMapData(world, mapdata); // call our own register method
		stack.getOrCreateTag().putInt("map", i);
		return mapdata;
	}

	public static String getMapName(int id) {
		return STR_ID + "_" + id;
	}

	// [VanillaCopy] of superclass, with sane variable names and noted changes
	@SuppressWarnings("unused")
	@Override
	public void update(Level world, Entity viewer, MapItemSavedData data) {
		if (world.dimension() == data.dimension && viewer instanceof Player) {
			int blocksPerPixel = 1 << data.scale;
			int centerX = data.x;
			int centerZ = data.z;
			int viewerX = Mth.floor(viewer.getX() - centerX) / blocksPerPixel + 64;
			int viewerZ = Mth.floor(viewer.getZ() - centerZ) / blocksPerPixel + 64;
			int viewRadiusPixels = 16; // TF this is smaller on the maze map

			if (world.dimensionType().hasCeiling()) {
				viewRadiusPixels /= 2;
			}

			MapItemSavedData.HoldingPlayer mapdata$mapinfo = data.getHoldingPlayer((Player) viewer);
			++mapdata$mapinfo.step;
			boolean flag = false;

			for (int xPixel = viewerX - viewRadiusPixels + 1; xPixel < viewerX + viewRadiusPixels; ++xPixel) {
				if ((xPixel & 15) == (mapdata$mapinfo.step & 15) || flag) {
					flag = false;
					double d0 = 0.0D;

					for (int zPixel = viewerZ - viewRadiusPixels - 1; zPixel < viewerZ + viewRadiusPixels; ++zPixel) {
						if (xPixel >= 0 && zPixel >= -1 && xPixel < 128 && zPixel < 128) {
							int xPixelDist = xPixel - viewerX;
							int zPixelDist = zPixel - viewerZ;
							boolean shouldFuzz = xPixelDist * xPixelDist + zPixelDist * zPixelDist > (viewRadiusPixels - 2) * (viewRadiusPixels - 2);
							int worldX = (centerX / blocksPerPixel + xPixel - 64) * blocksPerPixel;
							int worldZ = (centerZ / blocksPerPixel + zPixel - 64) * blocksPerPixel;
							Multiset<MaterialColor> multiset = HashMultiset.create();
							LevelChunk chunk = world.getChunkAt(new BlockPos(worldX, 0, worldZ));

							int brightness = 1;
							if (!chunk.isEmpty()) {
								int worldXRounded = worldX & 15;
								int worldZRounded = worldZ & 15;
								int numLiquid = 0;
								double d1 = 0.0D;

								if (world.dimensionType().hasCeiling()) {
									int l3 = worldX + worldZ * 231871;
									l3 = l3 * l3 * 31287121 + l3 * 11;

									if ((l3 >> 20 & 1) == 0) {
										multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(world, BlockPos.ZERO), 10);
									} else {
										multiset.add(Blocks.STONE.defaultBlockState().getMapColor(world, BlockPos.ZERO), 100);
									}

								} else {
									// TF - remove extra 2 levels of loops
									// maze maps are always 0 scale, which is 1 pixel = 1 block, so the loops are unneeded
									int yCenter = ((TFMazeMapData) data).yCenter;
									BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(worldXRounded, yCenter, worldZRounded);
									BlockState state = chunk.getBlockState(blockpos$mutableblockpos);

									multiset.add(state.getMapColor(world, blockpos$mutableblockpos));

									if (state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.AIR) {
										for (int i = -YSEARCH; i <= YSEARCH; i++) {
											blockpos$mutableblockpos.setY(yCenter + i);
											BlockState searchID = chunk.getBlockState(blockpos$mutableblockpos);
											if (searchID.getBlock() != Blocks.STONE && searchID.getBlock() != Blocks.AIR) {
												state = searchID;
												if (i > 0) {
													brightness = 2;
												}
												if (i < 0) {
													brightness = 0;
												}

												break;
											}
										}
									}

									if (mapOres) {
										// recolor ores
										if (state.getBlock() == Blocks.COAL_ORE) {
											multiset.add(MaterialColor.COLOR_BLACK, 1000);
										} else if (state.getBlock() == Blocks.GOLD_ORE) {
											multiset.add(MaterialColor.GOLD, 1000);
										} else if (state.getBlock() == Blocks.IRON_ORE) {
											multiset.add(MaterialColor.METAL, 1000);
										} else if (state.getBlock() == Blocks.LAPIS_ORE) {
											multiset.add(MaterialColor.LAPIS, 1000);
										} else if (state.getBlock() == Blocks.REDSTONE_ORE) {
											multiset.add(MaterialColor.COLOR_RED, 1000);
										} else if (state.getBlock() == Blocks.DIAMOND_ORE) {
											multiset.add(MaterialColor.DIAMOND, 1000);
										} else if (state.getBlock() == Blocks.EMERALD_ORE) {
											multiset.add(MaterialColor.EMERALD, 1000);
										} else if (state.getBlock() != Blocks.AIR && state.is(BlockTags.COAL_ORES)) {
											multiset.add(MaterialColor.COLOR_PINK, 1000);
										} else if (state.getBlock() != Blocks.AIR && state.is(BlockTags.IRON_ORES)) {
											multiset.add(MaterialColor.COLOR_PINK, 1000);
										} else if (state.getBlock() != Blocks.AIR && state.is(BlockTags.REDSTONE_ORES)) {
											multiset.add(MaterialColor.COLOR_PINK, 1000);
										} else if (state.getBlock() != Blocks.AIR && state.is(BlockTags.LAPIS_ORES)) {
											multiset.add(MaterialColor.COLOR_PINK, 1000);
										} else if (state.getBlock() != Blocks.AIR && state.is(BlockTags.DIAMOND_ORES)) {
											multiset.add(MaterialColor.COLOR_PINK, 1000);
										}
									}
								}

								MaterialColor mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MaterialColor.NONE);

								if (zPixel >= 0 && xPixelDist * xPixelDist + zPixelDist * zPixelDist < viewRadiusPixels * viewRadiusPixels && (!shouldFuzz || (xPixel + zPixel & 1) != 0)) {
									byte b0 = data.colors[xPixel + zPixel * 128];
									byte b1 = (byte) (mapcolor.id * 4 + brightness);

									if (b0 != b1) {
										data.colors[xPixel + zPixel * 128] = b1;
										data.setDirty();
										flag = true;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	// [VanillaCopy] super but shows a dot if player is too far in the vertical direction as well
	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int slot, boolean isSelected) {
		if (!worldIn.isClientSide) {
			TFMazeMapData mapdata = this.getCustomMapData(stack, worldIn);

			if (mapdata != null) {
				if (entityIn instanceof Player) {
					Player entityplayer = (Player) entityIn;
					mapdata.tickCarriedBy(entityplayer, stack);

					// TF - if player is far away vertically, show a dot
					int yProximity = Mth.floor(entityplayer.getY() - mapdata.yCenter);
					if (yProximity < -YSEARCH || yProximity > YSEARCH) {
						MapDecoration decoration = mapdata.decorations.get(entityplayer.getName().getString());
						if (decoration != null) {
							mapdata.decorations.put(entityplayer.getName().getString(), new MapDecoration(MapDecoration.Type.PLAYER_OFF_MAP, decoration.getX(), decoration.getY(), decoration.getRot(), null));
						}
					}
				}

				if (!mapdata.locked && (isSelected || entityIn instanceof Player && ((Player)entityIn).getOffhandItem() == stack)) {
					this.update(worldIn, entityIn, mapdata);
				}
			}
		}
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		// disable zooming
	}

	@Override
	@Nullable
	public Packet<?> getUpdatePacket(ItemStack stack, Level worldIn, Player player) {
		Packet<?> p = super.getUpdatePacket(stack, worldIn, player);
		if (p instanceof ClientboundMapItemDataPacket) {
			return new MazeMapPacket((ClientboundMapItemDataPacket) p);
		} else {
			return p;
		}
	}
}
