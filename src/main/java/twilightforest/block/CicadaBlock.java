package twilightforest.block;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFSounds;

import org.jetbrains.annotations.Nullable;
import twilightforest.loot.TFLootTables;

import java.util.List;

public class CicadaBlock extends CritterBlock {
	private static final MutableComponent TOOLTIP = Component.translatable("block.twilightforest.cicada.desc").withStyle(TwilightForestMod.getRarity().color).withStyle(ChatFormatting.ITALIC);

	public CicadaBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CicadaBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.CICADA.get(), CicadaBlockEntity::tick);
	}

	@Override
	public @Nullable ResourceLocation getSquishLootTable() {
		return TFLootTables.CICADA_SQUISH_DROPS;
	}

	@Override
	public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
		super.destroy(accessor, pos, state);
		if (accessor.isClientSide())
			Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.get().getLocation(), SoundSource.NEUTRAL);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, getter, tooltip, flag);

//		if (FabricLoader.getInstance().isModLoaded(TFCompat.IE_ID)) {
//			tooltip.add(TOOLTIP);
//		}
	}
}
