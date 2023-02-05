package twilightforest.client.model.block.doors;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.CustomLoaderBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelBuilder;
import twilightforest.TwilightForestMod;

public class CastleDoorBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	protected CastleDoorBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("castle_door"), parent, existingFileHelper);
	}

	public static <T extends ModelBuilder<T>> CastleDoorBuilder<T> begin(T parent, ExistingFileHelper helper) {
		return new CastleDoorBuilder<>(parent, helper);
	}
}