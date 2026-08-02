package twilightforest.client.model.block.carpet;

import io.github.fabricators_of_create.porting_lib.models.generators.CustomLoaderBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelBuilder;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

public class RoyalRagsBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	protected RoyalRagsBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(TwilightForestMod.prefix("royal_rags"), parent, existingFileHelper, false);
	}

	public static <T extends ModelBuilder<T>> RoyalRagsBuilder<T> begin(T parent, ExistingFileHelper helper) {
		return new RoyalRagsBuilder<>(parent, helper);
	}
}