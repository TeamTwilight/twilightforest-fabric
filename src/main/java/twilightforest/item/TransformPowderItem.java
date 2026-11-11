package twilightforest.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.util.entities.EntityUtil;

import java.util.Map;

public class TransformPowderItem extends Item {
	public static final Map<EntityType<?>, EntityType<?>> TRANSFORMATION_POWDER = Map.ofEntries(
		Map.entry(TFEntities.MINOTAUR, EntityTypes.ZOMBIFIED_PIGLIN),
		Map.entry(EntityTypes.ZOMBIFIED_PIGLIN, TFEntities.MINOTAUR),
		Map.entry(TFEntities.DEER, EntityTypes.COW),
		Map.entry(EntityTypes.COW, TFEntities.DEER),
		Map.entry(TFEntities.BOAR, EntityTypes.PIG),
		Map.entry(EntityTypes.PIG, TFEntities.BOAR),
		Map.entry(TFEntities.BIGHORN_SHEEP, EntityTypes.SHEEP),
		Map.entry(EntityTypes.SHEEP, TFEntities.BIGHORN_SHEEP),
		Map.entry(TFEntities.DWARF_RABBIT, EntityTypes.RABBIT),
		Map.entry(EntityTypes.RABBIT, TFEntities.DWARF_RABBIT),
		Map.entry(TFEntities.TINY_BIRD, EntityTypes.PARROT),
		Map.entry(EntityTypes.PARROT, TFEntities.TINY_BIRD),
		Map.entry(TFEntities.RAVEN, EntityTypes.BAT),
		Map.entry(EntityTypes.BAT, TFEntities.RAVEN),
		Map.entry(TFEntities.HOSTILE_WOLF, EntityTypes.WOLF),
		Map.entry(EntityTypes.WOLF, TFEntities.HOSTILE_WOLF),
		Map.entry(TFEntities.PENGUIN, EntityTypes.CHICKEN),
		Map.entry(EntityTypes.CHICKEN, TFEntities.PENGUIN),
		Map.entry(TFEntities.HEDGE_SPIDER, EntityTypes.SPIDER),
		Map.entry(EntityTypes.SPIDER, TFEntities.HEDGE_SPIDER),
		Map.entry(TFEntities.SWARM_SPIDER, EntityTypes.CAVE_SPIDER),
		Map.entry(EntityTypes.CAVE_SPIDER, TFEntities.SWARM_SPIDER),
		Map.entry(TFEntities.WRAITH, EntityTypes.VEX),
		Map.entry(EntityTypes.VEX, TFEntities.WRAITH),
		Map.entry(TFEntities.SKELETON_DRUID, EntityTypes.WITCH),
		Map.entry(EntityTypes.WITCH, TFEntities.SKELETON_DRUID),
		Map.entry(TFEntities.CARMINITE_GHASTGUARD, EntityTypes.GHAST),
		Map.entry(EntityTypes.GHAST, TFEntities.CARMINITE_GHASTGUARD),
		Map.entry(TFEntities.TOWERWOOD_BORER, EntityTypes.SILVERFISH),
		Map.entry(EntityTypes.SILVERFISH, TFEntities.TOWERWOOD_BORER),
		Map.entry(TFEntities.MAZE_SLIME, EntityTypes.SLIME),
		Map.entry(EntityTypes.SLIME, TFEntities.MAZE_SLIME)
	);

	public TransformPowderItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (!target.isAlive()) {
			return InteractionResult.PASS;
		}

		return transformEntityIfPossible(target, player, player.getItemInHand(hand), !player.isCreative()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide()) {
			AABB area = this.getEffectAABB(player);

			// particle effect
			for (int i = 0; i < 30; i++) {
				level.addParticle(ParticleTypes.CRIT, area.minX + level.getRandom().nextFloat() * (area.maxX - area.minX),
					area.minY + level.getRandom().nextFloat() * (area.maxY - area.minY),
					area.minZ + level.getRandom().nextFloat() * (area.maxZ - area.minZ),
					0, 0, 0);
			}

		}

		return InteractionResult.SUCCESS;
	}

	public static boolean transformEntityIfPossible(LivingEntity target, @Nullable Player user, ItemStack powder, boolean shrinkStack) {
		//dont transform tamed animals that have other owners
		if (target instanceof OwnableEntity ownable && ownable.getOwner() != user) return false;

		EntityType<?> result = TRANSFORMATION_POWDER.get(target.getType());

		if (result != null) {
			boolean flag = EntityUtil.convertEntity(target, result);
			if (flag && shrinkStack) {
				powder.shrink(1);
			}
			return flag;
		}
		return false;
	}

	private AABB getEffectAABB(Player player) {
		double range = 2.0D;
		double radius = 1.0D;
		Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3 lookVec = player.getLookAngle();
		Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);

		return new AABB(destVec.x() - radius, destVec.y() - radius, destVec.z() - radius, destVec.x() + radius, destVec.y() + radius, destVec.z() + radius);
	}
}