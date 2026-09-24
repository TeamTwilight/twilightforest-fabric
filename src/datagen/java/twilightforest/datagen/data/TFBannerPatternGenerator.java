package twilightforest.datagen.data;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.entity.BannerPattern;
import twilightforest.TFCommon;
import twilightforest.init.TFBannerPatterns;

public class TFBannerPatternGenerator {
	public static void bootstrap(BootstrapContext<BannerPattern> context) {
		TFCommon.LOGGER.info("Bootstrap called for banner patterns...");
		context.register(TFBannerPatterns.NAGA, new BannerPattern(TFCommon.prefix("naga"), "block.minecraft.banner.twilightforest.naga"));
		context.register(TFBannerPatterns.LICH, new BannerPattern(TFCommon.prefix("lich"), "block.minecraft.banner.twilightforest.lich"));
		context.register(TFBannerPatterns.MINOSHROOM, new BannerPattern(TFCommon.prefix("minoshroom"), "block.minecraft.banner.twilightforest.minoshroom"));
		context.register(TFBannerPatterns.HYDRA, new BannerPattern(TFCommon.prefix("hydra"), "block.minecraft.banner.twilightforest.hydra"));
		context.register(TFBannerPatterns.KNIGHT_PHANTOM, new BannerPattern(TFCommon.prefix("knight_phantom"), "block.minecraft.banner.twilightforest.knight_phantom"));
		context.register(TFBannerPatterns.UR_GHAST, new BannerPattern(TFCommon.prefix("ur_ghast"), "block.minecraft.banner.twilightforest.ur_ghast"));
		context.register(TFBannerPatterns.ALPHA_YETI, new BannerPattern(TFCommon.prefix("alpha_yeti"), "block.minecraft.banner.twilightforest.alpha_yeti"));
		context.register(TFBannerPatterns.SNOW_QUEEN, new BannerPattern(TFCommon.prefix("snow_queen"), "block.minecraft.banner.twilightforest.snow_queen"));
		context.register(TFBannerPatterns.QUESTING_RAM, new BannerPattern(TFCommon.prefix("quest_ram"), "block.minecraft.banner.twilightforest.quest_ram"));
	}
}
