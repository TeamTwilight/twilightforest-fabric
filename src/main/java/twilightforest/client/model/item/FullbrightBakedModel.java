package twilightforest.client.model.item;

import com.google.common.collect.Maps;
import io.github.fabricators_of_create.porting_lib.util.LightUtil;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FullbrightBakedModel extends ForwardingBakedModel {

	protected final Map<Direction, List<BakedQuad>> cachedQuads = Maps.newHashMap();
	protected boolean cache = true;

	public FullbrightBakedModel(BakedModel delegate) {
		this.wrapped = delegate;
	}

	public final FullbrightBakedModel disableCache() {
		cache = false;
		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
		return cache ? cachedQuads.computeIfAbsent(side, (face) -> {
			List<BakedQuad> quads = wrapped.getQuads(state, side, rand);
			return getQuads(face, quads);
		}) : getQuads(side, wrapped.getQuads(state, side, rand));
	}

	protected List<BakedQuad> getQuads(@Nullable Direction face, List<BakedQuad> quads) {
		for (BakedQuad quad : quads)
			LightUtil.setLightData(quad, 0xF000F0);
		return quads;
	}
}
