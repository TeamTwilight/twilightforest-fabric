package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFStructures;
import twilightforest.util.PlayerHelper;
import twilightforest.util.Restriction;

import java.util.List;
import java.util.Optional;

public class Restrictions {
	public static final ResourceKey<Registry<Restriction>> RESTRICTION_KEY = ResourceKey.createRegistryKey(TwilightForestMod.namedRegistry("restrictions"));
	public static final LazyRegistrar<Restriction> RESTRICTIONS = LazyRegistrar.create(RESTRICTION_KEY, TwilightForestMod.ID);
	public static final Codec<Holder<Restriction>> CODEC = RegistryFileCodec.create(Restrictions.RESTRICTION_KEY, Restriction.CODEC, false);

	public static final ResourceKey<Restriction> DARK_FOREST = makeKey(TFBiomes.DARK_FOREST.location());
	public static final ResourceKey<Restriction> DARK_FOREST_CENTER = makeKey(TFBiomes.DARK_FOREST_CENTER.location());
	public static final ResourceKey<Restriction> FINAL_PLATEAU = makeKey(TFBiomes.FINAL_PLATEAU.location());
	public static final ResourceKey<Restriction> FIRE_SWAMP = makeKey(TFBiomes.FIRE_SWAMP.location());
	public static final ResourceKey<Restriction> GLACIER = makeKey(TFBiomes.GLACIER.location());
	public static final ResourceKey<Restriction> HIGHLANDS = makeKey(TFBiomes.HIGHLANDS.location());
	public static final ResourceKey<Restriction> SNOWY_FOREST = makeKey(TFBiomes.SNOWY_FOREST.location());
	public static final ResourceKey<Restriction> SWAMP = makeKey(TFBiomes.SWAMP.location());
	public static final ResourceKey<Restriction> THORNLANDS = makeKey(TFBiomes.THORNLANDS.location());

