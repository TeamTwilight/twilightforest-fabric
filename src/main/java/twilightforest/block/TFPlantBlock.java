package twilightforest.block;

import net.minecraft.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.enums.PlantVariant;
import twilightforest.extensions.IBlockMethods;
import twilightforest.extensions.IBlockStateEx;
import twilightforest.network.SpawnFallenLeafFromPacket;
import twilightforest.network.TFPacketHandler;

import java.util.Random;

public class TFPlantBlock extends BushBlock implements BonemealableBlock, IBlockMethods, IPlantable {
	private static final VoxelShape MAYAPPLE_SHAPE = box(4, 0, 4, 13, 6, 13);
	private static final VoxelShape FALLEN_LEAVES_SHAPE = box(0, 0, 0, 16, 1, 16);
	private static final VoxelShape MUSHGLOOM_SHAPE = box(2, 0, 2, 14, 8, 14);
	private static final VoxelShape ROOT_SHAPE = box(2, 0, 2, 14, 16, 14);
	private static final VoxelShape TORCHBERRY_SHAPE = box(1, 2, 1, 15, 16, 15);
	private static final VoxelShape FIDDLEHEAD_SHAPE = box(3, 0, 3, 13, 14, 13);

	public final PlantVariant plantVariant;

	private Random RANDOM = new Random();

	protected TFPlantBlock(PlantVariant plant, BlockBehaviour.Properties props) {
		super(props);
		plantVariant = plant;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockState soil = world.getBlockState(pos.below());

		return switch (plantVariant) {
			case TORCHBERRY, ROOT_STRAND -> TFPlantBlock.canPlaceRootAt(world, pos);
			case FALLEN_LEAVES, MUSHGLOOM, MOSSPATCH -> soil.isFaceSturdy(world, pos, Direction.UP);
			default -> (world.getMaxLocalRawBrightness(pos) >= 3 || world.canSeeSkyFromBelowWater(pos)) && ((IBlockStateEx)soil).canSustainPlant(world, pos.below(), Direction.UP, this);
		};
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter access, BlockPos pos, CollisionContext context) {
		switch(plantVariant) {
			case MOSSPATCH -> { return createCTMShape(TFBlocks.moss_patch, access, pos); }
			case MAYAPPLE -> { return MAYAPPLE_SHAPE; }
			case CLOVERPATCH -> { return createCTMShape(TFBlocks.clover_patch, access, pos); }
			case FIDDLEHEAD -> { return FIDDLEHEAD_SHAPE; }
			case MUSHGLOOM -> { return MUSHGLOOM_SHAPE; }
			case TORCHBERRY -> { return TORCHBERRY_SHAPE; }
			case ROOT_STRAND -> { return ROOT_SHAPE; }
			case FALLEN_LEAVES -> { return FALLEN_LEAVES_SHAPE; }
			default -> { return Shapes.block(); }
		}
	}

	private VoxelShape createCTMShape(Block block, BlockGetter access, BlockPos pos) {
		long seed = pos.getX() * 3129871L ^ pos.getY() * 116129781L ^ pos.getZ();
		seed = seed * seed * 42317861L + seed * 11L;

		int xOff0 = (int) (seed >> 12 & 3L);
		int xOff1 = (int) (seed >> 15 & 3L);
		int zOff0 = (int) (seed >> 18 & 3L);
		int zOff1 = (int) (seed >> 21 & 3L);

		boolean xConnect0 = access.getBlockState(pos.east()).getBlock() == this && access.getBlockState(pos.east()).getBlock() == block;
		boolean xConnect1 = access.getBlockState(pos.west()).getBlock() == this && access.getBlockState(pos.west()).getBlock() == block;
		boolean zConnect0 = access.getBlockState(pos.south()).getBlock() == this && access.getBlockState(pos.north()).getBlock() == block;
		boolean zConnect1 = access.getBlockState(pos.north()).getBlock() == this && access.getBlockState(pos.south()).getBlock() == block;

		return Shapes.create(new AABB(xConnect1 ? 0F : (1F + xOff1) / 16F, 0.0F, zConnect1 ? 0F : (1F + zOff1) / 16F,
				xConnect0 ? 1F : (15F - xOff0) / 16F, 1F / 16F, zConnect0 ? 1F : (15F - zOff0) / 16F));
	}

