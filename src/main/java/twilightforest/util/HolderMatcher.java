package twilightforest.util;

import net.minecraft.core.Holder;
import twilightforest.util.TFBeanRegistry;

public class HolderMatcher {

	public static final HolderMatcher INSTANCE = new HolderMatcher();

	static {
		TFBeanRegistry.register(HolderMatcher.class, INSTANCE);
	}

	public <T> boolean match(Holder<T> a, Holder<T> b) {
		if (a.kind() == Holder.Kind.DIRECT || b.kind() == Holder.Kind.DIRECT) {
			return a.value() == b.value();
		}
		return b.unwrapKey().map(a::is).orElseGet(() -> a.unwrapKey().map(b::is).orElse(false));
	}

}
