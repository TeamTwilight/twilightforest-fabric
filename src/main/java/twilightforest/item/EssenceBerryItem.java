package twilightforest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Q37 1:1 port of TF {@code EssenceBerryItem}: eat → drop a 6-19 XP orb.
 */
public class EssenceBerryItem extends CodexItem {

    public EssenceBerryItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        stack.consume(1, player);
        if (!level.isClientSide()) {
            ExperienceOrb orb = new ExperienceOrb(level,
                    player.getX(), player.getY() + 1, player.getZ(),
                    6 + level.getRandom().nextInt(14));
            level.addFreshEntity(orb);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
