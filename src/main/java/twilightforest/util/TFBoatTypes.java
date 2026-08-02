package twilightforest.util;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.IntFunction;

/**
 * Manages custom boat types for Twilight Forest.
 * Uses reflection to add custom enum values to Boat.Type,
 * replacing NeoForge's enum extension system.
 *
 * 1.21.1: Boat.Type constructor changed to (String, int, Block, String).
 * BY_ID is now an IntFunction, not an array.
 */
public class TFBoatTypes {

	public static Boat.Type TWILIGHT_OAK;
	public static Boat.Type CANOPY;
	public static Boat.Type MANGROVE_TYPE;
	public static Boat.Type DARK;
	public static Boat.Type TIME;
	public static Boat.Type TRANSFORMATION;
	public static Boat.Type MINING;
	public static Boat.Type SORTING;

	private static boolean registered = false;
	private static boolean registrationFailed = false;

	/**
	 * Get a boat type safely, falling back to OAK if registration failed.
	 */
	public static Boat.Type getOrFallback(Boat.Type type) {
		return type != null ? type : Boat.Type.OAK;
	}

	@SuppressWarnings("unchecked")
	public static void registerCustomTypes() {
		if (registered) return;

		try {
			// Java 17+ blocks reflective enum construction. Use Unsafe to bypass.
			sun.misc.Unsafe unsafe = getUnsafe();

			TWILIGHT_OAK = addTypeUnsafe(unsafe, "TWILIGHTFOREST_TWILIGHT_OAK", 9,
				TFBlocks.TWILIGHT_OAK_PLANKS.get(), "twilight_oak");

			CANOPY = addTypeUnsafe(unsafe, "TWILIGHTFOREST_CANOPY", 10,
				TFBlocks.CANOPY_PLANKS.get(), "canopy");

			MANGROVE_TYPE = addTypeUnsafe(unsafe, "TWILIGHTFOREST_MANGROVE", 11,
				TFBlocks.MANGROVE_PLANKS.get(), "twilight_mangrove");

			DARK = addTypeUnsafe(unsafe, "TWILIGHTFOREST_DARK", 12,
				TFBlocks.DARK_PLANKS.get(), "dark");

			TIME = addTypeUnsafe(unsafe, "TWILIGHTFOREST_TIME", 13,
				TFBlocks.TIME_PLANKS.get(), "time");

			TRANSFORMATION = addTypeUnsafe(unsafe, "TWILIGHTFOREST_TRANSFORMATION", 14,
				TFBlocks.TRANSFORMATION_PLANKS.get(), "transformation");

			MINING = addTypeUnsafe(unsafe, "TWILIGHTFOREST_MINING", 15,
				TFBlocks.MINING_PLANKS.get(), "mining");

			SORTING = addTypeUnsafe(unsafe, "TWILIGHTFOREST_SORTING", 16,
				TFBlocks.SORTING_PLANKS.get(), "sorting");

			// Rebuild CODEC and BY_ID to include new enum values
			rebuildEnumSupport(Boat.Type.class);

			registered = true;

		} catch (Exception e) {
			registrationFailed = true;
			TwilightForestMod.LOGGER.error("Failed to register custom Twilight Forest boat types", e);
			throw new RuntimeException("Failed to register custom Twilight Forest boat types", e);
		}
	}

	private static sun.misc.Unsafe getUnsafe() throws Exception {
		Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		return (sun.misc.Unsafe) f.get(null);
	}

