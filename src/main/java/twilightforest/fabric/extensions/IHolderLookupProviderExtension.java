package twilightforest.fabric.extensions;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

/**
 * Injected into HolderLookup.Provider in twilightforest.classtweaker
 */
public interface IHolderLookupProviderExtension {
	private HolderLookup.Provider self() {
		return (HolderLookup.Provider) this;
	}

	default <T> Holder<T> holderOrThrow(ResourceKey<T> key) {
		return this.self().lookupOrThrow(key.registryKey()).getOrThrow(key);
	}

	default <T> Optional<Holder.Reference<T>> holder(ResourceKey<T> key) {
		Optional<? extends HolderLookup.RegistryLookup<T>> registry = this.self().lookup(key.registryKey());
		return registry.flatMap(tRegistryLookup -> tRegistryLookup.get(key));
	}
}