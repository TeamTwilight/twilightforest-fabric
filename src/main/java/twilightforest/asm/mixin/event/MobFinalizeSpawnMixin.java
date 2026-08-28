package twilightforest.asm.mixin.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.resources.ResourceKey;
import twilightforest.TFMain;
import twilightforest.config.TFConfig;
import twilightforest.tags.TFEntityTypeTags;

import java.util.List;

/**
 * Recreates NeoForge's FinalizeSpawnEvent: multiplayer boss fights scale the
 * entity's max health based on the number of nearby players.
 */
@Mixin(Mob.class)
public class MobFinalizeSpawnMixin {

	@Inject(method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;", at = @At("RETURN"))
	private void twilightforest$adjustBossHealth(ServerLevelAccessor accessor, net.minecraft.world.DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> cir) {
		Mob entity = (Mob) (Object) this;
		if (entity.is(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES)) {
			if (TFConfig.multiplayerFightAdjuster.adjustsHealth() && accessor instanceof ServerLevel level) {
				List<ServerPlayer> nearbyPlayers = level.getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(32, 10, 32), player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.ENTITY_STILL_ALIVE).test(player));
				if (nearbyPlayers.size() > 1 && entity.getAttribute(Attributes.MAX_HEALTH) != null) {
					entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(TFMain.prefix("group_health_boost"), getHealthBasedOnDifficulty(difficulty.getDifficulty()) * (nearbyPlayers.size() - 1), AttributeModifier.Operation.ADD_VALUE));
				}
			}
		}
	}

	private static double getHealthBasedOnDifficulty(Difficulty difficulty) {
		return switch (difficulty) {
			case EASY -> 20.0D;
			case NORMAL -> 40.0D;
			case HARD -> 60.0D;
			default -> 0.0D;
		};
	}
}
