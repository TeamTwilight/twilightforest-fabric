package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import twilightforest.TFConstants;
import twilightforest.world.registration.TFFeature;

public class NagaCourtyardHedgeCapComponent extends NagaCourtyardHedgeAbstractComponent {

    public NagaCourtyardHedgeCapComponent(ServerLevel level, CompoundTag nbt) {
        super(level, NagaCourtyardPieces.TFNCCp, nbt, new ResourceLocation(TFConstants.ID, "courtyard/hedge_end"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_end_big"));
    }

    @SuppressWarnings("WeakerAccess")
    public NagaCourtyardHedgeCapComponent(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCCp, feature, i, x, y, z, rotation, new ResourceLocation(TFConstants.ID, "courtyard/hedge_end"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_end_big"));
    }
}