package twilightforest.mixin;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.ModelBaker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockModel.class)
public interface BlockModelAccessor {
	@Invoker("getItemOverrides")
	ItemOverrides tf$callGetItemOverrides(ModelBaker baker, BlockModel model);
}
