package twilightforest.client.model.armor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import twilightforest.components.entity.TravellersWingsAnimAttachment;
import twilightforest.components.entity.TravellersWingsAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.util.TFMathUtil;

import java.util.Collections;
import java.util.List;

public class TravellersWingsModel extends HumanoidModel<net.minecraft.client.renderer.entity.state.HumanoidRenderState> {
	private static final double TAU = 4;  // Time (in ticks) in which distance reduces in e times
	private static final float ANGLE_10_DEG = Mth.PI / 18;
	private static final Vector3f SMALL_SWING = new Vector3f(8.0F, 8.0F, 8.0F);
	private static final Vector3f BIG_SWING = new Vector3f(15.0F, 15.0F, 15.0F);

	private static final float BASE_OFFSET = -0.50F;
	private static final float PART_OFFSET = 0.002f;

	private final ModelPart wingBaseRight;
	private final ModelPart wingBaseLeft;
	private final List<ModelPart> wingPartsRight;
	private final List<ModelPart> wingPartsLeft;
	private final Camera mainCamera;

	public TravellersWingsModel(ModelPart root) {
		super(root);
		root = root.getChild("body");
		this.wingBaseLeft = root.getChild("wingBaseLeft");
		this.wingPartsLeft = List.of(
			this.wingBaseLeft.getChild("wingEdgeLeft"),
			this.wingBaseLeft.getChild("wingInsetLeft"),
			this.wingBaseLeft.getChild("wingCenterLeft"),
			this.wingBaseLeft.getChild("wingFlangeLeft"),
			this.wingBaseLeft.getChild("wingAuxLeft")
		);
		this.wingBaseRight = root.getChild("wingBaseRight");
		this.wingPartsRight = List.of(
			this.wingBaseRight.getChild("wingEdgeRight"),
			this.wingBaseRight.getChild("wingInsetRight"),
			this.wingBaseRight.getChild("wingCenterRight"),
			this.wingBaseRight.getChild("wingFlangeRight"),
			this.wingBaseRight.getChild("wingAuxRight")
		);
		this.body.skipDraw = true;
		this.mainCamera = Minecraft.getInstance().gameRenderer.getMainCamera();
	}

	public static LayerDefinition createLayer(float deformation) {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(deformation), 0);
		PartDefinition root = mesh.getRoot().getChild("body");
		createWings(root);
		createBelt(root, 0.0F);

