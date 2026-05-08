package twilightforest.config;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigValue<T> {
	private final Supplier<T> getter;
	private final Consumer<T> setter;

	public ConfigValue(Supplier<T> getter, Consumer<T> setter) {
		this.getter = getter;
		this.setter = setter;
	}

	public T get() {
		return this.getter.get();
	}

	public void set(T value) {
		this.setter.accept(value);
	}

	public static final class BooleanValue extends ConfigValue<Boolean> {
		public BooleanValue(Supplier<Boolean> getter, Consumer<Boolean> setter) {
			super(getter, setter);
		}
	}

	public static final class IntValue extends ConfigValue<Integer> {
		public IntValue(Supplier<Integer> getter, Consumer<Integer> setter) {
			super(getter, setter);
		}
	}

	public static final class DoubleValue extends ConfigValue<Double> {
		public DoubleValue(Supplier<Double> getter, Consumer<Double> setter) {
			super(getter, setter);
		}
	}

	public static final class EnumValue<T extends Enum<T>> extends ConfigValue<T> {
		public EnumValue(Supplier<T> getter, Consumer<T> setter) {
			super(getter, setter);
		}
	}
}
