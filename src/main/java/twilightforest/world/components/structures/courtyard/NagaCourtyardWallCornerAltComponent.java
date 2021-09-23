package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import twilightforest.TFConstants;
import twilightforest.world.registration.TFFeature;

public class NagaCourtyardWallCornerAltComponent extends NagaCourtyardWallAbstractComponent {
    public NagaCourtyardWallCornerAltComponent(ServerLevel level, CompoundTag nbt) {
        super(level, NagaCourtyardPieces.TFNCWA, nbt, new ResourceLocation(TFConstants.ID, "courtyard/courtyard_wall_corner_inner"), new ResourceLocation(TFConstants.ID, "courtyard/courtyard_wall_corner_inner_decayed"));
    }

    public NagaCourtyardWallCornerAltComponent(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCWA, feature, i, x, y, z, rotation, new ResourceLocation(TFConstants.ID, "courtyard/courtyard_wall_corner_inner"), new ResourceLocation(TFConstants.ID, "courtyard/courtyard_wall_corner_inner_decayed"));
    }
}
