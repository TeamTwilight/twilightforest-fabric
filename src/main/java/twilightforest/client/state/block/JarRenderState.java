package twilightforest.client.state.block;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import org.jspecify.annotations.Nullable;

public class JarRenderState extends BlockEntityRenderState {

	public final ItemStackRenderState item = new ItemStackRenderState();
	@Nullable
	public WobbleStyle lastWobbleStyle;
	public float wobbleTicks;
	@Nullable
	public ExtraModelKey<BlockStateModelPart> lid;
	public float itemRotation;
}