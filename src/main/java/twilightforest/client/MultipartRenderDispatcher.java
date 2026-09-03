package twilightforest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.TFPart;

import java.util.ArrayList;
import java.util.List;

public class MultipartRenderDispatcher implements DebugRenderer.SimpleDebugRenderer {
	public static final MultipartRenderDispatcher INSTANCE = new MultipartRenderDispatcher();

	private final RenderStateDataKey<List<PartRender>> partRenders = RenderStateDataKey.create((() -> "multipart_renders"));
	private final int hitboxColor = ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F);

	public static void init() {
		LevelRenderEvents.END_EXTRACTION.register(INSTANCE::extractPartRenderStates);
		LevelRenderEvents.COLLECT_SUBMITS.register(INSTANCE::submitPartRenderStates);
	}

	private void extractPartRenderStates(LevelExtractionContext context) {
		ClientLevel level = context.level();
		LevelRenderState renderState = context.levelState();
		DeltaTracker deltaTracker = context.deltaTracker();
		TickRateManager tickRateManager = level.tickRateManager();
		Frustum frustum = context.camera().getCullFrustum();
		Vec3 cameraPos = context.camera().position();
		boolean showOutlines = context.levelRenderer().shouldShowEntityOutlines();
		List<PartRender> renders = new ArrayList<>();

		TFPart.forEachPart(level.entitiesForRendering(), part -> {
			EntityRenderer<Entity, EntityRenderState> renderer = BakedMultiPartRenderers.lookup(part.renderer());
			if (!renderer.shouldRender(part, frustum, cameraPos.x(), cameraPos.y(), cameraPos.z()))
				return;

			EntityRenderState state = renderer.createRenderState(part, deltaTracker.getGameTimeDeltaPartialTick(!tickRateManager.isEntityFrozen(part)));
			renders.add(new PartRender(renderer, state));
			if (showOutlines && state.appearsGlowing())
				renderState.haveGlowingEntities = true;
		});

		renderState.setData(this.partRenders, renders);
	}

	private void submitPartRenderStates(LevelRenderContext context) {
		LevelRenderState renderState = context.levelState();
		@Nullable List<PartRender> renders = renderState.getData(this.partRenders);
		if (renders == null || renders.isEmpty())
			return;

		CameraRenderState cameraRenderState = renderState.cameraRenderState;
		Vec3 cameraPos = cameraRenderState.pos;
		PoseStack stack = context.poseStack();

		for (PartRender render : renders) {
			EntityRenderState state = render.state();
			if (!renderState.haveGlowingEntities)
				state.outlineColor = 0;

			Vec3 offset = render.renderer().getRenderOffset(state);
			stack.pushPose();
			stack.translate(state.x - cameraPos.x() + offset.x(), state.y - cameraPos.y() + offset.y(), state.z - cameraPos.z() + offset.z());
			render.renderer().submit(state, stack, context.submitNodeCollector(), cameraRenderState);
			stack.popPose();
		}
	}

	@Override
	public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null || !minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.ENTITY_HITBOXES))
			return;

		TFPart.forEachPart(minecraft.level.entitiesForRendering(), part -> {
			if (part.isInvisible())
				return;

			AABB box = part.getBoundingBox();
			if (!frustum.isVisible(box))
				return;

			Gizmos.cuboid(box.move(part.getPosition(partialTicks).subtract(part.position())), GizmoStyle.stroke(this.hitboxColor));
		});
	}

	private record PartRender(EntityRenderer<Entity, EntityRenderState> renderer, EntityRenderState state) {
	}
}