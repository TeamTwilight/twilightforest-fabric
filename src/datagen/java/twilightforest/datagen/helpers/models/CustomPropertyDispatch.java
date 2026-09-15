package twilightforest.datagen.helpers.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.data.models.blockstates.PropertyValueList;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class CustomPropertyDispatch<V> {
	private final Map<PropertyValueList, V> values = new HashMap<>();

	protected void putValue(PropertyValueList key, V value) {
		V previous = values.put(key, value);
		if (previous != null) {
			throw new IllegalStateException("Value " + key + " is already defined");
		}
	}

	public Map<PropertyValueList, V> getEntries() {
		verifyComplete();
		return Map.copyOf(values);
	}

	private void verifyComplete() {
		List<Property<?>> properties = getDefinedProperties();
		var valuesToCover = Stream.of(PropertyValueList.EMPTY);

		for (Property<?> property : properties) {
			valuesToCover = valuesToCover.flatMap(current ->
				property.getAllValues().map(current::extend)
			);
		}

		List<PropertyValueList> undefined = valuesToCover
			.filter(key -> !values.containsKey(key))
			.toList();

		if (!undefined.isEmpty()) {
			throw new IllegalStateException("Missing definition for properties: " + undefined);
		}
	}

	abstract List<Property<?>> getDefinedProperties();

	public static <T1 extends Comparable<T1>>
	C1<T1> initial(Property<T1> property) {
		return new C1<>(property);
	}

	public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>>
	C2<T1, T2> initial(Property<T1> property1, Property<T2> property2) {
		return new C2<>(property1, property2);
	}

	public static <
		T1 extends Comparable<T1>,
		T2 extends Comparable<T2>,
		T3 extends Comparable<T3>
		>
	C3<T1, T2, T3> initial(
		Property<T1> property1,
		Property<T2> property2,
		Property<T3> property3
	) {
		return new C3<>(property1, property2, property3);
	}

	public static <
		T1 extends Comparable<T1>,
		T2 extends Comparable<T2>,
		T3 extends Comparable<T3>,
		T4 extends Comparable<T4>
		>
	C4<T1, T2, T3, T4> initial(
		Property<T1> property1,
		Property<T2> property2,
		Property<T3> property3,
		Property<T4> property4
	) {
		return new C4<>(property1, property2, property3, property4);
	}

	public static <
		T1 extends Comparable<T1>,
		T2 extends Comparable<T2>,
		T3 extends Comparable<T3>,
		T4 extends Comparable<T4>,
		T5 extends Comparable<T5>
		>
	C5<T1, T2, T3, T4, T5> initial(
		Property<T1> property1,
		Property<T2> property2,
		Property<T3> property3,
		Property<T4> property4,
		Property<T5> property5
	) {
		return new C5<>(
			property1,
			property2,
			property3,
			property4,
			property5
		);
	}

	public static class C1<T1 extends Comparable<T1>>
		extends CustomPropertyDispatch<BlockStateModel.Unbaked> {

		private final Property<T1> property1;

		private C1(Property<T1> property1) {
			this.property1 = property1;
		}

		@Override
		List<Property<?>> getDefinedProperties() {
			return List.of(property1);
		}

		public C1<T1> select(
			T1 value1,
			BlockStateModel.Unbaked model
		) {
			putValue(
				PropertyValueList.of(
					property1.value(value1)),
				model
			);

			return this;
		}

		public CustomPropertyDispatch<BlockStateModel.Unbaked> generate(
			Function<T1, BlockStateModel.Unbaked> generator
		) {
			property1.getPossibleValues().forEach(
				value -> select(value, generator.apply(value))
			);

			return this;
		}
	}

	public static class C2<
		T1 extends Comparable<T1>,
		T2 extends Comparable<T2>
		> extends CustomPropertyDispatch<BlockStateModel.Unbaked> {

		private final Property<T1> property1;
		private final Property<T2> property2;

		private C2(
			Property<T1> property1,
			Property<T2> property2
		) {
			this.property1 = property1;
			this.property2 = property2;
		}

		@Override
		List<Property<?>> getDefinedProperties() {
			return List.of(property1, property2);
		}

		public C2<T1, T2> select(
			T1 value1,
			T2 value2,
			BlockStateModel.Unbaked model
		) {
			putValue(
				PropertyValueList.of(
					property1.value(value1),
					property2.value(value2)),
				model
			);

			return this;
		}

		public CustomPropertyDispatch<BlockStateModel.Unbaked> generate(
			BiFunction<T1, T2, BlockStateModel.Unbaked> generator
		) {
			property1.getPossibleValues().forEach(value1 ->
				property2.getPossibleValues().forEach(value2 ->
					select(
						value1,
						value2,
						generator.apply(value1, value2)
					)
				)
			);

			return this;
		}
	}

	public static class C3<
		T1 extends Comparable<T1>,
		T2 extends Comparable<T2>,
		T3 extends Comparable<T3>
		> extends CustomPropertyDispatch<BlockStateModel.Unbaked> {

		private final Property<T1> property1;
		private final Property<T2> property2;
		private final Property<T3> property3;

		private C3(
			Property<T1> property1,
			Property<T2> property2,
			Property<T3> property3
		) {
			this.property1 = property1;
			this.property2 = property2;
			this.property3 = property3;
		}

		@Override
		List<Property<?>> getDefinedProperties() {
			return List.of(property1, property2, property3);
		}

		public C3<T1, T2, T3> select(
			T1 value1,
			T2 value2,
			T3 value3,
			BlockStateModel.Unbaked model
		) {
			putValue(
				PropertyValueList.of(
					property1.value(value1),
					property2.value(value2),
					property3.value(value3)),
				model
			);

			return this;
		}

		public CustomPropertyDispatch<BlockStateModel.Unbaked> generate(
			Function3<T1, T2, T3, BlockStateModel.Unbaked> generator
		) {
			property1.getPossibleValues().forEach(value1 ->
				property2.getPossibleValues().forEach(value2 ->
					property3.getPossibleValues().forEach(value3 ->
						select(
							value1,
							value2,
							value3,
							generator.apply(value1, value2, value3)
						)
					)
				)
			);

			return this;
		}
	}

	public static class C4<
		T1 extends Comparable<T1>,
		T2 extends Comparable<T2>,
		T3 extends Comparable<T3>,
		T4 extends Comparable<T4>
		> extends CustomPropertyDispatch<BlockStateModel.Unbaked> {

		private final Property<T1> property1;
		private final Property<T2> property2;
		private final Property<T3> property3;
		private final Property<T4> property4;

		private C4(
			Property<T1> property1,
			Property<T2> property2,
			Property<T3> property3,
			Property<T4> property4
		) {
			this.property1 = property1;
			this.property2 = property2;
			this.property3 = property3;
			this.property4 = property4;
		}

		@Override
		List<Property<?>> getDefinedProperties() {
			return List.of(
				property1,
				property2,
				property3,
				property4
			);
		}

		public C4<T1, T2, T3, T4> select(
			T1 value1,
			T2 value2,
			T3 value3,
			T4 value4,
			BlockStateModel.Unbaked model
		) {
			putValue(
				PropertyValueList.of(
					property1.value(value1),
					property2.value(value2),
					property3.value(value3),
					property4.value(value4)),
				model
			);

			return this;
		}

		public CustomPropertyDispatch<BlockStateModel.Unbaked> generate(
			Function4<
				T1,
				T2,
				T3,
				T4,
				BlockStateModel.Unbaked
				> generator
		) {
			property1.getPossibleValues().forEach(value1 ->
				property2.getPossibleValues().forEach(value2 ->
					property3.getPossibleValues().forEach(value3 ->
						property4.getPossibleValues().forEach(value4 ->
							select(
								value1,
								value2,
								value3,
								value4,
								generator.apply(
									value1,
									value2,
									value3,
									value4
								)
							)
						)
					)
				)
			);

			return this;
		}
	}

	public static class C5<
		T1 extends Comparable<T1>,
		T2 extends Comparable<T2>,
		T3 extends Comparable<T3>,
		T4 extends Comparable<T4>,
		T5 extends Comparable<T5>
		> extends CustomPropertyDispatch<BlockStateModel.Unbaked> {

		private final Property<T1> property1;
		private final Property<T2> property2;
		private final Property<T3> property3;
		private final Property<T4> property4;
		private final Property<T5> property5;

		private C5(
			Property<T1> property1,
			Property<T2> property2,
			Property<T3> property3,
			Property<T4> property4,
			Property<T5> property5
		) {
			this.property1 = property1;
			this.property2 = property2;
			this.property3 = property3;
			this.property4 = property4;
			this.property5 = property5;
		}

		@Override
		List<Property<?>> getDefinedProperties() {
			return List.of(
				property1,
				property2,
				property3,
				property4,
				property5
			);
		}

		public C5<T1, T2, T3, T4, T5> select(
			T1 value1,
			T2 value2,
			T3 value3,
			T4 value4,
			T5 value5,
			BlockStateModel.Unbaked model
		) {
			putValue(
				PropertyValueList.of(
					property1.value(value1),
					property2.value(value2),
					property3.value(value3),
					property4.value(value4),
					property5.value(value5)),
				model
			);

			return this;
		}

		public CustomPropertyDispatch<BlockStateModel.Unbaked> generate(
			Function5<
				T1,
				T2,
				T3,
				T4,
				T5,
				BlockStateModel.Unbaked
				> generator
		) {
			property1.getPossibleValues().forEach(value1 ->
				property2.getPossibleValues().forEach(value2 ->
					property3.getPossibleValues().forEach(value3 ->
						property4.getPossibleValues().forEach(value4 ->
							property5.getPossibleValues().forEach(value5 ->
								select(
									value1,
									value2,
									value3,
									value4,
									value5,
									generator.apply(
										value1,
										value2,
										value3,
										value4,
										value5
									)
								)
							)
						)
					)
				)
			);

			return this;
		}
	}
}