	public static boolean canPlaceRootAt(LevelReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos.above());
		if (state.getMaterial() == Material.DIRT || state.getMaterial() == Material.GRASS) {
			// can always hang below dirt blocks
			return true;
		} else {
			return (state.getBlock() == TFBlocks.root_strand
					|| state == TFBlocks.root.defaultBlockState());
		}
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() == this) {
			return switch (plantVariant) {
				case MOSSPATCH, MUSHGLOOM -> PlantType.CAVE;
				default -> PlantType.PLAINS;
			};
		}
		return PlantType.PLAINS;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		super.animateTick(state, world, pos, random);

		if (state.getBlock() == TFBlocks.moss_patch && random.nextInt(10) == 0) {
			world.addParticle(ParticleTypes.MYCELIUM, pos.getX() + random.nextFloat(), pos.getY() + 0.1F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		} else if (state.getBlock() == TFBlocks.fallen_leaves && random.nextInt(50) == 0) {
			float dist = 10F;
			if (!world.canSeeSkyFromBelowWater(pos)) {
				for (int y = 0; y <= dist; y++)
					if (world.getBlockState(pos.above(y)).getMaterial() == Material.LEAVES) {
						dist = y;
						break;
					}
				if (dist > 10F)
					return;
			}

			int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), world, pos, 0);
			int r = Mth.clamp(((color >> 16) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int g = Mth.clamp(((color >> 8) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int b = Mth.clamp((color & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			world.addParticle(new LeafParticleData(r, g, b), pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entityIn) {
		super.entityInside(state, world, pos, entityIn);
		if (state.getBlock() == TFBlocks.fallen_leaves && entityIn instanceof LivingEntity && (entityIn.getDeltaMovement().x() != 0 || entityIn.getDeltaMovement().z() != 0) && RANDOM.nextBoolean()) {
			if(world.isClientSide) {
				int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), world, pos, 0);
				int r = Mth.clamp(((color >> 16) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
				int g = Mth.clamp(((color >> 8) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
				int b = Mth.clamp((color & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
					world.addParticle(new LeafParticleData(r, g, b),
							pos.getX() + world.random.nextFloat(),
							pos.getY(),
							pos.getZ() + world.random.nextFloat(),

							(world.random.nextFloat() * -0.5F) * entityIn.getDeltaMovement().x(),
							world.random.nextFloat() * 0.5F + 0.25F,
							(world.random.nextFloat() * -0.5F) * entityIn.getDeltaMovement().z()
				);
			} else if (world instanceof ServerLevel)
				((ServerChunkCache)entityIn.getCommandSenderWorld().getChunkSource()).broadcast(entityIn, new SpawnFallenLeafFromPacket(pos, entityIn.getDeltaMovement()));
				//TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entityIn), new SpawnFallenLeafFromPacket(pos, entityIn.getDeltaMovement()));
		}
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
		return state.getBlock() == TFBlocks.root_strand && isBottomOpen(level, pos);
	}

	@Override
	public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
		return state.getBlock() == TFBlocks.root_strand && isBottomOpen(level, pos);
	}

	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
		if(state.getBlock() == TFBlocks.root_strand) {
			BlockPos.MutableBlockPos mutable = pos.mutable();
			do {
				mutable.move(Direction.DOWN);
			} while(level.getBlockState(mutable).is(TFBlocks.root_strand));
			if(level.getBlockState(mutable).isAir() || level.getBlockState(mutable).getMaterial().isReplaceable()) {
				level.setBlockAndUpdate(mutable, TFBlocks.root_strand.defaultBlockState());
			}
		}
	}

	private boolean isBottomOpen(BlockGetter level, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		do {
			mutable.move(Direction.DOWN);
		} while(level.getBlockState(mutable).is(TFBlocks.root_strand));

		return level.getBlockState(mutable).isAir() || level.getBlockState(mutable).getMaterial().isReplaceable();
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 100;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}
}
