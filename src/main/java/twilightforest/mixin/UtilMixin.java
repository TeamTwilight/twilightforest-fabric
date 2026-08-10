package twilightforest.mixin;

import net.minecraft.Util;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * This mixin is completely harmless. It just suppresses useless log spam
 * that may have otherwise confused players. The path that would otherwise mask
 * this logging doesn't seem to be present in Porting Lib.
 */
@Mixin(Util.class)
public class UtilMixin {

	@Redirect(
		method = "doFetchChoiceType",
		at = @At(
			value = "INVOKE",
			target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V"
		)
	)
	private static void twilightforest$suppressOwnDataFixerWarning(
		Logger logger,
		String message,
		Object choiceName
	) {
		if (choiceName instanceof String s && s.startsWith("twilightforest:")) {
			return;
		}

		logger.error(message, choiceName);
	}
}