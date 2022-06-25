package twilightforest.data.custom;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.data.ExistingFileHelper;
import oshi.util.tuples.Pair;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRecipes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class CrumbleHornProvider implements DataProvider {

	private final DataGenerator generator;
	private final String modId;
	private final ExistingFileHelper helper;
	protected final Map<String, Pair<BlockState, BlockState>> builders = Maps.newLinkedHashMap();

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	public CrumbleHornProvider(DataGenerator generator, final String modId, final ExistingFileHelper helper) {
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
					.filter(s -> Registry.BLOCK.containsKey(Registry.BLOCK.getKey(transform.getA().getBlock())))
					.filter(s -> Registry.BLOCK.containsKey(Registry.BLOCK.getKey(transform.getB().getBlock())))
					.filter(s -> !this.builders.containsKey(s))
					.filter(this::missing)
					.collect(Collectors.toList());

			if (!list.isEmpty()) {
				throw new IllegalArgumentException(String.format("Duplicate Crumble Horn Transformations: %s", list.stream().map(Objects::toString).collect(Collectors.joining(", "))));
			} else {
				JsonObject obj = serializeToJson(transform.getA(), transform.getB());
				Path path = createPath(new ResourceLocation(modId, name));
				try {
					DataProvider.saveStable(cache, obj, path);
				} catch (IOException e) {
					TwilightForestMod.LOGGER.error("Couldn't save Crumble Transformation to {}", path, e);
				}
			}
		});
	}

	private boolean missing(String name) {
		return helper == null || !helper.exists(new ResourceLocation(modId, name), new ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", "crumble_horn/"));
	}

	private Path createPath(ResourceLocation name) {
		return generator.getOutputFolder().resolve("data/" + name.getNamespace() + "/recipes/crumble_horn/" + name.getPath() + ".json");
	}

	private JsonObject serializeToJson(BlockState transformFrom, BlockState transformTo) {
		JsonObject jsonobject = new JsonObject();

		jsonobject.addProperty("type", Registry.RECIPE_SERIALIZER.getKey(TFRecipes.CRUMBLE_SERIALIZER.get()).toString());
		jsonobject.addProperty("from", Registry.BLOCK.getKey(transformFrom.getBlock()).toString());
		jsonobject.addProperty("to", Registry.BLOCK.getKey(transformTo.getBlock()).toString());
		return jsonobject;
	}

	@Override
	public String getName() {
		return modId + " Crumble Horn Transformations";
	}

	//helper methods
	public void addTransform(Block from, Block to) {
		builders.put(Registry.BLOCK.getKey(from).getPath() + "_to_" + Registry.BLOCK.getKey(to).getPath(), new Pair<>(from.defaultBlockState(), to.defaultBlockState()));
	}

	public void addDissolve(Block dissolveBlock) {
		builders.put("dissolve_" + Registry.BLOCK.getKey(dissolveBlock).getPath(), new Pair<>(dissolveBlock.defaultBlockState(), Blocks.AIR.defaultBlockState()));
	}
}
