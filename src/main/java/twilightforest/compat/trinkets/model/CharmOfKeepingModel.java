package twilightforest.compat.trinkets.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

public class CharmOfKeepingModel extends HumanoidModel<LivingEntity> {

	public CharmOfKeepingModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0F), 0.0F);
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-17.0F, 37.0F, -9.5F, 16.0F, 16.0F, 0.0F),
				PartPose.ZERO);

		return LayerDefinition.create(meshDefinition, 32, 16);
	}

	@Nonnull
	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Nonnull
	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
		stack.pushPose();
		stack.scale(0.25F, 0.25F, 0.25F);
		this.body.render(stack, consumer, light, overlay, red, green, blue, alpha);
		stack.popPose();
	}
}
