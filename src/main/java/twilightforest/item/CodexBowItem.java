package twilightforest.item;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;

/**
 * Base for TF bow variants. The paired client renders the real
 * {@code twilightforest:*} item id directly.
 */
public class CodexBowItem extends BowItem {

    public CodexBowItem(Properties properties, Item vanillaTemplate, int ignoredModelData) {
        super(properties);
    }

    public CodexBowItem(Properties properties, Item vanillaTemplate) {
        this(properties, vanillaTemplate, -1);
    }

}
