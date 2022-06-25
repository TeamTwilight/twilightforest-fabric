package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import twilightforest.init.TFSounds;
import twilightforest.network.ChangeBiomePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.WorldUtil;
import twilightforest.init.BiomeKeys;

public class TransLogCoreBlock extends SpecialMagicLogBlock {

	public TransLogCoreBlock(Properties props) {
		super(props);
	}

	/**
	 * The tree of transformation transforms the biome in the area near it into the enchanted forest biome.
	 * TODO: also change entities
	 */
	@Override
	void performTreeEffect(Level level, BlockPos pos, RandomSource rand) {
		ResourceKey<Biome> target = BiomeKeys.ENCHANTED_FOREST;
		Holder<Biome> biome = level.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).getHolderOrThrow(target);
		for (int i = 0; i < 16; i++) {
			BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16, 0, 16);
			if (dPos.distSqr(pos) > 256.0)
				continue;

			if (level.getBiome(dPos).is(target))
				continue;

			int minY = QuartPos.fromBlock(level.getMinBuildHeight());
			int maxY = minY + QuartPos.fromBlock(level.getHeight()) - 1;

			int x = QuartPos.fromBlock(dPos.getX());
			int z = QuartPos.fromBlock(dPos.getZ());

			LevelChunk chunkAt = level.getChunk(dPos.getX() >> 4, dPos.getZ() >> 4);
			for (LevelChunkSection section : chunkAt.getSections()) {
				int y = Mth.clamp(QuartPos.fromBlock(section.bottomBlockY()), minY, maxY);
				if (section.getBiomes().get(x & 3, y & 3, z & 3).is(target))
					continue;
				if (section.getBiomes() instanceof PalettedContainer<Holder<Biome>> container)
					container.set(x & 3, y & 3, z & 3, biome);
			}

			if (level instanceof ServerLevel) {
				sendChangedBiome(chunkAt, dPos, target);
			}
			break;
		}
	}

	/**
	 * Send a tiny update packet to the client to inform it of the changed biome
	 */
	private void sendChangedBiome(LevelChunk chunk, BlockPos pos, ResourceKey<Biome> biome) {
		ChangeBiomePacket message = new ChangeBiomePacket(pos, biome);
		TFPacketHandler.CHANNEL.sendToClientsTracking(message, (ServerLevel) chunk.getLevel(), chunk.getPos());
	}

	@Override
	protected void playSound(Level level, BlockPos pos, RandomSource rand) {
		level.playSound(null, pos, TFSounds.TRANSFORMATION_CORE.get(), SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 2F);
	}
}
