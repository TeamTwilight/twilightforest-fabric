package net.neoforged.neoforge.common.util;

import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private T value;
    private boolean initialized;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    @Override
    public T get() {
        if (!this.initialized) {
            this.value = this.supplier.get();
            this.initialized = true;
        }
        return this.value;
    }
}
