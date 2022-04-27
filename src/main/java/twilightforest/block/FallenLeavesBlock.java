package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.network.SpawnFallenLeafFromPacket;
import twilightforest.network.TFPacketHandler;

import java.util.Random;

public class FallenLeavesBlock extends TFPlantBlock {

	private static final VoxelShape FALLEN_LEAVES_SHAPE = box(0, 0, 0, 16, 1, 16);

	public FallenLeavesBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).isFaceSturdy(world, pos, Direction.UP);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter access, BlockPos pos, CollisionContext context) {
		return FALLEN_LEAVES_SHAPE;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		super.animateTick(state, world, pos, random);
		if (random.nextInt(50) == 0) {
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
		if (entityIn instanceof LivingEntity && (entityIn.getDeltaMovement().x() != 0 || entityIn.getDeltaMovement().z() != 0) && RANDOM.nextBoolean()) {
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
				TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entityIn), new SpawnFallenLeafFromPacket(pos, entityIn.getDeltaMovement()));
		}
	}
}
