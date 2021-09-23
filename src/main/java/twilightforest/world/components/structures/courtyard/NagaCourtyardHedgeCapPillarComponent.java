package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import twilightforest.TFConstants;
import twilightforest.world.registration.TFFeature;

public class NagaCourtyardHedgeCapPillarComponent extends NagaCourtyardHedgeAbstractComponent {
    public NagaCourtyardHedgeCapPillarComponent(ServerLevel level, CompoundTag nbt) {
        super(level, NagaCourtyardPieces.TFNCCpP, nbt, new ResourceLocation(TFConstants.ID, "courtyard/hedge_end_pillar"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_end_pillar_big"));
    }

    public NagaCourtyardHedgeCapPillarComponent(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCCpP, feature, i, x, y, z, rotation, new ResourceLocation(TFConstants.ID, "courtyard/hedge_end_pillar"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_end_pillar_big"));
    }
}