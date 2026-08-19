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
import org.spongepowered.asm.mixin.Unique;
import twilightforest.TwilightForestMod;
import twilightforest.util.TFBoatTypes;

import java.util.Map;

@Mixin(BoatRenderer.class)
public class BoatRendererMixin implements CustomBoatModel {

	@Unique
	private static final Map<Boat.Type, String> TWILIGHTFOREST_BOAT_TEXTURES = Map.of(
		TFBoatTypes.TWILIGHT_OAK, "twilight_oak",
		TFBoatTypes.CANOPY, "canopy",
		TFBoatTypes.MANGROVE_TYPE, "mangrove",
		TFBoatTypes.DARK, "dark",
		TFBoatTypes.TIME, "time",
		TFBoatTypes.TRANSFORMATION, "transformation",
		TFBoatTypes.MINING, "mining",
		TFBoatTypes.SORTING, "sorting"
	);

	@Shadow
	@Final
	private Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

	@Override
	public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
		Boat.Type type = boat.getVariant();
		String name = TWILIGHTFOREST_BOAT_TEXTURES.get(type);
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
}