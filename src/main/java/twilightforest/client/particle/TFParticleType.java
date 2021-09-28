package twilightforest.client.particle;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import twilightforest.TFConstants;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.client.particle.data.PinnedFireflyData;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class TFParticleType {

	//public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TwilightForestMod.ID);

	public static final SimpleParticleType> LARGE_FLAME = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":large_flame", new SimpleParticleType(false));
	public static final SimpleParticleType> LEAF_RUNE = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":leaf_rune", new SimpleParticleType(false));
	public static final SimpleParticleType> BOSS_TEAR = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":boss_tear", new SimpleParticleType(false));
	public static final SimpleParticleType> GHAST_TRAP = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":ghast_trap", new SimpleParticleType(false));
	public static final SimpleParticleType> PROTECTION = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":protection", new SimpleParticleType(true));
	public static final SimpleParticleType> SNOW = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":snow", new SimpleParticleType(false));
	public static final SimpleParticleType> SNOW_WARNING = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":snow_warning", new SimpleParticleType(false));
	public static final SimpleParticleType> SNOW_GUARDIAN = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":snow_guardian", new SimpleParticleType(false));
	public static final SimpleParticleType> ICE_BEAM = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":ice_beam", new SimpleParticleType(false));
	public static final SimpleParticleType> ANNIHILATE = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":annihilate", new SimpleParticleType(false));
	public static final SimpleParticleType> HUGE_SMOKE = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":huge_smoke", new SimpleParticleType(false));
	public static final SimpleParticleType> FIREFLY = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":firefly", new SimpleParticleType(false));
	public static final SimpleParticleType> WANDERING_FIREFLY = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":wandering_firefly", new SimpleParticleType(false));
	public static final SimpleParticleType> JAR_WANDERING_FIREFLY = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":jar_wandering_firefly", new SimpleParticleType(false));
	public static final ParticleType<PinnedFireflyData> FIREFLY_PINNED = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":firefly_pinned", new ParticleType<PinnedFireflyData>(false, new PinnedFireflyData.Deserializer()) {
		@Override
		public Codec<PinnedFireflyData> codec() {
			return PinnedFireflyData.codecFirefly();
		}
	});
	public static final ParticleType<LeafParticleData> FALLEN_LEAF = Registry.register(Registry.PARTICLE_TYPE, TFConstants.ID + ":fallen_leaf", new ParticleType<LeafParticleData>(false, new LeafParticleData.Deserializer()) {
		@Override
		public Codec<LeafParticleData> codec() {
			return LeafParticleData.codecLeaf();
		}
	});

	public static void init() {}

		ParticleFactoryRegistry.getInstance().register(TFParticleType.LARGE_FLAME, LargeFlameParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.LEAF_RUNE, LeafRuneParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.BOSS_TEAR, new GhastTearParticle.Factory());
		ParticleFactoryRegistry.getInstance().register(TFParticleType.GHAST_TRAP, GhastTrapParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.PROTECTION, ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW, SnowParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW_GUARDIAN, SnowGuardianParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW_WARNING, SnowWarningParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ICE_BEAM, IceBeamParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ANNIHILATE, AnnihilateParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.HUGE_SMOKE, SmokeScaleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FIREFLY, FireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.WANDERING_FIREFLY, WanderingFireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.JAR_WANDERING_FIREFLY, WanderingFireflyParticle.FromJarFactory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FIREFLY_PINNED, PinnedFireflyParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FALLEN_LEAF, LeafParticle.Factory::new);
	}
}
