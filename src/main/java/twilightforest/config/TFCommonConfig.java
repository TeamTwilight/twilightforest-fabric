package twilightforest.config;

import java.util.List;

public class TFCommonConfig {

	public final Dimension DIMENSION = new Dimension();
	public final Portal PORTAL = new Portal();
	public final MagicTrees MAGIC_TREES = new MagicTrees();
	public final UncraftingStuff UNCRAFTING_STUFFS = new UncraftingStuff();
	public final ShieldInteractions SHIELD_INTERACTIONS = new ShieldInteractions();

	public final ConfigValue.BooleanValue casketUUIDLocking = bool(() -> TFConfig.casketUUIDLocking, value -> TFConfig.casketUUIDLocking = value);
	public final ConfigValue.BooleanValue disableSkullCandles = bool(() -> TFConfig.disableSkullCandles, value -> TFConfig.disableSkullCandles = value);
	public final ConfigValue.BooleanValue defaultItemEnchants = bool(() -> TFConfig.defaultItemEnchants, value -> TFConfig.defaultItemEnchants = value);
	public final ConfigValue.BooleanValue bossDropChests = bool(() -> TFConfig.bossDropChests, value -> TFConfig.bossDropChests = value);
	public final ConfigValue.IntValue cloudBlockPrecipitationDistance = integer(() -> TFConfig.commonCloudBlockPrecipitationDistance, value -> TFConfig.commonCloudBlockPrecipitationDistance = Math.max(0, value));
	public final ConfigValue.EnumValue<TFConfig.MultiplayerFightAdjuster> multiplayerFightAdjuster = enumValue(() -> TFConfig.multiplayerFightAdjuster, value -> TFConfig.multiplayerFightAdjuster = value);

	private static ConfigValue.BooleanValue bool(java.util.function.Supplier<Boolean> getter, java.util.function.Consumer<Boolean> setter) {
		return new ConfigValue.BooleanValue(getter, setter);
	}

	private static ConfigValue.IntValue integer(java.util.function.Supplier<Integer> getter, java.util.function.Consumer<Integer> setter) {
		return new ConfigValue.IntValue(getter, setter);
	}

	private static ConfigValue.DoubleValue decimal(java.util.function.Supplier<Double> getter, java.util.function.Consumer<Double> setter) {
		return new ConfigValue.DoubleValue(getter, setter);
	}

	private static <T extends Enum<T>> ConfigValue.EnumValue<T> enumValue(java.util.function.Supplier<T> getter, java.util.function.Consumer<T> setter) {
		return new ConfigValue.EnumValue<>(getter, setter);
	}

	private static ConfigValue<List<String>> strings(java.util.function.Supplier<List<String>> getter, java.util.function.Consumer<List<String>> setter) {
		return new ConfigValue<>(getter, setter);
	}

	public static class Dimension {
		public final ConfigValue.BooleanValue newPlayersSpawnInTF = bool(() -> TFConfig.newPlayersSpawnInTF, value -> TFConfig.newPlayersSpawnInTF = value);
		public final ConfigValue.BooleanValue portalForNewPlayerSpawn = bool(() -> TFConfig.portalForNewPlayerSpawn, value -> TFConfig.portalForNewPlayerSpawn = value);
	}

	public static class Portal {
		public final ConfigValue<String> originDimension = new ConfigValue<>(() -> TFConfig.originDimension, value -> TFConfig.originDimension = value);
		public final ConfigValue.BooleanValue allowPortalsInOtherDimensions = bool(() -> TFConfig.allowPortalsInOtherDimensions, value -> TFConfig.allowPortalsInOtherDimensions = value);
		public final ConfigValue.IntValue portalCreationPermission = integer(() -> TFConfig.portalCreationPermission, value -> TFConfig.portalCreationPermission = Math.max(0, value));
		public final ConfigValue.BooleanValue disablePortalCreation = bool(() -> TFConfig.disablePortalCreation, value -> TFConfig.disablePortalCreation = value);
		public final ConfigValue.BooleanValue checkPortalPlacement = bool(() -> TFConfig.checkPortalPlacement, value -> TFConfig.checkPortalPlacement = value);
		public final ConfigValue.BooleanValue destructivePortalLightning = bool(() -> TFConfig.destructivePortalLightning, value -> TFConfig.destructivePortalLightning = value);
		public final ConfigValue.BooleanValue shouldReturnPortalBeUsable = bool(() -> TFConfig.shouldReturnPortalBeUsable, value -> TFConfig.shouldReturnPortalBeUsable = value);
		public final ConfigValue<String> portalAdvancementLock = new ConfigValue<>(() -> TFConfig.portalLockingAdvancement, value -> TFConfig.portalLockingAdvancement = value);
		public final ConfigValue.IntValue maxPortalSize = integer(() -> TFConfig.maxPortalSize, value -> TFConfig.maxPortalSize = Math.max(4, value));
	}

