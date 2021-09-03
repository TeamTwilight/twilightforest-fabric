package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.client.TextUtils;
import blusunrize.immersiveengineering.api.shader.IShaderItem;
import blusunrize.immersiveengineering.api.shader.ShaderCase;
import blusunrize.immersiveengineering.api.shader.ShaderLayer;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.shader.impl.ShaderCaseItem;
import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerStandingBlock;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerTileEntity;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerWallBlock;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.item.*;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

// TODO Move to shader capability, where we can then just turn item Trophies into the shaders
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;

public class TFShaderItem extends Item implements IShaderItem {

    public TFShaderItem() {
        super(TFItems.defaultBuilder().stacksTo(1));
    }

    static final String TAG_SHADER = "shader_name";

    @Override
    public ShaderCase getShaderCase(ItemStack shader, ItemStack tool, ResourceLocation shaderType) {
        return ShaderRegistry.getShader(getShaderName(shader), shaderType);
    }

    @Override
    public ResourceLocation getShaderName(ItemStack stack) {
        return getShaderType(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        ResourceLocation rawShaderName = this.getShaderName(stack);
        String unlocalizedShaderName = ("item." + rawShaderName.getNamespace() + ".shader.name." + rawShaderName.getPath().replace(' ', '_').toLowerCase(Locale.ROOT));
        String localizedShaderName = new TranslatableComponent(unlocalizedShaderName).getString();

        if (unlocalizedShaderName.equals(localizedShaderName))
            return new TranslatableComponent(this.getDescriptionId(stack), rawShaderName.getPath()).append(" *TRANSLATION FAILURE*").withStyle(ChatFormatting.DARK_RED); // Translation failure
        else
            return new TranslatableComponent(this.getDescriptionId(stack), localizedShaderName);
    }

    //Copy of ShaderItem.onItemUse so we can apply to banners
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        ResourceLocation name = getShaderName(ctx.getItemInHand());
        if(ShaderRegistry.shaderRegistry.containsKey(name))
        {
            BlockState blockState = world.getBlockState(pos);
            BlockEntity tile = world.getBlockEntity(pos);
            if(tile instanceof BannerBlockEntity)
            {
                ShaderCase sCase = ShaderRegistry.shaderRegistry.get(name).getCase(new ResourceLocation("immersiveengineering", "banner"));
                if(sCase!=null)
                {
                    boolean wall = blockState.getBlock() instanceof WallBannerBlock;

                    if(wall)
                        world.setBlockAndUpdate(pos, IEBlocks.Cloth.shaderBannerWall.defaultBlockState()
                                .setValue(ShaderBannerWallBlock.FACING, blockState.getValue(WallBannerBlock.FACING)));
                    else
                        world.setBlockAndUpdate(pos, IEBlocks.Cloth.shaderBanner.defaultBlockState()
                                .setValue(ShaderBannerStandingBlock.ROTATION, blockState.getValue(BannerBlock.ROTATION)));
                    tile = world.getBlockEntity(pos);
                    if(tile instanceof ShaderBannerTileEntity)
                    {
                        ((ShaderBannerTileEntity)tile).shader.setShaderItem(ItemHandlerHelper.copyStackWithSize(ctx.getItemInHand(), 1));
                        tile.setChanged();
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            else if(tile instanceof ShaderBannerTileEntity)
            {
                ((ShaderBannerTileEntity)tile).shader.setShaderItem(ItemHandlerHelper.copyStackWithSize(ctx.getItemInHand(), 1));
                tile.setChanged();
                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.FAIL;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        list.add(new TranslatableComponent(Lib.DESC_INFO + "shader.level").append(TextUtils.applyFormat(new TranslatableComponent(Lib.DESC_INFO + "shader.rarity." + this.getRarity(stack).name().toLowerCase(Locale.US)), getRarity(stack).color)));
        if(ShaderRegistry.shaderRegistry.containsKey(getShaderName(stack))) {
            if (!Screen.hasShiftDown())
                list.add(new TranslatableComponent(Lib.DESC_INFO + "shader.applyTo").append(" ").append(new TranslatableComponent(Lib.DESC_INFO + "holdShift")));
            else {
                list.add(new TranslatableComponent(Lib.DESC_INFO + "shader.applyTo"));

                for (ShaderCase sCase : ShaderRegistry.shaderRegistry.get(getShaderName(stack)).getCases())
                    if (!(sCase instanceof ShaderCaseItem))
                        list.add(TextUtils.applyFormat(new TranslatableComponent(Lib.DESC_INFO + "shader." + sCase.getShaderType()), ChatFormatting.DARK_GRAY));
            }
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return ShaderRegistry.shaderRegistry.containsKey(getShaderName(stack)) ? ShaderRegistry.shaderRegistry.get(getShaderName(stack)).getRarity() : Rarity.COMMON;
    }

    public static ResourceLocation getShaderType(ItemStack stack) {
        return ItemNBTHelper.hasKey(stack, TAG_SHADER) ? new ResourceLocation(ItemNBTHelper.getString(stack, TAG_SHADER)) : TwilightForestMod.prefix("twilight");
    }

    public static int getShaderColors(ItemStack stack, int pass) {
        ResourceLocation name = getShaderType(stack);
        if(ShaderRegistry.shaderRegistry.containsKey(name)) {
            ShaderCase sCase = ShaderRegistry.shaderRegistry.get(name).getCase(new ResourceLocation("immersiveengineering", "item"));
            if(sCase!=null) {
                ShaderLayer[] layers = sCase.getLayers();
                if(pass < layers.length && layers[pass] != null) {
                    return Utils.intFromRGBA(layers[pass].getColor());
                }
                return 0xffffffff;
            }
        }
        return 0xffffffff;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (group == TFItems.creativeTab) {
            for (ShaderRegistry.ShaderRegistryEntry entry : IEShaderRegister.getAllTwilightShaders()) {
                ItemStack stack = new ItemStack(this);
                ItemNBTHelper.putString(stack, TAG_SHADER, entry.getName().toString());
                items.add(stack);
            }
        }
    }
}
