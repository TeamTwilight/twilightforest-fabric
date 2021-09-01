package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import twilightforest.inventory.UncraftingContainer;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class UncraftingTableBlock extends Block {

	protected UncraftingTableBlock() {
		super(Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD));
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!world.isClientSide) {
			player.openMenu(state.getMenuProvider(world, pos));
			player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
		return new SimpleMenuProvider((id, inv, player) -> new UncraftingContainer(id, inv, player.level, ContainerLevelAccess.create(world, pos)),
						new TranslatableComponent(getDescriptionId()));
	}
}
