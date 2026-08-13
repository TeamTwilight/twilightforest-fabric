package carminite.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;

import java.util.Collection;
import java.util.function.Function;

public final class NonNullListUtil {
	public static <E> Codec<NonNullList<E>> codecOf(Codec<E> entryCodec) {
		return entryCodec.listOf().xmap(
			NonNullListUtil::copyOf,
			Function.identity()
		);
	}

	public static <E> NonNullList<E> copyOf(Collection<? extends E> entries) {
		NonNullList<E> result = NonNullList.createWithCapacity(entries.size());
		result.addAll(entries);
		return result;
	}
}