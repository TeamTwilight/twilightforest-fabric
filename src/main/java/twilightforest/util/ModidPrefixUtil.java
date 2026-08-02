package twilightforest.util;

import twilightforest.TwilightForestMod;

public class ModidPrefixUtil {
	public static final ModidPrefixUtil INSTANCE = new ModidPrefixUtil();

	public String stringPrefix(String suffix) {
		return TwilightForestMod.ID.concat(":").concat(suffix);
	}
}
