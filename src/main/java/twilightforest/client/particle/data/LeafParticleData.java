package twilightforest.client.particle.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFParticleTypes;

public record LeafParticleData(int r, int g, int b) implements ParticleOptions {
	public static final MapCodec<LeafParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.INT.fieldOf("r").forGetter(LeafParticleData::r),
			Codec.INT.fieldOf("g").forGetter(LeafParticleData::g),
			Codec.INT.fieldOf("b").forGetter(LeafParticleData::b)
	).apply(instance, LeafParticleData::new));

	public static final StreamCodec<? super RegistryFriendlyByteBuf, LeafParticleData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, LeafParticleData::r,
			ByteBufCodecs.VAR_INT, LeafParticleData::g,
			ByteBufCodecs.VAR_INT, LeafParticleData::b,
			LeafParticleData::new
	);

	@NotNull
	@Override
	public ParticleType<?> getType() {
		return TFParticleTypes.FALLEN_LEAF;
	}
}
