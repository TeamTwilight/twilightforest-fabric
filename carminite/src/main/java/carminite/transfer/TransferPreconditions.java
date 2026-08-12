package carminite.transfer;

import carminite.transfer.resource.IResource;

public class TransferPreconditions {
	private TransferPreconditions() {}

	public static void checkNonEmpty(IResource resource) {
		if (resource.isEmpty()) {
			throw new IllegalArgumentException("Expected resource to be non-empty: " + resource);
		}
	}

	public static void checkNonNegative(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Expected value to be non-negative: " + value);
		}
	}

	public static void checkNonEmptyNonNegative(IResource resource, int value) {
		checkNonEmpty(resource);
		checkNonNegative(value);
	}
}