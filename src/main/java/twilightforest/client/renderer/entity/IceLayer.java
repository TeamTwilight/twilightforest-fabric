package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.mojang.math.Vector3f;
import twilightforest.potions.FrostedEffect;

import java.util.Random;

public class IceLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final Random random = new Random();

	public IceLayer(RenderLayerParent<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(FrostedEffect.MODIFIER_UUID) == null) { //Movement speed
			return;
		}

		random.setSeed(entity.getId() * entity.getId() * 3121 + entity.getId() * 45238971);

		// number of cubes
		int numCubes = (int) (entity.getBbHeight() / 0.4F);

		// make cubes
		for (int i = 0; i < numCubes; i++) {
			stack.pushPose();
			float dx = (float) (random.nextGaussian() * 0.2F * entity.getBbWidth());
			float dy = (float) (random.nextGaussian() * 0.2F * entity.getBbHeight()) + entity.getBbHeight() / 2F;
			float dz = (float) (random.nextGaussian() * 0.2F * entity.getBbWidth());
			stack.translate(dx, dy, dz);
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360F));
			stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360F));
			stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360F));
			stack.translate(-0.5F, -0.5F, -0.5F);

			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.ICE.defaultBlockState(), stack, buffer, light, OverlayTexture.NO_OVERLAY);
			stack.popPose();
		}
	}
}
