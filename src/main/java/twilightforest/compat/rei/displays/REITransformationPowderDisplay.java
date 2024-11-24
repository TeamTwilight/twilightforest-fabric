package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import twilightforest.compat.rei.TwilightForestREIClientPlugin;
import twilightforest.compat.rei.categories.REITransformationPowderCategory;
import twilightforest.item.recipe.TransformPowderRecipe;

import javax.annotation.Nullable;
import java.util.*;

public class REITransformationPowderDisplay extends BasicDisplay {

	private static final Set<EntityType<?>> IGNORED_ENTITIES = new HashSet<>();
	private static final Map<EntityType<?>, Entity> ENTITY_MAP = new HashMap<>();

	public final boolean isReversible;

	public final EntityType<?> inputType;
	public final EntityType<?> resultType;

	public REITransformationPowderDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, TransformPowderRecipe recipe) {
		super(inputs,
				outputs,
				Optional.of(recipe.recipeID()));

		this.isReversible = recipe.isReversible();

		this.inputType = recipe.input();
		this.resultType = recipe.result();
	}

	@Nullable
	public static REITransformationPowderDisplay of(TransformPowderRecipe recipe) {
		List<EntryIngredient> inputs = new ArrayList<>();
		List<EntryIngredient> outputs = new ArrayList<>();

		getEntity(Minecraft.getInstance().level, recipe.input())
				.ifPresent(entity -> inputs.add(EntryIngredients.of(TwilightForestREIClientPlugin.ENTITY_DEFINITION, List.of(entity))));

		getEntity(Minecraft.getInstance().level, recipe.result())
				.ifPresent(entity -> outputs.add(EntryIngredients.of(TwilightForestREIClientPlugin.ENTITY_DEFINITION, List.of(entity))));

		if (!inputs.isEmpty() && !outputs.isEmpty()) {

			if (recipe.isReversible()) {
				inputs.addAll(outputs);

				outputs.add(inputs.get(0));
				outputs.add(inputs.get(1));
			}

			return new REITransformationPowderDisplay(inputs, outputs, recipe);
		}

		return null;
	}

	@Nullable
	public static Optional<Entity> getEntity(Level level, EntityType<?> type) {
		if (level != null && !IGNORED_ENTITIES.contains(type)) {
			Entity entity;

			// players cannot be created using the type, but we can use the client player
			// side effect is it renders armor/items
			if (type == EntityType.PLAYER) {
				entity = Minecraft.getInstance().player;
			} else {
				// entity is created with the client world, but the entity map is thrown away when JEI restarts so they should be okay I think
				entity = ENTITY_MAP.computeIfAbsent(type, t -> t.create(level));
			}

			// only can draw living entities, plus non-living ones don't get recipes anyways
			if (entity instanceof LivingEntity livingEntity) {
				return Optional.of(livingEntity);
			} else {
				// not living, so might as well skip next time
				IGNORED_ENTITIES.add(type);
				ENTITY_MAP.remove(type);
			}
		}

		return Optional.empty();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REITransformationPowderCategory.TRANSFORMATION;
	}
}
