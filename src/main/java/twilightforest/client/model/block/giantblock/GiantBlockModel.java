package twilightforest.client.model.block.giantblock;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import twilightforest.block.GiantBlock;

import java.util.List;
import java.util.function.Predicate;

public class GiantBlockModel implements BlockStateModel {
	private final BlockStateModel[] voxels;

	public GiantBlockModel(BlockStateModel[] voxels) {
		this.voxels = voxels;
	}

	@Override
	public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
		// This method will not do anything as this is a Fabric model
	}

	@Override
	public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
		this.voxels[GiantBlock.packCoords(pos)].emitQuads(emitter, level, pos, state, random, cullTest);
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.voxels[GiantBlock.packCoords(pos)].particleMaterial(level, pos, state);
	}

	@Override
	public Material.Baked particleMaterial() {
		return this.voxels[0].particleMaterial();
	}

	@Override
	public @MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
		return this.voxels[GiantBlock.packCoords(pos)].materialFlags(level, pos, state, random);
	}

	@Override
	public @MaterialFlags int materialFlags() {
		return this.voxels[0].materialFlags();
	}
}