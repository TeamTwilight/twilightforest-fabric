package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import twilightforest.TwilightForestMod;

public class TFBannerPatterns {
	public static final LazyRegistrar<BannerPattern> BANNER_PATTERNS = LazyRegistrar.create(Registries.BANNER_PATTERN, TwilightForestMod.ID);

	public static final RegistryObject<BannerPattern> NAGA = BANNER_PATTERNS.register("naga", () -> new BannerPattern("tfn"));
	public static final RegistryObject<BannerPattern> LICH = BANNER_PATTERNS.register("lich", () -> new BannerPattern("tfl"));
	public static final RegistryObject<BannerPattern> MINOSHROOM = BANNER_PATTERNS.register("minoshroom", () -> new BannerPattern("tfm"));
	public static final RegistryObject<BannerPattern> HYDRA = BANNER_PATTERNS.register("hydra", () -> new BannerPattern("tfh"));
	public static final RegistryObject<BannerPattern> KNIGHT_PHANTOM = BANNER_PATTERNS.register("knight_phantom", () -> new BannerPattern("tfp"));
	public static final RegistryObject<BannerPattern> UR_GHAST = BANNER_PATTERNS.register("ur_ghast", () -> new BannerPattern("tfg"));
	public static final RegistryObject<BannerPattern> ALPHA_YETI = BANNER_PATTERNS.register("alpha_yeti", () -> new BannerPattern("tfy"));
	public static final RegistryObject<BannerPattern> SNOW_QUEEN = BANNER_PATTERNS.register("snow_queen", () -> new BannerPattern("tfq"));
	public static final RegistryObject<BannerPattern> QUEST_RAM = BANNER_PATTERNS.register("quest_ram", () -> new BannerPattern("tfr"));

}
