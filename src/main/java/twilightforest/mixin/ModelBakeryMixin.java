package twilightforest.mixin;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.TwilightForestMod;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

	@Shadow
	protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation modelLocation);

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V",
			ordinal = 1,
			shift = At.Shift.AFTER
		)
	)
	public void addStuff(CallbackInfo ci) {
		loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, "giant_sword_gui")));
		loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, "giant_pickaxe_gui")));
	}
}