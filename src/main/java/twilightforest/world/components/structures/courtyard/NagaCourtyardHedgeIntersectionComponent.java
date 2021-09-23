package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import twilightforest.TFConstants;
import twilightforest.world.registration.TFFeature;

public class NagaCourtyardHedgeIntersectionComponent extends NagaCourtyardHedgeAbstractComponent {
    public NagaCourtyardHedgeIntersectionComponent(ServerLevel level, CompoundTag nbt) {
        super(level, NagaCourtyardPieces.TFNCIs, nbt, new ResourceLocation(TFConstants.ID, "courtyard/hedge_intersection"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_intersection_big"));
    }

    public NagaCourtyardHedgeIntersectionComponent(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCIs, feature, i, x, y, z, rotation, new ResourceLocation(TFConstants.ID, "courtyard/hedge_intersection"), new ResourceLocation(TFConstants.ID, "courtyard/hedge_intersection_big"));
    }
}
