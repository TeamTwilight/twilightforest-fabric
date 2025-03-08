package twilightforest.world.components.structures.lichtowerrevamp;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public final class LichTowerPieces {
	public static final int YARD_SPAWNS = 0;
	public static final int INTERIOR_SPAWNS = 1;
	public static final int EMPTY = 2;

	@Deprecated public final ResourceLocation keepsakeCasketRoom = TwilightForestMod.prefix("lich_tower/9x9/keepsake_casket");
	@Deprecated public final ResourceLocation enclosedBridgeCentral = TwilightForestMod.prefix("lich_tower/central_bridge");
	@Deprecated public final ResourceLocation directAttachment = TwilightForestMod.prefix("lich_tower/no_bridge");
	@Deprecated public final ResourceLocation yardGrave = TwilightForestMod.prefix("lich_tower/grave");

	@Deprecated
	final ResourceLocation[] centerBridges = new ResourceLocation[] {
		this.enclosedBridgeCentral,
		TwilightForestMod.prefix("lich_tower/central_bridge_open")
	};
	@Deprecated
	final ResourceLocation[] endBridges = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/end_bridge_1"),
		TwilightForestMod.prefix("lich_tower/end_bridge_2"),
		TwilightForestMod.prefix("lich_tower/end_bridge_3"),
		TwilightForestMod.prefix("lich_tower/end_bridge_4"),
		TwilightForestMod.prefix("lich_tower/end_bridge_5")
	};
	@Deprecated
	final ResourceLocation[] roomBridges = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/room_bridge_1"),
		TwilightForestMod.prefix("lich_tower/room_bridge_2"),
		TwilightForestMod.prefix("lich_tower/room_bridge_3"),
		TwilightForestMod.prefix("lich_tower/room_bridge_4"),
		TwilightForestMod.prefix("lich_tower/room_bridge_5")
	};

	public static final ResourceLocation DOOR_STOPPER = TwilightForestMod.prefix("lich_tower/door_stopper");
	public static final ResourceLocation DOOR_STOPPER_FALLBACK = TwilightForestMod.prefix("lich_tower/door_stopper_fallback");
	public static final ResourceLocation MOB_BRIDGE = TwilightForestMod.prefix("lich_tower/mob_bridge");
	public static final ResourceLocation CENTER_DECOR = TwilightForestMod.prefix("lich_tower/center_decor");
	public static final ResourceLocation ROOM_DECOR = TwilightForestMod.prefix("lich_tower/room_decor");

	public static final ResourceLocation ROOM_3 = TwilightForestMod.prefix("lich_tower/3x3");
	public static final ResourceLocation ROOM_5 = TwilightForestMod.prefix("lich_tower/5x5");
	public static final ResourceLocation ROOM_7 = TwilightForestMod.prefix("lich_tower/7x7");
	public static final ResourceLocation ROOM_9 = TwilightForestMod.prefix("lich_tower/9x9");
	public static final ResourceLocation ROOM_3_ROOF = TwilightForestMod.prefix("lich_tower/3x3/roof");
	public static final ResourceLocation ROOM_5_ROOF = TwilightForestMod.prefix("lich_tower/5x5/roof");
	public static final ResourceLocation ROOM_7_ROOF = TwilightForestMod.prefix("lich_tower/7x7/roof");
	public static final ResourceLocation ROOM_9_ROOF = TwilightForestMod.prefix("lich_tower/9x9/roof");
	public static final ResourceLocation ROOM_3_SIDE_ROOF = TwilightForestMod.prefix("lich_tower/3x3/side_roof");
	public static final ResourceLocation ROOM_5_SIDE_ROOF = TwilightForestMod.prefix("lich_tower/5x5/side_roof");
	public static final ResourceLocation ROOM_7_SIDE_ROOF = TwilightForestMod.prefix("lich_tower/7x7/side_roof");
	public static final ResourceLocation ROOM_9_SIDE_ROOF = TwilightForestMod.prefix("lich_tower/9x9/side_roof");

	public static final ResourceLocation GALLERY = TwilightForestMod.prefix("lich_tower/gallery");
	public static final ResourceLocation GALLERY_ROOF_EVEN = TwilightForestMod.prefix("lich_tower/gallery_even");
	public static final ResourceLocation GALLERY_ROOF_ODD = TwilightForestMod.prefix("lich_tower/gallery_odd");

	@Deprecated
	final ResourceLocation[][] wingBeards = new ResourceLocation[][] {
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/5x5/beard_checkered"),
			TwilightForestMod.prefix("lich_tower/5x5/beard_chiseled"),
			TwilightForestMod.prefix("lich_tower/5x5/beard_chunks"),
			TwilightForestMod.prefix("lich_tower/5x5/beard_staggered")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/7x7/beard_checkered"),
			TwilightForestMod.prefix("lich_tower/7x7/beard_chiseled"),
			TwilightForestMod.prefix("lich_tower/7x7/beard_chunks"),
			TwilightForestMod.prefix("lich_tower/7x7/beard_staggered")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/9x9/beard_checkered"),
			TwilightForestMod.prefix("lich_tower/9x9/beard_chiseled"),
			TwilightForestMod.prefix("lich_tower/9x9/beard_chunks"),
			TwilightForestMod.prefix("lich_tower/9x9/beard_staggered")
		}
	};

	// The "beards" that generate at bottoms of sidetowers
	@Deprecated
	final ResourceLocation[] wingTrims = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/5x5/beard_trim"),
		TwilightForestMod.prefix("lich_tower/7x7/beard_trim"),
		TwilightForestMod.prefix("lich_tower/9x9/beard_trim")
	};

	// Fallback roofs in case of no space to generate anything else
	@Deprecated
	final ResourceLocation[] flatSideRoofs = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/3x3/flat_side_roof"),
		TwilightForestMod.prefix("lich_tower/5x5/flat_side_roof"),
		TwilightForestMod.prefix("lich_tower/7x7/flat_side_roof"),
		TwilightForestMod.prefix("lich_tower/9x9/flat_side_roof")
	};
	@Deprecated
	final ResourceLocation[] flatRoofs = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/3x3/flat_roof"),
		TwilightForestMod.prefix("lich_tower/5x5/flat_roof"),
		TwilightForestMod.prefix("lich_tower/7x7/flat_roof"),
		TwilightForestMod.prefix("lich_tower/9x9/flat_roof")
	};
	// Fallback tower beards in case of no space to generate anything else
	@Deprecated
	final ResourceLocation[] flatBeards = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/5x5/beard_flat"),
		TwilightForestMod.prefix("lich_tower/7x7/beard_flat"),
		TwilightForestMod.prefix("lich_tower/9x9/beard_flat")
	};

	final Set<String> ladderPlacements1 = new HashSet<>(List.of(
		"twilightforest:ladder_below/0",
		"twilightforest:ladder_below/2"
	));
	final Set<String> ladderPlacements2 = new HashSet<>(List.of(
		"twilightforest:ladder_below/0",
		"twilightforest:ladder_below/1",
		"twilightforest:ladder_below/3",
		"twilightforest:ladder_below/4"
	));
	final Set<String> ladderPlacements3 = new HashSet<>(List.of(
		"twilightforest:ladder_below/1",
		"twilightforest:ladder_below/2",
		"twilightforest:ladder_below/4",
		"twilightforest:ladder_below/5"
	));

	@Deprecated
	final List<Int2ObjectMap<List<ResourceLocation>>> ladderRooms = List.of(
		new Int2ObjectArrayMap<>(Map.of(
			0, List.of(
				TwilightForestMod.prefix("lich_tower/5x5/straight_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/t_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/desk"),
				TwilightForestMod.prefix("lich_tower/5x5/lectern"),
				TwilightForestMod.prefix("lich_tower/5x5/lone_chest"),
				TwilightForestMod.prefix("lich_tower/5x5/altar"),
				TwilightForestMod.prefix("lich_tower/5x5/potion"),
				TwilightForestMod.prefix("lich_tower/5x5/trinity")
			),
			2, List.of(
				TwilightForestMod.prefix("lich_tower/5x5/straight_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/t_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/desk"),
				TwilightForestMod.prefix("lich_tower/5x5/lectern"),
				TwilightForestMod.prefix("lich_tower/5x5/lone_chest"),
				TwilightForestMod.prefix("lich_tower/5x5/altar"),
				TwilightForestMod.prefix("lich_tower/5x5/potion"),
				TwilightForestMod.prefix("lich_tower/5x5/trinity")
			)
		)),
		new Int2ObjectArrayMap<>(Map.of(
			0, List.of(
				TwilightForestMod.prefix("lich_tower/7x7/full_junction_1"),
				TwilightForestMod.prefix("lich_tower/7x7/desk"),
				TwilightForestMod.prefix("lich_tower/7x7/altars"),
				TwilightForestMod.prefix("lich_tower/7x7/altar")
			),
			1, List.of(
				TwilightForestMod.prefix("lich_tower/7x7/potion"),
				TwilightForestMod.prefix("lich_tower/7x7/library_hall"),
				TwilightForestMod.prefix("lich_tower/7x7/jar_study"),
				TwilightForestMod.prefix("lich_tower/7x7/elbow_chest"),
				TwilightForestMod.prefix("lich_tower/7x7/guarded_chest"),
				TwilightForestMod.prefix("lich_tower/7x7/potion_lab")
			),
			3, List.of(
				TwilightForestMod.prefix("lich_tower/7x7/library_hall"),
				TwilightForestMod.prefix("lich_tower/7x7/jar_study"),
				TwilightForestMod.prefix("lich_tower/7x7/elbow_chest"),
				TwilightForestMod.prefix("lich_tower/7x7/guarded_chest"),
				TwilightForestMod.prefix("lich_tower/7x7/garden_lab"),
				TwilightForestMod.prefix("lich_tower/7x7/potion_lab")
			),
			4, List.of(
				TwilightForestMod.prefix("lich_tower/7x7/full_junction_1"),
				TwilightForestMod.prefix("lich_tower/7x7/desk"),
				TwilightForestMod.prefix("lich_tower/7x7/altars"),
				TwilightForestMod.prefix("lich_tower/7x7/jar_study"),
				TwilightForestMod.prefix("lich_tower/7x7/altar")
			)
		)),
		new Int2ObjectArrayMap<>(Map.of(
			1, List.of(
				TwilightForestMod.prefix("lich_tower/9x9/archives"),
				TwilightForestMod.prefix("lich_tower/9x9/enchanting_prison"),
				TwilightForestMod.prefix("lich_tower/9x9/mossy_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/study"),
				TwilightForestMod.prefix("lich_tower/9x9/centerpiece"),
				TwilightForestMod.prefix("lich_tower/9x9/altar"),
				TwilightForestMod.prefix("lich_tower/9x9/lectern_hall"),
				TwilightForestMod.prefix("lich_tower/9x9/cauldron_keep"),
				TwilightForestMod.prefix("lich_tower/9x9/large_study")
			),
			2, List.of(
				TwilightForestMod.prefix("lich_tower/9x9/classic_library"),
				TwilightForestMod.prefix("lich_tower/9x9/study"),
				TwilightForestMod.prefix("lich_tower/9x9/library_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/cauldron_keep"),
				TwilightForestMod.prefix("lich_tower/9x9/seven")
			),
			4, List.of(
				TwilightForestMod.prefix("lich_tower/9x9/classic_library"),
				TwilightForestMod.prefix("lich_tower/9x9/study"),
				TwilightForestMod.prefix("lich_tower/9x9/library_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/seven")
			),
			5, List.of(
				TwilightForestMod.prefix("lich_tower/9x9/archives"),
				TwilightForestMod.prefix("lich_tower/9x9/enchanting_prison"),
				TwilightForestMod.prefix("lich_tower/9x9/mossy_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/study"),
				TwilightForestMod.prefix("lich_tower/9x9/centerpiece"),
				TwilightForestMod.prefix("lich_tower/9x9/altar"),
				TwilightForestMod.prefix("lich_tower/9x9/lectern_hall"),
				TwilightForestMod.prefix("lich_tower/9x9/large_study")
			)
		))
	);
}