		return LayerDefinition.create(mesh, 128, 32);
	}

	protected static void createWings(PartDefinition root) {
		PartDefinition wbl = root.addOrReplaceChild("wingBaseLeft", CubeListBuilder.create()
				.texOffs(64, 9).mirror()
				.addBox(BASE_OFFSET, -1.0F, 0.0F, 1, 2, 10),
			PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, ANGLE_10_DEG * 3, ANGLE_10_DEG * 3, 0.0F));

		wbl.addOrReplaceChild("wingEdgeLeft", CubeListBuilder.create()
				.texOffs(64, 21).mirror()
				.addBox(0.0F, 0.0F, -2.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 1, -1.0F, 10.0F, ANGLE_10_DEG * 3, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingInsetLeft", CubeListBuilder.create()
				.texOffs(70, 21).mirror()
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 2, 0.0F, 7.8F, ANGLE_10_DEG * 2, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingCenterLeft", CubeListBuilder.create()
				.texOffs(76, 21).mirror()
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 3, 0.3F, 6.3F, ANGLE_10_DEG, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingFlangeLeft", CubeListBuilder.create()
				.texOffs(82, 21).mirror()
				.addBox(0.0F, 0.0F, -1.0F, 1, 8, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 4, 0.3F, 5.1F, 0.0F, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingAuxLeft", CubeListBuilder.create()
				.texOffs(88, 21).mirror()
				.addBox(0.0F, 0.0F, -1.0F, 1, 7, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 5, 0.1F, 4.0F, -ANGLE_10_DEG, 0.0F, 0.0F));

		PartDefinition wbr = root.addOrReplaceChild("wingBaseRight", CubeListBuilder.create()
				.texOffs(98, 9)
				.addBox(BASE_OFFSET, -1.0F, 0.0F, 1, 2, 10),
			PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, ANGLE_10_DEG * 3, -ANGLE_10_DEG * 3, 0.0F));

		wbr.addOrReplaceChild("wingEdgeRight", CubeListBuilder.create()
				.texOffs(98, 21)
				.addBox(0.0F, 0.0F, -2.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 1, -1.0F, 10.0F, ANGLE_10_DEG * 3, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingInsetRight", CubeListBuilder.create()
				.texOffs(104, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 2, 0.0F, 7.8F, ANGLE_10_DEG * 2, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingCenterRight", CubeListBuilder.create()
				.texOffs(110, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 3, 0.3F, 6.3F, ANGLE_10_DEG, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingFlangeRight", CubeListBuilder.create()
				.texOffs(116, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 8, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 4, 0.3F, 5.1F, 0.0F, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingAuxRight", CubeListBuilder.create()
				.texOffs(122, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 7, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 5, 0.1F, 4.0F, -ANGLE_10_DEG, 0.0F, 0.0F));

	}

	protected static void createBelt(PartDefinition root, float deformation) {
		CubeDeformation cubeDeformation = new CubeDeformation(deformation);
		root.addOrReplaceChild("buckle", CubeListBuilder.create()
				.texOffs(8, 9)
				.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 1, cubeDeformation),
			PartPose.offset(0.0F, 10.0F, -2.75F)
		);

		root.addOrReplaceChild("frontRight", CubeListBuilder.create()
				.texOffs(0, 9)
				.addBox(-3.0F, -1.0F, 0.0F, 3, 2, 1, cubeDeformation),
			PartPose.offset(-1.75F, 10.0F, -2.5F)
		);

		root.addOrReplaceChild("frontLeft", CubeListBuilder.create()
				.texOffs(18, 9)
				.addBox(0.0F, -1.0F, 0.0F, 3, 2, 1, cubeDeformation),
			PartPose.offset(1.75F, 10.0F, -2.5F)
		);

		root.addOrReplaceChild("sideRight", CubeListBuilder.create()
				.texOffs(0, 3)
				.addBox(-1.0F, -1.0F, 0.0F, 1, 2, 4, cubeDeformation.extend(-0.01F)),
			PartPose.offset(-3.75F, 10.0F, -2.0F)
		);

		root.addOrReplaceChild("sideLeft", CubeListBuilder.create()
				.texOffs(16, 3)
				.addBox(0.0F, -1.0F, 0.0F, 1, 2, 4, cubeDeformation.extend(-0.01F)),
			PartPose.offset(3.75F, 10.0F, -2.0F)
		);

		root.addOrReplaceChild("back", CubeListBuilder.create()
				.texOffs(2, 0)
				.addBox(-4.5F, -1.0F, 0.0F, 9, 2, 1, cubeDeformation),
			PartPose.offset(0.0F, 10.0F, 1.5F)
		);
	}

	@Override
	public void setupAnim(net.minecraft.client.renderer.entity.state.HumanoidRenderState state) {
		super.setupAnim(state);
		// TODO [Fabric] the live wings animation relies on entity data (wings
		// attachments, walk animation). That data needs to be piped into the
		// render state during extract before the animation can be restored.
	}

	private Vector3f calculateRotations(TravellersWingsAnimAttachment attachment, double dtInTicks, float phaseDivisor, float xOffset, float yOffset, float zOffset, Vector3f sinDivisors) {
		attachment.accumulatedPhase += dtInTicks / phaseDivisor;
		float sinT = (float) Math.sin(attachment.accumulatedPhase);
		return new Vector3f(
			sinT / sinDivisors.x + xOffset,
			sinT / sinDivisors.y + yOffset,
			sinT / sinDivisors.z + zOffset
		);
	}

	public static void skipWings(ModelPart leggingsLayer, boolean skip) {
		ModelPart body = leggingsLayer.getChild("body");

		ModelPart wbl = body.getChild("wingBaseLeft");
		wbl.skipDraw = skip;
		wbl.getChild("wingEdgeLeft").skipDraw = skip;
		wbl.getChild("wingInsetLeft").skipDraw = skip;
		wbl.getChild("wingCenterLeft").skipDraw = skip;
		wbl.getChild("wingFlangeLeft").skipDraw = skip;
		wbl.getChild("wingAuxLeft").skipDraw = skip;

		ModelPart wbr = body.getChild("wingBaseRight");
		wbr.skipDraw = skip;
		wbr.getChild("wingEdgeRight").skipDraw = skip;
		wbr.getChild("wingInsetRight").skipDraw = skip;
		wbr.getChild("wingCenterRight").skipDraw = skip;
		wbr.getChild("wingFlangeRight").skipDraw = skip;
		wbr.getChild("wingAuxRight").skipDraw = skip;
	}

	public static void skipBelt(ModelPart leggingsLayer, boolean skip) {
		ModelPart body = leggingsLayer.getChild("body");

		body.getChild("buckle").skipDraw = skip;
		body.getChild("frontRight").skipDraw = skip;
		body.getChild("frontLeft").skipDraw = skip;
		body.getChild("sideRight").skipDraw = skip;
		body.getChild("sideLeft").skipDraw = skip;
		body.getChild("back").skipDraw = skip;
	}
}
