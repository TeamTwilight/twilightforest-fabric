package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

// Ideally, you should not be mixing this with other decorators unless you know what you're doing
// This litters memory with 64 block positions for each chunk. USE SPARINGLY
public class ChunkBlanketingDecorator extends FeatureDecorator<ChunkBlanketingDecorator.ChunkBlanketingConfig> {
    public ChunkBlanketingDecorator(Codec<ChunkBlanketingConfig> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecorationContext context, Random random, ChunkBlanketingConfig config, BlockPos placement) {
        int chunkOriginX = placement.getX() & 0xfffffff0;
        int chunkOriginZ = placement.getZ() & 0xfffffff0;

        ArrayList<BlockPos> coordinates = new ArrayList<>();

        for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
            for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
                if (random.nextFloat() > config.integrity)
                    continue;

                BlockPos pos = new BlockPos(chunkOriginX + xInChunk, context.getHeight(config.heightmap, chunkOriginX + xInChunk, chunkOriginZ + zInChunk), chunkOriginZ + zInChunk);

                if (config.biomeRLOptional.isPresent()) {
                    if (config.biomeRLOptional.get().equals(BuiltinRegistries.BIOME.getKey(context.getLevel().getBiome(pos)))) {
                        coordinates.add(pos);
                    }
                } else {
                    coordinates.add(pos);
                }
            }
        }

        return coordinates.stream();
    }

    public static class ChunkBlanketingConfig implements DecoratorConfiguration {
        public static final Codec<ChunkBlanketingConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.floatRange(0.0f, 1.0f).fieldOf("integrity").forGetter(o -> o.integrity),
                Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(o -> o.heightmap),
                ResourceLocation.CODEC.optionalFieldOf("biome_lock").forGetter(o -> o.biomeRLOptional)
        ).apply(instance, ChunkBlanketingConfig::new));

        public final float integrity;
        public final Heightmap.Types heightmap;
        public final Optional<ResourceLocation> biomeRLOptional;

        public ChunkBlanketingConfig(float integrity, Heightmap.Types heightmap, Optional<ResourceLocation> biomeRLOptional) {
            this.integrity = integrity;
            this.heightmap = heightmap;
            this.biomeRLOptional = biomeRLOptional;
        }
    }
}
