package twilightforest.client.renderer.tileentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFConstants;
import twilightforest.block.AbstractTrophyBlock;
import twilightforest.block.TrophyBlock;
import twilightforest.block.TrophyWallBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.tileentity.*;
import twilightforest.enums.BossVariant;
import twilightforest.tileentity.TrophyTileEntity;

import javax.annotation.Nullable;
import java.util.Map;

//Legacy lines are commented out and labeled
public class TrophyTileEntityRenderer implements BlockEntityRenderer<TrophyTileEntity> {

	private final Map<BossVariant, GenericTrophyModel> trophies;

	private static final ResourceLocation textureLocHydra = TFConstants.getModelTexture("hydra4.png");
	private static final ResourceLocation textureLocNaga = TFConstants.getModelTexture("nagahead.png");
	private static final ResourceLocation textureLocLich = TFConstants.getModelTexture("twilightlich64.png");
	private static final ResourceLocation textureLocUrGhast = TFConstants.getModelTexture("towerboss.png");
	private static final ResourceLocation textureLocSnowQueen = TFConstants.getModelTexture("snowqueen.png");
	private static final ResourceLocation textureLocMinoshroom = TFConstants.getModelTexture("minoshroomtaur.png");
	private static final ResourceLocation textureLocKnightPhantom = TFConstants.getModelTexture("phantomskeleton.png");
	private static final ResourceLocation textureLocKnightPhantomArmor = TFConstants.getArmorTexture("phantom_1.png");
	private static final ResourceLocation textureLocYeti = TFConstants.getModelTexture("yetialpha.png");
	private static final ResourceLocation textureLocQuestRam = TFConstants.getModelTexture("questram.png");
	private static final ResourceLocation textureLocQuestRamLines = TFConstants.getModelTexture("questram_lines.png");

