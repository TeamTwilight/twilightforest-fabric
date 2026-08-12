package carminite.transfer.resource;

import java.util.function.Supplier;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import org.jspecify.annotations.Nullable;

public interface IDataComponentHolderResource<T> extends IRegisteredResource<T>, DataComponentHolder {
	boolean isComponentsPatchEmpty();

	IDataComponentHolderResource<T> withMergedPatch(DataComponentPatch patch);

	<D> IDataComponentHolderResource<T> with(DataComponentType<D> type, @Nullable D data);

	IDataComponentHolderResource<T> without(DataComponentType<?> type);

	DataComponentPatch getComponentsPatch();

	default <D> IDataComponentHolderResource<T> with(Supplier<? extends DataComponentType<D>> type, @Nullable D data) {
		return with(type.get(), data);
	}

	default IDataComponentHolderResource<T> without(Supplier<? extends DataComponentType<?>> type) {
		return without(type.get());
	}
}