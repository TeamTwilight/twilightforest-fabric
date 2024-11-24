package twilightforest.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.TrophyPedestalBlock;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin<O, S> extends StateHolder<O, S> {
	protected BlockStateBaseMixin(O owner, ImmutableMap<Property<?>, Comparable<?>> values, MapCodec<S> propertiesCodec) {
		super(owner, values, propertiesCodec);
	}

	@Shadow
	public abstract Block getBlock();

	@Inject(method = "getPistonPushReaction", at = @At("HEAD"), cancellable = true)
	private void changePistonPushReactions(CallbackInfoReturnable<PushReaction> cir) {
		if (this.getBlock() instanceof TrophyPedestalBlock && this.getValue(TrophyPedestalBlock.ACTIVE))
			cir.setReturnValue(PushReaction.NORMAL);
	}
}
