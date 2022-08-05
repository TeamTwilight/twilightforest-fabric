package twilightforest.compat.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("rawtypes")
public class EntityHelper implements IIngredientHelper<EntityType> {

	@Override
	public IIngredientType<EntityType> getIngredientType() {
		return JEICompat.ENTITY_TYPE;
	}

	@Override
	public String getDisplayName(EntityType type) {
		return type.getDescription().getString();
	}

	@Override
	public String getUniqueId(EntityType type, UidContext context) {
		return Objects.requireNonNull(Registry.ENTITY_TYPE.getKey(type)).toString();
	}

	@Override
	public ResourceLocation getResourceLocation(EntityType type) {
		return Objects.requireNonNull(Registry.ENTITY_TYPE.getKey(type));
	}

	@Override
	public EntityType copyIngredient(EntityType type) {
		return type;
	}

	@Override
	public String getErrorInfo(@Nullable EntityType type) {
		if (type == null) {
			return "null";
		}
		ResourceLocation name = Registry.ENTITY_TYPE.getKey(type);
		if (name == null) {
			return "unnamed sadface :(";
		}
		return name.toString();
	}
}