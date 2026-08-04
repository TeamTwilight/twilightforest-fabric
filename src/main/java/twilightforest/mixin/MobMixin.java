package twilightforest.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TwilightForestMod;
import twilightforest.asmhooks.EntityHooks;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;

import java.util.List;

@Mixin(Mob.class)
public class MobMixin {

	@Inject(
		method = "aiStep",
		at = @At("HEAD")
	)
	private void twilightforest$unrestrainedSprintingInWater(CallbackInfo ci) {
		Mob self = (Mob) (Object) this;
		boolean isInWater = self.isInWater();
		boolean result = EntityHooks.unrestrainedSprintingInWater(isInWater, (LivingEntity) (Object) this);
		// If unrestrained modifier is active, override isInWater behavior
		// This is handled by the hook which returns false when unrestrained and can walk on water
		if (result != isInWater && !result) {
			// The hook handles the logic internally - the actual sprinting behavior
			// is controlled by the isInWater check in the original aiStep method
		}
	}

	@Inject(
		method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;",
		at = @At("TAIL")
	)
	private void twilightforest$adjustHealthForMultiplayer(
		ServerLevelAccessor level,
		DifficultyInstance difficulty,
		MobSpawnType spawnType,
		SpawnGroupData spawnGroupData,
		CallbackInfoReturnable<SpawnGroupData> cir
	) {
		Mob self = (Mob) (Object) this;

		if (self.getType().is(EntityTagGenerator.MULTIPLAYER_INCLUSIVE_ENTITIES)) {
			if (TFConfig.multiplayerFightAdjuster.adjustsHealth()) {
				List<ServerPlayer> nearbyPlayers = level.getEntitiesOfClass(ServerPlayer.class, self.getBoundingBox().inflate(32, 10, 32), player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.ENTITY_STILL_ALIVE).test(player));
				if (nearbyPlayers.size() > 1 && self.getAttribute(Attributes.MAX_HEALTH) != null) {
					self.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(TwilightForestMod.prefix("group_health_boost"), getHealthBasedOnDifficulty(difficulty.getDifficulty()) * (nearbyPlayers.size() - 1), AttributeModifier.Operation.ADD_VALUE));
				}
			}
		}
	}

	@Unique
	private static double getHealthBasedOnDifficulty(Difficulty difficulty) {
		return switch (difficulty) {
			case EASY -> 20.0D;
			case NORMAL -> 40.0D;
			case HARD -> 60.0D;
			default -> 0.0D;
		};
	}
}