package carminite.datamaps;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SimpleDataMap<R, T> {
	static final List<SimpleDataMap<?, ?>> ALL = new ArrayList<>();

	private final Identifier id;
	private final ResourceKey<? extends Registry<R>> registryKey;
	private final Codec<T> codec;
	private final boolean synced;
	private Map<Identifier, T> entries = Map.of();

	private SimpleDataMap(Identifier id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec, boolean synced) {
		this.id = id;
		this.registryKey = registryKey;
		this.codec = codec;
		this.synced = synced;
		ALL.add(this);
	}

	public Identifier id() { return id; }
	public ResourceKey<? extends Registry<R>> registryKey() { return registryKey; }
	public Codec<T> codec() { return codec; }
	public boolean synced() { return synced; }

	@Nullable
	public T get(Identifier entryId) {
		return entries.get(entryId);
	}

	@Nullable
	public T get(Holder<R> holder) {
		return holder.unwrapKey().map(k -> entries.get(k.identifier())).orElse(null);
	}

	void load(Map<Identifier, T> newEntries) {
		this.entries = newEntries;
	}

	public static <R, T> Builder<R, T> builder(Identifier id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec) {
		return new Builder<>(id, registryKey, codec);
	}

	public static final class Builder<R, T> {
		private final Identifier id;
		private final ResourceKey<? extends Registry<R>> registryKey;
		private final Codec<T> codec;
		private boolean synced = false;

		private Builder(Identifier id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec) {
			this.id = id;
			this.registryKey = registryKey;
			this.codec = codec;
		}

		public Builder<R, T> synced(Codec<T> networkCodec, boolean mandatory) {
			this.synced = true;
			return this;
		}

		public SimpleDataMap<R, T> build() {
			return new SimpleDataMap<>(id, registryKey, codec, synced);
		}
	}
}