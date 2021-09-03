package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.TFModelLayers;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.tileentity.KeepsakeCasketTileEntity;

import java.util.Locale;

/**
 * Keepsake Casket Model - MCVinnyq
 * Created using Tabula 8.0.0
 */
//Most of the other stuff is derived from ChestTileEntityRenderer
@Environment(EnvType.CLIENT)
public class CasketTileEntityRenderer<T extends KeepsakeCasketTileEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
    public ModelPart base;
    public ModelPart lid;

    public CasketTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
        var root = renderer.bakeLayer(TFModelLayers.KEEPSAKE_CASKET);

        this.base = root.getChild("base");
        this.lid = root.getChild("lid");
    }

    public static LayerDefinition create() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("lid",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8.0F, -8.0F, -13.0F, 16.0F, 10.0F, 14.0F)
                        .texOffs(0, 46)
                        .addBox(-8.0F, -10.0F, -13.0F, 16.0F, 2.0F, 0.0F)
                        .texOffs(2, 34)
                        .addBox(-7.99F, -10.0F, -12.0F, 0.0F, 2.0F, 14.0F)
                        .texOffs(2, 36)
                        .addBox(7.99F, -10.0F, -12.0F, 0.0F, 2.0F, 14.0F),
                PartPose.offset(0.0F, -6.0F, 6.0F));
        partdefinition.addOrReplaceChild("base",
                CubeListBuilder.create()
                        .texOffs(1, 28)
                        .addBox(-7.0F, -10.0F, -2.0F, 14.0F, 10.0F, 8.0F)
                        .texOffs(0, 26)
                        .addBox(-7.0F, -10.0F, -6.0F, 1.0F, 6.0F, 4.0F)
                        .texOffs(40, 26)
                        .addBox(6.0F, -10.0F, -6.0F, 1.0F, 6.0F, 4.0F)
                        .texOffs(0, 56)
                        .addBox(-7.0F, -4.0F, -6.0F, 14.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, -0.01F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void render(T tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Level world = tileEntityIn.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? tileEntityIn.getBlockState() : TFBlocks.keepsake_casket.get().defaultBlockState();
        Block block = blockstate.getBlock();
        if (block instanceof KeepsakeCasketBlock) {
            BlockLoggingEnum type = blockstate.getValue(BlockLoggingEnum.MULTILOGGED);
            int damage = blockstate.getValue(KeepsakeCasketBlock.BREAKAGE);
            boolean solid = type.getBlock() != Blocks.AIR && type.getFluid() == Fluids.EMPTY;
            float facing = blockstate.getValue(HorizontalDirectionalBlock.FACING).toYRot();

            if(solid) {
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.5D, 0.5D, 0.5D);
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-facing));
                matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
                ResourceLocation BLOCK = TwilightForestMod.prefix("block/casket_" + type.getBlock().getRegistryName().getPath().toLowerCase(Locale.ROOT));
                BakedModel blockrender = Minecraft.getInstance().getModelManager().getModel(BLOCK);
                BlockRenderDispatcher render = Minecraft.getInstance().getBlockRenderer();
                render.getModelRenderer().tesselateBlock(world, blockrender, type.getBlock().defaultBlockState(), tileEntityIn.getBlockPos(), matrixStackIn, bufferIn.getBuffer(RenderType.translucent()), false, world.random, Mth.getSeed(BlockPos.ZERO), OverlayTexture.NO_OVERLAY);
                matrixStackIn.popPose();
            }

            matrixStackIn.pushPose();
            matrixStackIn.scale(-1, -1, -1);
            matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-facing));

            DoubleBlockCombiner.NeighborCombineResult<? extends KeepsakeCasketTileEntity> icallbackwrapper = DoubleBlockCombiner.Combiner::acceptNone;
            float f1 = icallbackwrapper.apply(KeepsakeCasketBlock.getLidRotationCallback(tileEntityIn)).get(partialTicks);
            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;

            ResourceLocation CASKET = TwilightForestMod.getModelTexture("casket/keepsake_casket_" + damage + ".png");
            this.renderModels(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutoutNoCull(CASKET)), this.lid, this.base, f1, combinedLightIn, combinedOverlayIn);
            matrixStackIn.popPose();
        }
    }

    private void renderModels(PoseStack matrixStackIn, VertexConsumer bufferIn, ModelPart lid, ModelPart base, float lidAngle, int combinedLightIn, int combinedOverlayIn) {
        lid.xRot = -(lidAngle * ((float)Math.PI / 2F));
        lid.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        base.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
