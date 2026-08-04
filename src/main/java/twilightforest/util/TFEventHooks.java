package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BaseSpawner;
import org.jetbrains.annotations.Nullable;

/**
 * Fabric 兼容层，替代 NeoForge 的 EventHooks。
 * 对于 NeoForge 中触发事件的调用，在 Fabric 中简化为直接返回或使用原版逻辑。
 */
public class TFEventHooks {

	/**
	 * 替代 NeoForge EventHooks.canEntityGrief
	 */
	public static boolean canEntityGrief(Level level, @Nullable Entity entity) {
		return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
	}

	/**
	 * 替代 NeoForge EventHooks.finalizeMobSpawnSpawner
	 */
	public static void finalizeMobSpawnSpawner(Mob mob, LevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable BaseSpawner spawner, boolean flag) {
		mob.finalizeSpawn((ServerLevel) mob.level(), difficulty, spawnType, data);
	}

	/**
	 * 替代 NeoForge EventHooks.checkSpawnPositionSpawner
	 */
	public static boolean checkSpawnPositionSpawner(Mob mob, ServerLevel level, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable BaseSpawner spawner) {
		return mob.checkSpawnRules(level, spawnType) && mob.checkSpawnObstruction(level);
	}

	/**
	 * 替代 NeoForge EventHooks.onArrowLoose
	 */
	public static int onArrowLoose(net.minecraft.world.item.ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
		return charge;
	}

	/**
	 * 替代 NeoForge EventHooks.onEntityStruckByLightning
	 */
	public static boolean onEntityStruckByLightning(Entity entity, net.minecraft.world.entity.LightningBolt bolt) {
		return io.github.fabricators_of_create.porting_lib.entity.EntityHooks.onEntityStruckByLightning(entity, bolt);
	}

	/**
	 * 替代 NeoForge EventHooks.doPlayerHarvestCheck
	 */
	public static boolean doPlayerHarvestCheck(Player player, BlockState state, Level level, BlockPos pos) {
		return !state.requiresCorrectToolForDrops() || player.hasCorrectToolForDrops(state);
	}

	/**
	 * 替代 NeoForge EventHooks.onEntityDestroyBlock
	 */
	public static boolean onEntityDestroyBlock(LivingEntity entity, BlockPos pos, BlockState state) {
		return true;
	}

	/**
	 * 替代 NeoForge EventHooks.canLivingConvert
	 */
	// net.minecraft.util.Timer 在 1.21.1 中不存在，使用 Minecraft.getInstance().getTimer() 替代
	/*
	public static boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, net.minecraft.util.Timer timer) {
		return true;
	}
	*/

	/**
	 * 替代 NeoForge EventHooks.onLivingConvert
	 */
	public static void onLivingConvert(LivingEntity entity, LivingEntity outcome) {
	}
}