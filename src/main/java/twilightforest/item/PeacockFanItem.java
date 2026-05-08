package twilightforest.item;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFSounds;
import twilightforest.network.MovePlayerPacket;

public class PeacockFanItem extends CodexItem {

    public PeacockFanItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(stack);
        }

        boolean midAirJump = !player.onGround()
                && !player.isSwimming()
                && !TFDataAttachments.get(player, TFDataAttachments.FEATHER_FAN);

        if (level.isClientSide()) {
            if (player.isFallFlying()) {
                Vec3 look = player.getLookAngle();
                Vec3 movement = player.getDeltaMovement();
                player.setDeltaMovement(movement.add(
                        look.x() * 0.1D + (look.x() * 2.0D - movement.x()) * 0.5D,
                        (look.y() * 0.1D + (look.y() * 2.0D - movement.y()) * 0.5D) + 1.25D,
                        look.z() * 0.1D + (look.z() * 2.0D - movement.z()) * 0.5D));
            }
            if (midAirJump) {
                player.setDeltaMovement(player.getDeltaMovement().x() * 1.05D,
                        1.5D,
                        player.getDeltaMovement().z() * 1.05D);
            }
            return InteractionResultHolder.success(stack);
        }

        ServerLevel serverLevel = (ServerLevel) level;
        AABB fanBox = this.getEffectAABB(player);
        Vec3 push = player.getLookAngle().scale(2.0D);

        int fanned = 0;

        // Push entities + projectiles
        for (Entity entity : serverLevel.getEntitiesOfClass(Entity.class, fanBox)) {
            if (entity == player) continue;
            if (entity.isPushable() || entity instanceof ItemEntity || entity instanceof Projectile) {
                entity.setDeltaMovement(push.x(), push.y(), push.z());
                entity.hurtMarked = true;
                fanned++;
            }
            if (entity instanceof ServerPlayer pushed && pushed != player && !pushed.isShiftKeyDown()) {
                pushed.setDeltaMovement(push.x(), push.y(), push.z());
                pushed.hurtMarked = true;
                if (ServerPlayNetworking.canSend(pushed, MovePlayerPacket.TYPE)) {
                    ServerPlayNetworking.send(pushed, new MovePlayerPacket(push.x(), push.y(), push.z()));
                }
                player.getCooldowns().addCooldown(this, 40);
                fanned += 2;
            }
        }

        // Break flowers + extinguish candles in the fan AABB
        for (BlockPos pos : BlockPos.betweenClosed(
                (int) Math.floor(fanBox.minX), (int) Math.floor(fanBox.minY), (int) Math.floor(fanBox.minZ),
                (int) Math.floor(fanBox.maxX), (int) Math.floor(fanBox.maxY), (int) Math.floor(fanBox.maxZ))) {
            BlockState state = serverLevel.getBlockState(pos);
            if (state.getBlock() instanceof FlowerBlock) {
                if (serverLevel.getRandom().nextInt(3) == 0) {
                    serverLevel.destroyBlock(pos.immutable(), true, player);
                    fanned++;
                }
            } else if (state.getBlock() instanceof AbstractCandleBlock && state.getValue(AbstractCandleBlock.LIT)) {
                AbstractCandleBlock.extinguish(null, state, serverLevel, pos);
            }
        }

        // Particle burst
        for (int i = 0; i < 30; i++) {
            double px = fanBox.minX + serverLevel.getRandom().nextDouble() * (fanBox.maxX - fanBox.minX);
            double py = fanBox.minY + serverLevel.getRandom().nextDouble() * (fanBox.maxY - fanBox.minY);
            double pz = fanBox.minZ + serverLevel.getRandom().nextDouble() * (fanBox.maxZ - fanBox.minZ);
            serverLevel.sendParticles(ParticleTypes.CLOUD, px, py, pz, 1, push.x() * 0.1D, push.y() * 0.1D, push.z() * 0.1D, 0.0D);
        }

        level.playSound(null, player.blockPosition(), TFSounds.PEACOCK_FAN_USE, SoundSource.PLAYERS,
                1.0F + level.getRandom().nextFloat(),
                level.getRandom().nextFloat() * 0.7F + 0.3F);

        // Mid-air jump impulse
        if (midAirJump) {
            TFDataAttachments.set(player, TFDataAttachments.FEATHER_FAN, true);
            player.setDeltaMovement(player.getDeltaMovement().x * 1.05D, 1.5D, player.getDeltaMovement().z * 1.05D);
            player.hurtMarked = true;
            if (player instanceof ServerPlayer serverPlayer && ServerPlayNetworking.canSend(serverPlayer, MovePlayerPacket.TYPE)) {
                ServerPlayNetworking.send(serverPlayer, new MovePlayerPacket(player.getDeltaMovement().x(), player.getDeltaMovement().y(), player.getDeltaMovement().z()));
            }
        }

        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(fanned + 1, player, LivingEntity.getSlotForHand(hand));
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 20;
    }

    private AABB getEffectAABB(Player player) {
        double range = 3.0D;
        double radius = 2.0D;
        Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        Vec3 lookVec = player.getLookAngle().scale(range);
        Vec3 destVec = srcVec.add(lookVec);
        return new AABB(destVec.x - radius, destVec.y - radius, destVec.z - radius,
                destVec.x + radius, destVec.y + radius, destVec.z + radius);
    }
}
