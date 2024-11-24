package twilightforest.init.custom;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.data.AtlasGenerator;
import twilightforest.data.LangGenerator;
import twilightforest.entity.MagicPainting;
import twilightforest.util.MagicPaintingVariant;
import twilightforest.util.MagicPaintingVariant.Layer;

import java.util.List;
import java.util.Optional;

import static twilightforest.util.MagicPaintingVariant.Layer.OpacityModifier;
import static twilightforest.util.MagicPaintingVariant.Layer.Parallax;

public class MagicPaintingVariants {
	public static final ResourceKey<Registry<MagicPaintingVariant>> REGISTRY_KEY = ResourceKey.createRegistryKey(TwilightForestMod.namedRegistry("magic_paintings"));
	public static final LazyRegistrar<MagicPaintingVariant> MAGIC_PAINTINGS = LazyRegistrar.create(REGISTRY_KEY, TwilightForestMod.ID);
	public static final Codec<Holder<MagicPaintingVariant>> CODEC = RegistryFileCodec.create(REGISTRY_KEY, MagicPaintingVariant.CODEC, false);

	public static final ResourceKey<MagicPaintingVariant> DARKNESS = makeKey(TwilightForestMod.prefix("darkness"));
	public static final MagicPaintingVariant DARKNESS_PAINTING = new MagicPaintingVariant(64, 32, List.of(
			new Layer("background", null, null, true),
			new Layer("sky", new Parallax(Parallax.Type.VIEW_ANGLE, 0.01F, 128, 32), new OpacityModifier(OpacityModifier.Type.SINE_TIME, 0.03F, false), true),
			new Layer("terrain", null, null, false),
			new Layer("gems", null, null, true),
			new Layer("gems", null, new OpacityModifier(OpacityModifier.Type.DAY_TIME, 2.0F, true), true),
			new Layer("lightning", null, new OpacityModifier(OpacityModifier.Type.LIGHTNING, 1.0F, false), true)
	));
	public static final MagicPaintingVariant LUCID_LANDS_PAINTING = new MagicPaintingVariant(32, 32, List.of(
			new Layer("background", null, null, true),
			new Layer("clouds", new Parallax(Parallax.Type.SINE_TIME, 0.01F, 48, 32), null, true),
			new Layer("volcanic_lands", null, null, true),
			new Layer("agate_jungle", new Parallax(Parallax.Type.VIEW_ANGLE, 0.02F, 44, 32), null, true),
			new Layer("crystal_plains", new Parallax(Parallax.Type.VIEW_ANGLE, 0.025F, 58, 32), null, true)
	));
	public static final ResourceKey<MagicPaintingVariant> LUCID_LANDS = makeKey(TwilightForestMod.prefix("lucid_lands"));

	private static ResourceKey<MagicPaintingVariant> makeKey(ResourceLocation name) {
		return ResourceKey.create(REGISTRY_KEY, name);
	}

	public static void bootstrap(BootstapContext<MagicPaintingVariant> context) {
		register(context, DARKNESS, "Darkness", "???", DARKNESS_PAINTING);
		register(context, LUCID_LANDS, "Lucid Lands", "Androsa", LUCID_LANDS_PAINTING);
	}

	public static void register(BootstapContext<MagicPaintingVariant> context, ResourceKey<MagicPaintingVariant> key, String title, String author, MagicPaintingVariant variant) {
		AtlasGenerator.MAGIC_PAINTING_HELPER.put(key.location(), variant);
		LangGenerator.MAGIC_PAINTING_HELPER.put(key.location(), Pair.of(title, author));
		context.register(key, variant);
	}

	public static Optional<MagicPaintingVariant> getVariant(RegistryAccess regAccess, String id) {
		return getVariant(regAccess, new ResourceLocation(id));
	}

	public static Optional<MagicPaintingVariant> getVariant(RegistryAccess regAccess, ResourceLocation id) {
		return regAccess.registry(REGISTRY_KEY).map(reg -> reg.get(id));
	}

	public static String getVariantId(RegistryAccess regAccess, MagicPaintingVariant variant) {
		return getVariantResourceLocation(regAccess, variant).toString();
	}

	public static ResourceLocation getVariantResourceLocation(RegistryAccess regAccess, MagicPaintingVariant variant) {
		return regAccess.registry(REGISTRY_KEY).map(reg -> reg.getKey(variant)).orElse(new ResourceLocation(MagicPainting.EMPTY));
	}

	public static void registerMagicPaintings() {
		MagicPaintingVariants.MAGIC_PAINTINGS.register(MagicPaintingVariants.DARKNESS.location(), () -> MagicPaintingVariants.DARKNESS_PAINTING);
		MagicPaintingVariants.MAGIC_PAINTINGS.register(MagicPaintingVariants.LUCID_LANDS.location(), () -> MagicPaintingVariants.LUCID_LANDS_PAINTING);
	}
}
