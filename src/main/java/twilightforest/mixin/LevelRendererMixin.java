package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.MultipartHooks;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.entity.TFPart;
import twilightforest.init.TFDimension;
import twilightforest.item.GiantPickItem;

import java.util.Iterator;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

	private static final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);

	@Shadow
	private ClientLevel level;

	@Shadow
	private EntityRenderDispatcher entityRenderDispatcher;

	@WrapOperation(
		method = "renderLevel",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"
		)
	)
	private Iterable<Entity> twilightforest$resolveEntitiesForRendering(ClientLevel level, Operation<Iterable<Entity>> original) {
		Iterable<Entity> originalIterable = original.call(level);
		Iterator<Entity> iterator = originalIterable.iterator();
		Iterator<Entity> resolvedIterator = MultipartHooks.resolveEntitiesForRendering(iterator);
		return () -> resolvedIterator;
	}

	@Redirect(
		method = "renderEntity",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
		)
	)
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void twilightforest$redirectRenderEntity(EntityRenderDispatcher dispatcher, Entity entity, double x, double y, double z, float yRot, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer renderer = BakedMultiPartRenderers.lookup(part.renderer());
			if (renderer != null) {
				Vec3 offset = renderer.getRenderOffset(entity, partialTick);
				poseStack.pushPose();
				poseStack.translate(x + offset.x, y + offset.y, z + offset.z);
				renderer.render(entity, yRot, partialTick, poseStack, bufferSource, packedLight);
				poseStack.popPose();
				return;
			}
		}
		this.entityRenderDispatcher.render(entity, x, y, z, yRot, partialTick, poseStack, bufferSource, packedLight);
	}

	@Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
	private void twilightforest$renderSky(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable setupFog, CallbackInfo ci) {
		if (this.level != null && TFDimension.isTwilightPortalDestination(this.level)) {
			if (TFSkyRenderer.renderSky(this.level, partialTick, frustumMatrix, camera, projectionMatrix, setupFog)) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "renderSnowAndRain", at = @At("HEAD"), cancellable = true)
	private void twilightforest$renderSnowAndRain(LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
		if (this.level != null && TFDimension.isTwilightPortalDestination(this.level)) {
			if (TFWeatherRenderer.renderSnowAndRain(this.level, (int) this.level.getGameTime(), partialTick, lightTexture, new Vec3(camX, camY, camZ))) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "tickRain", at = @At("HEAD"), cancellable = true)
	private void twilightforest$tickRain(Camera camera, CallbackInfo ci) {
		if (this.level != null && TFDimension.isTwilightPortalDestination(this.level)) {
			if (TFWeatherRenderer.tickRain(this.level, (int) this.level.getGameTime(), camera.getBlockPosition())) {
				ci.cancel();
			}
		}
	}

	@Inject(
		method = "renderHitOutline",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$renderGiantBlockOutlines(PoseStack poseStack, VertexConsumer consumer, Entity entity, double camX, double camY, double camZ, BlockPos pos, BlockState state, CallbackInfo ci) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;

		HitResult hitResult = Minecraft.getInstance().hitResult;
		if (!(hitResult instanceof BlockHitResult blockHit)) return;

		ItemStack mainHand = player.getMainHandItem();

		// Check if holding a GiantPick or GiantBlock
		boolean isGiantTool = mainHand.getItem() instanceof GiantPickItem;
		boolean isGiantBlock = mainHand.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GiantBlock;
		if (!isGiantTool && !isGiantBlock) return;

		// MiniatureStructureBlock should not show outlines
		if (state.getBlock() instanceof MiniatureStructureBlock) {
			ci.cancel();
			return;
		}

		// Render giant 4x4x4 outline for giant blocks
		BlockPos offsetPos = new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
		Vec3 camPos = new Vec3(camX, camY, camZ);
		Vec3 xyz = Vec3.atLowerCornerOf(offsetPos).subtract(camPos);
		LevelRenderer.renderShape(poseStack, consumer, GIANT_BLOCK, xyz.x(), xyz.y(), xyz.z(), 0.0F, 0.0F, 0.0F, 0.45F);
		ci.cancel();
	}
}