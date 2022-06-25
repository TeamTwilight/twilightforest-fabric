package twilightforest.world.components.structures.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface DecorationClearance {
    boolean isSurfaceDecorationsAllowed();

    boolean isUndergroundDecoAllowed();

    @Deprecated // Method exists for the logic transition. Ultimately this logic should be handled by vanilla.
    boolean shouldAdjustToTerrain();

    record DecorationConfig(boolean surfaceDecorations, boolean undergroundDecorations, @Deprecated boolean adjustElevation) {
        public static MapCodec<DecorationConfig> FLAT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.fieldOf("allow_biome_surface_decorations").forGetter(DecorationConfig::surfaceDecorations),
                Codec.BOOL.fieldOf("allow_biome_underground_decorations").forGetter(DecorationConfig::undergroundDecorations),
                Codec.BOOL.fieldOf("adjust_structure_elevation").forGetter(DecorationConfig::adjustElevation)
        ).apply(instance, DecorationConfig::new));

        public static Codec<DecorationConfig> CODEC = FLAT_CODEC.codec();
    }
}
