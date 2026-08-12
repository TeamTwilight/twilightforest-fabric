package twilightforest.util;

import twilightforest.TFMain;

public class ModidPrefixUtil {
	public static final ModidPrefixUtil INSTANCE = new ModidPrefixUtil();

	public String stringPrefix(String suffix) {
		return TFMain.ID.concat(":").concat(suffix);
	}
}