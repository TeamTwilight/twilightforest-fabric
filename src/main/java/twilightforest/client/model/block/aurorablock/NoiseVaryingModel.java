package twilightforest.client.model.block.aurorablock;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.util.SimplexNoiseHelper;

import java.util.function.Supplier;

public class NoiseVaryingModel extends ForwardingBakedModel {
	private final BakedModel[] variants;

	public NoiseVaryingModel(BakedModel[] variants) {
		this.wrapped = variants[0];
		this.variants = variants;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		int variant = SimplexNoiseHelper.calcVariant(pos, this.variants.length);
		this.variants[variant].emitBlockQuads(level, state, pos, randomSupplier, context);
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		this.wrapped.emitItemQuads(stack, randomSupplier, context);
	}
}