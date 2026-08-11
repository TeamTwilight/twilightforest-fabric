package twilightforest.init;

import carminite.datamaps.SimpleDataMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import twilightforest.TFMain;
import twilightforest.util.datamaps.CrumbledBlock;
import twilightforest.util.datamaps.EntityTransformation;
import twilightforest.util.datamaps.MagicMapBiomeColor;
import twilightforest.util.datamaps.OreMapOreColor;

public class TFDataMaps {
	public static final SimpleDataMap<EntityType<?>, EntityTransformation> TRANSFORMATION_POWDER = SimpleDataMap.builder(TFMain.prefix("transformation_powder"), Registries.ENTITY_TYPE, EntityTransformation.CODEC).synced(EntityTransformation.CODEC, false).build();
	public static final SimpleDataMap<EntityType<?>, EntityTransformation> OMINOUS_FIRE = SimpleDataMap.builder(TFMain.prefix("ominous_fire"), Registries.ENTITY_TYPE, EntityTransformation.CODEC).synced(EntityTransformation.CODEC, false).build();
	public static final SimpleDataMap<Block, CrumbledBlock> CRUMBLE_HORN = SimpleDataMap.builder(TFMain.prefix("crumble_horn"), Registries.BLOCK, CrumbledBlock.CODEC).synced(CrumbledBlock.CODEC, false).build();
	public static final SimpleDataMap<Biome, MagicMapBiomeColor> MAGIC_MAP_BIOME_COLOR = SimpleDataMap.builder(TFMain.prefix("magic_map_color"), Registries.BIOME, MagicMapBiomeColor.CODEC).synced(MagicMapBiomeColor.CODEC, false).build();
	public static final SimpleDataMap<Block, OreMapOreColor> ORE_MAP_ORE_COLOR = SimpleDataMap.builder(TFMain.prefix("ore_map_color"), Registries.BLOCK, OreMapOreColor.CODEC).synced(OreMapOreColor.CODEC, false).build();
}