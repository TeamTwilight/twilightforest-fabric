package twilightforest.data.custom;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.tuple.Triple;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRecipes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class TransformationPowderProvider implements DataProvider {
	private final DataGenerator generator;
	private final String modId;
	private final ExistingFileHelper helper;
	protected final Map<String, Triple<EntityType<?>, EntityType<?>, Boolean>> builders = Maps.newLinkedHashMap();

	public TransformationPowderProvider(DataGenerator generator, final String modId, final ExistingFileHelper helper) {
		this.generator = generator;
		this.modId = modId;
		this.helper = helper;
	}

	public abstract void registerTransforms();

	@Override
	public void run(CachedOutput cache) {
		this.builders.clear();
		this.registerTransforms();
		this.builders.forEach((name, transform) -> {
			List<String> list = builders.keySet().stream()
					.filter(s -> Registry.ENTITY_TYPE.containsKey(Registry.ENTITY_TYPE.getKey(transform.getLeft())))
					.filter(s -> Registry.ENTITY_TYPE.containsKey(Registry.ENTITY_TYPE.getKey(transform.getMiddle())))
					.filter(s -> !this.builders.containsKey(s))
					.filter(this::missing)
					.collect(Collectors.toList());

			if (!list.isEmpty()) {
				throw new IllegalArgumentException(String.format("Duplicate Transformation Powder Transformations: %s", list.stream().map(Objects::toString).collect(Collectors.joining(", "))));
			} else {
				JsonObject obj = serializeToJson(transform.getLeft(), transform.getMiddle(), transform.getRight());
				Path path = createPath(new ResourceLocation(modId, name));
				try {
					DataProvider.saveStable(cache, obj, path);
				} catch (IOException e) {
					TwilightForestMod.LOGGER.error("Couldn't save Transformation Powder recipe to {}", path, e);
				}
			}
		});
	}

	private boolean missing(String name) {
		return helper == null || !helper.exists(new ResourceLocation(modId, name), new ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", "crumble_horn/"));
	}

	private Path createPath(ResourceLocation name) {
		return generator.getOutputFolder().resolve("data/" + name.getNamespace() + "/recipes/transformation_powder/" + name.getPath() + ".json");
	}

	private JsonObject serializeToJson(EntityType<?> transformFrom, EntityType<?> transformTo, boolean reversible) {
		JsonObject jsonobject = new JsonObject();

		jsonobject.addProperty("type", Registry.RECIPE_SERIALIZER.getKey(TFRecipes.TRANSFORMATION_SERIALIZER.get()).toString());
		jsonobject.addProperty("from", Registry.ENTITY_TYPE.getKey(transformFrom).toString());
		jsonobject.addProperty("to", Registry.ENTITY_TYPE.getKey(transformTo).toString());
		jsonobject.addProperty("reversible", reversible);
		return jsonobject;
	}

	@Override
	public String getName() {
		return modId + " Transformation Powder Transformations";
	}

	//helper methods
	public void addOneWayTransform(EntityType<?> from, EntityType<?> to) {
		builders.put(Registry.ENTITY_TYPE.getKey(from).getPath() + "_to_" + Registry.ENTITY_TYPE.getKey(to).getPath(), Triple.of(from, to, false));
	}

	public void addTwoWayTransform(EntityType<?> from, EntityType<?> to) {
		builders.put(Registry.ENTITY_TYPE.getKey(from).getPath() + "_to_" + Registry.ENTITY_TYPE.getKey(to).getPath(), Triple.of(from, to, true));
	}
}
