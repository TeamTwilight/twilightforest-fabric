package carminite.transfer.resource;

import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import org.jetbrains.annotations.ApiStatus;

public interface IRegisteredResource<T> extends IResource, TypedInstance<T> {
	T value();

	@ApiStatus.NonExtendable
	default boolean is(Predicate<Holder<T>> predicate) {
		return predicate.test(typeHolder());
	}
}