package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Q22 ported behaviour for charm_of_keeping_1/2/3.
 *
 * <p>On player death (without keepInventory game rule), the highest-tier charm
 * in the player's inventory is consumed and a slice of inventory is preserved
 * across the death drop:</p>
 * <ul>
 *   <li>Tier 1: hotbar (slots 0-8) + offhand</li>
 *   <li>Tier 2: hotbar + offhand + chestplate slot</li>
 *   <li>Tier 3: full inventory + offhand + armor</li>
 * </ul>
 *
 * <p>The drop interception is wired via Mixin in
 * {@code mixin.PlayerInventoryDeathDropMixin} (see Q22 Lane A entry in AGENTS.md).</p>
 */
public class CharmOfKeepingItem extends CodexItem {

    private final int tier;

    public CharmOfKeepingItem(Properties properties, Item fallback, int tier) {
        super(properties, fallback, -1);
        this.tier = tier;
    }

    public int charmTier() {
        return this.tier;
    }

    /** Find the highest-tier CharmOfKeeping in the player's inventory and consume it. Returns the consumed tier (0 if none). */
    public static int consumeBestCharm(Player player) {
        Inventory inv = player.getInventory();
        int bestTier = 0;
        int bestSlot = -1;
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack stack = inv.getItem(slot);
            if (stack.getItem() instanceof CharmOfKeepingItem charm && charm.tier > bestTier) {
                bestTier = charm.tier;
                bestSlot = slot;
            }
        }
        if (bestSlot >= 0) {
            inv.getItem(bestSlot).shrink(1);
        }
        return bestTier;
    }

    /**
     * Move items the player should keep into the {@code preserved} list and
     * remove them from {@code inventory}. The remaining inventory contents drop
     * normally.
     */
    public static void preserveInventory(Inventory inventory, int tier, NonNullList<ItemStack> preserved) {
        // Hotbar slots 0-8 are always kept.
        for (int i = 0; i <= 8 && i < inventory.items.size(); i++) {
            preserved.set(i, inventory.items.get(i).copy());
            inventory.items.set(i, ItemStack.EMPTY);
        }
        // Offhand always kept.
        if (!inventory.offhand.isEmpty() && !inventory.offhand.get(0).isEmpty()) {
            preserved.set(40, inventory.offhand.get(0).copy());
            inventory.offhand.set(0, ItemStack.EMPTY);
        }
        if (tier >= 2) {
            // Tier 2: also keep chestplate (armor slot index 2).
            if (inventory.armor.size() > 2 && !inventory.armor.get(2).isEmpty()) {
                preserved.set(38, inventory.armor.get(2).copy());
                inventory.armor.set(2, ItemStack.EMPTY);
            }
        }
        if (tier >= 3) {
            // Tier 3: keep entire main inventory + all armor.
            for (int i = 9; i < inventory.items.size(); i++) {
                if (!inventory.items.get(i).isEmpty()) {
                    preserved.set(i, inventory.items.get(i).copy());
                    inventory.items.set(i, ItemStack.EMPTY);
                }
            }
            for (int i = 0; i < inventory.armor.size(); i++) {
                if (!inventory.armor.get(i).isEmpty()) {
                    preserved.set(36 + i, inventory.armor.get(i).copy());
                    inventory.armor.set(i, ItemStack.EMPTY);
                }
            }
        }
    }
}
