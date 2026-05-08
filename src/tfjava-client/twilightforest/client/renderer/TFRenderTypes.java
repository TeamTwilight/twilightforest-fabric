package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.entity.LichRenderer;

public class TFRenderTypes extends RenderType {
	public TFRenderTypes(String name, VertexFormat vertexFormat, VertexFormat.Mode mode, int bufferSize, boolean crumbling, boolean sort, Runnable setup, Runnable clear) {
		super(name, vertexFormat, mode, bufferSize, crumbling, sort, setup, clear);
	}

	public static final RenderType PROTECTION_BOX = RenderType.energySwirl(TwilightForestMod.getModelTexture("protectionbox.png"), 0.0F, 0.0F);
	public static final RenderType SHADOW_CLONE = RenderType.entityTranslucentCull(LichRenderer.TEXTURE);

	public static final class ProtectionBoxTexturingStateShard extends RenderStateShard.TexturingStateShard {
		public ProtectionBoxTexturingStateShard() {
			super("protection_offset_texturing", () -> {
				float tick = (float) (Minecraft.getInstance().cameraEntity != null ? Minecraft.getInstance().cameraEntity.tickCount : 0) + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
				RenderSystem.setTextureMatrix((new Matrix4f()).translation((-tick * 0.06F) % 1.0F, (-tick * 0.035F) % 1.0F, 0.0F).scale(0.5F));
			}, RenderSystem::resetTextureMatrix);
		}
	}
}
