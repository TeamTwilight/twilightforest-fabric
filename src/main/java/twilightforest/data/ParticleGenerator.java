package twilightforest.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ParticleGenerator implements DataProvider {

	private final PackOutput output;
	private final Map<String, String[]> particles = new LinkedHashMap<>();

	public ParticleGenerator(PackOutput output) {
		this.output = output;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		this.registerParticles();

		CompletableFuture<?>[] futures = this.particles.entrySet().stream()
			.map(entry -> {
				Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
					.resolve(TwilightForestMod.ID + "/particles/" + entry.getKey() + ".json");
				JsonObject json = new JsonObject();
				JsonArray textures = new JsonArray();
				for (String texture : entry.getValue()) {
					textures.add(texture);
				}
				json.add("textures", textures);
				return DataProvider.saveStable(cache, json, path);
			})
			.toArray(CompletableFuture[]::new);

		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Twilight Forest particle descriptions";
	}

	private void registerParticles() {
		this.particles.put("large_flame", new String[]{"twilightforest:dim_flame"});
		this.particles.put("leaf_rune", new String[]{"twilightforest:fallen_leaf"});
		this.particles.put("boss_tear", new String[]{"minecraft:ghast_tear"});
		this.particles.put("ghast_trap", new String[]{"twilightforest:twilight_orb"});
		this.particles.put("protection", new String[]{"minecraft:enchanted_hit"});
		this.particles.put("snow", new String[]{"twilightforest:snow_0", "twilightforest:snow_1", "twilightforest:snow_2", "twilightforest:snow_3"});
		this.particles.put("snow_warning", new String[]{"twilightforest:snow_0", "twilightforest:snow_1", "twilightforest:snow_2", "twilightforest:snow_3"});
		this.particles.put("extended_snow_warning", new String[]{"twilightforest:snow_0", "twilightforest:snow_1", "twilightforest:snow_2", "twilightforest:snow_3"});
		this.particles.put("snow_guardian", new String[]{"twilightforest:snow_0", "twilightforest:snow_1", "twilightforest:snow_2", "twilightforest:snow_3"});
		this.particles.put("ice_beam", new String[]{"twilightforest:twilight_orb_swirl"});
		this.particles.put("annihilate", new String[]{"twilightforest:annihilate_particle"});
		this.particles.put("perfect_dodge", new String[]{"twilightforest:twilight_orb"});
		this.particles.put("double_jump", new String[]{"twilightforest:twilight_orb"});
		this.particles.put("huge_smoke", new String[]{"minecraft:generic_7", "minecraft:generic_6", "minecraft:generic_5", "minecraft:generic_4", "minecraft:generic_3", "minecraft:generic_2", "minecraft:generic_1", "minecraft:generic_0"});
		this.particles.put("firefly", new String[]{"twilightforest:firefly"});
		this.particles.put("wandering_firefly", new String[]{"twilightforest:firefly"});
		this.particles.put("particle_spawner_firefly", new String[]{"twilightforest:firefly"});
		this.particles.put("fallen_leaf", new String[]{"twilightforest:fallen_leaf"});
		this.particles.put("dim_flame", new String[]{"twilightforest:dim_flame"});
		this.particles.put("ominous_flame", new String[]{"twilightforest:ominous_flame"});
		this.particles.put("sorting_particle", new String[]{"twilightforest:log_core"});
		this.particles.put("transformation_particle", new String[]{"twilightforest:log_core"});
		this.particles.put("log_core_particle", new String[]{"twilightforest:log_core"});
		this.particles.put("cloud_puff", new String[]{"twilightforest:twilight_orb"});
		this.particles.put("twilight_orb", new String[]{"twilightforest:twilight_orb"});
		this.particles.put("drying_rack", new String[]{"minecraft:generic_7", "minecraft:generic_6", "minecraft:generic_5", "minecraft:generic_4", "minecraft:generic_3", "minecraft:generic_2", "minecraft:generic_1", "minecraft:generic_0"});
		this.particles.put("magic_effect", new String[]{"minecraft:effect_7", "minecraft:effect_6", "minecraft:effect_5", "minecraft:effect_4", "minecraft:effect_3", "minecraft:effect_2", "minecraft:effect_1", "minecraft:effect_0"});
		this.particles.put("angry_lich", new String[]{"minecraft:angry"});
		this.particles.put("shield_break", new String[]{"twilightforest:shield_break"});
	}
}
