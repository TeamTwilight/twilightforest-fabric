package twilightforest.mixin;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.TwilightForestMod;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelLoaderMixin {
	@Shadow
	protected abstract void loadTopLevel(ModelResourceLocation location);

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadTopLevel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V", ordinal = 3, shift = At.Shift.AFTER))
	public void addStuff(BlockColors blockColors, ProfilerFiller profilerFiller, Map modelResources, Map blockStateResources, CallbackInfo ci) {
		loadTopLevel(new ModelResourceLocation(TwilightForestMod.ID, "giant_sword_gui", "inventory"));
		loadTopLevel(new ModelResourceLocation(TwilightForestMod.ID, "giant_pickaxe_gui", "inventory"));
	}
}
