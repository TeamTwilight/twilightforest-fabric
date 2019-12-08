package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.enums.CastleBrickVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.ItemTFMazebreakerPick;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;

/**
 * Castle block makes a castle
 *
 * @author Ben
 */
public class BlockTFCastleBlock extends Block implements ModelRegisterCallback {

	public static final IProperty<CastleBrickVariant> VARIANT = PropertyEnum.create("variant", CastleBrickVariant.class);

	public BlockTFCastleBlock() {
		super(Material.ROCK, MaterialColor.QUARTZ);
		this.setHardness(100F);
		this.setResistance(35F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, CastleBrickVariant.NORMAL));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	public int getMetaFromState(BlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	@Deprecated
	public BlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, CastleBrickVariant.values()[meta]);
	}

	@Override
	public MaterialColor getMaterialColor(BlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(VARIANT) == CastleBrickVariant.ROOF ? MaterialColor.GRAY : super.getMaterialColor(state, world, pos);
	}

	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		ItemStack cei = player.getHeldItemMainhand();
		if (cei.getItem() instanceof ToolItem && !(cei.getItem() instanceof ItemTFMazebreakerPick)) {
			cei.damageItem(16, player);
		}

		super.harvestBlock(world, player, pos, state, te, stack);
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> items) {
		for (CastleBrickVariant variant : CastleBrickVariant.values()) {
			items.add(new ItemStack(this, 1, variant.ordinal()));
		}
	}

	@Override
	public int damageDropped(BlockState state) {
		return getMetaFromState(state);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
