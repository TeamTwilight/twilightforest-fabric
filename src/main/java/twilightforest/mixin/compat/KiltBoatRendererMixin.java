package twilightforest.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.TwilightForestMod;
import twilightforest.asmhooks.BoatHooks;
import xyz.bluspring.kilt.injections.client.renderer.entity.BoatRendererInjection;

@Mixin(BoatRenderer.class)
public abstract class KiltBoatRendererMixin implements BoatRendererInjection {

	@ModifyReturnValue(
		method = "getTextureLocation(Lnet/minecraft/world/entity/vehicle/Boat$Type;Z)Lnet/minecraft/resources/ResourceLocation;",
		at = @At("RETURN")
	)
	private static ResourceLocation twilightforest$getTextureLocation(
		ResourceLocation original,
		Boat.Type type,
		boolean chest
	) {
		String name = BoatHooks.TWILIGHTFOREST_BOAT_TEXTURES.get(type);
		if (name == null) {
			return original;
		}
		return TwilightForestMod.prefix("textures/entity/" + (chest ? "chest_boat" : "boat") + "/" + name + ".png");
	}
}