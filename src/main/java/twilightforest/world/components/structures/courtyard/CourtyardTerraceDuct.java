package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerUtil;

import java.util.Random;

public class CourtyardTerraceDuct extends TwilightTemplateStructurePiece {
    public CourtyardTerraceDuct(ServerLevel level, CompoundTag nbt) {
        super(NagaCourtyardPieces.TFNCDu, nbt, level, LichTowerUtil.readSettings(nbt).addProcessor(CourtyardMain.TERRACE_PROCESSOR));
    }

    public CourtyardTerraceDuct(int i, int x, int y, int z, Rotation rotation, StructureManager structureManager) {
        super(NagaCourtyardPieces.TFNCDu, i, structureManager, TwilightForestMod.prefix("courtyard/terrace_duct"), LichTowerUtil.makeSettings(rotation).addProcessor(CourtyardMain.TERRACE_PROCESSOR), new BlockPos(x, y, z));
    }

    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, Random random, BoundingBox boundingBox) {

    }

    @Override
    public NoiseEffect getNoiseEffect() {
        return NoiseEffect.NONE;
    }
}
