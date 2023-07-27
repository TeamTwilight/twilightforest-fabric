package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;

public class AntibuilderBlockEntity extends BlockEntity {
	private static final int REVERT_CHANCE = 10;

	private static final int RADIUS = 4;
	private static final int DIAMETER = 2 * RADIUS + 1;
	private static final double PLAYER_RANGE = 16.0;

	private final RandomSource rand = RandomSource.create();

	private int tickCount;
	private boolean slowScan;
	private int ticksSinceChange;

	private BlockState[] blockData;

	public AntibuilderBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.ANTIBUILDER.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, AntibuilderBlockEntity te) {
		if (te.anyPlayerInRange()) {
			te.tickCount++;

			if (level.isClientSide()) {
				double x = pos.getX() + level.getRandom().nextFloat();
				double y = pos.getY() + level.getRandom().nextFloat();
				double z = pos.getZ() + level.getRandom().nextFloat();
				level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);

				// occasionally make a little red dust line to outline our radius
				if (te.rand.nextInt(10) == 0) {
					te.makeRandomOutline();
					te.makeRandomOutline();
					te.makeRandomOutline();
				}
			} else {

				// new plan, take a snapshot of the world when we are first activated, and then rapidly revert changes
				if (te.blockData == null && level.isAreaLoaded(pos, AntibuilderBlockEntity.RADIUS)) {
					te.captureBlockData();
					te.slowScan = true;
				}

				if (te.blockData != null && (!te.slowScan || te.tickCount % 20 == 0)) {
					if (te.scanAndRevertChanges()) {
						te.slowScan = false;
						te.ticksSinceChange = 0;
					} else {
						te.ticksSinceChange++;

						if (te.ticksSinceChange > 20) {
							te.slowScan = true;
						}
					}
				}
			}
		} else {
			// remove data
			te.blockData = null;
			te.tickCount = 0;
		}
	}


	/**
	 * Display a random one of the 12 possible outlines
	 */
	private void makeRandomOutline() {
		makeOutline(this.rand.nextInt(12));
	}

	/**
	 * Display a specific outline
	 */
	private void makeOutline(int outline) {
		// src
		double sx = this.getBlockPos().getX();
		double sy = this.getBlockPos().getY();
		double sz = this.getBlockPos().getZ();
		// dest
		double dx = this.getBlockPos().getX();
		double dy = this.getBlockPos().getY();
		double dz = this.getBlockPos().getZ();

		switch (outline) {
			case 0, 8 -> {
				sx -= RADIUS;
				dx += RADIUS + 1;
				sz -= RADIUS;
				dz -= RADIUS;
			}
			case 1, 9 -> {
				sx -= RADIUS;
				dx -= RADIUS;
				sz -= RADIUS;
				dz += RADIUS + 1;
			}
			case 2, 10 -> {
				sx -= RADIUS;
				dx += RADIUS + 1;
				sz += RADIUS + 1;
				dz += RADIUS + 1;
			}
			case 3, 11 -> {
				sx += RADIUS + 1;
				dx += RADIUS + 1;
				sz -= RADIUS;
				dz += RADIUS + 1;
			}
			case 4 -> {
				sx -= RADIUS;
				dx -= RADIUS;
				sz -= RADIUS;
				dz -= RADIUS;
			}
			case 5 -> {
				sx += RADIUS + 1;
				dx += RADIUS + 1;
				sz -= RADIUS;
				dz -= RADIUS;
			}
			case 6 -> {
				sx += RADIUS + 1;
				dx += RADIUS + 1;
				sz += RADIUS + 1;
				dz += RADIUS + 1;
			}
			case 7 -> {
				sx -= RADIUS;
				dx -= RADIUS;
				sz += RADIUS + 1;
				dz += RADIUS + 1;
			}
		}

		switch (outline) {
			case 0, 1, 2, 3 -> {
				sy += RADIUS + 1;
				dy += RADIUS + 1;
			}
			case 4, 5, 6, 7 -> {
				sy -= RADIUS;
				dy += RADIUS + 1;
			}
			case 8, 9, 10, 11 -> {
				sy -= RADIUS;
				dy -= RADIUS;
			}
		}

		if (this.rand.nextBoolean()) {
			this.drawParticleLine(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, dx, dy, dz);
		} else {
			this.drawParticleLine(sx, sy, sz, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5);
		}
		this.drawParticleLine(sx, sy, sz, dx, dy, dz);
	}

	private void drawParticleLine(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
		// make particle trail
		int particles = 16;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);

			double tx = srcX + (destX - srcX) * trailFactor + this.rand.nextFloat() * 0.005;
			double ty = srcY + (destY - srcY) * trailFactor + this.rand.nextFloat() * 0.005;
			double tz = srcZ + (destZ - srcZ) * trailFactor + this.rand.nextFloat() * 0.005;
			this.getLevel().addParticle(DustParticleOptions.REDSTONE, tx, ty, tz, 0, 0, 0);
		}
	}

	private boolean scanAndRevertChanges() {
		int index = 0;
		boolean reverted = false;

		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int y = -RADIUS; y <= RADIUS; y++) {
				for (int z = -RADIUS; z <= RADIUS; z++) {
					BlockState stateThere = this.getLevel().getBlockState(this.getBlockPos().offset(x, y, z));

					if (this.blockData[index].getBlock() != stateThere.getBlock()) {
						if (revertBlock(this.getBlockPos().offset(x, y, z), stateThere, this.blockData[index])) {
							reverted = true;
						} else {
							this.blockData[index] = stateThere;
						}
					}

					index++;
				}
			}
		}

		return reverted;
	}

	private boolean revertBlock(BlockPos pos, BlockState stateThere, BlockState replaceWith) {
		if (stateThere.isAir() && !replaceWith.blocksMotion()) {
			return false;
		}
		if (stateThere.getDestroySpeed(this.getLevel(), pos) < 0 || this.isUnrevertable(stateThere, replaceWith)) {
			return false;
		} else if (this.rand.nextInt(REVERT_CHANCE) == 0) {
			// don't revert everything instantly
			if (!replaceWith.isAir()) {
				replaceWith = TFBlocks.ANTIBUILT_BLOCK.get().defaultBlockState();
			}

			if (stateThere.isAir()) {
				this.getLevel().levelEvent(2001, pos, Block.getId(replaceWith));
			}
			Block.updateOrDestroy(stateThere, replaceWith, this.getLevel(), pos, 2);
		}

		return true;
	}

	private boolean isUnrevertable(BlockState stateThere, BlockState replaceWith) {
		return stateThere.is(BlockTagGenerator.ANTIBUILDER_IGNORES) || replaceWith.is(BlockTagGenerator.ANTIBUILDER_IGNORES);
	}

	private void captureBlockData() {
		this.blockData = new BlockState[DIAMETER * DIAMETER * DIAMETER];

		int index = 0;

		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int y = -RADIUS; y <= RADIUS; y++) {
				for (int z = -RADIUS; z <= RADIUS; z++) {
					this.blockData[index] = this.getLevel().getBlockState(this.getBlockPos().offset(x, y, z));
					index++;
				}
			}
		}
	}

	private boolean anyPlayerInRange() {
		return this.getLevel().hasNearbyAlivePlayer(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, AntibuilderBlockEntity.PLAYER_RANGE);
	}
}
