package twilightforest.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.fabricators_of_create.porting_lib.client.entity.CustomBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import twilightforest.TwilightForestMod;
import twilightforest.util.TFBoatTypes;

import java.util.Map;

@Mixin(BoatRenderer.class)
public class BoatRendererMixin implements CustomBoatModel {

	@Shadow
	@Final
	private Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

	/**
	 * PortingLib's BoatRendererMixin (priority 951) calls this for ALL boats via instanceof check.
	 * For TF boats we return TF texture + OAK model.
	 * For vanilla boats we fall through to boatResources.get(type).
	 */
	@Override
	public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
		Boat.Type type = boat.getVariant();
		String name = getTFTextureName(type);
		if (name == null) {
			return boatResources.get(type);
		}

		Pair<ResourceLocation, ListModel<Boat>> oakPair = boatResources.get(Boat.Type.OAK);
		if (oakPair == null) return boatResources.get(type);

		boolean chest = boat.getType() == EntityType.CHEST_BOAT;
		return Pair.of(
			TwilightForestMod.prefix("textures/entity/" + (chest ? "chest_boat" : "boat") + "/" + name + ".png"),
			oakPair.getSecond()
		);
	}

	private static String getTFTextureName(Boat.Type type) {
		if (type == TFBoatTypes.TWILIGHT_OAK) return "twilight_oak";
		if (type == TFBoatTypes.CANOPY) return "canopy";
		if (type == TFBoatTypes.MANGROVE_TYPE) return "mangrove";
		if (type == TFBoatTypes.DARK) return "dark";
		if (type == TFBoatTypes.TIME) return "time";
		if (type == TFBoatTypes.TRANSFORMATION) return "transformation";
		if (type == TFBoatTypes.MINING) return "mining";
		if (type == TFBoatTypes.SORTING) return "sorting";
		return null;
	}
}