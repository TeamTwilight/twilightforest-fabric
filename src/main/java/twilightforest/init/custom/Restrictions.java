package twilightforest.init.custom;

import com.mojang.serialization.Codec;
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

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
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

    private static ResourceKey<Restriction> makeKey(ResourceLocation name) {
        return ResourceKey.create(RESTRICTION_KEY, name);
    }

    public static void bootstrap(BootstapContext<Restriction> context) {
        context.register(DARK_FOREST, new Restriction(TFStructures.KNIGHT_STRONGHOLD, Enforcement.DARKNESS.getKey(), 0.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(DARK_FOREST_CENTER, new Restriction(TFStructures.DARK_TOWER, Enforcement.DARKNESS.getKey(), 0.0F, asStack(TFBlocks.KNIGHT_PHANTOM_TROPHY), List.of(TwilightForestMod.prefix("progress_knights"))));
        context.register(FINAL_PLATEAU, new Restriction(TFStructures.FINAL_CASTLE, Enforcement.ACID_RAIN.getKey(), 1.5F, asStack(TFItems.LAMP_OF_CINDERS), List.of(TwilightForestMod.prefix("progress_troll"))));
        context.register(FIRE_SWAMP, new Restriction(TFStructures.HYDRA_LAIR, Enforcement.FIRE.getKey(), 8.0F, asStack(TFItems.MEEF_STROGANOFF), List.of(TwilightForestMod.prefix("progress_labyrinth"))));
        context.register(GLACIER, new Restriction(TFStructures.AURORA_PALACE, Enforcement.FROST.getKey(), 3.0F, asStack(TFItems.ALPHA_YETI_FUR), List.of(TwilightForestMod.prefix("progress_yeti"))));
        context.register(HIGHLANDS, new Restriction(TFStructures.TROLL_CAVE, Enforcement.ACID_RAIN.getKey(), 0.5F, asStack(TFBlocks.UBEROUS_SOIL), List.of(TwilightForestMod.prefix("progress_merge"))));
        context.register(SNOWY_FOREST, new Restriction(TFStructures.YETI_CAVE, Enforcement.FROST.getKey(), 2.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(SWAMP, new Restriction(TFStructures.LABYRINTH, Enforcement.HUNGER.getKey(), 1.0F, asStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE), List.of(TwilightForestMod.prefix("progress_lich"))));
        context.register(THORNLANDS, new Restriction(TFStructures.FINAL_CASTLE, Enforcement.ACID_RAIN.getKey(), 1.0F, asStack(TFItems.LAMP_OF_CINDERS), List.of(TwilightForestMod.prefix("progress_troll"))));
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
}
