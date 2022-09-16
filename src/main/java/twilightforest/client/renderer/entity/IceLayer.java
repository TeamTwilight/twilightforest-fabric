package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Blocks;
import twilightforest.potions.FrostedEffect;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class IceLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final RandomSource random = RandomSource.create();

	public IceLayer(RenderLayerParent<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(FrostedEffect.MODIFIER_UUID) == null) { //Movement speed
			return;
		}

		this.random.setSeed(entity.getId() * entity.getId() * 3121L + entity.getId() * 45238971L);

		// number of cubes
		int numCubes = (int) (entity.getBbHeight() / 0.4F) + 1;

		// make cubes
		for (int i = 0; i < numCubes; i++) {
			stack.pushPose();
			float dx = ((this.random.nextFloat() * (entity.getBbWidth() * 2.0F)) - entity.getBbWidth()) * 0.1F;
			float dy = Math.max(1.5F - (this.random.nextFloat()) * (entity.getBbHeight()), -0.1F); //gotta limit the height because otherwise frozen giants make blocks spawn like 10 blocks above them
			float dz = ((this.random.nextFloat() * (entity.getBbWidth() * 2.0F)) - entity.getBbWidth()) * 0.1F;
			stack.translate(dx, dy, dz);
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.mulPose(Vector3f.XP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.mulPose(Vector3f.YP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.mulPose(Vector3f.ZP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.translate(-0.5F, -0.5F, -0.5F);

			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.ICE.defaultBlockState(), stack, buffer, light, OverlayTexture.NO_OVERLAY);
			stack.popPose();
		}
	}
}
