package twilightforest.item;

import net.minecraft.world.item.Item;

/**
 * Generic Twilight item registered under its real {@code twilightforest:*} id.
 *
 * <p>The paired client mod supplies the official model and behavior-facing
 * subclasses keep their vanilla base class where needed. The old paired-client item
 * substitution path has been removed.</p>
 *
 * <p>Use this for non-equipment items (essences, ingots, scepters, foods, charms
 * etc.). For armor/weapons/tools, use the specialized variants in this package
 * which extend the corresponding vanilla Item subclass to keep stats/durability.</p>
 */
public class CodexItem extends Item {

    public CodexItem(Properties properties, Item vanillaTemplate, int ignoredModelData) {
        super(properties);
    }

    public CodexItem(Properties properties, Item vanillaTemplate) {
        this(properties, vanillaTemplate, -1);
    }
}
