package twilightforest.client.model.item;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class TintIndexAwareFullbrightBakedModel extends FullbrightBakedModel {

	public TintIndexAwareFullbrightBakedModel(BakedModel delegate) {
		super(delegate);
	}

	@Override
	protected List<BakedQuad> getQuads(@Nullable Direction face, List<BakedQuad> quads) {
		for (BakedQuad quad : quads)
			if (quad.isTinted())
				IQuadTransformer.applyingLightmap(0xF000F0).process(quads);
		return quads;
	}
}
