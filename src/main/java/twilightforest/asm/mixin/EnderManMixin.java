package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.init.custom.TravellersModifiersManager;

import java.util.function.Predicate;

@Mixin(EnderMan.class)
public abstract class EnderManMixin {

	@WrapOperation(
		method = "isBeingStaredBy(Lnet/minecraft/world/entity/player/Player;)Z",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"
		)
	)
	@SuppressWarnings("rawtypes")
	private boolean twilightforest$suppressAnger(
		Predicate instance,
		Object t,
		Operation<Boolean> original,
		@Local(argsOnly = true, name = "player") Player player
	) {
		if (TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			return false;
		}

		return original.call(instance, t);
	}
}