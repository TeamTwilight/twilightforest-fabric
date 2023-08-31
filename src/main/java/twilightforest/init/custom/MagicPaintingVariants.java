package twilightforest.init.custom;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.data.AtlasGenerator;
import twilightforest.data.LangGenerator;
import twilightforest.entity.MagicPainting;
import twilightforest.util.MagicPaintingVariant;
import twilightforest.util.MagicPaintingVariant.Layer;

import java.util.List;
import java.util.Optional;

import static twilightforest.util.MagicPaintingVariant.Layer.*;

public class MagicPaintingVariants {
    public static final ResourceKey<Registry<MagicPaintingVariant>> REGISTRY_KEY = ResourceKey.createRegistryKey(TwilightForestMod.namedRegistry("magic_paintings"));
    public static final DeferredRegister<MagicPaintingVariant> MAGIC_PAINTINGS = DeferredRegister.create(REGISTRY_KEY, TwilightForestMod.ID);
    public static final Codec<Holder<MagicPaintingVariant>> CODEC = RegistryFileCodec.create(REGISTRY_KEY, MagicPaintingVariant.CODEC, false);

    public static final ResourceKey<MagicPaintingVariant> DARKNESS = makeKey(TwilightForestMod.prefix("darkness"));

    private static ResourceKey<MagicPaintingVariant> makeKey(ResourceLocation name) {
        return ResourceKey.create(REGISTRY_KEY, name);
    }

    public static void bootstrap(BootstapContext<MagicPaintingVariant> context) {
        register(context, DARKNESS, "Darkness", "???", 64, 32, List.of(
                new Layer("background", null, null, true),
                new Layer("sky", new Parallax(Parallax.Type.VIEW_ANGLE, 0.01F, 128, 32), new OpacityModifier(OpacityModifier.Type.SINE_TIME, 0.03F, false), true),
                new Layer("terrain", null, null, false),
                new Layer("gems", null, null, true),
                new Layer("gems", null, new OpacityModifier(OpacityModifier.Type.DAY_TIME, 2.0F, true), true),
                new Layer("lightning", null, new OpacityModifier(OpacityModifier.Type.LIGHTNING, 1.0F, false), true)
        ));
    }

    public static void register(BootstapContext<MagicPaintingVariant> context, ResourceKey<MagicPaintingVariant> key, String title, String author, int width, int height, List<Layer> layers) {
        MagicPaintingVariant variant = new MagicPaintingVariant(width, height, layers);
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
}
