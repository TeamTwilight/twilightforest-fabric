package twilightforest.datagen.assets;

import carminite.datagen.ParticleDescriptionProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.resources.Identifier;
import twilightforest.TFCommon;
import twilightforest.init.TFParticleType;

import java.util.Iterator;

public class ParticleGenerator extends ParticleDescriptionProvider {
	public ParticleGenerator(FabricPackOutput output) {
		super(output);
	}

	@Override
	protected void addDescriptions() {
		this.spriteSet(TFParticleType.ANNIHILATE, TFCommon.prefix("annihilate_particle"));
		this.spriteSet(TFParticleType.CLOUD_PUFF, Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.DIM_FLAME, TFCommon.prefix("dim_flame"));
		this.spriteSet(TFParticleType.DOUBLE_JUMP, Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.DRYING_RACK, Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.EXTENDED_SNOW_WARNING, TFCommon.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.FALLEN_LEAF, TFCommon.prefix("fallen_leaf"));
		this.spriteSet(TFParticleType.FIREFLY, TFCommon.prefix("firefly"));
		this.spriteSet(TFParticleType.GHAST_TRAP, Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.HUGE_SMOKE, Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.ICE_BEAM, TFCommon.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.LARGE_FLAME, Identifier.withDefaultNamespace("flame"));
		this.spriteSet(TFParticleType.LEAF_RUNE, () -> new Iterator<>() {
			private int counter = 0;

			@Override
			public boolean hasNext() {
				return this.counter < 26;
			}

			@Override
			public Identifier next() {
				Identifier texture = Identifier.withDefaultNamespace("sga_" + Character.toString('a' + this.counter));
				this.counter++;
				return texture;
			}
		});
		this.spriteSet(TFParticleType.LOG_CORE_PARTICLE, TFCommon.prefix("log_core"));
		this.spriteSet(TFParticleType.OMINOUS_FLAME, TFCommon.prefix("ominous_flame"));
		this.spriteSet(TFParticleType.PARTICLE_SPAWNER_FIREFLY, TFCommon.prefix("firefly"));
		this.spriteSet(TFParticleType.PERFECT_DODGE, Identifier.withDefaultNamespace("generic"), 8, true);
		this.spriteSet(TFParticleType.PROTECTION, Identifier.withDefaultNamespace("glint"));
		this.spriteSet(TFParticleType.SNOW, TFCommon.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.SNOW_GUARDIAN, TFCommon.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.SNOW_WARNING, TFCommon.prefix("snow"), 4, false);
		this.spriteSet(TFParticleType.SORTING_PARTICLE, TFCommon.prefix("log_core"));
		this.spriteSet(TFParticleType.TRANSFORMATION_PARTICLE, TFCommon.prefix("log_core"));
		this.spriteSet(TFParticleType.WANDERING_FIREFLY, TFCommon.prefix("firefly"));
		this.spriteSet(TFParticleType.MAGIC_EFFECT, Identifier.withDefaultNamespace("effect"), 8, true);
		this.spriteSet(TFParticleType.ANGRY_LICH, Identifier.withDefaultNamespace("angry"));
		this.spriteSet(TFParticleType.TWILIGHT_ORB, TFCommon.prefix("twilight_orb"));
		this.spriteSet(TFParticleType.SHIELD_BREAK, TFCommon.prefix("shield_break"));
	}
}