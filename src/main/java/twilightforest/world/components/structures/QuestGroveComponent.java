package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import twilightforest.TFConstants;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;
import twilightforest.util.FeaturePlacers;
import twilightforest.world.components.processors.StoneBricksTemplateProcessor;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

public class QuestGroveComponent extends TemplateStructurePiece implements TwilightFeature {
	public QuestGroveComponent(ServerLevel serverLevel, CompoundTag compoundTag) {
		super(TFFeature.TFQuestGrove, compoundTag, serverLevel, rl -> makeSettings(Rotation.valueOf(compoundTag.getString("Rot"))));
	}

	public QuestGroveComponent(StructureManager structureManager, Random random, BlockPos templatePosition) {
		this(structureManager, TFConstants.prefix("quest_grove"), makeSettings(random), templatePosition);
	}

	public QuestGroveComponent(StructureManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings structurePlaceSettings, BlockPos templatePosition) {
		super(TFFeature.TFQuestGrove, 0, structureManager, templateLocation, templateLocation.toString(), structurePlaceSettings, templatePosition);
	}

	@Override
	public TFFeature getFeatureType() {
		return TFFeature.QUEST_GROVE;
	}

	@Override
	protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor levelAccessor, Random random, BoundingBox boundingBox) {
		if (!boundingBox.isInside(pos)) return;

		if ("quest_ram".equals(name)) {
			FeaturePlacers.placeEntity(TFEntities.quest_ram, pos, levelAccessor);
		} else if ("dispenser".equals(name)) {
			TFTreasure.quest_grove.generateLootContainer(levelAccessor, pos, Blocks.DROPPER.defaultBlockState().setValue(DispenserBlock.FACING, this.placeSettings.getRotation().rotate(Direction.NORTH)), 16 | 4 | 2, random.nextLong());
		}
	}

	public static StructurePlaceSettings makeSettings(Random random) {
		return makeSettings(Rotation.getRandom(random));
	}

	public static StructurePlaceSettings makeSettings(Rotation rotation) {
		return new StructurePlaceSettings().setRotation(rotation).addProcessor(StoneBricksTemplateProcessor.INSTANCE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
	}
}
