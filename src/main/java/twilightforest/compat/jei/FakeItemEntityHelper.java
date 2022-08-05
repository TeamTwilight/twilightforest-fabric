package twilightforest.compat.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FakeItemEntityHelper implements IIngredientHelper<FakeItemEntity> {

	@Override
	public IIngredientType<FakeItemEntity> getIngredientType() {
		return JEICompat.FAKE_ITEM_ENTITY;
	}

	@Override
	public String getDisplayName(FakeItemEntity ingredient) {
		return ingredient.stack().getItem().getDescription().toString();
	}

	@Override
	public String getUniqueId(FakeItemEntity ingredient, UidContext context) {
		return Objects.requireNonNull(Registry.ITEM.getKey(ingredient.stack().getItem())).toString();
	}

	@Override
	public ResourceLocation getResourceLocation(FakeItemEntity ingredient) {
		return Objects.requireNonNull(Registry.ITEM.getKey(ingredient.stack().getItem()));
	}

	@Override
	public FakeItemEntity copyIngredient(FakeItemEntity ingredient) {
		return ingredient;
	}

	@Override
	public String getErrorInfo(@Nullable FakeItemEntity ingredient) {
		if (ingredient == null) {
			return "null";
		};
		return ingredient.stack().toString();
	}
}
