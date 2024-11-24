package twilightforest.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ComparableResourceKey<T> extends ResourceKey<T> implements Comparable<ResourceKey<?>> {
	public static <T> ComparableResourceKey<T> of(ResourceKey<T> registryKey) {
		return new ComparableResourceKey<>(registryKey.registry(), registryKey.location());
	}

	protected ComparableResourceKey(ResourceLocation registryName, ResourceLocation location) {
		super(registryName, location);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return location().equals(((ResourceKey<?>) o).location()) && registry().equals(((ResourceKey<?>) o).registry());
	}

	@Override
	public int compareTo(ResourceKey<?> o) {
		int ret = this.registry().compareTo(o.registry());
		if (ret == 0) ret = this.location().compareTo(o.location());
		return ret;
	}
}
