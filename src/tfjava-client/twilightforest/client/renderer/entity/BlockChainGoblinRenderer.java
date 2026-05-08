package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.codex.twilight.client.render.CodexModelLayers;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ChainModel;
import twilightforest.client.model.entity.SpikeBlockModel;
import twilightforest.entity.monster.BlockChainGoblin;

public class BlockChainGoblinRenderer<T extends BlockChainGoblin, M extends HumanoidModel<T>> extends HumanoidMobRenderer<T, M> {

	private static final ResourceLocation GOBLIN_TEXTURE = TwilightForestMod.getModelTexture("blockgoblin.png");
	private static final ResourceLocation BLOCK_AND_CHAIN_TEXTURE = TwilightForestMod.getModelTexture("block_and_chain.png");

	private final Model model;
	private final Model chainModel;

	public BlockChainGoblinRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize);
		this.model = new SpikeBlockModel(context.bakeLayer(CodexModelLayers.CHAIN_BLOCK));
		this.chainModel = new ChainModel(context.bakeLayer(CodexModelLayers.CHAIN));
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(entity, yaw, partialTicks, stack, buffer, light);

		stack.pushPose();

		Vec3 entityPosition = entity.getPosition(partialTicks);
		Vec3 blockPosition = entity.getSpikePosition();
		double blockInX = (blockPosition.x() - entityPosition.x());
		double blockInY = (blockPosition.y() - entityPosition.y());
		double blockInZ = (blockPosition.z() - entityPosition.z());

		stack.translate(blockInX, blockInY, blockInZ);

		stack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));

		stack.scale(-1.0F, -1.0F, 1.0F);

		this.model.renderToBuffer(stack, buffer.getBuffer(this.model.renderType(BLOCK_AND_CHAIN_TEXTURE)), light, OverlayTexture.NO_OVERLAY);
		stack.popPose();

		if (entity.isAlive()) {
			stack.pushPose();
			stack.translate(0.0D, entity.getEyeHeight(), 0.0D);
			Vec3 eye = entity.getEyePosition(partialTicks);
			renderChain(entity.getChainPart1().subtract(eye), stack, buffer, light, this.chainModel);
			renderChain(entity.getChainPart2().subtract(eye), stack, buffer, light, this.chainModel);
			renderChain(entity.getChainPart3().subtract(eye), stack, buffer, light, this.chainModel);
			renderChain(entity.getSpikePosition().subtract(eye), stack, buffer, light, this.chainModel);
			stack.popPose();
		}
	}

	private static void renderChain(Vec3 offset, PoseStack stack, MultiBufferSource buffer, int light, Model chainModel) {
		stack.pushPose();
		VertexConsumer vertexConsumer = buffer.getBuffer(chainModel.renderType(BLOCK_AND_CHAIN_TEXTURE));
		stack.translate(offset.x(), offset.y(), offset.z());
		stack.scale(-1.0F, -1.0F, 1.0F);
		chainModel.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
		stack.popPose();
	}

	@Override
	public boolean shouldRender(T entity, Frustum frustum, double camX, double camY, double camZ) {
		if (super.shouldRender(entity, frustum, camX, camY, camZ)) {
			return true;
		} else {
			Vec3 vec3d = entity.getSpikePosition();
			Vec3 vec3d1 = entity.getEyePosition(1.0F);
			return frustum.isVisible(new AABB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z));
		}
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return GOBLIN_TEXTURE;
	}
}
