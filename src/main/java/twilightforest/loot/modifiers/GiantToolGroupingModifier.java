package twilightforest.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.LootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import twilightforest.block.GiantBlock;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.giant_pick.GiantPickMineCapability;
import twilightforest.item.GiantPickItem;

import java.util.HashMap;
import java.util.Map;

public class GiantToolGroupingModifier extends LootModifier {
	public static final Map<Block, Item> CONVERSIONS = new HashMap<>(); // Map of block-to-giant block conversions. Supposed to be similar to vanilla's AxeItem.STRIPPABLES Map

	public static final Codec<GiantToolGroupingModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, GiantToolGroupingModifier::new));

	public GiantToolGroupingModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (context.getParam(LootContextParams.THIS_ENTITY) instanceof Player player) {
			BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
			if (CONVERSIONS.containsKey(state.getBlock())) { // Should be true but let's double-check
				GiantPickMineCapability giantPickMineCapability = CapabilityList.GIANT_PICK_MINE.get(player);
				int blockConversion = giantPickMineCapability.getGiantBlockConversion(); // Get how many conversions are left
				giantPickMineCapability.setGiantBlockConversion(blockConversion - 1);
				if (blockConversion == 64)
					return ObjectArrayList.of(new ItemStack(CONVERSIONS.get(state.getBlock()))); // If it's the first conversion, drop our giant block
				else return new ObjectArrayList<>(); // Drop nothing, all gets converted into giant block
			}
		}
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return GiantToolGroupingModifier.CODEC;
	}

	public static boolean breakBlock(Level world, Player player, BlockPos pos, BlockState state, /* Nullable */ BlockEntity blockEntity) {
		if (player instanceof ServerPlayer serverPlayer && canHarvestWithGiantPick(serverPlayer, state)) {
			GiantPickMineCapability capability = CapabilityList.GIANT_PICK_MINE.get(serverPlayer);
			if (shouldBreakGiantBlock(serverPlayer, capability)) {
				capability.setBreaking(true); // Tell the capability that a block breaking loop is happening, so it knows to fail the if check above. Otherwise, this would go on forever

				boolean allTheSame = CONVERSIONS.containsKey(state.getBlock()); // If the blockstate is convertible, check if the rest are too
				for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
					if (allTheSame && !serverPlayer.level().getBlockState(offsetPos).is(state.getBlock()))
						allTheSame = false;
				}
				capability.setGiantBlockConversion(allTheSame ? 64 : 0); // NO IN-BETWEEN! Either the whole 64 get converted, or none do


				serverPlayer.level().levelEvent(2001, pos, Block.getId(state));
				serverPlayer.gameMode.destroyBlock(pos); // Brake the block we broke, for real this time

				// Break all the other blocks, if they're the same type
				for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
					if (!offsetPos.equals(pos) && serverPlayer.level().getBlockState(offsetPos).is(state.getBlock())) {
						BlockPos newPos = new BlockPos(offsetPos); // This feels dumb, but without it, the client thinks the last block in the iterator is broken too
						serverPlayer.level().levelEvent(2001, newPos, Block.getId(serverPlayer.level().getBlockState(newPos)));
						serverPlayer.gameMode.destroyBlock(newPos);
					}
				}

				capability.setBreaking(false); // Tell the capability that the loop is over, and all is good in the world
				return false;// We cancel this event, since we want to break the block we're looking at first
			}
		}
		return true;
	}

	private static boolean canHarvestWithGiantPick(Player player, BlockState state) {
		return player.getMainHandItem().getItem() instanceof GiantPickItem;// && ForgeHooks.isCorrectToolForDrops(state, player);
	}

	private static boolean shouldBreakGiantBlock(Player player, GiantPickMineCapability capability) {
		return capability.getMining() == player.level().getGameTime() && !capability.getBreaking();
	}
}
