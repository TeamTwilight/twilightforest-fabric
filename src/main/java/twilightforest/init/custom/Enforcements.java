package twilightforest.init.custom;

import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFSounds;
import twilightforest.util.Enforcement;

public final class Enforcements {
	public static final Enforcement DARKNESS = register("darkness", new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
			player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, (int) restriction.multiplier(), false, true));
		}
	}));

	public static final Enforcement HUNGER = register("hunger", new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
			MobEffectInstance currentHunger = player.getEffect(MobEffects.HUNGER);
			int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + (int) restriction.multiplier() : (int) restriction.multiplier();
			player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, hungerLevel, false, true));
		}
	}));

	public static final Enforcement FIRE = register("fire", new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
			player.igniteForSeconds((int) restriction.multiplier());
		}
	}));

	public static final Enforcement FROST = register("frost", new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
			player.addEffect(new MobEffectInstance(TFMobEffects.FROSTY, 100, (int) restriction.multiplier(), false, true));
		}
	}));

	public static final Enforcement ACID_RAIN = register("acid_rain", new Enforcement((player, level, restriction) -> {
		if (player.tickCount % 5 == 0 && level.tickRateManager().runsNormally()) {
			if (player.hurt(TFDamageTypes.source(level, TFDamageTypes.ACID_RAIN), restriction.multiplier())) {
				level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
		}
	}));

	private Enforcements() {
	}

	private static Enforcement register(String path, Enforcement enforcement) {
		return Registry.register(TFRegistries.ENFORCEMENT, TwilightForestMod.prefix(path), enforcement);
	}

	public static void bootstrap() {
		DARKNESS.toString();
		HUNGER.toString();
		FIRE.toString();
		FROST.toString();
		ACID_RAIN.toString();
	}
}
