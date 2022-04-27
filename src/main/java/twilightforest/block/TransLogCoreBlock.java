package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import twilightforest.TFSounds;
import twilightforest.network.ChangeBiomePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.WorldUtil;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.Random;

public class TransLogCoreBlock extends SpecialMagicLogBlock {

	public TransLogCoreBlock(Properties props) {
		super(props);
	}

	/**
	 * The tree of transformation transforms the biome in the area near it into the enchanted forest biome.
	 * TODO: also change entities
	 */
	@Override
	void performTreeEffect(Level world, BlockPos pos, Random rand) {
		final int WIDTH_BITS = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
		final int HEIGHT_BITS = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
		final int HORIZONTAL_MASK = (1 << WIDTH_BITS) - 1;
		final int VERTICAL_MASK = (1 << HEIGHT_BITS) - 1;
		Biome targetBiome = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.ENCHANTED_FOREST);

		for (int i = 0; i < 16; i++) {

			BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16, 0, 16);
			if (dPos.distSqr(pos) > 256.0)
				continue;

			Biome biomeAt = world.getBiome(dPos).value();
			if (biomeAt == targetBiome)
				continue;

			//FIXME
			LevelChunk chunkAt = world.getChunk(dPos.getX() >> 4, dPos.getZ() >> 4);
			int x = (dPos.getX() >> 2) & HORIZONTAL_MASK;
			int z = (dPos.getZ() >> 2) & HORIZONTAL_MASK;
//			if (chunkAt.getBiomes().biomes[z << WIDTH_BITS | x] == targetBiome)
//				continue;
			for (int dy = 0; dy < 255; dy += 4) {
				int y = Mth.clamp(dy >> 2, 0, VERTICAL_MASK);
				//chunkAt.getBiomes().biomes[y << WIDTH_BITS + WIDTH_BITS | z << WIDTH_BITS | x] = targetBiome;
			}

			if (world instanceof ServerLevel) {
				sendChangedBiome(chunkAt, dPos, targetBiome);
			}
			break;
		}
	}

	/**
	 * Send a tiny update packet to the client to inform it of the changed biome
	 */
	private void sendChangedBiome(LevelChunk chunk, BlockPos pos, Biome biome) {
		ChangeBiomePacket message = new ChangeBiomePacket(pos, chunk.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome));
		TFPacketHandler.CHANNEL.sendToClientsTracking(message, (ServerLevel) chunk.getLevel(), chunk.getPos());
	}

	@Override
	protected void playSound(Level level, BlockPos pos, Random rand) {
		level.playSound(null, pos, TFSounds.TRANSFORMATION_CORE, SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 2F);
	}
}
