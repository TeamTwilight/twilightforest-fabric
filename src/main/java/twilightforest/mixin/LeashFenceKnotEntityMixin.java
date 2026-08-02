package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BlockHooks;

@Mixin(LeashFenceKnotEntity.class)
public class LeashFenceKnotEntityMixin {

	@ModifyReturnValue(
		method = "survives()Z",
		at = @At("RETURN")
	)
	private boolean twilightforest$leashFenceKnotSurvives(
		boolean original
	) {
		return BlockHooks.leashFenceKnotSurvives(
			original,
			(LeashFenceKnotEntity)(Object)this
		);
	}
}
