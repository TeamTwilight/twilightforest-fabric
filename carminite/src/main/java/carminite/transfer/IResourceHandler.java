package carminite.transfer;

import carminite.transfer.resource.IResource;
import carminite.transfer.transaction.ITransactionContext;
import com.google.common.primitives.Ints;
import org.jetbrains.annotations.ApiStatus;

public interface IResourceHandler<T extends IResource> {
	int size();

	T getResource(int index);

	long getAmountAsLong(int index);

	@ApiStatus.NonExtendable
	default int getAmountAsInt(int index) {
		return Ints.saturatedCast(getAmountAsLong(index));
	}

	long getCapacityAsLong(int index, T resource);

	@ApiStatus.NonExtendable
	default int getCapacityAsInt(int index, T resource) {
		return Ints.saturatedCast(getCapacityAsLong(index, resource));
	}

	boolean isValid(int index, T resource);

	int insert(int index, T resource, int amount, ITransactionContext transaction);

	default int insert(T resource, int amount, ITransactionContext transaction) {
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

		int inserted = 0;
		int size = size();
		for (int index = 0; index < size; index++) {
			inserted += insert(index, resource, amount - inserted, transaction);
			if (inserted == amount) break;
		}
		return inserted;
	}

	int extract(int index, T resource, int amount, ITransactionContext transaction);

	default int extract(T resource, int amount, ITransactionContext transaction) {
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount);

		int extracted = 0;
		int size = size();
		for (int index = 0; index < size; index++) {
			extracted += extract(index, resource, amount - extracted, transaction);
			if (extracted == amount) break;
		}
		return extracted;
	}

	@SuppressWarnings("unchecked")
	static <T extends IResource> Class<IResourceHandler<T>> asClass() {
		return (Class<IResourceHandler<T>>) (Object) IResourceHandler.class;
	}
}