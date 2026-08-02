package twilightforest.util;

import twilightforest.TwilightForestMod;
import twilightforest.util.TFBeanRegistry;

public class ModidPrefixUtil {

	public static final ModidPrefixUtil INSTANCE = new ModidPrefixUtil();

	static {
		TFBeanRegistry.register(ModidPrefixUtil.class, INSTANCE);
	}

	public String stringPrefix(String suffix) {
		return TwilightForestMod.ID.concat(":").concat(suffix);
	}

}
