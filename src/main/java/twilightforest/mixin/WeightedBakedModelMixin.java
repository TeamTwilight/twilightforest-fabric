package twilightforest.mixin;

import java.util.List;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WeightedBakedModel.class)
public class WeightedBakedModelMixin implements FabricBakedModel {

	@Shadow
	@Final
	private List<WeightedEntry.Wrapper<BakedModel>> list;

	@Shadow
	@Final
	private int totalWeight;

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		BakedModel selected = getRandomModel(randomSupplier.get());
		selected.emitBlockQuads(blockView, state, pos, randomSupplier, context);
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		BakedModel selected = getRandomModel(randomSupplier.get());
		selected.emitItemQuads(stack, randomSupplier, context);
	}

	private BakedModel getRandomModel(RandomSource random) {
		int i = random.nextInt(this.totalWeight);
		for (WeightedEntry.Wrapper<BakedModel> wrapper : this.list) {
			i -= wrapper.getWeight().asInt();
			if (i < 0) {
				return wrapper.data();
			}
		}
		return this.list.get(0).data();
	}
}