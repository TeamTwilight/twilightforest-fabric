package twilightforest.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

/**
 * Armor variant of {@link CodexItem}. Keeps real {@link ArmorItem} stats,
 * durability and equip slot for server-side gameplay while syncing the real
 * Twilight item id to paired clients.
 */
public class CodexArmorItem extends ArmorItem {

    public CodexArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties, Item vanillaTemplate, int ignoredModelData) {
        super(material, type, properties);
    }

}
