package twilightforest.client.particle;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.data.LeafParticleData;
import twilightforest.client.particle.data.PinnedFireflyData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class TFParticleType {

	//public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TwilightForestMod.ID);

	public static final SimpleParticleType LARGE_FLAME = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":large_flame", new SimpleParticleType(false));
	public static final SimpleParticleType LEAF_RUNE = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":leaf_rune", new SimpleParticleType(false));
	public static final SimpleParticleType BOSS_TEAR = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":boss_tear", new SimpleParticleType(false));
	public static final SimpleParticleType GHAST_TRAP = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":ghast_trap", new SimpleParticleType(false));
	public static final SimpleParticleType PROTECTION = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":protection", new SimpleParticleType(true));
	public static final SimpleParticleType SNOW = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":snow", new SimpleParticleType(false));
	public static final SimpleParticleType SNOW_WARNING = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":snow_warning", new SimpleParticleType(false));
	public static final SimpleParticleType SNOW_GUARDIAN = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":snow_guardian", new SimpleParticleType(false));
	public static final SimpleParticleType ICE_BEAM = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":ice_beam", new SimpleParticleType(false));
	public static final SimpleParticleType ANNIHILATE = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":annihilate", new SimpleParticleType(false));
	public static final SimpleParticleType HUGE_SMOKE = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":huge_smoke", new SimpleParticleType(false));
	public static final SimpleParticleType FIREFLY = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":firefly", new SimpleParticleType(false));
	public static final ParticleType<PinnedFireflyData> FIREFLY_PINNED = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":firefly_pinned", new ParticleType<PinnedFireflyData>(false, new PinnedFireflyData.Deserializer()) {
		@Override
		public Codec<PinnedFireflyData> codec() {
			return PinnedFireflyData.codecFirefly();
		}
	});
	public static final ParticleType<LeafParticleData> FALLEN_LEAF = Registry.register(Registry.PARTICLE_TYPE,TwilightForestMod.ID+":fallen_leaf", new ParticleType<LeafParticleData>(false, new LeafParticleData.Deserializer()) {
		@Override
		public Codec<LeafParticleData> codec() {
			return LeafParticleData.codecLeaf();
		}
	});

	@Environment(EnvType.CLIENT)
	public static void registerFactories() {
		ParticleEngine particles = Minecraft.getInstance().particleEngine;

		particles.register(TFParticleType.LARGE_FLAME, LargeFlameParticle.Factory::new);
		particles.register(TFParticleType.LEAF_RUNE, LeafRuneParticle.Factory::new);
		particles.register(TFParticleType.BOSS_TEAR, new GhastTearParticle.Factory());
		particles.register(TFParticleType.GHAST_TRAP, GhastTrapParticle.Factory::new);
		particles.register(TFParticleType.PROTECTION, ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		particles.register(TFParticleType.SNOW, SnowParticle.Factory::new);
		particles.register(TFParticleType.SNOW_GUARDIAN, SnowGuardianParticle.Factory::new);
		particles.register(TFParticleType.SNOW_WARNING, SnowWarningParticle.Factory::new);
		particles.register(TFParticleType.ICE_BEAM, IceBeamParticle.Factory::new);
		particles.register(TFParticleType.ANNIHILATE, AnnihilateParticle.Factory::new);
		particles.register(TFParticleType.HUGE_SMOKE, SmokeScaleParticle.Factory::new);
		particles.register(TFParticleType.FIREFLY, FireflyParticle.Factory::new);
		particles.register(TFParticleType.FIREFLY_PINNED, PinnedFireflyParticle.Factory::new);
		particles.register(TFParticleType.FALLEN_LEAF, LeafParticle.Factory::new);
	}
}
