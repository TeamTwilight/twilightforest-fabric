package twilightforest.item;

import net.minecraft.server.level.ServerPlayer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.init.TFSounds;

/**
 * Q23 ported behaviour: right-click grants 4 hearts of absorption (1 minute) +
 * resistance II (10 seconds). Consumes 1 durability per use.
 */
public class FortificationWandItem extends CodexItem {

    public FortificationWandItem(Properties properties, Item fallback, int cmd) {
        super(properties, fallback, cmd);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(stack);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                TFSounds.FORTIFICATION_SCEPTER_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!level.isClientSide()) {
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
            if (!player.getAbilities().instabuild && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer sp) {
                stack.hurtAndBreak(1, serverLevel, sp, removed -> {});
            }
        }
        return InteractionResultHolder.success(stack);
    }
}
