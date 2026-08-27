package twilightforest.client.model.block;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.RandomSource;
import twilightforest.block.entity.ReactorDebrisBlockEntity;
import twilightforest.client.renderer.block.ReactorDebrisRenderer;

import java.util.List;

public class ReactorDebrisModel implements BlockStateModel {

	private final BlockStateModel defaultModel;

	public ReactorDebrisModel(BlockStateModel defaultModel) {
		this.defaultModel = defaultModel;
	}

	@Override
	public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
		this.defaultModel.collectParts(random, parts);
	}

	@Override
	public Material.Baked particleMaterial() {
		// 26.1's particleMaterial() has no level/pos context, so the per-block
		// random texture pick is dropped and the default texture is used
		return ReactorDebrisRenderer.getSprite(ReactorDebrisBlockEntity.DEFAULT_TEXTURE);
	}
	@Override
	public int materialFlags() {
		return this.defaultModel.materialFlags();
	}
}
