package twilightforest.util;

import twilightforest.TFCommon;

public class ModidPrefixUtil {
	public static final ModidPrefixUtil INSTANCE = new ModidPrefixUtil();

	public String stringPrefix(String suffix) {
		return TFCommon.ID.concat(":").concat(suffix);
	}
}