	public static final Restriction DARK_FOREST_RESTRICTION = new Restriction(
			TFStructures.KNIGHT_STRONGHOLD,
			Enforcement.DARKNESS.getKey(),
			0.0F,
			asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE),
			List.of(TwilightForestMod.prefix("progress_lich"))
	);
	public static final Restriction DARK_FOREST_CENTER_RESTRICTION = new Restriction(
			TFStructures.DARK_TOWER,
			Enforcement.DARKNESS.getKey(),
			0.0F,
			asStack(TFBlocks.KNIGHT_PHANTOM_TROPHY),
			List.of(TwilightForestMod.prefix("progress_knights"))
	);
	public static final Restriction FINAL_PLATEAU_RESTRICTION = new Restriction(
			TFStructures.FINAL_CASTLE,
			Enforcement.ACID_RAIN.getKey(),
			1.5F,
			asStack(TFItems.LAMP_OF_CINDERS),
			List.of(TwilightForestMod.prefix("progress_troll"))
	);
	public static final Restriction FIRE_SWAMP_RESTRICTION = new Restriction(
			TFStructures.HYDRA_LAIR,
			Enforcement.FIRE.getKey(),
			8.0F,
			asStack(TFItems.MEEF_STROGANOFF),
			List.of(TwilightForestMod.prefix("progress_labyrinth"))
	);
	public static final Restriction GLACIER_RESTRICTION = new Restriction(
			TFStructures.AURORA_PALACE,
			Enforcement.FROST.getKey(),
			3.0F,
			asStack(TFItems.ALPHA_YETI_FUR),
			List.of(TwilightForestMod.prefix("progress_yeti"))
	);
	public static final Restriction HIGHLANDS_RESTRICTION = new Restriction(
			TFStructures.TROLL_CAVE,
			Enforcement.ACID_RAIN.getKey(),
			0.5F,
			asStack(TFBlocks.UBEROUS_SOIL),
			List.of(TwilightForestMod.prefix("progress_merge"))
	);
	public static final Restriction SNOWY_FOREST_RESTRICTION = new Restriction(
			TFStructures.YETI_CAVE,
			Enforcement.FROST.getKey(),
			2.0F,
			asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE),
			List.of(TwilightForestMod.prefix("progress_lich"))
	);
	public static final Restriction SWAMP_RESTRICTION = new Restriction(
			TFStructures.LABYRINTH,
			Enforcement.HUNGER.getKey(),
			1.0F,
			asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE),
			List.of(TwilightForestMod.prefix("progress_lich"))
	);
	public static final Restriction THORNLANDS_RESTRICTION = new Restriction(
			TFStructures.FINAL_CASTLE,
			Enforcement.ACID_RAIN.getKey(),
			1.0F,
			asStack(TFItems.LAMP_OF_CINDERS),
			List.of(TwilightForestMod.prefix("progress_troll"))
	);

	private static ResourceKey<Restriction> makeKey(ResourceLocation name) {
		return ResourceKey.create(RESTRICTION_KEY, name);
	}

	public static void bootstrap(BootstapContext<Restriction> context) {
		context.register(DARK_FOREST, DARK_FOREST_RESTRICTION);
		context.register(DARK_FOREST_CENTER, DARK_FOREST_CENTER_RESTRICTION);
		context.register(FINAL_PLATEAU, FINAL_PLATEAU_RESTRICTION);
		context.register(FIRE_SWAMP, FIRE_SWAMP_RESTRICTION);
		context.register(GLACIER, GLACIER_RESTRICTION);
		context.register(HIGHLANDS, HIGHLANDS_RESTRICTION);
		context.register(SNOWY_FOREST, SNOWY_FOREST_RESTRICTION);
		context.register(SWAMP, SWAMP_RESTRICTION);
		context.register(THORNLANDS, THORNLANDS_RESTRICTION);
	}

	public static ItemStack asStack(RegistryObject<?> item) {
		return asStack(item.get() instanceof ItemLike itemLike ? itemLike : TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get());
	}

	public static ItemStack asStack(ItemLike itemLike) {
		return new ItemStack(itemLike);
	}

	public static Optional<Restriction> getRestrictionForBiome(Biome biome, Entity entity) {
		if (entity instanceof Player player) {
			RegistryAccess access = entity.level().registryAccess();
			ResourceLocation biomeLocation = access.registryOrThrow(Registries.BIOME).getKey(biome);
			if (biomeLocation != null) {
				Restriction restrictions = access.registryOrThrow(RESTRICTION_KEY).get(biomeLocation);
				if (restrictions != null && !PlayerHelper.doesPlayerHaveRequiredAdvancements(player, restrictions.advancements())) {
					return Optional.of(restrictions);
				}
			}
		}
		return Optional.empty();
	}

	public static boolean isBiomeSafeFor(Biome biome, Entity entity) {
		return getRestrictionForBiome(biome, entity).isEmpty();
	}

	public static void registerRestrictions() {
		Restrictions.RESTRICTIONS.register(Restrictions.DARK_FOREST.location(), () -> Restrictions.DARK_FOREST_RESTRICTION);
		Restrictions.RESTRICTIONS.register(Restrictions.DARK_FOREST_CENTER.location(), () -> Restrictions.DARK_FOREST_CENTER_RESTRICTION);
		Restrictions.RESTRICTIONS.register(Restrictions.FINAL_PLATEAU.location(), () -> Restrictions.FINAL_PLATEAU_RESTRICTION);
		Restrictions.RESTRICTIONS.register(Restrictions.FIRE_SWAMP.location(), () -> Restrictions.FIRE_SWAMP_RESTRICTION);
		Restrictions.RESTRICTIONS.register(Restrictions.GLACIER.location(), () -> Restrictions.GLACIER_RESTRICTION);
		Restrictions.RESTRICTIONS.register(Restrictions.HIGHLANDS.location(), () -> Restrictions.HIGHLANDS_RESTRICTION);
		Restrictions.RESTRICTIONS.register(Restrictions.SNOWY_FOREST.location(), () -> Restrictions.SNOWY_FOREST_RESTRICTION);
		Restrictions.RESTRICTIONS.register(Restrictions.SWAMP.location(), () -> Restrictions.SWAMP_RESTRICTION);
		Restrictions.RESTRICTIONS.register(Restrictions.THORNLANDS.location(), () -> Restrictions.THORNLANDS_RESTRICTION);
	}
}
