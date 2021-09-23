package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import twilightforest.TFConstants;
import twilightforest.world.registration.TFFeature;

public class NagaCourtyardHedgeTJunctionComponent extends NagaCourtyardHedgeAbstractComponent {
    public NagaCourtyardHedgeTJunctionComponent(ServerLevel level, CompoundTag nbt) {
        super(level, NagaCourtyardPieces.TFNCT, nbt, new ResourceLocation(TFConstants.ID, "courtyard/hedge_t"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_t_big"));
    }

    public NagaCourtyardHedgeTJunctionComponent(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCT, feature, i, x, y, z, rotation, new ResourceLocation(TFConstants.ID, "courtyard/hedge_t"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_t_big"));
    }
}