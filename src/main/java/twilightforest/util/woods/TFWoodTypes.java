package twilightforest.util.woods;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.TFMain;

public class TFWoodTypes {

	public static final BlockSetType TWILIGHT_OAK_SET = BlockSetType.register(new BlockSetType(TFMain.prefix("twilight_oak").toString()));
	public static final BlockSetType CANOPY_WOOD_SET = BlockSetType.register(new BlockSetType(TFMain.prefix("canopy").toString()));
	public static final BlockSetType MANGROVE_WOOD_SET = BlockSetType.register(new BlockSetType(TFMain.prefix("mangrove").toString()));
	public static final BlockSetType DARK_WOOD_SET = BlockSetType.register(new BlockSetType(TFMain.prefix("dark").toString()));
	public static final BlockSetType TIME_WOOD_SET = BlockSetType.register(new BlockSetType(TFMain.prefix("time").toString()));
	public static final BlockSetType TRANSFORMATION_WOOD_SET = BlockSetType.register(new BlockSetType(TFMain.prefix("transformation").toString()));
	public static final BlockSetType MINING_WOOD_SET = BlockSetType.register(new BlockSetType(TFMain.prefix("mining").toString()));
	public static final BlockSetType SORTING_WOOD_SET = BlockSetType.register(new BlockSetType(TFMain.prefix("sorting").toString()));

	public static final WoodType TWILIGHT_OAK_WOOD_TYPE = WoodType.register(new WoodType(TFMain.prefix("twilight_oak").toString(), TWILIGHT_OAK_SET));
	public static final WoodType CANOPY_WOOD_TYPE = WoodType.register(new WoodType(TFMain.prefix("canopy").toString(), CANOPY_WOOD_SET));
	public static final WoodType MANGROVE_WOOD_TYPE = WoodType.register(new WoodType(TFMain.prefix("mangrove").toString(), MANGROVE_WOOD_SET));
	public static final WoodType DARK_WOOD_TYPE = WoodType.register(new WoodType(TFMain.prefix("dark").toString(), DARK_WOOD_SET));
	public static final WoodType TIME_WOOD_TYPE = WoodType.register(new WoodType(TFMain.prefix("time").toString(), TIME_WOOD_SET));
	public static final WoodType TRANSFORMATION_WOOD_TYPE = WoodType.register(new WoodType(TFMain.prefix("transformation").toString(), TRANSFORMATION_WOOD_SET));
	public static final WoodType MINING_WOOD_TYPE = WoodType.register(new WoodType(TFMain.prefix("mining").toString(), MINING_WOOD_SET));
	public static final WoodType SORTING_WOOD_TYPE = WoodType.register(new WoodType(TFMain.prefix("sorting").toString(), SORTING_WOOD_SET));
}
