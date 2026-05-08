package twilightforest.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.init.TFSounds;

/**
 * Q23 ported behaviour: right-click throws an {@link IceBomb} projectile that
 * freezes water and damages mobs on impact. The projectile entity ({@code
 * twilightforest:thrown_ice}) is already registered by Lane B; this item just
 * spawns it.
 */
public class IceBombItem extends CodexItem {

    public IceBombItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                TFSounds.ICE_BOMB_FIRED, SoundSource.PLAYERS,
                0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide()) {
            IceBomb ice = new IceBomb(level, player);
            ice.shootFromRotation(player, player.getXRot(), player.getYRot(), -5.0F, 1.25F, 1.0F);
            level.addFreshEntity(ice);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
