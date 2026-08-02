package io.github.fabricators_of_create.porting_lib.entity.data;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 // PortingLib stub - 已通过 Fabric API AttachmentType 实现
 * Implements Fabric API's AttachmentType for compatibility.
 */
public class AttachmentType<T> implements net.fabricmc.fabric.api.attachment.v1.AttachmentType<T> {
	private final Supplier<T> defaultValue;
	private final Codec<T> codec;
	private final StreamCodec<?, T> streamCodec;
	private final boolean copyOnDeath;
	private ResourceLocation identifier;

	private AttachmentType(Supplier<T> defaultValue, Codec<T> codec, StreamCodec<?, T> streamCodec, boolean copyOnDeath) {
		this.defaultValue = defaultValue;
		this.codec = codec;
		this.streamCodec = streamCodec;
		this.copyOnDeath = copyOnDeath;
	}

	public static <T> Builder<T> builder(Supplier<T> defaultValue) {
		return new Builder<>(defaultValue);
	}

	public T getDefaultValue() {
		return defaultValue.get();
	}

	@Override
	public ResourceLocation identifier() {
		return this.identifier;
	}

	@Override
	public boolean copyOnDeath() {
		return this.copyOnDeath;
	}

	@Override
	public Codec<T> persistenceCodec() {
		return this.codec;
	}

	@Override
	public boolean isSynced() {
		return this.streamCodec != null;
	}

	@Override
	public Supplier<T> initializer() {
		return this.defaultValue;
	}

	public void setIdentifier(ResourceLocation identifier) {
		this.identifier = identifier;
	}

	public static class Builder<T> {
		private final Supplier<T> defaultValue;
		private Codec<T> codec;
		private StreamCodec<?, T> streamCodec;
		private boolean copyOnDeath;

		private Builder(Supplier<T> defaultValue) {
			this.defaultValue = defaultValue;
		}

		public Builder<T> serialize(Codec<T> codec) {
			this.codec = codec;
			return this;
		}

		public Builder<T> sync(StreamCodec<?, T> streamCodec) {
			this.streamCodec = streamCodec;
			return this;
		}

		public Builder<T> copyOnDeath() {
			this.copyOnDeath = true;
			return this;
		}

		public AttachmentType<T> build() {
			return new AttachmentType<>(defaultValue, codec, streamCodec, copyOnDeath);
		}
	}
}