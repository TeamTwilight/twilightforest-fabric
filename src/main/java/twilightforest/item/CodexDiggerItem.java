package twilightforest.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

/**
 * Tool variants (pickaxe/axe/shovel/hoe). Each subclass keeps its real vanilla
 * item subclass so loot tables and tier mining stay intact.
 */
public final class CodexDiggerItem {
    private CodexDiggerItem() {}

    public static class Pickaxe extends PickaxeItem {
        public Pickaxe(Tier tier, Properties properties, Item vanillaTemplate, int ignoredModelData) {
            super(tier, properties);
        }
    }

    public static class Axe extends AxeItem {
        public Axe(Tier tier, Properties properties, Item vanillaTemplate, int ignoredModelData) {
            super(tier, properties);
        }
    }

    public static class Shovel extends ShovelItem {
        public Shovel(Tier tier, Properties properties, Item vanillaTemplate, int ignoredModelData) {
            super(tier, properties);
        }
    }

    public static class Hoe extends HoeItem {
        public Hoe(Tier tier, Properties properties, Item vanillaTemplate, int ignoredModelData) {
            super(tier, properties);
        }
    }
}
