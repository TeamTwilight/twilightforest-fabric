package twilightforest.client.model.block.doors;

import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

public class CastleDoorBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	protected CastleDoorBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("castle_door"), parent, existingFileHelper);
	}

	public static <T extends ModelBuilder<T>> CastleDoorBuilder<T> begin(T parent, ExistingFileHelper helper) {
		return new CastleDoorBuilder<>(parent, helper);
	}
}