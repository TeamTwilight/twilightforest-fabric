package twilightforest.init;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import twilightforest.block.GiantBlock;
import twilightforest.item.GiantPickItem;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

import java.util.List;

public final class TFLootModifiers {
	private TFLootModifiers() {
	}

	public static void bootstrap() {
		GiantToolGroupingModifier.bootstrapConversions();
		PlayerBlockBreakEvents.BEFORE.register(TFLootModifiers::beforeBlockBreak);
		PlayerBlockBreakEvents.AFTER.register(TFLootModifiers::afterBlockBreak);
	}

	public static ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootParams context) {
		return GiantToolGroupingModifier.apply(FieryToolSmeltingModifier.apply(generatedLoot, context), context);
	}

	private static boolean beforeBlockBreak(net.minecraft.world.level.Level level, net.minecraft.world.entity.player.Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (!(player instanceof ServerPlayer serverPlayer) || !(player.getMainHandItem().getItem() instanceof GiantPickItem) || !player.hasCorrectToolForDrops(state)) {
			return true;
		}
		var attachment = TFDataAttachments.get(player, TFDataAttachments.GIANT_PICKAXE_MINING);
		if (attachment.getBreaking()) {
			return true;
		}

		attachment.setMining(level.getGameTime());
		attachment.setBreaking(false);
		attachment.setGiantBlockConversion(0);

		LootParams.Builder builder = new LootParams.Builder(serverPlayer.serverLevel())
			.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
			.withParameter(LootContextParams.BLOCK_STATE, state)
			.withOptionalParameter(LootContextParams.THIS_ENTITY, player)
			.withParameter(LootContextParams.TOOL, player.getMainHandItem());
		List<ItemStack> drops = state.getDrops(builder);
		if (!drops.isEmpty() && drops.getFirst().getItem() instanceof BlockItem blockItem && GiantToolGroupingModifier.CONVERSIONS.containsKey(blockItem.getBlock())) {
			boolean allTheSame = true;
			for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
				if (!level.getBlockState(offsetPos).is(state.getBlock())) {
					allTheSame = false;
					break;
				}
			}
			attachment.setGiantBlockConversion(allTheSame ? 64 : 0);
		}
		return true;
	}

	private static void afterBlockBreak(net.minecraft.world.level.Level level, net.minecraft.world.entity.player.Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (!(player instanceof ServerPlayer serverPlayer) || !(player.getMainHandItem().getItem() instanceof GiantPickItem)) {
			return;
		}
		var attachment = TFDataAttachments.get(player, TFDataAttachments.GIANT_PICKAXE_MINING);
		if (attachment.getBreaking() || player.level().getGameTime() != attachment.getMining()) {
			return;
		}

		attachment.setBreaking(true);
		for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
			if (!offsetPos.equals(pos) && serverPlayer.serverLevel().getBlockState(offsetPos).is(state.getBlock())) {
				BlockPos target = offsetPos.immutable();
				serverPlayer.serverLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, target, Block.getId(serverPlayer.serverLevel().getBlockState(target)));
				serverPlayer.gameMode.destroyBlock(target);
			}
		}
		attachment.setBreaking(false);
	}
}
