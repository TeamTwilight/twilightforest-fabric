package twilightforest.item;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import twilightforest.entity.monster.LoyalZombie;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

/**
 * Q26 ported behaviour: right-click while looking at a block summons a tamed
 * {@link LoyalZombie} at the cursor position. The zombie owner is the player,
 * with strength I (60s). Wearing {@code mystic_crown} gives 10% chance to spawn
 * a baby zombie. Consumes 1 durability per use.
 */
public class ZombieScepterItem extends CodexItem {

    public ZombieScepterItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (hit.getType() == HitResult.Type.MISS) {
                return InteractionResultHolder.pass(stack);
            }
            LoyalZombie zombie = TFEntities.LOYAL_ZOMBIE.get().create(level);
            if (zombie == null) return InteractionResultHolder.pass(stack);
            zombie.moveTo(hit.getLocation());
            if (!level.noCollision(zombie, zombie.getBoundingBox())) {
                return InteractionResultHolder.pass(stack);
            }
            zombie.setTame(true, false);
            zombie.setOwnerUUID(player.getUUID());
            zombie.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1));
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN.get()) && level.getRandom().nextFloat() <= 0.1f) {
                zombie.setBaby(true);
            }
            level.addFreshEntity(zombie);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, hit.getBlockPos());
            zombie.playSound(TFSounds.ZOMBIE_SCEPTER_USE, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild && player instanceof ServerPlayer sp) {
                stack.hurtAndBreak(1, serverLevel, sp, removed -> {});
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
