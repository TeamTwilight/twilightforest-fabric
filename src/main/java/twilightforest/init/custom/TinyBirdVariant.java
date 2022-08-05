package twilightforest.init.custom;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public record TinyBirdVariant(ResourceLocation texture) {
	public static final ResourceKey<Registry<TinyBirdVariant>> TINY_BIRD_TYPE_KEY = ResourceKey.createRegistryKey(TwilightForestMod.prefix("tiny_bird_variant"));
	public static final DeferredRegister<TinyBirdVariant> TINY_BIRDS = DeferredRegister.create(TINY_BIRD_TYPE_KEY, TwilightForestMod.ID);
	public static final Supplier<IForgeRegistry<TinyBirdVariant>> TINY_BIRD_REGISTRY = TINY_BIRDS.makeRegistry(RegistryBuilder::new);

	public static final RegistryObject<TinyBirdVariant> BLUE = TINY_BIRDS.register("blue", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdblue.png")));
	public static final RegistryObject<TinyBirdVariant> BROWN = TINY_BIRDS.register("brown", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdbrown.png")));
	public static final RegistryObject<TinyBirdVariant> GOLD = TINY_BIRDS.register("gold", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdgold.png")));
	public static final RegistryObject<TinyBirdVariant> RED = TINY_BIRDS.register("red", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdred.png")));

	public static TinyBirdVariant getRandomVariant(RandomSource random) {
		return TINY_BIRD_REGISTRY.get().getValues().toArray(TinyBirdVariant[]::new)[random.nextInt(TINY_BIRD_REGISTRY.get().getValues().size())];
	}

	public static Optional<TinyBirdVariant> getVariant(String id) {
		return Optional.ofNullable(TINY_BIRD_REGISTRY.get().getValue(new ResourceLocation(id)));
	}

	public static String getVariantId(TinyBirdVariant variant) {
		return Objects.requireNonNull(TINY_BIRD_REGISTRY.get().getKey(variant)).toString();
	}
}
