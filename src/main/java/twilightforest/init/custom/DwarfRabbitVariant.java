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

public record DwarfRabbitVariant(ResourceLocation texture) {

	public static final ResourceKey<Registry<DwarfRabbitVariant>> DWARF_RABBIT_TYPE_KEY = ResourceKey.createRegistryKey(TwilightForestMod.prefix("dwarf_rabbit_variant"));
	public static final DeferredRegister<DwarfRabbitVariant> DWARF_RABBITS = DeferredRegister.create(DWARF_RABBIT_TYPE_KEY, TwilightForestMod.ID);
	public static final Supplier<IForgeRegistry<DwarfRabbitVariant>> DWARF_RABBIT_REGISTRY = DWARF_RABBITS.makeRegistry(RegistryBuilder::new);

	public static final RegistryObject<DwarfRabbitVariant> BROWN = DWARF_RABBITS.register("brown", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnybrown.png")));
	public static final RegistryObject<DwarfRabbitVariant> DUTCH = DWARF_RABBITS.register("dutch", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnydutch.png")));
	public static final RegistryObject<DwarfRabbitVariant> WHITE = DWARF_RABBITS.register("white", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnywhite.png")));

	public static DwarfRabbitVariant getRandomVariant(RandomSource random) {
		return DwarfRabbitVariant.DWARF_RABBIT_REGISTRY.get().getValues().toArray(DwarfRabbitVariant[]::new)[random.nextInt(DwarfRabbitVariant.DWARF_RABBIT_REGISTRY.get().getValues().size())];
	}

	public static Optional<DwarfRabbitVariant> getVariant(String id) {
		return Optional.ofNullable(DWARF_RABBIT_REGISTRY.get().getValue(new ResourceLocation(id)));
	}

	public static String getVariantId(DwarfRabbitVariant variant) {
		return Objects.requireNonNull(DWARF_RABBIT_REGISTRY.get().getKey(variant)).toString();
	}
}
