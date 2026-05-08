package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.config.TFConfig;
import twilightforest.init.TFStats;
import twilightforest.init.TFSounds;
import twilightforest.inventory.UncraftingMenu;

import java.util.List;

public class UncraftingTableBlock extends Block {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	private static final Component TITLE = Component.translatable("container.twilightforest.uncrafting_table");

	public UncraftingTableBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (TFConfig.disableEntireTable) {
			if (!level.isClientSide()) {
				player.displayClientMessage(Component.translatable("block.twilightforest.uncrafting_table.disabled"), true);
			}
			return InteractionResult.sidedSuccess(level.isClientSide());
		}

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		player.openMenu(state.getMenuProvider(level, pos));
		player.awardStat(TFStats.UNCRAFTING_TABLE_INTERACTIONS);
		return InteractionResult.CONSUME;
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (level.isClientSide()) {
			return;
		}
		boolean powered = level.hasNeighborSignal(pos);
		if (powered != state.getValue(POWERED)) {
			level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
			if (powered && level.getBlockState(pos.below()).is(Blocks.AMETHYST_BLOCK)) {
				level.playSound(null, pos, TFSounds.UNCRAFTING_TABLE_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new SimpleMenuProvider((id, inventory, player) ->
				new UncraftingMenu(id, inventory, level, ContainerLevelAccess.create(level, pos)), TITLE);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (TFConfig.disableEntireTable) {
			tooltip.add(Component.translatable("block.twilightforest.uncrafting_table.disabled"));
		}
	}
}
