package twilightforest.init.custom;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import twilightforest.TwilightForestMod;

import java.util.Optional;
import java.util.stream.Stream;

public class TFRegistryAccess implements RegistryAccess.Frozen {
    Registry<? extends Registry<?>> registryOfRegistries = BuiltInRegistries.REGISTRY;

    public <T> Optional<Registry<T>> registry(ResourceKey<? extends Registry<? extends T>> registryKey) {
        Stream<RegistryEntry<?>> registries = registries();
        Optional<RegistryEntry<?>> entry = registries.filter(obj -> obj.key().equals(registryKey)).findFirst();
        return Optional.of((Registry<T>) entry.get().value());
    }

    public Stream<RegistryEntry<?>> registries() {
        var oldStream = registryOfRegistries.entrySet().stream().map(RegistryEntry::fromMapEntry);
        Stream newStream = oldStream.filter(obj -> {
            var value = obj.value();
            if (value.key().location().getNamespace().equals(TwilightForestMod.REGISTRY_NAMESPACE)) {
                return false;
            }
            return true;
        });
        return newStream;
    }

    public Frozen freeze() {
        return this;
    }
}
