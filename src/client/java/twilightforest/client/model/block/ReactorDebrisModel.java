package twilightforest.client.model.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.ReactorDebrisBlockEntity;
import twilightforest.client.renderer.block.ReactorDebrisRenderer;

import java.util.List;

public class ReactorDebrisModel implements BakedModel {
	private final BakedModel defaultModel;

	public ReactorDebrisModel(BakedModel defaultModel) {
		this.defaultModel = defaultModel;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
		return this.defaultModel.getQuads(state, side, random);
	}

	public TextureAtlasSprite getParticleIcon(BlockAndTintGetter level, BlockPos pos) {
		ResourceLocation texturePath = ReactorDebrisBlockEntity.DEFAULT_TEXTURE;
		if (level.getBlockEntity(pos) instanceof ReactorDebrisBlockEntity reactorDebrisBlockEntity && level instanceof ClientLevel clientLevel) {
			texturePath = reactorDebrisBlockEntity.textures[clientLevel.random.nextInt(reactorDebrisBlockEntity.textures.length)];
		}
		return ReactorDebrisRenderer.getSprite(texturePath);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.defaultModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.defaultModel.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return this.defaultModel.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return this.defaultModel.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return ReactorDebrisRenderer.getSprite(ReactorDebrisBlockEntity.DEFAULT_TEXTURE);
	}

	@Override
	public ItemTransforms getTransforms() {
		return this.defaultModel.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.defaultModel.getOverrides();
	}
}
