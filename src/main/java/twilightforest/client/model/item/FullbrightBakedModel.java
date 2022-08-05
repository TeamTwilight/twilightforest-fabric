package twilightforest.client.model.item;

import com.google.common.collect.Maps;
import io.github.fabricators_of_create.porting_lib.util.LightUtil;
import net.minecraft.client.renderer.RenderType;
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
import java.util.Map;

public class FullbrightBakedModel implements BakedModel {

	protected final BakedModel delegate;
	protected final Map<Direction, List<BakedQuad>> cachedQuads = Maps.newHashMap();
	protected boolean cache = true;

	public FullbrightBakedModel(BakedModel delegate) {
		this.delegate = delegate;
	}

	public final FullbrightBakedModel disableCache() {
		cache = false;
		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
		if (cache) {
			List<BakedQuad> quads = cachedQuads.get(side);
			if (quads == null) {
				quads = getQuads(side, delegate.getQuads(state, side, rand));
				cachedQuads.put(side, quads);
			}
			return quads; // computeIfAbsent has issues, don't use it
		}
		return getQuads(side, delegate.getQuads(state, side, rand));
	}

	protected List<BakedQuad> getQuads(@Nullable Direction face, List<BakedQuad> quads) {
		return QuadTransformers.applyingLightmap(0xF000F0).process(quads);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return delegate.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return delegate.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return delegate.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return delegate.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return delegate.getParticleIcon();
	}

	@Override
	public ItemTransforms getTransforms() {
		return delegate.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return delegate.getOverrides();
	}
}