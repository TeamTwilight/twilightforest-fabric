package twilightforest.world.components.structures.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;

public interface DecorationClearance {
    boolean isSurfaceDecorationsAllowed();

    boolean isUndergroundDecoAllowed();

    boolean shouldAdjustToTerrain();

    default int adjustForTerrain(Structure.GenerationContext context, int x, int z) {
        return this.shouldAdjustToTerrain()
                ? Mth.clamp(context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()), context.chunkGenerator().getSeaLevel() + 1, context.chunkGenerator().getSeaLevel() + 7)
                : context.chunkGenerator().getSeaLevel();
    }

    record DecorationConfig(boolean surfaceDecorations, boolean undergroundDecorations, @Deprecated boolean adjustElevation) {
        public static MapCodec<DecorationConfig> FLAT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.fieldOf("allow_biome_surface_decorations").forGetter(DecorationConfig::surfaceDecorations),
                Codec.BOOL.fieldOf("allow_biome_underground_decorations").forGetter(DecorationConfig::undergroundDecorations),
                Codec.BOOL.fieldOf("adjust_structure_elevation").forGetter(DecorationConfig::adjustElevation)
        ).apply(instance, DecorationConfig::new));

        public static Codec<DecorationConfig> CODEC = FLAT_CODEC.codec();
    }
}
