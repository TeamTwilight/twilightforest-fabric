package twilightforest.client.model.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class FullbrightBakedModel implements BakedModel {

	private final BakedModel delegate;
	private Map<Direction, List<BakedQuad>> cachedQuads = Maps.newHashMap();

	public FullbrightBakedModel(BakedModel delegate) {
		this.delegate = delegate;
	}

	public static void setLightData(BakedQuad q, int light)
	{
		int[] data = q.getVertices();
		for (int i = 0; i < 4; i++)
		{
			data[(i * 8) + 6] = light;
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
		return cachedQuads.computeIfAbsent(side, (face) -> {
			List<BakedQuad> quads = delegate.getQuads(state, side, rand);
			for (BakedQuad quad : quads)
				setLightData(quad, 0xF000F0);
			return quads;
		});
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

//	@Override
//	public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack mat) {
//		return net.minecraftforge.client.ForgeHooksClient.handlePerspective(this, cameraTransformType, mat);
//	}
}