	public static class UncraftingStuff {
		public final ConfigValue.DoubleValue uncraftingXpCostMultiplier = decimal(() -> TFConfig.uncraftingXpCostMultiplier, value -> TFConfig.uncraftingXpCostMultiplier = Math.max(0.0D, value));
		public final ConfigValue.DoubleValue repairingXpCostMultiplier = decimal(() -> TFConfig.repairingXpCostMultiplier, value -> TFConfig.repairingXpCostMultiplier = Math.max(0.0D, value));
		public final ConfigValue.BooleanValue allowShapelessUncrafting = bool(() -> TFConfig.allowShapelessUncrafting, value -> TFConfig.allowShapelessUncrafting = value);
		public final ConfigValue.BooleanValue disableIngredientSwitching = bool(() -> TFConfig.disableIngredientSwitching, value -> TFConfig.disableIngredientSwitching = value);
		public final ConfigValue<List<String>> disableUncraftingRecipes = strings(() -> TFConfig.disableUncraftingRecipes, value -> {
			TFConfig.disableUncraftingRecipes.clear();
			TFConfig.disableUncraftingRecipes.addAll(value);
		});
		public final ConfigValue.BooleanValue reverseRecipeBlacklist = bool(() -> TFConfig.reverseRecipeBlacklist, value -> TFConfig.reverseRecipeBlacklist = value);
		public final ConfigValue<List<String>> blacklistedUncraftingModIds = strings(() -> TFConfig.blacklistedUncraftingModIds, value -> {
			TFConfig.blacklistedUncraftingModIds.clear();
			TFConfig.blacklistedUncraftingModIds.addAll(value);
		});
		public final ConfigValue.BooleanValue flipUncraftingModIdList = bool(() -> TFConfig.flipUncraftingModIdList, value -> TFConfig.flipUncraftingModIdList = value);
		public final ConfigValue.BooleanValue disableUncraftingOnly = bool(() -> TFConfig.disableUncraftingOnly, value -> TFConfig.disableUncraftingOnly = value);
		public final ConfigValue.BooleanValue disableEntireTable = bool(() -> TFConfig.disableEntireTable, value -> TFConfig.disableEntireTable = value);
	}

	public static class MagicTrees {
		public final ConfigValue.BooleanValue disableTime = bool(() -> TFConfig.disableTimeCore, value -> TFConfig.disableTimeCore = value);
		public final ConfigValue.BooleanValue disableMining = bool(() -> TFConfig.disableMiningCore, value -> TFConfig.disableMiningCore = value);
		public final ConfigValue.BooleanValue disableSorting = bool(() -> TFConfig.disableSortingCore, value -> TFConfig.disableSortingCore = value);
		public final ConfigValue.BooleanValue disableTransformation = bool(() -> TFConfig.disableTransformationCore, value -> TFConfig.disableTransformationCore = value);
		public final ConfigValue.IntValue timeRange = integer(() -> TFConfig.timeCoreRange, value -> TFConfig.timeCoreRange = Math.max(0, value));
		public final ConfigValue.IntValue transformationRange = integer(() -> TFConfig.transformationCoreRange, value -> TFConfig.transformationCoreRange = Math.max(0, value));
		public final ConfigValue.IntValue miningRange = integer(() -> TFConfig.miningCoreRange, value -> TFConfig.miningCoreRange = Math.max(0, value));
		public final ConfigValue.IntValue sortingRange = integer(() -> TFConfig.sortingCoreRange, value -> TFConfig.sortingCoreRange = Math.max(0, value));
	}

	public static class ShieldInteractions {
		public final ConfigValue.BooleanValue parryNonTwilightAttacks = bool(() -> TFConfig.parryNonTwilightAttacks, value -> TFConfig.parryNonTwilightAttacks = value);
		public final ConfigValue.IntValue shieldParryTicks = integer(() -> TFConfig.shieldParryTicks, value -> TFConfig.shieldParryTicks = Math.max(0, value));
	}
}
