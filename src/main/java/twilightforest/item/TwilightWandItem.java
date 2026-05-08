package twilightforest.item;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

/**
 * Q23/Q24 ported behaviour: right-click spawns a {@link twilightforest.entity.projectile.TwilightWandBolt}
 * projectile that travels and damages the first living target it hits for 6 HP.
 * Consumes 1 durability per cast. Skips durability when wearing
 * {@link twilightforest.init.TFItems#MYSTIC_CROWN}.
 *
 * <p>Q24 promoted Q23's raycast fallback to a real projectile after WAND_BOLT
 * was registered in TFEntities (cross-lane authorisation).</p>
 */
public class TwilightWandItem extends CodexItem {

    public TwilightWandItem(Properties properties, Item fallback, int cmd) {
        super(properties, fallback, cmd);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(stack);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                TFSounds.TWILIGHT_SCEPTER_USE, SoundSource.PLAYERS,
                1.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);

        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            TwilightWandBolt bolt = new TwilightWandBolt(serverLevel, player);
            serverLevel.addFreshEntity(bolt);
            // Mystic Crown grants 95% chance to skip durability cost.
            boolean wearingCrown = player.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN.get());
            if (!player.getAbilities().instabuild && (!wearingCrown || serverLevel.getRandom().nextFloat() > 0.05F) && player instanceof ServerPlayer sp) {
                stack.hurtAndBreak(1, serverLevel, sp, removed -> {});
            }
        }
        return InteractionResultHolder.success(stack);
    }
}
