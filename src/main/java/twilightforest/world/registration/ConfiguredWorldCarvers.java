package twilightforest.world.registration;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.*;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.TFCavesCarver;

//this was all put into 1 class because it seems like a waste to have it in 2
public class ConfiguredWorldCarvers {
	public static final TFCavesCarver TFCAVES = new TFCavesCarver(CaveCarverConfiguration.CODEC, false);
	public static final TFCavesCarver HIGHLANDCAVES = new TFCavesCarver(CaveCarverConfiguration.CODEC, true);


	public static void register() {
		Registry.register(Registry.CARVER, TwilightForestMod.prefix("tf_caves"), TFCAVES);
		Registry.register(Registry.CARVER, TwilightForestMod.prefix("highland_caves"), HIGHLANDCAVES);
	}
	

	//FIXME if I can go back to the old way of registering this that would be great, but this will do for now
	public static final Holder<ConfiguredWorldCarver<CaveCarverConfiguration>> TFCAVES_CONFIGURED = register(TwilightForestMod.prefix("tf_caves").toString(), TFCAVES.configured(new CaveCarverConfiguration(0.1F, UniformHeight.of(VerticalAnchor.aboveBottom(5), VerticalAnchor.absolute(-5)), ConstantFloat.of(0.6F), VerticalAnchor.bottom(), CarverDebugSettings.of(false, Blocks.GLASS.defaultBlockState(), Blocks.BLUE_STAINED_GLASS.defaultBlockState(), Blocks.RED_STAINED_GLASS.defaultBlockState(), Blocks.RED_WOOL.defaultBlockState()), ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), ConstantFloat.of(-0.7F))));
	public static final Holder<ConfiguredWorldCarver<CaveCarverConfiguration>> HIGHLANDCAVES_CONFIGURED = register(TwilightForestMod.prefix("highland_caves").toString(), HIGHLANDCAVES.configured(new CaveCarverConfiguration(0.1F, BiasedToBottomHeight.of(VerticalAnchor.aboveBottom(5), VerticalAnchor.absolute(65), 48), ConstantFloat.of(0.75F), VerticalAnchor.bottom(), CarverDebugSettings.of(false, Blocks.GLASS.defaultBlockState(), Blocks.BLUE_STAINED_GLASS.defaultBlockState(), Blocks.RED_STAINED_GLASS.defaultBlockState(), Blocks.RED_WOOL.defaultBlockState()), ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), ConstantFloat.of(-0.7F))));

//	public static void registerConfigurations(Registry<ConfiguredWorldCarver<?>> registry) {
//		Registry.register(registry, TwilightForestMod.prefix("tf_caves"), ConfiguredWorldCarvers.TFCAVES_CONFIGURED.value());
//		Registry.register(registry, TwilightForestMod.prefix("highland_caves"), ConfiguredWorldCarvers.HIGHLANDCAVES_CONFIGURED.value());
//	}

	private static <WC extends CarverConfiguration> Holder<ConfiguredWorldCarver<WC>> register(String name, ConfiguredWorldCarver<WC> carver) {
		return BuiltinRegistries.registerExact(BuiltinRegistries.CONFIGURED_CARVER, name, carver);
	}
}
