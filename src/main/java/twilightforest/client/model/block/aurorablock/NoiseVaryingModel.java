package twilightforest.client.model.block.aurorablock;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import twilightforest.util.SimplexNoiseHelper;

import java.util.function.Predicate;

public class NoiseVaryingModel extends WrapperBlockStateModel implements BlockStateModel {
	private final BlockStateModel[] variants;

	public NoiseVaryingModel(BlockStateModel[] variants) {
		// First variation will propagate properties among other variants
		super(variants[0]);
		this.variants = variants;
	}

	@Override
	public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
		this.chooseVariant(pos).emitQuads(emitter, level, pos, state, random, cullTest);
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.chooseVariant(pos).particleMaterial(level, pos, state);
	}

	@Override
	public @MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
		return this.chooseVariant(pos).materialFlags(level, pos, state, random);
	}

	private BlockStateModel chooseVariant(BlockPos pos) {
		return this.variants[SimplexNoiseHelper.calcVariant(pos, this.variants.length)];
	}

}