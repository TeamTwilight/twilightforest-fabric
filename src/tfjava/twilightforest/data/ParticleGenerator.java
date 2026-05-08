package twilightforest.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFParticleTypes;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ParticleGenerator implements DataProvider {
	private final PackOutput.PathProvider pathProvider;
	private final Map<ResourceLocation, List<ResourceLocation>> particles = new LinkedHashMap<>();

	public ParticleGenerator(PackOutput output, Object helper) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "particles");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		this.particles.clear();
		this.addDescriptions();

		List<CompletableFuture<?>> futures = new ArrayList<>();
		this.particles.forEach((particle, textures) -> {
			JsonObject json = new JsonObject();
			if (!textures.isEmpty()) {
				JsonArray array = new JsonArray();
				textures.forEach(texture -> array.add(texture.toString()));
				json.add("textures", array);
			}

			Path path = this.pathProvider.json(particle);
			futures.add(DataProvider.saveStable(output, json, path));
		});
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Twilight Forest particle descriptions";
	}

	protected void addDescriptions() {
		this.sprite(TFParticleTypes.ANNIHILATE, TwilightForestMod.prefix("annihilate_particle"));
		this.spriteSet(TFParticleTypes.CLOUD_PUFF, ResourceLocation.withDefaultNamespace("generic"), 8, true);
		this.sprite(TFParticleTypes.DIM_FLAME, TwilightForestMod.prefix("dim_flame"));
		this.spriteSet(TFParticleTypes.EXTENDED_SNOW_WARNING, TwilightForestMod.prefix("snow"), 4, false);
		this.sprite(TFParticleTypes.FALLEN_LEAF, TwilightForestMod.prefix("fallen_leaf"));
		this.sprite(TFParticleTypes.FIREFLY, TwilightForestMod.prefix("firefly"));
		this.spriteSet(TFParticleTypes.GHAST_TRAP, ResourceLocation.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleTypes.HUGE_SMOKE, ResourceLocation.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleTypes.ICE_BEAM, TwilightForestMod.prefix("snow"), 4, false);
		this.sprite(TFParticleTypes.LARGE_FLAME, ResourceLocation.withDefaultNamespace("flame"));
		this.spriteSet(TFParticleTypes.LEAF_RUNE, () -> new Iterator<>() {
			private int counter = 0;

			@Override
			public boolean hasNext() {
				return this.counter < 26;
			}

			@Override
			public ResourceLocation next() {
				ResourceLocation texture = ResourceLocation.withDefaultNamespace("sga_" + Character.toString('a' + this.counter));
				this.counter++;
				return texture;
			}
		});
		this.sprite(TFParticleTypes.LOG_CORE_PARTICLE, TwilightForestMod.prefix("log_core"));
		this.sprite(TFParticleTypes.OMINOUS_FLAME, TwilightForestMod.prefix("ominous_flame"));
		this.sprite(TFParticleTypes.PARTICLE_SPAWNER_FIREFLY, TwilightForestMod.prefix("firefly"));
		this.spriteSet(TFParticleTypes.PERFECT_DODGE, ResourceLocation.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleTypes.DOUBLE_JUMP, ResourceLocation.withDefaultNamespace("generic"), 8, true);
		this.sprite(TFParticleTypes.PROTECTION, ResourceLocation.withDefaultNamespace("glint"));
		this.spriteSet(TFParticleTypes.SNOW, TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleTypes.SNOW_GUARDIAN, TwilightForestMod.prefix("snow"), 4, false);
		this.spriteSet(TFParticleTypes.SNOW_WARNING, TwilightForestMod.prefix("snow"), 4, false);
		this.sprite(TFParticleTypes.SORTING_PARTICLE, TwilightForestMod.prefix("log_core"));
		this.sprite(TFParticleTypes.TRANSFORMATION_PARTICLE, TwilightForestMod.prefix("log_core"));
		this.sprite(TFParticleTypes.WANDERING_FIREFLY, TwilightForestMod.prefix("firefly"));
		this.spriteSet(TFParticleTypes.DRYING_RACK, ResourceLocation.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleTypes.MAGIC_EFFECT, ResourceLocation.withDefaultNamespace("effect"), 8, true);
		this.sprite(TFParticleTypes.ANGRY_LICH, ResourceLocation.withDefaultNamespace("angry"));
		this.sprite(TFParticleTypes.TWILIGHT_ORB, TwilightForestMod.prefix("twilight_orb"));
		this.sprite(TFParticleTypes.SHIELD_BREAK, TwilightForestMod.prefix("shield_break"));
	}

	protected void sprite(ParticleType<?> particle, ResourceLocation texture) {
		this.particles.put(this.particleId(particle), List.of(texture));
	}

	protected void spriteSet(ParticleType<?> particle, ResourceLocation baseName, int count, boolean reverse) {
		List<ResourceLocation> textures = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			int index = reverse ? count - i - 1 : i;
			textures.add(baseName.withSuffix("_" + index));
		}
		this.particles.put(this.particleId(particle), List.copyOf(textures));
	}

	protected void spriteSet(ParticleType<?> particle, Iterable<ResourceLocation> textures) {
		List<ResourceLocation> list = new ArrayList<>();
		textures.forEach(list::add);
		this.particles.put(this.particleId(particle), List.copyOf(list));
	}

	private ResourceLocation particleId(ParticleType<?> particle) {
		ResourceLocation id = BuiltInRegistries.PARTICLE_TYPE.getKey(particle);
		if (id == null) {
			throw new IllegalStateException("Unknown particle type " + particle);
		}
		return id;
	}
}
