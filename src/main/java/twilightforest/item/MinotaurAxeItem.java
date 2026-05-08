package twilightforest.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

/**
 * Q38 1:1 port of TF {@code MinotaurAxeItem}: gold-tier enchantment value
 * (more enchantable than diamond) on top of an iron-tier axe stat block.
 */
public class MinotaurAxeItem extends CodexDiggerItem.Axe {

    public MinotaurAxeItem(Tier tier, Properties properties, Item fallback, int cmd) {
        super(tier, properties, fallback, cmd);
    }

    @Override
    public int getEnchantmentValue() {
        return Tiers.GOLD.getEnchantmentValue();
    }
}
