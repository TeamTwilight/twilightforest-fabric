package twilightforest.init;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class TFRegistryObject<T> implements Holder<T>, Supplier<T> {
    private final T value;
    private final ResourceKey<T> key;

    public TFRegistryObject(T value) {
        this(value, null);
    }

    public TFRegistryObject(T value, ResourceKey<T> key) {
        this.value = Objects.requireNonNull(value, "value");
        this.key = key;
    }

    public static <T> TFRegistryObject<T> direct(T value) {
        return new TFRegistryObject<>(value);
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public T value() {
        return this.value;
    }

    @Override
    public boolean isBound() {
        return true;
    }

    @Override
    public boolean is(ResourceLocation location) {
        return this.key != null && this.key.location().equals(location);
    }

    @Override
    public boolean is(ResourceKey<T> key) {
        return this.key != null && this.key.equals(key);
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return this.key != null && predicate.test(this.key);
    }

    @Override
    public boolean is(TagKey<T> tagKey) {
        return false;
    }

    @Override
    public boolean is(Holder<T> holder) {
        return holder.value() == this.value;
    }

    @Override
    public Stream<TagKey<T>> tags() {
        return Stream.empty();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return this.key == null ? Either.right(this.value) : Either.left(this.key);
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return Optional.ofNullable(this.key);
    }

    @Override
    public Kind kind() {
        return this.key == null ? Kind.DIRECT : Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return true;
    }
}
