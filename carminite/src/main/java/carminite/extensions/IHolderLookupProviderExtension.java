package carminite.extensions;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

public interface IHolderLookupProviderExtension {
	private HolderLookup.Provider self() {
		return (HolderLookup.Provider) this;
	}

	default <T> Holder<T> carminite$holderOrThrow(ResourceKey<T> key) {
		return this.self().lookupOrThrow(key.registryKey()).getOrThrow(key);
	}

	default <T> Optional<Holder.Reference<T>> carminite$holder(ResourceKey<T> key) {
		Optional<? extends HolderLookup.RegistryLookup<T>> registry = this.self().lookup(key.registryKey());
		return registry.flatMap(tRegistryLookup -> tRegistryLookup.get(key));
	}
}