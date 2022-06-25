package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;

import java.util.concurrent.Executor;

public class ChangeBiomePacket implements S2CPacket {
	private final BlockPos pos;
	private final ResourceKey<Biome> biomeId;

	public ChangeBiomePacket(BlockPos pos, ResourceKey<Biome> id) {
		this.pos = pos;
		this.biomeId = id;
	}

	public ChangeBiomePacket(FriendlyByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), 0, buf.readInt());
		this.biomeId = ResourceKey.create(Registry.BIOME_REGISTRY, buf.readResourceLocation());
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getZ());
		buf.writeResourceLocation(this.biomeId.location());
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {

		@SuppressWarnings("Convert2Lambda")
		public static boolean onMessage(ChangeBiomePacket message, Executor ctx) {
			ctx.execute(new Runnable() {
				@Override
				public void run() {
					ClientLevel world = Minecraft.getInstance().level;
					LevelChunk chunkAt = (LevelChunk) world.getChunk(message.pos);

					Holder<Biome> biome = world.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).getHolderOrThrow(message.biomeId);

					int minY = QuartPos.fromBlock(world.getMinBuildHeight());
					int maxY = minY + QuartPos.fromBlock(world.getHeight()) - 1;

					int x = QuartPos.fromBlock(message.pos.getX());
					int z = QuartPos.fromBlock(message.pos.getZ());

					for (LevelChunkSection section : chunkAt.getSections()) {
						int y = Mth.clamp(QuartPos.fromBlock(section.bottomBlockY()), minY, maxY);
						if (section.getBiomes() instanceof PalettedContainer<Holder<Biome>> container)
							container.set(x & 3, y & 3, z & 3, biome);
						SectionPos pos = SectionPos.of(message.pos.getX() >> 4, section.bottomBlockY() >> 4, message.pos.getZ() >> 4);
						world.setSectionDirtyWithNeighbors(pos.x(), pos.y(), pos.z());
					}
					world.onChunkLoaded(new ChunkPos(message.pos));
				}
			});

			return true;
		}
	}
}
