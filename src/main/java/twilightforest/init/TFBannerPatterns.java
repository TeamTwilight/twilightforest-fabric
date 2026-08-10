package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import twilightforest.TFMain;

public class TFBannerPatterns {
	public static final ResourceKey<BannerPattern> NAGA = register("naga");
	public static final ResourceKey<BannerPattern> LICH = register("lich");
	public static final ResourceKey<BannerPattern> MINOSHROOM = register("minoshroom");
	public static final ResourceKey<BannerPattern> HYDRA = register("hydra");
	public static final ResourceKey<BannerPattern> KNIGHT_PHANTOM = register("knight_phantom");
	public static final ResourceKey<BannerPattern> UR_GHAST = register("ur_ghast");
	public static final ResourceKey<BannerPattern> ALPHA_YETI = register("alpha_yeti");
	public static final ResourceKey<BannerPattern> SNOW_QUEEN = register("snow_queen");
	public static final ResourceKey<BannerPattern> QUESTING_RAM = register("questing_ram");

	private static ResourceKey<BannerPattern> register(String name) {
		return ResourceKey.create(Registries.BANNER_PATTERN, TFMain.prefix(name));
	}

	public static void bootstrap(BootstrapContext<BannerPattern> context) {
		context.register(NAGA, new BannerPattern(TFMain.prefix("naga"), "block.minecraft.banner.twilightforest.naga"));
		context.register(LICH, new BannerPattern(TFMain.prefix("lich"), "block.minecraft.banner.twilightforest.lich"));
		context.register(MINOSHROOM, new BannerPattern(TFMain.prefix("minoshroom"), "block.minecraft.banner.twilightforest.minoshroom"));
		context.register(HYDRA, new BannerPattern(TFMain.prefix("hydra"), "block.minecraft.banner.twilightforest.hydra"));
		context.register(KNIGHT_PHANTOM, new BannerPattern(TFMain.prefix("knight_phantom"), "block.minecraft.banner.twilightforest.knight_phantom"));
		context.register(UR_GHAST, new BannerPattern(TFMain.prefix("ur_ghast"), "block.minecraft.banner.twilightforest.ur_ghast"));
		context.register(ALPHA_YETI, new BannerPattern(TFMain.prefix("alpha_yeti"), "block.minecraft.banner.twilightforest.alpha_yeti"));
		context.register(SNOW_QUEEN, new BannerPattern(TFMain.prefix("snow_queen"), "block.minecraft.banner.twilightforest.snow_queen"));
		context.register(QUESTING_RAM, new BannerPattern(TFMain.prefix("quest_ram"), "block.minecraft.banner.twilightforest.quest_ram"));
	}
}
