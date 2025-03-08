package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerPieces;

import java.util.concurrent.CompletableFuture;

public class StructureTemplateDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public StructureTemplateDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper);
	}

	@Override
	protected void generatePools() {
		this.addAllTemplatesToPool(LichTowerPieces.MOB_BRIDGE, 1,
			"lich_tower/bridge_spawner",
			"lich_tower/bridge_spawner_bend",
			"lich_tower/bridge_spawner_ropes",
			"lich_tower/bridge_spawner_wide",
			"lich_tower/bridge_spawner_zag",
			"lich_tower/bridge_spawner_zig"
		);

		this.addToAllPools("lich_tower/wall_bars", 1,
			LichTowerPieces.DOOR_STOPPER
		);
		this.addToAllPools("lich_tower/wall_cobble", 1,
			LichTowerPieces.DOOR_STOPPER,
			LichTowerPieces.DOOR_STOPPER_FALLBACK
		);

		this.addAllTemplatesToPool(LichTowerPieces.CENTER_DECOR, 1,
			"lich_tower/3x3/tree",
			"lich_tower/3x3/water_fountain",
			"lich_tower/3x3/water_well",
			"lich_tower/3x3/wither_rose"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_DECOR, 1,
			"lich_tower/3x3/chest",
			"lich_tower/3x3/lava_well",
			"lich_tower/3x3/sapling",
			"lich_tower/3x3/water_fountain",
			"lich_tower/3x3/water_well"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_3, 1,
			"lich_tower/3x3/short_lookout",
			"lich_tower/3x3/lookout",
			"lich_tower/3x3/double",
			"lich_tower/3x3/taller_double"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_3_ROOF, 1,
			"lich_tower/3x3/pyramid_roof",
			"lich_tower/3x3/slabs_roof",
			"lich_tower/3x3/fence_roof",
			"lich_tower/3x3/curved_roof",
			"lich_tower/3x3/pointed_roof"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_3_SIDE_ROOF, 1,
			"lich_tower/3x3/pyramid_roof",
			"lich_tower/3x3/slabs_roof",
			"lich_tower/3x3/fence_roof",
			"lich_tower/3x3/curved_roof",
			"lich_tower/3x3/pointed_roof"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5, 1,
			"lich_tower/5x5/elbow_junction",
			"lich_tower/5x5/full_junction",
			"lich_tower/5x5/straight_junction",
			"lich_tower/5x5/t_junction",
			"lich_tower/5x5/altar",
			"lich_tower/5x5/desk",
			"lich_tower/5x5/zombie_trap",
			"lich_tower/5x5/full_junction_2",
			"lich_tower/5x5/full_junction_3",
			"lich_tower/5x5/ladder",
			"lich_tower/5x5/library",
			"lich_tower/5x5/lone_chest",
			"lich_tower/5x5/potion",
			"lich_tower/5x5/spawner_1",
			"lich_tower/5x5/spawner_2",
			"lich_tower/5x5/spawner_3",
			"lich_tower/5x5/spawner_4",
			"lich_tower/5x5/spawner_5",
			"lich_tower/5x5/trinity",
			"lich_tower/5x5/webbed_spawner"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5_ROOF, 1,
			"lich_tower/5x5/pyramid_roof",
			"lich_tower/5x5/slabs_roof",
			"lich_tower/5x5/fence_roof",
			"lich_tower/5x5/curved_roof",
			"lich_tower/5x5/pointed_roof"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5_SIDE_ROOF, 1,
			"lich_tower/5x5/pyramid_roof",
			"lich_tower/5x5/slabs_roof",
			"lich_tower/5x5/fence_roof",
			"lich_tower/5x5/curved_roof",
			"lich_tower/5x5/pointed_roof"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7, 1,
			"lich_tower/7x7/elbow_junction",
			"lich_tower/7x7/full_junction",
			"lich_tower/7x7/straight_junction",
			"lich_tower/7x7/t_junction",
			"lich_tower/7x7/altars",
			"lich_tower/7x7/book_staircases",
			"lich_tower/7x7/cactus",
			"lich_tower/7x7/desk",
			"lich_tower/7x7/full_junction_1",
			"lich_tower/7x7/full_junction_2",
			"lich_tower/7x7/grave",
			"lich_tower/7x7/garden_lab",
			"lich_tower/7x7/library_hall",
			"lich_tower/7x7/nursery",
			"lich_tower/7x7/potion",
			"lich_tower/7x7/ritual",
			"lich_tower/7x7/tiered_library",
			"lich_tower/7x7/tiered_study",
			"lich_tower/7x7/pedestal_junction",
			"lich_tower/7x7/shelved_jars",
			"lich_tower/7x7/walled_library",
			"lich_tower/7x7/pedestal_library",
			"lich_tower/7x7/barbed_webs",
			"lich_tower/7x7/corner_caging"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_ROOF, 1,
			"lich_tower/7x7/pyramid_roof",
			"lich_tower/7x7/slabs_roof",
			"lich_tower/7x7/fence_roof",
			"lich_tower/7x7/curved_roof",
			"lich_tower/7x7/pointed_roof"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_SIDE_ROOF, 1,
			"lich_tower/7x7/pyramid_roof",
			"lich_tower/7x7/slabs_roof",
			"lich_tower/7x7/fence_roof",
			"lich_tower/7x7/curved_roof",
			"lich_tower/7x7/pointed_roof"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9, 1,
			"lich_tower/9x9/elbow_junction",
			"lich_tower/9x9/full_junction",
			"lich_tower/9x9/straight_junction",
			"lich_tower/9x9/t_junction",
			"lich_tower/9x9/archives",
			"lich_tower/9x9/enchanting_prison",
			"lich_tower/9x9/tiered_library",
			"lich_tower/9x9/mossy_junction",
			"lich_tower/9x9/altar",
			"lich_tower/9x9/lectern_hall",
			"lich_tower/9x9/tiered_study",
			"lich_tower/9x9/center_decor",
			"lich_tower/9x9/library_junction",
			"lich_tower/9x9/winding_ways",
			"lich_tower/9x9/tiered_spawner",
			"lich_tower/9x9/cauldron_keep",
			"lich_tower/9x9/large_study",
			"lich_tower/9x9/lockup"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_ROOF, 1,
			"lich_tower/9x9/pyramid_roof",
			"lich_tower/9x9/slabs_roof",
			"lich_tower/9x9/fence_roof",
			"lich_tower/9x9/curved_roof",
			"lich_tower/9x9/pointed_roof"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_SIDE_ROOF, 1,
			"lich_tower/9x9/pyramid_roof",
			"lich_tower/9x9/slabs_roof",
			"lich_tower/9x9/fence_roof",
			"lich_tower/9x9/curved_roof",
			"lich_tower/9x9/pointed_roof"
		);

		this.addAllTemplatesToPool(LichTowerPieces.GALLERY, 1,
			"lich_tower/gallery/castaway_paradise",
			"lich_tower/gallery/darkness",
			"lich_tower/gallery/lucid_lands",
			"lich_tower/gallery/music_in_the_mire",
			"lich_tower/gallery/the_hostile_paradise"
		);
		this.addAllTemplatesToPool(LichTowerPieces.GALLERY_ROOF_EVEN, 1,
			"lich_tower/gallery/fence_roof_even",
			"lich_tower/gallery/slabs_roof_even",
			"lich_tower/gallery/stairs_roof_even"
		);
		this.addAllTemplatesToPool(LichTowerPieces.GALLERY_ROOF_ODD, 1,
			"lich_tower/gallery/fence_roof_odd",
			"lich_tower/gallery/slabs_roof_odd",
			"lich_tower/gallery/stairs_roof_odd"
		);
	}
}
