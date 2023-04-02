package twilightforest.util;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeRegistry;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.TwilightForestMod;

public class TFWoodTypes {

	public static final BlockSetType TWILIGHT_OAK_SET = new BlockSetType(TwilightForestMod.prefix("twilight_oak").toString());
	public static final BlockSetType CANOPY_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("canopy").toString());
	public static final BlockSetType MANGROVE_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("mangrove").toString());
	public static final BlockSetType DARKWOOD_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("darkwood").toString());
	public static final BlockSetType TIMEWOOD_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("timewood").toString());
	public static final BlockSetType TRANSFORMATION_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("transformation").toString());
	public static final BlockSetType MINING_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("mining").toString());
	public static final BlockSetType SORTING_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("sorting").toString());

	public static final WoodType TWILIGHT_OAK_WOOD_TYPE = WoodTypeRegistry.register(TwilightForestMod.prefix("twilight_oak"), TWILIGHT_OAK_SET);
	public static final WoodType CANOPY_WOOD_TYPE = WoodTypeRegistry.register(TwilightForestMod.prefix("canopy"), CANOPY_WOOD_SET);
	public static final WoodType MANGROVE_WOOD_TYPE = WoodTypeRegistry.register(TwilightForestMod.prefix("mangrove"), MANGROVE_WOOD_SET);
	public static final WoodType DARKWOOD_WOOD_TYPE = WoodTypeRegistry.register(TwilightForestMod.prefix("darkwood"), DARKWOOD_WOOD_SET);
	public static final WoodType TIMEWOOD_WOOD_TYPE = WoodTypeRegistry.register(TwilightForestMod.prefix("timewood"), TIMEWOOD_WOOD_SET);
	public static final WoodType TRANSFORMATION_WOOD_TYPE = WoodTypeRegistry.register(TwilightForestMod.prefix("transformation"), TRANSFORMATION_WOOD_SET);
	public static final WoodType MINING_WOOD_TYPE = WoodTypeRegistry.register(TwilightForestMod.prefix("mining"), MINING_WOOD_SET);
	public static final WoodType SORTING_WOOD_TYPE = WoodTypeRegistry.register(TwilightForestMod.prefix("sorting"), SORTING_WOOD_SET);
}
