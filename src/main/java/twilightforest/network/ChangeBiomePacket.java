package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.concurrent.Executor;

public class ChangeBiomePacket implements S2CPacket {
	private final BlockPos pos;
	private final ResourceLocation biomeId;

	public ChangeBiomePacket(BlockPos pos, ResourceLocation id) {
		this.pos = pos;
		this.biomeId = id;
	}

	public ChangeBiomePacket(FriendlyByteBuf buf) {
		pos = new BlockPos(buf.readInt(), 0, buf.readInt());
		biomeId = buf.readResourceLocation();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getZ());
		buf.writeResourceLocation(biomeId);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, SimpleChannel.ResponseTarget responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {

		public static boolean onMessage(ChangeBiomePacket message, Executor ctx) {
			ctx.execute(new Runnable() {
				@Override
				public void run() {
					final int WIDTH_BITS = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
					final int HEIGHT_BITS = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
					final int HORIZONTAL_MASK = (1 << WIDTH_BITS) - 1;
					final int VERTICAL_MASK = (1 << HEIGHT_BITS) - 1;

					ClientLevel world = Minecraft.getInstance().level;
					LevelChunk chunkAt = (LevelChunk) world.getChunk(message.pos);

					Biome targetBiome = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(message.biomeId);

					for (int dy = 0; dy < 255; dy += 4) {
						int x = (message.pos.getX() >> 2) & HORIZONTAL_MASK;
						int y = Mth.clamp(dy >> 2, 0, VERTICAL_MASK);
						int z = (message.pos.getZ() >> 2) & HORIZONTAL_MASK;
						//FIXME
						//chunkAt.getBiomes().biomes[y << WIDTH_BITS + WIDTH_BITS | z << WIDTH_BITS | x] = targetBiome;
					}

					world.getChunk(message.pos.getX() >> 4, message.pos.getZ() >> 4);
					for (int k = 0; k < 16; ++k)
						world.setSectionDirtyWithNeighbors(message.pos.getX() >> 4, k, message.pos.getZ() >> 4);

				}
			});

			return true;
		}
	}
}
