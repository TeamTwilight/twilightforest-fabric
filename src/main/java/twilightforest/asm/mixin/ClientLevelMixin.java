package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.AddBreakingBlockEffectDuck;
import twilightforest.asm.hooks.ClientBlockExtensionHooks;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level implements AddBreakingBlockEffectDuck {

	@Shadow
	@Final
	private Minecraft minecraft;

	protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
		super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
	}

	@ModifyExpressionValue(
		method = "addDestroyBlockEffect(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;shouldSpawnTerrainParticles()Z"
		)
	)
	private boolean twilightforest$addDestroyBlockEffect(
		boolean original,
		@Local(argsOnly = true, name = "pos") BlockPos pos,
		@Local(argsOnly = true, name = "blockState") BlockState blockState
	) {
		return !ClientBlockExtensionHooks.shouldApplyDestroyEffects(blockState, this, pos, this.minecraft.particleEngine);
	}

	@WrapMethod(method = "addBreakingBlockEffect(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)V")
	private void twilightforest$wrapAddBreakingBlockEffect(
		BlockPos pos,
		Direction direction,
		Operation<Void> original
	) {
		twilightforest$addBreakingBlockEffect(pos, direction, null);
	}

	@Override
	public void twilightforest$addBreakingBlockEffect(final BlockPos pos, final Direction direction, @Nullable HitResult hitResult) {
		BlockState blockState = this.getBlockState(pos);
		if (blockState.getRenderShape() != RenderShape.INVISIBLE && !ClientBlockExtensionHooks.shouldApplyHitEffects(blockState, this, hitResult, this.minecraft.particleEngine)) {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			float r = 0.1F;
			AABB shape = blockState.getShape(this, pos).bounds();
			double xp = (double)x + this.random.nextDouble() * (shape.maxX - shape.minX - (double)0.2F) + (double)0.1F + shape.minX;
			double yp = (double)y + this.random.nextDouble() * (shape.maxY - shape.minY - (double)0.2F) + (double)0.1F + shape.minY;
			double zp = (double)z + this.random.nextDouble() * (shape.maxZ - shape.minZ - (double)0.2F) + (double)0.1F + shape.minZ;
			if (direction == Direction.DOWN) {
				yp = (double)y + shape.minY - (double)0.1F;
			}

			if (direction == Direction.UP) {
				yp = (double)y + shape.maxY + (double)0.1F;
			}

			if (direction == Direction.NORTH) {
				zp = (double)z + shape.minZ - (double)0.1F;
			}

			if (direction == Direction.SOUTH) {
				zp = (double)z + shape.maxZ + (double)0.1F;
			}

			if (direction == Direction.WEST) {
				xp = (double)x + shape.minX - (double)0.1F;
			}

			if (direction == Direction.EAST) {
				xp = (double)x + shape.maxX + (double)0.1F;
			}

			this.minecraft.particleEngine.add((new TerrainParticle(((ClientLevel) (Object) this), xp, yp, zp, (double)0.0F, (double)0.0F, (double)0.0F, blockState, pos)).setPower(0.2F).scale(0.6F));
		}
	}
}