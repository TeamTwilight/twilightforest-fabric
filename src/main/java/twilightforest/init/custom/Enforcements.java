package twilightforest.init.custom;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.Identifier;
import twilightforest.TFMain;
import twilightforest.TFRegistries;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFSounds;
import twilightforest.util.Enforcement;

public final class Enforcements {

	public static final ResourceKey<Enforcement> DARKNESS_KEY = makeKey("darkness");
	public static final Enforcement DARKNESS = register(DARKNESS_KEY, new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
			player.addEffect(new MobEffectInstance(
				MobEffects.DARKNESS,
				200,
				(int) restriction.multiplier(),
				false,
				true
			));
		}
	}));

	public static final ResourceKey<Enforcement> HUNGER_KEY = makeKey("hunger");
	public static final Enforcement HUNGER = register(HUNGER_KEY, new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
			MobEffectInstance currentHunger = player.getEffect(MobEffects.HUNGER);
			int hungerLevel = currentHunger != null
				? currentHunger.getAmplifier() + (int) restriction.multiplier()
				: (int) restriction.multiplier();

			player.addEffect(new MobEffectInstance(
				MobEffects.HUNGER,
				100,
				hungerLevel,
				false,
				true
			));
		}
	}));

	public static final ResourceKey<Enforcement> FIRE_KEY = makeKey("fire");
	public static final Enforcement FIRE = register(FIRE_KEY, new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
			player.igniteForSeconds((int) restriction.multiplier());
		}
	}));

	public static final ResourceKey<Enforcement> FROST_KEY = makeKey("frost");
	public static final Enforcement FROST = register(FROST_KEY, new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
			player.addEffect(new MobEffectInstance(
				TFMobEffects.FROSTY,
				100,
				(int) restriction.multiplier(),
				false,
				true
			));
		}
	}));

	public static final ResourceKey<Enforcement> ACID_RAIN_KEY = makeKey("acid_rain");
	public static final Enforcement ACID_RAIN = register(ACID_RAIN_KEY, new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 5 == 0 && level.tickRateManager().runsNormally()) {
			if (player.hurtServer(
				level,
				TFDamageTypes.getDamageSource(level, TFDamageTypes.ACID_RAIN),
				restriction.multiplier()
			)) {
				level.playSound(
					null,
					player.getX(),
					player.getY(),
					player.getZ(),
					TFSounds.ACID_RAIN_BURNS,
					SoundSource.PLAYERS,
					1.0F,
					1.0F
				);
			}
		}
	}));

	private static ResourceKey<Enforcement> makeKey(String name) {
		return ResourceKey.create(TFRegistries.Keys.ENFORCEMENT, TFMain.prefix(name));
	}

	private static Enforcement register(ResourceKey<Enforcement> key, Enforcement enforcement) {
		return Registry.register(
			TFRegistries.ENFORCEMENT,
			Identifier.fromNamespaceAndPath(TFMain.ID, key.identifier().getPath()),
			enforcement
		);
	}

	private Enforcements() {
	}

	public static void init() {
		TFMain.LOGGER.info("Initializing enforcements...");
	}
}