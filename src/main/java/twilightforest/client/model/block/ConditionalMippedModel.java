package twilightforest.client.model.block;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConditionalMippedModel extends ForwardingBakedModel {

	public ConditionalMippedModel(BakedModel originalModel) {
		this.wrapped = originalModel;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
		return this.wrapped.getQuads(state, side, rand);
	}
}