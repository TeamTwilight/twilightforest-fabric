package twilightforest.client.particle.data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import twilightforest.init.TFParticleType;

public class PinnedFireflyData implements ParticleOptions {

	public final int follow;

	public PinnedFireflyData(int follow) {
		this.follow = follow;
	}

	@Override
	public ParticleType<?> getType() {
		return TFParticleType.FIREFLY_PINNED.get();
	}

	public static Codec<PinnedFireflyData> codecFirefly() {
		return Codec.INT.xmap(PinnedFireflyData::new, (obj) -> obj.follow);
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeVarInt(follow);
	}

	@Override
	public String writeToString() {
		return Integer.toString(follow);
	}

	public static class Deserializer implements ParticleOptions.Deserializer<PinnedFireflyData> {
		@Override
		public PinnedFireflyData fromCommand(ParticleType<PinnedFireflyData> type, StringReader rdr) throws CommandSyntaxException {
			rdr.skipWhitespace();
			return new PinnedFireflyData(rdr.readInt());
		}

		@Override
		public PinnedFireflyData fromNetwork(ParticleType<PinnedFireflyData> type, FriendlyByteBuf buf) {
			return new PinnedFireflyData(buf.readVarInt());
		}
	}
}