	private static Boat.Type addTypeUnsafe(sun.misc.Unsafe unsafe,
	                                       String name, int ordinal, Block planks, String typeName) throws Exception {
		// Allocate enum instance without calling constructor (bypasses Java 17+ enum restriction)
		Boat.Type value = (Boat.Type) unsafe.allocateInstance(Boat.Type.class);

		// Set the fields using Unsafe to bypass Java module restrictions
		setFieldWithUnsafe(unsafe, Enum.class, "name", value, name);
		setFieldWithUnsafe(unsafe, Enum.class, "ordinal", value, ordinal);

		// Set custom fields from Boat.Type constructor (planks Block and name String)
		for (Field f : Boat.Type.class.getDeclaredFields()) {
			if (f.getType() == Block.class && !java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				setFieldWithUnsafe(unsafe, Boat.Type.class, f.getName(), value, planks);
			} else if (f.getType() == String.class && !java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				setFieldWithUnsafe(unsafe, Boat.Type.class, f.getName(), value, typeName);
			}
		}

		// Add to $VALUES array using Unsafe
		Field valuesField = Boat.Type.class.getDeclaredField("$VALUES");
		long valuesOffset = unsafe.staticFieldOffset(valuesField);
		Object valuesBase = unsafe.staticFieldBase(valuesField);
		Boat.Type[] oldValues = (Boat.Type[]) unsafe.getObject(valuesBase, valuesOffset);
		Boat.Type[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
		newValues[oldValues.length] = value;
		unsafe.putObject(valuesBase, valuesOffset, newValues);

		return value;
	}

	private static void setFieldWithUnsafe(sun.misc.Unsafe unsafe, Class<?> clazz, String fieldName,
	                                       Object target, Object value) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		long offset = unsafe.objectFieldOffset(field);
		unsafe.putObject(target, offset, value);
	}

	private static void setFieldWithUnsafe(sun.misc.Unsafe unsafe, Class<?> clazz, String fieldName,
	                                       Object target, int value) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		long offset = unsafe.objectFieldOffset(field);
		unsafe.putInt(target, offset, value);
	}

	/**
	 * Rebuild CODEC and BY_ID static fields to include the newly added enum values.
	 * Uses Unsafe to bypass Java module restrictions.
	 */
	private static void rebuildEnumSupport(Class<Boat.Type> clazz) throws Exception {
		sun.misc.Unsafe unsafe = getUnsafe();

		// CODEC: StringRepresentable.fromEnum(Type::values)
		try {
			Field codecField = clazz.getDeclaredField("CODEC");
			long codecOffset = unsafe.staticFieldOffset(codecField);
			Object codecBase = unsafe.staticFieldBase(codecField);
			unsafe.putObject(codecBase, codecOffset, StringRepresentable.fromEnum(Boat.Type::values));
		} catch (NoSuchFieldException e) {
			TwilightForestMod.LOGGER.warn("Could not find CODEC field in Boat.Type, trying alternative names...");
			// Try alternative field names for different mappings
			for (Field f : clazz.getDeclaredFields()) {
				if (f.getType().getName().contains("Codec") || f.getType().getName().contains("StringRepresentable")) {
					long offset = unsafe.staticFieldOffset(f);
					Object base = unsafe.staticFieldBase(f);
					unsafe.putObject(base, offset, StringRepresentable.fromEnum(Boat.Type::values));
					TwilightForestMod.LOGGER.info("Patched CODEC via field: {}", f.getName());
					break;
				}
			}
		}

		// BY_ID: IntFunction - maps int id -> Boat.Type
		try {
			Field byIdField = clazz.getDeclaredField("BY_ID");
			long byIdOffset = unsafe.staticFieldOffset(byIdField);
			Object byIdBase = unsafe.staticFieldBase(byIdField);
			Boat.Type[] values = Boat.Type.values();
			IntFunction<Boat.Type> newById = id -> id >= 0 && id < values.length ? values[id] : values[0];
			unsafe.putObject(byIdBase, byIdOffset, newById);
		} catch (NoSuchFieldException e) {
			TwilightForestMod.LOGGER.warn("Could not find BY_ID field in Boat.Type, trying alternative names...");
			// Try alternative field names for different mappings
			for (Field f : clazz.getDeclaredFields()) {
				if (f.getType() == IntFunction.class || IntFunction.class.isAssignableFrom(f.getType())) {
					long offset = unsafe.staticFieldOffset(f);
					Object base = unsafe.staticFieldBase(f);
					Boat.Type[] values = Boat.Type.values();
					IntFunction<Boat.Type> newById = id -> id >= 0 && id < values.length ? values[id] : values[0];
					unsafe.putObject(base, offset, newById);
					TwilightForestMod.LOGGER.info("Patched BY_ID via field: {}", f.getName());
					break;
				}
			}
		}
	}
}
