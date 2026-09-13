package twilightforest.mixin.compat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.*;
import twilightforest.TwilightForestMod;
import twilightforest.asmhooks.BoatHooks;
import xyz.bluspring.kilt.injections.client.renderer.entity.BoatRendererInjection;

import java.util.Map;

@Implements(value = @Interface(iface = BoatRendererInjection.class, prefix = "tf$"))
@Mixin(BoatRenderer.class)
public class KiltBoatRendererCompatibilityMixin {

	@Shadow
	@Final
	private Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

	@Intrinsic
	public Pair<ResourceLocation, ListModel<Boat>> tf$getModelWithLocation(Boat boat) {
		Boat.Type type = boat.getVariant();
		String name = BoatHooks.TWILIGHTFOREST_BOAT_TEXTURES.get(type);
		if (name == null) {
			return boatResources.get(type);
		}

		Pair<ResourceLocation, ListModel<Boat>> oakPair = boatResources.get(Boat.Type.OAK);
		if (oakPair == null) {
			return boatResources.get(type);
		}

		boolean chest = boat.getType() == EntityType.CHEST_BOAT;
		return Pair.of(
			TwilightForestMod.prefix("textures/entity/" + (chest ? "chest_boat" : "boat") + "/" + name + ".png"),
			oakPair.getSecond()
		);
	}
}