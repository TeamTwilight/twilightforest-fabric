package twilightforest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import twilightforest.TFConstants;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.AbstractTrophyBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.tileentity.GenericTrophyModel;
import twilightforest.client.renderer.tileentity.SkullCandleTileEntityRenderer;
import twilightforest.client.renderer.tileentity.TrophyTileEntityRenderer;
import twilightforest.enums.BossVariant;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;

public class ISTER extends BlockEntityWithoutLevelRenderer {
	private final ResourceLocation typeId;
	private BlockEntity dummy;

	// When this is called from Item register, TEType register has not run yet so we can't pass the actual object
	public ISTER(ResourceLocation typeId) {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
		this.typeId = typeId;
	}

	@Override
	public void renderByItem(ItemStack stack, ItemTransforms.TransformType camera, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {

		if (dummy == null) {
			dummy = Registry.BLOCK_ENTITY_TYPE.get(typeId).create(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
		}
		Item item = stack.getItem();
		if (item instanceof BlockItem) {
			Block block = ((BlockItem) item).getBlock();
			if (block instanceof AbstractTrophyBlock) {

				BossVariant variant = ((AbstractTrophyBlock)block).getVariant();
				GenericTrophyModel trophy = TrophyTileEntityRenderer.createTrophyRenderers(Minecraft.getInstance().getEntityModels()).get(variant);

				if(camera == ItemTransforms.TransformType.GUI) {

					ModelResourceLocation back = new ModelResourceLocation(TFConstants.prefix(((AbstractTrophyBlock) block).getVariant().getTrophyType().getModelName()), "inventory");
					BakedModel modelBack = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(back);

					ms.pushPose();
					ms.translate(0.5F, 0.5F, -1.5F);
					Minecraft.getInstance().getItemRenderer().render(TrophyTileEntityRenderer.stack, ItemTransforms.TransformType.GUI, false, ms, buffers, 240, overlay, modelBack /*ForgeHooksClient.handleCameraTransforms(ms, modelBack, camera, false)*/);
					ms.popPose();

					ms.pushPose();
					ms.translate(0.5F, 0.5F, 0.5F);
					if(((AbstractTrophyBlock) block).getVariant() == BossVariant.HYDRA || ((AbstractTrophyBlock) block).getVariant() == BossVariant.QUEST_RAM) ms.scale(0.9F, 0.9F, 0.9F);
					ms.mulPose(Vector3f.XP.rotationDegrees(30));
					ms.mulPose(Vector3f.YN.rotationDegrees(TFClientSetup.CLIENT_CONFIG.rotateTrophyHeadsGui ? TFClientEvents.rotationTicker : -45));
					ms.translate(-0.5F, -0.5F, -0.5F);
					ms.translate(0.0F, 0.25F, 0.0F);
					if(((AbstractTrophyBlock) block).getVariant() == BossVariant.UR_GHAST) ms.translate(0.0F, 0.5F, 0.0F);
					if(((AbstractTrophyBlock) block).getVariant() == BossVariant.ALPHA_YETI) ms.translate(0.0F, -0.15F, 0.0F);
					TrophyTileEntityRenderer.render((Direction) null, 180.0F, trophy, variant, 0.0F, ms, buffers, light, camera);
					ms.popPose();

				} else {
					TrophyTileEntityRenderer.render((Direction) null, 180.0F, trophy, variant, 0.0F, ms, buffers, light, camera);
				}
			} else if (block instanceof KeepsakeCasketBlock) {
				Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new KeepsakeCasketBlockEntity(BlockPos.ZERO, TFBlocks.keepsake_casket.defaultBlockState()), ms, buffers, light, overlay);
			} else if (block instanceof AbstractSkullCandleBlock){
				SkullBlock.Type type = ((AbstractSkullCandleBlock)block).getType();
				SkullModelBase base = SkullCandleTileEntityRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels()).get(type);
				SkullCandleTileEntityRenderer.renderSkull(null, 180.0F, 0.0F, ms, buffers, light, base, RenderType.entityCutoutNoCull(SkullCandleTileEntityRenderer.SKIN_BY_TYPE.get(type)));
				//we put the candle
				ms.translate(0.0F, 0.5F, 0.0F);
				CompoundTag tag = stack.getTagElement("BlockStateTag");
				if(tag != null && tag.contains("color") && tag.contains("candles")) {
					Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
							AbstractSkullCandleBlock.candleColorToCandle(tag.getString("color"))
									.defaultBlockState().setValue(CandleBlock.CANDLES, tag.getInt("candles")), ms, buffers, light, overlay);
					}
			} else {
				BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(dummy);
				renderer.render(null, 0, ms, buffers, light, overlay);
			}
		}
	}
}
