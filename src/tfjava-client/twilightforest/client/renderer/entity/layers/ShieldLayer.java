package twilightforest.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.Lich;

public class ShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	public static final ModelResourceLocation LOC = ModelResourceLocation.inventory(TwilightForestMod.prefix("item/shield"));
	private static final Direction[] DIRS = ArrayUtils.add(Direction.values(), null);

	public ShieldLayer(RenderLayerParent<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (this.getShieldCount(entity) > 0) {
			this.renderShields(stack, buffer, entity, partialTicks);
		}
	}

	private int getShieldCount(T entity) {
		return entity instanceof Lich lich ? lich.getShieldStrength() : 0;
	}

	private void renderShields(PoseStack stack, MultiBufferSource buffer, T entity, float partialTicks) {
		float age = entity.tickCount + partialTicks;
		float rotateAngleY = age / -5.0F;
		float rotateAngleX = Mth.sin(age / 5.0F) / 4.0F;
		float rotateAngleZ = Mth.cos(age / 5.0F) / 4.0F;

		int count = getShieldCount(entity);
		for (int c = 0; c < count; c++) {
			stack.pushPose();

			// perform the rotations, accounting for the fact that baked models are corner-based
			// Z gets extra 180 degrees to flip visual upside-down, since scaling y by -1 will cause back-faces to render instead
			stack.mulPose(Axis.ZP.rotationDegrees(180.0F + rotateAngleZ * (180.0F / Mth.PI)));
			stack.mulPose(Axis.YP.rotationDegrees(rotateAngleY * (180.0F / Mth.PI) + (c * (360.0F / count))));
			stack.mulPose(Axis.XP.rotationDegrees(rotateAngleX * (180.0F / Mth.PI)));
			stack.translate(-0.5F, -0.65F, -0.5F);

			// push the shields outwards from the center of rotation
			stack.translate(0.0F, 0.0F, -0.7F);

			BakedModel model = Minecraft.getInstance().getModelManager().getModel(LOC);
			VertexConsumer consumer = buffer.getBuffer(Sheets.translucentCullBlockSheet());
			for (Direction dir : DIRS) {
				for (BakedQuad quad : model.getQuads(null, dir, entity.getRandom())) {
					consumer.putBulkData(stack.last(), quad, 1.0F, 1.0F, 1.0F, 1.0F, 0xF000F0, OverlayTexture.NO_OVERLAY);
				}
			}

			stack.popPose();
		}
	}
}
