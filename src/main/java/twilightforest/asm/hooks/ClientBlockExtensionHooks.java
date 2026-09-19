package twilightforest.asm.hooks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;

public final class ClientBlockExtensionHooks {
	public static boolean shouldApplyHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
		if (state.is(TFBlocks.WISPY_CLOUD) || state.is(TFBlocks.SNOWY_CLOUD) || state.is(TFBlocks.RAINY_CLOUD) || state.is(TFBlocks.FLUFFY_CLOUD)) {
			return addHitEffects(level, target, manager);
		}
		return !state.shouldSpawnTerrainParticles();
	}

	public static boolean shouldApplyDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
		if (state.is(TFBlocks.WISPY_CLOUD) || state.is(TFBlocks.SNOWY_CLOUD) || state.is(TFBlocks.RAINY_CLOUD) || state.is(TFBlocks.FLUFFY_CLOUD)) {
			return addDestroyEffects(state, level, pos, manager);
		}
		return !state.shouldSpawnTerrainParticles();
	}

	private static boolean addHitEffects(Level level, HitResult target, ParticleEngine manager) {
		if (level.getRandom().nextBoolean() && target instanceof BlockHitResult hitResult) {
			BlockPos pos = hitResult.getBlockPos();
			BlockState blockstate = level.getBlockState(pos);
			if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				Direction side = hitResult.getDirection();

				int posX = pos.getX();
				int posY = pos.getY();
				int posZ = pos.getZ();

				AABB aabb = blockstate.getShape(level, pos).bounds();
				double x = (double) posX + level.getRandom().nextDouble() * (aabb.maxX - aabb.minX - (double) 0.2F) + (double) 0.1F + aabb.minX;
				double y = (double) posY + level.getRandom().nextDouble() * (aabb.maxY - aabb.minY - (double) 0.2F) + (double) 0.1F + aabb.minY;
				double z = (double) posZ + level.getRandom().nextDouble() * (aabb.maxZ - aabb.minZ - (double) 0.2F) + (double) 0.1F + aabb.minZ;

				if (side == Direction.DOWN) y = (double) posY + aabb.minY - (double) 0.1F;
				if (side == Direction.UP) y = (double) posY + aabb.maxY + (double) 0.1F;

				if (side == Direction.NORTH) z = (double) posZ + aabb.minZ - (double) 0.1F;
				if (side == Direction.SOUTH) z = (double) posZ + aabb.maxZ + (double) 0.1F;

				if (side == Direction.WEST) x = (double) posX + aabb.minX - (double) 0.1F;
				if (side == Direction.EAST) x = (double) posX + aabb.maxX + (double) 0.1F;

				Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF, x, y, z, (double) side.getStepX() * 0.01D, (double) side.getStepY() * 0.01D, (double) side.getStepZ() * 0.01D);
				if (particle == null) return true;
				manager.add(particle);
			}
		}
		return true;
	}

	private static boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
		state.getShape(level, pos).forAllBoxes((boxX, boxY, boxZ, boxX1, boxY1, boxZ1) -> {
			double xSize = Math.min(1.0D, boxX1 - boxX);
			double ySize = Math.min(1.0D, boxY1 - boxY);
			double zSize = Math.min(1.0D, boxZ1 - boxZ);

			int xMax = Math.max(2, Mth.ceil(xSize / 0.25D));
			int yMax = Math.max(2, Mth.ceil(ySize / 0.25D));
			int zMax = Math.max(2, Mth.ceil(zSize / 0.25D));

			for (int xSlice = 0; xSlice < xMax; ++xSlice) {
				if (level.getRandom().nextInt(3) == 1) continue;
				for (int ySlice = 0; ySlice < yMax; ++ySlice) {
					if (level.getRandom().nextInt(3) == 1) continue;
					for (int zSlice = 0; zSlice < zMax; ++zSlice) {
						if (level.getRandom().nextInt(3) == 1) continue;

						double speedX = ((double) xSlice + 0.5D) / (double) xMax;
						double speedY = ((double) ySlice + 0.5D) / (double) yMax;
						double speedZ = ((double) zSlice + 0.5D) / (double) zMax;

						double x = speedX * xSize + boxX;
						double y = speedY * ySize + boxY;
						double z = speedZ * zSize + boxZ;

						speedX = (speedX - 0.5D) * 0.05D;
						speedY = (speedY - 0.5D) * 0.05D;
						speedZ = (speedZ - 0.5D) * 0.05D;

						Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, speedX, speedY, speedZ);
						if (particle == null) return;
						manager.add(particle);
					}
				}
			}
		});
		return true;
	}
}