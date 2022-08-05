package twilightforest.init.custom;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import twilightforest.TwilightForestMod;

import java.util.Objects;
import java.util.Optional;

public record TinyBirdVariant(ResourceLocation texture) {
	public static final Registry<TinyBirdVariant> TINY_BIRD_REGISTRY = FabricRegistryBuilder
			.createSimple(TinyBirdVariant.class, TwilightForestMod.prefix("tiny_bird_variant"))
			.attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final ResourceKey<Registry<TinyBirdVariant>> TINY_BIRD_TYPE_KEY = (ResourceKey<Registry<TinyBirdVariant>>) TINY_BIRD_REGISTRY.key();
	public static final LazyRegistrar<TinyBirdVariant> TINY_BIRDS = LazyRegistrar.create(TINY_BIRD_TYPE_KEY, TwilightForestMod.ID);

	public static final RegistryObject<TinyBirdVariant> BLUE = TINY_BIRDS.register("blue", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdblue.png")));
	public static final RegistryObject<TinyBirdVariant> BROWN = TINY_BIRDS.register("brown", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdbrown.png")));
	public static final RegistryObject<TinyBirdVariant> GOLD = TINY_BIRDS.register("gold", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdgold.png")));
	public static final RegistryObject<TinyBirdVariant> RED = TINY_BIRDS.register("red", () -> new TinyBirdVariant(TwilightForestMod.getModelTexture("tinybirdred.png")));

	public static TinyBirdVariant getRandomVariant(RandomSource random) {
		return TINY_BIRD_REGISTRY.getRandom(random).get().value();
	}

	public static Optional<TinyBirdVariant> getVariant(String id) {
		return Optional.ofNullable(TINY_BIRD_REGISTRY.get(new ResourceLocation(id)));
	}

	public static String getVariantId(TinyBirdVariant variant) {
		return Objects.requireNonNull(TINY_BIRD_REGISTRY.getKey(variant)).toString();
	}
}
