package twilightforest;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFRegistryObject;
import twilightforest.init.TFSounds;
import twilightforest.world.components.BiomeColorAlgorithms;

import java.lang.reflect.Proxy;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public final class TFEnumExtensions {
	private static final BiomeColorAlgorithms BIOME_COLOR_ALGORITHMS = new BiomeColorAlgorithms();

	private TFEnumExtensions() {
	}

	public static Object DamageEffects_PINCH(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> cast(type, prefix("pinch"));
			case 1 -> castSound(type, TFSounds.PINCH_BEETLE_ATTACK);
			default -> unexpected(idx);
		};
	}

	public static Object Rarity_TWILIGHT(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> cast(type, 5);
			case 1 -> cast(type, prefix("twilight"));
			case 2 -> cast(type, (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.DARK_GREEN));
			default -> unexpected(idx);
		};
	}

	public static Object GrassColorModifier_ENCHANTED_FOREST(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> cast(type, prefix("enchanted_forest"));
			case 1 -> grassColorModifier(type, (x, z, color) -> BIOME_COLOR_ALGORITHMS.enchanted(color, (int) x, (int) z));
			default -> unexpected(idx);
		};
	}

	public static Object GrassColorModifier_SWAMP(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> cast(type, prefix("swamp"));
			case 1 -> grassColorModifier(type, (x, z, color) -> BIOME_COLOR_ALGORITHMS.swamp(BiomeColorAlgorithms.Type.Grass));
			default -> unexpected(idx);
		};
	}

	public static Object GrassColorModifier_DARK_FOREST(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> cast(type, prefix("dark_forest"));
			case 1 -> grassColorModifier(type, (x, z, color) -> BIOME_COLOR_ALGORITHMS.darkForest(BiomeColorAlgorithms.Type.Grass));
			default -> unexpected(idx);
		};
	}

	public static Object GrassColorModifier_DARK_FOREST_CENTER(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> cast(type, prefix("dark_forest_center"));
			case 1 -> grassColorModifier(type, (x, z, color) -> BIOME_COLOR_ALGORITHMS.darkForestCenterGrass(x, z));
			default -> unexpected(idx);
		};
	}

	public static Object GrassColorModifier_SPOOKY_FOREST(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> cast(type, prefix("spooky_forest"));
			case 1 -> grassColorModifier(type, (x, z, color) -> BIOME_COLOR_ALGORITHMS.spookyGrass(x, z));
			default -> unexpected(idx);
		};
	}

	public static Object ItemDisplayContext_JARRED(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> cast(type, -1);
			case 1 -> cast(type, prefix("jarred"));
			case 2 -> cast(type, "FIXED");
			default -> unexpected(idx);
		};
	}

	public static Object Boat$Type_TWILIGHT_OAK(int idx, Class<?> type) {
		return boatType(idx, type, TFBlocks.TWILIGHT_OAK_PLANKS, "twilight_oak", TFItems.TWILIGHT_OAK_BOAT, TFItems.TWILIGHT_OAK_CHEST_BOAT);
	}

	public static Object Boat$Type_CANOPY(int idx, Class<?> type) {
		return boatType(idx, type, TFBlocks.CANOPY_PLANKS, "canopy", TFItems.CANOPY_BOAT, TFItems.CANOPY_CHEST_BOAT);
	}

	public static Object Boat$Type_MANGROVE(int idx, Class<?> type) {
		return boatType(idx, type, TFBlocks.MANGROVE_PLANKS, "mangrove", TFItems.MANGROVE_BOAT, TFItems.MANGROVE_CHEST_BOAT);
	}

	public static Object Boat$Type_DARK(int idx, Class<?> type) {
		return boatType(idx, type, TFBlocks.DARK_PLANKS, "dark", TFItems.DARK_BOAT, TFItems.DARK_CHEST_BOAT);
	}

	public static Object Boat$Type_TIME(int idx, Class<?> type) {
		return boatType(idx, type, TFBlocks.TIME_PLANKS, "time", TFItems.TIME_BOAT, TFItems.TIME_CHEST_BOAT);
	}

	public static Object Boat$Type_TRANSFORMATION(int idx, Class<?> type) {
		return boatType(idx, type, TFBlocks.TRANSFORMATION_PLANKS, "transformation", TFItems.TRANSFORMATION_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT);
	}

	public static Object Boat$Type_MINING(int idx, Class<?> type) {
		return boatType(idx, type, TFBlocks.MINING_PLANKS, "mining", TFItems.MINING_BOAT, TFItems.MINING_CHEST_BOAT);
	}

	public static Object Boat$Type_SORTING(int idx, Class<?> type) {
		return boatType(idx, type, TFBlocks.SORTING_PLANKS, "sorting", TFItems.SORTING_BOAT, TFItems.SORTING_CHEST_BOAT);
	}

	private static Object boatType(int idx, Class<?> type, TFRegistryObject<Block> planks, String name, TFRegistryObject<Item> boat, TFRegistryObject<Item> chestBoat) {
		if (idx == 5) {
			return false;
		}
		return switch (idx) {
			case 0 -> castRegistry(type, planks);
			case 1 -> cast(type, prefix(name));
			case 2 -> castRegistry(type, boat);
			case 3 -> castRegistry(type, chestBoat);
			case 4 -> castSupplier(type, (Supplier<Item>) () -> Items.STICK);
			default -> unexpected(idx);
		};
	}

	private static Object castRegistry(Class<?> type, TFRegistryObject<?> value) {
		if (Supplier.class.isAssignableFrom(type)) {
			return type.cast(value);
		}
		return type.cast(value.get());
	}

	private static Object castSound(Class<?> type, SoundEvent sound) {
		if (Supplier.class.isAssignableFrom(type)) {
			return castSupplier(type, (Supplier<SoundEvent>) () -> sound);
		}
		return type.cast(sound);
	}

	private static Object grassColorModifier(Class<?> type, GrassColorFunction function) {
		return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (proxy, method, args) -> {
			if ("toString".equals(method.getName()) && method.getParameterCount() == 0) {
				return "TwilightForestGrassColorModifier";
			}
			if (args != null && args.length == 3 && args[0] instanceof Number x && args[1] instanceof Number z && args[2] instanceof Number color) {
				return function.apply(x.doubleValue(), z.doubleValue(), color.intValue());
			}
			return method.invoke(function, args);
		}));
	}

	private static Object castSupplier(Class<?> type, Supplier<?> supplier) {
		return type.cast(supplier);
	}

	private static Object cast(Class<?> type, Object value) {
		return type.cast(value);
	}

	private static String prefix(String path) {
		return TwilightForestMod.prefix(path).toString();
	}

	private static Object unexpected(int idx) {
		throw new IllegalArgumentException("Unexpected parameter index: " + idx);
	}

	@FunctionalInterface
	private interface GrassColorFunction {
		int apply(double x, double z, int color);
	}
}
