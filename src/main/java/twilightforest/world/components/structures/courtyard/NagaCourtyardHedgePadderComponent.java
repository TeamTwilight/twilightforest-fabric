package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import twilightforest.TFConstants;
import twilightforest.world.registration.TFFeature;

public class NagaCourtyardHedgePadderComponent extends NagaCourtyardHedgeAbstractComponent {
    public NagaCourtyardHedgePadderComponent(ServerLevel level, CompoundTag nbt) {
        super(level, NagaCourtyardPieces.TFNCPd, nbt, new ResourceLocation(TFConstants.ID, "courtyard/hedge_between"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_between_big"));
    }

    public NagaCourtyardHedgePadderComponent(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCPd, feature, i, x, y, z, rotation, new ResourceLocation(TFConstants.ID, "courtyard/hedge_between"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_between_big"));
    }
}
