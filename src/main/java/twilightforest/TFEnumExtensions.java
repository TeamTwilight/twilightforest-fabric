package twilightforest;

import twilightforest.util.ModidPrefixUtil;

@SuppressWarnings("unused") // Referenced by enumextender.json
public class TFEnumExtensions {

	private static final ModidPrefixUtil modidPrefixUtil = new ModidPrefixUtil(); // Enum extensions run before the bean context loads

	/**
	 * {@link net.minecraft.world.item.ItemDisplayContext}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFItemDisplayContextEnumExtension#JARRED}
	 */
	public static Object ItemDisplayContext_JARRED(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> -1;
			case 1 -> modidPrefixUtil.stringPrefix("jarred");
			case 2 -> "FIXED";
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}
}
