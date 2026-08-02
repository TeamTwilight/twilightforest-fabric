package twilightforest.mixin;

import com.ibm.icu.text.RuleBasedNumberFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.SplashRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

@Environment(EnvType.CLIENT)
@Mixin(SplashRenderer.class)
public class SplashRendererMixin {

	@Shadow
	@Final
	@Mutable
	private String splash;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void twilightforest$customizeSplash(String splash, CallbackInfo ci) {
		LocalDate date = LocalDate.now();
		if (date.getMonth() == Month.AUGUST && date.getDayOfMonth() == 19) {
			RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(Locale.US, RuleBasedNumberFormat.ORDINAL);
			this.splash = String.format("Happy %s birthday to the Twilight Forest!", formatter.format(date.getYear() - 2011));
		}
	}
}