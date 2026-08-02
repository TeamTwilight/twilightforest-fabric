package twilightforest.client.model.block;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ConditionalMippedModel extends ForwardingBakedModel {

	public ConditionalMippedModel(BakedModel originalModel) {
		this.wrapped = originalModel;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		this.wrapped.emitBlockQuads(level, state, pos, randomSupplier, context);
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		this.wrapped.emitItemQuads(stack, randomSupplier, context);
	}

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
		return this.wrapped.getQuads(state, side, rand);
	}
}