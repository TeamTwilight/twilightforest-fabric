package twilightforest.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Q38 1:1 port of TF {@code MazebreakerPickItem}: 16× destroy speed on blocks
 * tagged {@code twilightforest:mazebreaker_accelerated_mining} (mazestone +
 * castle blocks). Otherwise behaves as a regular diamond-tier pickaxe.
 */
public class MazebreakerPickItem extends CodexDiggerItem.Pickaxe {

    public static final TagKey<Block> MAZEBREAKER_ACCELERATED = TagKey.create(
            net.minecraft.core.registries.Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("twilightforest", "mazebreaker_accelerated_mining"));

    public MazebreakerPickItem(Tier tier, Properties properties, Item fallback, int cmd) {
        super(tier, properties, fallback, cmd);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float base = super.getDestroySpeed(stack, state);
        return state.is(MAZEBREAKER_ACCELERATED) ? base * 16.0F : base;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack other) {
        return false;
    }
}
