package twilightforest.compat.config;

import com.google.common.reflect.Reflection;
import twilightforest.TFCommon;

public final class FCAPCompat {
	public static void setup() {
		Reflection.initialize(ConfigSetup.class);
		TFCommon.config = new FCAPConfig();
		ConfigSetup.setup();
	}
}
