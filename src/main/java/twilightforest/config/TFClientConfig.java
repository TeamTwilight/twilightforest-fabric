package twilightforest.config;

import java.util.List;

public class TFClientConfig {

	public final ConfigValue.BooleanValue silentCicadas = bool(() -> TFConfig.silentCicadas, value -> TFConfig.silentCicadas = value);
	public final ConfigValue.BooleanValue silentCicadasOnHead = bool(() -> TFConfig.silentCicadasOnHead, value -> TFConfig.silentCicadasOnHead = value);
	public final ConfigValue.BooleanValue firstPersonEffects = bool(() -> TFConfig.firstPersonEffects, value -> TFConfig.firstPersonEffects = value);
	public final ConfigValue.BooleanValue rotateTrophyHeadsGui = bool(() -> TFConfig.rotateTrophyHeadsGui, value -> TFConfig.rotateTrophyHeadsGui = value);
	public final ConfigValue.BooleanValue disableOptifineNagScreen = bool(() -> TFConfig.disableOptifineNagScreen, value -> TFConfig.disableOptifineNagScreen = value);
	public final ConfigValue.BooleanValue disableLockedBiomeToasts = bool(() -> TFConfig.disableLockedBiomeToasts, value -> TFConfig.disableLockedBiomeToasts = value);
	public final ConfigValue.BooleanValue showQuestRamCrosshairIndicator = bool(() -> TFConfig.showQuestRamCrosshairIndicator, value -> TFConfig.showQuestRamCrosshairIndicator = value);
	public final ConfigValue.BooleanValue showFortificationShieldIndicator = bool(() -> TFConfig.showFortificationShieldIndicator, value -> TFConfig.showFortificationShieldIndicator = value);
	public final ConfigValue.BooleanValue showFortificationShieldIndicatorInCreative = bool(() -> TFConfig.showFortificationShieldIndicatorInCreative, value -> TFConfig.showFortificationShieldIndicatorInCreative = value);
	public final ConfigValue.IntValue cloudBlockPrecipitationDistance = integer(() -> TFConfig.clientCloudBlockPrecipitationDistance, value -> TFConfig.clientCloudBlockPrecipitationDistance = Math.max(-1, value));
	public final ConfigValue<List<String>> giantSkinUUIDs = strings(() -> TFConfig.giantSkinUUIDs, TFConfig::setGiantSkinUUIDs);
	public final ConfigValue<List<String>> auroraBiomes = strings(() -> TFConfig.auroraBiomes, value -> {
		TFConfig.auroraBiomes.clear();
		TFConfig.auroraBiomes.addAll(value);
	});
	public final ConfigValue.BooleanValue prettifyOreMeterGui = bool(() -> TFConfig.prettifyOreMeterGui, value -> TFConfig.prettifyOreMeterGui = value);
	public final ConfigValue.BooleanValue spawnCharmAnimationAsTotem = bool(() -> TFConfig.spawnCharmAnimationAsTotem, value -> TFConfig.spawnCharmAnimationAsTotem = value);
	public final ConfigValue.BooleanValue manualTravellersWingsGradualGlide = bool(() -> TFConfig.manualTravellersWingsGradualGlide, value -> TFConfig.manualTravellersWingsGradualGlide = value);
	public final ConfigValue.BooleanValue firstPersonGloveOverlay = bool(() -> TFConfig.firstPersonGloveOverlay, value -> TFConfig.firstPersonGloveOverlay = value);

	public final ItemDisplay ITEM_DISPLAY = new ItemDisplay();

	private static ConfigValue.BooleanValue bool(java.util.function.Supplier<Boolean> getter, java.util.function.Consumer<Boolean> setter) {
		return new ConfigValue.BooleanValue(getter, setter);
	}

	private static ConfigValue.IntValue integer(java.util.function.Supplier<Integer> getter, java.util.function.Consumer<Integer> setter) {
		return new ConfigValue.IntValue(getter, setter);
	}

	private static ConfigValue.DoubleValue decimal(java.util.function.Supplier<Double> getter, java.util.function.Consumer<Double> setter) {
		return new ConfigValue.DoubleValue(getter, setter);
	}

	private static ConfigValue<List<String>> strings(java.util.function.Supplier<List<String>> getter, java.util.function.Consumer<List<String>> setter) {
		return new ConfigValue<>(getter, setter);
	}

	public static class ItemDisplay {
		public final ConfigValue.IntValue screenOffsetX = integer(() -> TFConfig.itemDisplayScreenOffsetX, value -> TFConfig.itemDisplayScreenOffsetX = value);
		public final ConfigValue.IntValue screenOffsetY = integer(() -> TFConfig.itemDisplayScreenOffsetY, value -> TFConfig.itemDisplayScreenOffsetY = value);
		public final ConfigValue.DoubleValue screenScale = decimal(() -> TFConfig.itemDisplayScreenScale, value -> TFConfig.itemDisplayScreenScale = Math.max(0.1D, value));
		public final ConfigValue.BooleanValue twentyFourHourFormat = bool(() -> TFConfig.twentyFourHourFormat, value -> TFConfig.twentyFourHourFormat = value);
	}
}
