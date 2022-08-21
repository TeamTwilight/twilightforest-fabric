package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.network.SpawnFallenLeafFromPacket;
import twilightforest.network.TFPacketHandler;

public class FallenLeavesBlock extends TFPlantBlock {

	private static final VoxelShape FALLEN_LEAVES_SHAPE = box(0, 0, 0, 16, 1, 16);

	public FallenLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.below()).isFaceSturdy(reader, pos, Direction.UP) || reader.getFluidState(pos.below()).getType() == Fluids.WATER;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return FALLEN_LEAVES_SHAPE;
	}

	@Override
	public boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
		return super.mayPlaceOn(state, getter, pos) || ((getter.getFluidState(pos).getType() == Fluids.WATER || state.getMaterial() == Material.ICE) && getter.getFluidState(pos.above()).getType() == Fluids.EMPTY);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		if (random.nextInt(50) == 0) {
			float dist = 10F;
			if (!level.canSeeSkyFromBelowWater(pos)) {
				for (int y = 0; y <= dist; y++)
					if (level.getBlockState(pos.above(y)).getMaterial() == Material.LEAVES) {
						dist = y;
						break;
					}
				if (dist > 10F)
					return;
			}

			int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), level, pos, 0);
			int r = Mth.clamp(((color >> 16) & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int g = Mth.clamp(((color >> 8) & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int b = Mth.clamp((color & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
			level.addParticle(new LeafParticleData(r, g, b), pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		super.entityInside(state, level, pos, entity);
		if (entity instanceof LivingEntity && (entity.getDeltaMovement().x() != 0 || entity.getDeltaMovement().z() != 0) && level.getRandom().nextBoolean()) {
			if(level.isClientSide()) {
				int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), level, pos, 0);
				int r = Mth.clamp(((color >> 16) & 0xFF) + level.getRandom().nextInt(0x22) - 0x11, 0x00, 0xFF);
				int g = Mth.clamp(((color >> 8) & 0xFF) + level.getRandom().nextInt(0x22) - 0x11, 0x00, 0xFF);
				int b = Mth.clamp((color & 0xFF) + level.getRandom().nextInt(0x22) - 0x11, 0x00, 0xFF);
				level.addParticle(new LeafParticleData(r, g, b),
						pos.getX() + level.getRandom().nextFloat(),
						pos.getY(),
						pos.getZ() + level.getRandom().nextFloat(),

						(level.getRandom().nextFloat() * -0.5F) * entity.getDeltaMovement().x(),
						level.getRandom().nextFloat() * 0.5F + 0.25F,
						(level.getRandom().nextFloat() * -0.5F) * entity.getDeltaMovement().z()
				);
			} else if (level instanceof ServerLevel)
				TFPacketHandler.CHANNEL.sendToClientsTracking(new SpawnFallenLeafFromPacket(pos, entity.getDeltaMovement()), entity);
		}
	}
}
