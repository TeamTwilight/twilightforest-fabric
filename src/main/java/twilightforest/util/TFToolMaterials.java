package twilightforest.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.data.tags.ItemTagGenerator;

import java.util.function.Supplier;

public final class TFToolMaterials {
    public static final Tier IRONWOOD = new FabricTier(BlockTagGenerator.INCORRECT_FOR_IRONWOOD_TOOL, 512, 6.5F, 2.0F, 25, () -> Ingredient.of(ItemTagGenerator.REPAIRS_IRONWOOD_TOOLS));
    public static final Tier FIERY = new FabricTier(BlockTagGenerator.INCORRECT_FOR_FIERY_TOOL, 1024, 9.0F, 4.0F, 10, () -> Ingredient.of(ItemTagGenerator.REPAIRS_FIERY_TOOLS));
    public static final Tier STEELEAF = new FabricTier(BlockTagGenerator.INCORRECT_FOR_STEELEAF_TOOL, 131, 8.0F, 3.0F, 9, () -> Ingredient.of(ItemTagGenerator.REPAIRS_STEELEAF_TOOLS));
    public static final Tier KNIGHTMETAL = new FabricTier(BlockTagGenerator.INCORRECT_FOR_KNIGHTMETAL_TOOL, 512, 8.0F, 3.0F, 8, () -> Ingredient.of(ItemTagGenerator.REPAIRS_KNIGHTMETAL_TOOLS));
    public static final Tier GIANT = new FabricTier(BlockTagGenerator.INCORRECT_FOR_GIANT_TOOL, 1024, 4.0F, 1.0F, 5, () -> Ingredient.of(ItemTagGenerator.REPAIRS_GIANT_TOOLS));
    public static final Tier ICE = new FabricTier(BlockTagGenerator.INCORRECT_FOR_ICE_TOOL, 32, 1.0F, 3.5F, 5, () -> Ingredient.of(ItemTagGenerator.REPAIRS_ICE_TOOLS));
    public static final Tier GLASS = new FabricTier(BlockTagGenerator.INCORRECT_FOR_GLASS_TOOL, 1, 1.0F, 36.0F, 30, () -> Ingredient.EMPTY);

    private TFToolMaterials() {
    }

    private record FabricTier(TagKey<Block> incorrectBlocksForDrops, int uses, float speed,
                              float attackDamageBonus, int enchantmentValue,
                              Supplier<Ingredient> repairIngredient) implements Tier {
        @Override
        public int getUses() {
            return this.uses;
        }

        @Override
        public float getSpeed() {
            return this.speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return this.attackDamageBonus;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return this.incorrectBlocksForDrops;
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }
    }
}