	public TrophyTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
		this.trophies = createTrophyRenderers(renderer.getModelSet());
	}

	public static Map<BossVariant, GenericTrophyModel> createTrophyRenderers(EntityModelSet set) {
		ImmutableMap.Builder<BossVariant, GenericTrophyModel> trophyList = ImmutableMap.builder();
		trophyList.put(BossVariant.NAGA, new NagaTrophyModel(set.bakeLayer(TFModelLayers.NAGA_TROPHY)));
		trophyList.put(BossVariant.LICH, new LichTrophyModel(set.bakeLayer(TFModelLayers.LICH_TROPHY)));
		trophyList.put(BossVariant.MINOSHROOM, new MinoshroomTrophyModel(set.bakeLayer(TFModelLayers.MINOSHROOM_TROPHY)));
		trophyList.put(BossVariant.HYDRA, new HydraTrophyModel(set.bakeLayer(TFModelLayers.HYDRA_TROPHY)));
		trophyList.put(BossVariant.KNIGHT_PHANTOM, new KnightPhantomTrophyModel(set.bakeLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY)));
		trophyList.put(BossVariant.UR_GHAST, new UrGhastTrophyModel(set.bakeLayer(TFModelLayers.UR_GHAST_TROPHY)));
		trophyList.put(BossVariant.ALPHA_YETI, new AlphaYetiTrophyModel(set.bakeLayer(TFModelLayers.ALPHA_YETI_TROPHY)));
		trophyList.put(BossVariant.SNOW_QUEEN, new SnowQueenTrophyModel(set.bakeLayer(TFModelLayers.SNOW_QUEEN_TROPHY)));
		trophyList.put(BossVariant.QUEST_RAM, new QuestRamTrophyModel(set.bakeLayer(TFModelLayers.QUEST_RAM_TROPHY)));
		return trophyList.build();
	}

	public static ItemStack stack = new ItemStack(TFBlocks.naga_trophy);
	@Override
	public void render(TrophyTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		float f = tileEntityIn.getAnimationProgress(partialTicks);
		BlockState blockstate = tileEntityIn.getBlockState();
		boolean flag = blockstate.getBlock() instanceof TrophyWallBlock;
		Direction direction = flag ? blockstate.getValue(TrophyWallBlock.FACING) : null;
		float f1 = 22.5F * (flag ? (2 + direction.get2DDataValue()) * 4 : blockstate.getValue(TrophyBlock.ROTATION));
		BossVariant variant = ((AbstractTrophyBlock) blockstate.getBlock()).getVariant();
		GenericTrophyModel trophy = this.trophies.get(variant);
		matrixStackIn.pushPose();
		if (((AbstractTrophyBlock) blockstate.getBlock()).getVariant() == BossVariant.HYDRA) {
			//FIXME: both rotation points are legacy
			//hydraHead.mouth.setRotationPoint(0.0F, 15.0F, -19.0F); (flag = true)
			//hydraHead.mouth.setRotationPoint(0.0F, 10.0F, -20.0F); (flag = false)
			((HydraTrophyModel) trophy).openMouthForTrophy(flag ? 0.5F : 0.0F);
		}
		if (((AbstractTrophyBlock) blockstate.getBlock()).getVariant() == BossVariant.UR_GHAST) {
			((UrGhastTrophyModel)trophy).setTranslate(matrixStackIn, 0F, 1.0F, 0F);
		}
		render(direction, f1, trophy, variant, f, matrixStackIn, bufferIn, combinedLightIn, ItemTransforms.TransformType.NONE);
		matrixStackIn.popPose();
	}

	public static void render(@Nullable Direction directionIn, float y, GenericTrophyModel trophy, BossVariant variant, float animationProgress, PoseStack matrixStackIn, MultiBufferSource buffer, int combinedLight, ItemTransforms.TransformType camera) {
		matrixStackIn.pushPose();
		if (directionIn == null || variant == BossVariant.UR_GHAST) {
			matrixStackIn.translate(0.5D, 0.0D, 0.5D);
		} else {
			matrixStackIn.translate(0.5F - directionIn.getStepX() * 0.249F, 0.25D, 0.5F - directionIn.getStepZ() * 0.249F);
		}
		matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
		switch (variant) {
		case HYDRA:
			matrixStackIn.scale(0.25F, 0.25F, 0.25F);
			matrixStackIn.translate(0.0F, -1.0F, 0.0F);
			if (camera == ItemTransforms.TransformType.GUI) ((HydraTrophyModel)trophy).openMouthForTrophy(0.35F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer hydraVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocHydra));
			((HydraTrophyModel)trophy).head.render(matrixStackIn, hydraVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			break;
		case NAGA:
			matrixStackIn.scale(0.5f, 0.5f, 0.5f);
			matrixStackIn.translate(0F, .25F, 0F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer nagaVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocNaga));
			((NagaTrophyModel)trophy).head.render(matrixStackIn, nagaVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			break;
		case LICH:
			matrixStackIn.translate(0.0F, .25F, 0.0F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer lichVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocLich));
			((LichTrophyModel)trophy).head.render(matrixStackIn, lichVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			break;
		case UR_GHAST:
			matrixStackIn.scale(0.5F, 0.5F, 0.5F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer ghastVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocUrGhast));
			((UrGhastTrophyModel)trophy).body.render(matrixStackIn, ghastVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			break;
		case SNOW_QUEEN:
			//FIXME: Legacy
			//matrixStackIn.translate(0.0F, 0.25F, 0.0F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer waifuVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocSnowQueen));
			((SnowQueenTrophyModel)trophy).head.render(matrixStackIn, waifuVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			break;
		case MINOSHROOM:
			//FIXME: Legacy
			//matrixStackIn.translate(0.0F, 0.25F, 0.0F);
			matrixStackIn.translate(0.0F, 0.31F, 0.0F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer minoVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocMinoshroom));
			((MinoshroomTrophyModel)trophy).head.render(matrixStackIn, minoVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			break;
		case KNIGHT_PHANTOM:
			matrixStackIn.translate(0.0F, 0.25F, 0.0F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer phantomVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocKnightPhantom));
			((KnightPhantomTrophyModel)trophy).head.render(matrixStackIn, phantomVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			
			matrixStackIn.scale(1.1F, 1.1F, 1.1F);
			matrixStackIn.translate(0.0F, 0.05F, 0.0F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer phantomArmorVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocKnightPhantomArmor));
			((KnightPhantomTrophyModel)trophy).helmet.render(matrixStackIn, phantomArmorVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.0625F);
			break;
		case ALPHA_YETI:
			matrixStackIn.scale(0.2F, 0.2F, 0.2F);
			matrixStackIn.translate(0.0F, -1.5F, 0.0F);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer yetiVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocYeti));
			((AlphaYetiTrophyModel)trophy).main.render(matrixStackIn, yetiVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			break;
		case QUEST_RAM:
			matrixStackIn.scale(0.7f, 0.7f, 0.7f);
			trophy.setRotations(animationProgress * 4.5F, y, 0.0F);
			VertexConsumer ramVertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocQuestRam));
			((QuestRamTrophyModel)trophy).head.render(matrixStackIn, ramVertex, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			break;
		default:
			break;
		}
		matrixStackIn.popPose();
	}
}
