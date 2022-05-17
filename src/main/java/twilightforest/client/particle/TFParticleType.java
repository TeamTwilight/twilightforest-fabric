package twilightforest.client.particle;

import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.client.particle.data.PinnedFireflyData;

public class TFParticleType {

	public static final LazyRegistrar<ParticleType<?>> PARTICLE_TYPES = LazyRegistrar.create(Registry.PARTICLE_TYPE, TwilightForestMod.ID);

	public static final RegistryObject<SimpleParticleType> LARGE_FLAME = PARTICLE_TYPES.register("large_flame", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> LEAF_RUNE = PARTICLE_TYPES.register("leaf_rune", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> BOSS_TEAR = PARTICLE_TYPES.register("boss_tear", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> GHAST_TRAP = PARTICLE_TYPES.register("ghast_trap", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> PROTECTION = PARTICLE_TYPES.register("protection", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> SNOW = PARTICLE_TYPES.register("snow", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SNOW_WARNING = PARTICLE_TYPES.register("snow_warning", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SNOW_GUARDIAN = PARTICLE_TYPES.register("snow_guardian", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> ICE_BEAM = PARTICLE_TYPES.register("ice_beam", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> ANNIHILATE = PARTICLE_TYPES.register("annihilate", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> HUGE_SMOKE = PARTICLE_TYPES.register("huge_smoke", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> FIREFLY = PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> WANDERING_FIREFLY = PARTICLE_TYPES.register("wandering_firefly", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> JAR_WANDERING_FIREFLY = PARTICLE_TYPES.register("jar_wandering_firefly", () -> new SimpleParticleType(false));
	public static final RegistryObject<ParticleType<PinnedFireflyData>> FIREFLY_PINNED = PARTICLE_TYPES.register("firefly_pinned", () -> new ParticleType<PinnedFireflyData>(false, new PinnedFireflyData.Deserializer()) {
		@Override
		public Codec<PinnedFireflyData> codec() {
			return PinnedFireflyData.codecFirefly();
		}
	});
	public static final RegistryObject<ParticleType<LeafParticleData>> FALLEN_LEAF = PARTICLE_TYPES.register("fallen_leaf", () -> new ParticleType<LeafParticleData>(false, new LeafParticleData.Deserializer()) {
		@Override
		public Codec<LeafParticleData> codec() {
			return LeafParticleData.codecLeaf();
		}
	});
	public static final RegistryObject<SimpleParticleType> OMINOUS_FLAME = PARTICLE_TYPES.register("ominous_flame", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SORTING_PARTICLE = PARTICLE_TYPES.register("sorting_particle", () -> new SimpleParticleType(false));

	@Environment(EnvType.CLIENT)
	public static void registerFactories() {
		ParticleEngine particles = Minecraft.getInstance().particleEngine;

		ParticleFactoryRegistry.getInstance().register(TFParticleType.LARGE_FLAME.get(), LargeFlameParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.LEAF_RUNE.get(), LeafRuneParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.BOSS_TEAR.get(), new GhastTearParticle.Factory());
		ParticleFactoryRegistry.getInstance().register(TFParticleType.GHAST_TRAP.get(), GhastTrapParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.PROTECTION.get(), ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW.get(), SnowParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW_GUARDIAN.get(), SnowGuardianParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW_WARNING.get(), SnowWarningParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ICE_BEAM.get(), IceBeamParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ANNIHILATE.get(), AnnihilateParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.HUGE_SMOKE.get(), SmokeScaleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FIREFLY.get(), FireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.WANDERING_FIREFLY.get(), WanderingFireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.JAR_WANDERING_FIREFLY.get(), WanderingFireflyParticle.FromJarFactory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FIREFLY_PINNED.get(), PinnedFireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FALLEN_LEAF.get(), LeafParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.OMINOUS_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SORTING_PARTICLE.get(), SortingParticle.Factory::new);
	}
}
