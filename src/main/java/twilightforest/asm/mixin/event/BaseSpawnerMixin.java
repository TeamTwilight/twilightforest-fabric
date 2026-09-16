package twilightforest.asm.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BaseSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asm.hooks.event.EntityEventHooks;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {

	@Inject(
		method = "serverTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Mob;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;"
		)
	)
	private void twilightforest$adjustEntityHealthInMultiplayerFights(
		ServerLevel level,
		BlockPos pos,
		CallbackInfo ci,
		@Local(name = "mob") Mob mob
	) {
		EntityEventHooks.adjustEntityHealthInMultiplayerFights(level, mob, level.getCurrentDifficultyAt(mob.blockPosition()));
	}
}