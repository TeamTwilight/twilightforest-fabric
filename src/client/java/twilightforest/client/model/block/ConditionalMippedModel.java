package twilightforest.client.model.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConditionalMippedModel implements BakedModel {
	private final BakedModel originalModel;

	public ConditionalMippedModel(BakedModel originalModel) {
		this.originalModel = originalModel;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
		return this.originalModel.getQuads(state, side, rand);
	}

	public boolean useMippedLayer() {
		return Minecraft.useFancyGraphics();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.originalModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.originalModel.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return this.originalModel.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return this.originalModel.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.originalModel.getParticleIcon();
	}

	@Override
	public ItemTransforms getTransforms() {
		return this.originalModel.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.originalModel.getOverrides();
	}
}
