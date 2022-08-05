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

public record DwarfRabbitVariant(ResourceLocation texture) {

	public static final Registry<DwarfRabbitVariant> DWARF_RABBIT_REGISTRY = FabricRegistryBuilder
			.createSimple(DwarfRabbitVariant.class, TwilightForestMod.prefix("dwarf_rabbit_variant"))
			.attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final ResourceKey<Registry<DwarfRabbitVariant>> DWARF_RABBIT_TYPE_KEY = (ResourceKey<Registry<DwarfRabbitVariant>>) DWARF_RABBIT_REGISTRY.key();
	public static final LazyRegistrar<DwarfRabbitVariant> DWARF_RABBITS = LazyRegistrar.create(DWARF_RABBIT_TYPE_KEY, TwilightForestMod.ID);

	public static final RegistryObject<DwarfRabbitVariant> BROWN = DWARF_RABBITS.register("brown", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnybrown.png")));
	public static final RegistryObject<DwarfRabbitVariant> DUTCH = DWARF_RABBITS.register("dutch", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnydutch.png")));
	public static final RegistryObject<DwarfRabbitVariant> WHITE = DWARF_RABBITS.register("white", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnywhite.png")));

	public static DwarfRabbitVariant getRandomVariant(RandomSource random) {
		return DwarfRabbitVariant.DWARF_RABBIT_REGISTRY.getRandom(random).get().value();
	}

	public static Optional<DwarfRabbitVariant> getVariant(String id) {
		return Optional.ofNullable(DWARF_RABBIT_REGISTRY.get(new ResourceLocation(id)));
	}

	public static String getVariantId(DwarfRabbitVariant variant) {
		return Objects.requireNonNull(DWARF_RABBIT_REGISTRY.getKey(variant)).toString();
	}
}
