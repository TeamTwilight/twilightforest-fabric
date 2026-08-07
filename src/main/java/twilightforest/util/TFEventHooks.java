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

public class TFEventHooks {

	public static boolean canEntityGrief(Level level, @Nullable Entity entity) {
		return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
	}

	public static void finalizeMobSpawnSpawner(Mob mob, LevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable BaseSpawner spawner, boolean flag) {
		mob.finalizeSpawn((ServerLevel) mob.level(), difficulty, spawnType, data);
	}

	public static boolean checkSpawnPositionSpawner(Mob mob, ServerLevel level, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable BaseSpawner spawner) {
		return mob.checkSpawnRules(level, spawnType) && mob.checkSpawnObstruction(level);
	}

	public static int onArrowLoose(net.minecraft.world.item.ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
		return charge;
	}

	public static boolean onEntityStruckByLightning(Entity entity, net.minecraft.world.entity.LightningBolt bolt) {
		return io.github.fabricators_of_create.porting_lib.entity.EntityHooks.onEntityStruckByLightning(entity, bolt);
	}

	public static boolean doPlayerHarvestCheck(Player player, BlockState state, Level level, BlockPos pos) {
		return !state.requiresCorrectToolForDrops() || player.hasCorrectToolForDrops(state);
	}

	public static boolean onEntityDestroyBlock(LivingEntity entity, BlockPos pos, BlockState state) {
		return true;
	}
}