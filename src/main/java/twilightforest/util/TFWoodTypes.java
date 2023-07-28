package twilightforest.util;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.TwilightForestMod;

public class TFWoodTypes {

	public static final BlockSetType TWILIGHT_OAK_SET = new BlockSetType(TwilightForestMod.prefix("twilight_oak").toString());
	public static final BlockSetType CANOPY_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("canopy").toString());
	public static final BlockSetType MANGROVE_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("mangrove").toString());
	public static final BlockSetType DARK_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("dark").toString());
	public static final BlockSetType TIME_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("time").toString());
	public static final BlockSetType TRANSFORMATION_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("transformation").toString());
	public static final BlockSetType MINING_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("mining").toString());
	public static final BlockSetType SORTING_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("sorting").toString());

	public static final WoodType TWILIGHT_OAK_WOOD_TYPE = register(TwilightForestMod.prefix("twilight_oak"), TWILIGHT_OAK_SET);
	public static final WoodType CANOPY_WOOD_TYPE = register(TwilightForestMod.prefix("canopy"), CANOPY_WOOD_SET);
	public static final WoodType MANGROVE_WOOD_TYPE = register(TwilightForestMod.prefix("mangrove"), MANGROVE_WOOD_SET);
	public static final WoodType DARK_WOOD_TYPE = register(TwilightForestMod.prefix("dark"), DARK_WOOD_SET);
	public static final WoodType TIME_WOOD_TYPE = register(TwilightForestMod.prefix("time"), TIME_WOOD_SET);
	public static final WoodType TRANSFORMATION_WOOD_TYPE = register(TwilightForestMod.prefix("transformation"), TRANSFORMATION_WOOD_SET);
	public static final WoodType MINING_WOOD_TYPE = register(TwilightForestMod.prefix("mining"), MINING_WOOD_SET);
	public static final WoodType SORTING_WOOD_TYPE = register(TwilightForestMod.prefix("sorting"), SORTING_WOOD_SET);

	private static WoodType register(ResourceLocation id, BlockSetType setType) {
		return new WoodTypeBuilder().register(id, setType);
	}
}
