package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import twilightforest.TFConstants;
import twilightforest.world.registration.TFFeature;

public class NagaCourtyardTerraceDuctComponent extends NagaCourtyardTerraceAbstractComponent {
    public NagaCourtyardTerraceDuctComponent(ServerLevel level, CompoundTag nbt) {
        super(level, NagaCourtyardPieces.TFNCDu, nbt, new ResourceLocation(TFConstants.ID, "courtyard/terrace_duct"));
    }

    public NagaCourtyardTerraceDuctComponent(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCDu, feature, i, x, y, z, rotation, new ResourceLocation(TFConstants.ID, "courtyard/terrace_duct"));
    }
}
