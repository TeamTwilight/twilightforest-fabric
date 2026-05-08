package twilightforest.loot.modifiers;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;

import java.util.HashMap;
import java.util.Map;

public final class GiantToolGroupingModifier {
	public static final Map<Block, Item> CONVERSIONS = new HashMap<>();

	private GiantToolGroupingModifier() {
	}

	public static void bootstrapConversions() {
		CONVERSIONS.put(Blocks.COBBLESTONE, TFBlocks.GIANT_COBBLESTONE.get().asItem());
		CONVERSIONS.put(Blocks.OAK_LOG, TFBlocks.GIANT_LOG.get().asItem());
		CONVERSIONS.put(Blocks.OAK_LEAVES, TFBlocks.GIANT_LEAVES.get().asItem());
		CONVERSIONS.put(Blocks.OBSIDIAN, TFBlocks.GIANT_OBSIDIAN.get().asItem());
	}

	public static ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootParams context) {
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		if (!(entity instanceof Player player) || generatedLoot.isEmpty() || !(generatedLoot.getFirst().getItem() instanceof BlockItem blockItem)) {
			return generatedLoot;
		}

		Item giantBlock = CONVERSIONS.get(blockItem.getBlock());
		if (giantBlock == null) {
			return generatedLoot;
		}

		var attachment = TFDataAttachments.get(player, TFDataAttachments.GIANT_PICKAXE_MINING);
		if (player.level().getGameTime() != attachment.getMining() || !attachment.canMakeGiantBlock()) {
			return generatedLoot;
		}

		int blockConversion = attachment.getGiantBlockConversion();
		attachment.setGiantBlockConversion(blockConversion - 1);
		return blockConversion == 64 ? ObjectArrayList.of(new ItemStack(giantBlock)) : new ObjectArrayList<>();
	}
}
