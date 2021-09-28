package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import twilightforest.TFConstants;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.block.entity.TFBlockEntities;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class CicadaBlock extends CritterBlock {
	@Environment(EnvType.CLIENT)
	private static final MutableComponent TOOLTIP = new TranslatableComponent("block.twilightforest.cicada.desc").withStyle(TFConstants.getRarity().color).withStyle(ChatFormatting.ITALIC);

	protected CicadaBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CicadaBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.CICADA, CicadaBlockEntity::tick);
	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.GRAY_DYE, 1);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);

		if (FabricLoader.getInstance().isModLoaded("immersiveengineering")) {
			tooltip.add(TOOLTIP);
		}
	}
}
