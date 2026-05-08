package twilightforest.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFDataMaps;
import twilightforest.util.entities.EntityUtil;

/**
 * Fabric port of Twilight Forest's transformation powder. The mapping lives in
 * {@link TFDataMaps}, mirroring the upstream entity data map while keeping
 * the conversion behaviour in {@link EntityUtil#convertEntity(LivingEntity, EntityType)}.
 */
public class TransformPowderItem extends CodexItem {

    public TransformPowderItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!target.isAlive() || target.level().isClientSide()) {
            return InteractionResult.PASS;
        }
        return transformEntityIfPossible(target, stack, !player.getAbilities().instabuild) ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide()) {
            AABB area = this.getEffectAABB(player);
            for (int i = 0; i < 30; i++) {
                level.addParticle(ParticleTypes.CRIT,
                        area.minX + level.getRandom().nextFloat() * (area.maxX - area.minX),
                        area.minY + level.getRandom().nextFloat() * (area.maxY - area.minY),
                        area.minZ + level.getRandom().nextFloat() * (area.maxZ - area.minZ),
                        0, 0, 0);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    private AABB getEffectAABB(Player player) {
        double range = 2.0D;
        double radius = 1.0D;
        Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        Vec3 lookVec = player.getLookAngle();
        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
        return new AABB(destVec.x() - radius, destVec.y() - radius, destVec.z() - radius,
                destVec.x() + radius, destVec.y() + radius, destVec.z() + radius);
    }

    public static boolean transformEntityIfPossible(LivingEntity target, ItemStack powder, boolean shrinkStack) {
        if (target instanceof OwnableEntity ownable && ownable.getOwner() != null) {
            return false;
        }

        EntityType<?> targetType = TFDataMaps.getTransformationPowderResult(target.getType());
        if (targetType == null) {
            return false;
        }

        boolean transformed = EntityUtil.convertEntity(target, targetType);
        if (transformed && shrinkStack) {
            powder.shrink(1);
        }
        return transformed;
    }
}
