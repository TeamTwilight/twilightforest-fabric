package twilightforest.util;

import twilightforest.TFMain;
import tamaized.beanification.Component;

@Component
public class ModidPrefixUtil {

	public String stringPrefix(String suffix) {
		return TFMain.ID.concat(":").concat(suffix);
	}

}
