package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.MultipartHooks;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.init.TFDimension;
import twilightforest.item.GiantPickItem;

import java.util.Iterator;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

	@Unique
	private static final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);

	@Shadow
	private ClientLevel level;

	@ModifyExpressionValue(
		method = "renderLevel(Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;"
		)
	)
	private Iterator<Entity> twilightforest$resolveEntitiesForRendering(Iterator<Entity> iterator) {
		return MultipartHooks.resolveEntitiesForRendering(iterator);
	}

	@Inject(
		method = "renderSky",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$renderSky(
		Matrix4f frustumMatrix,
		Matrix4f projectionMatrix,
		float partialTick,
		Camera camera,
		boolean isFoggy,
		Runnable skyFogSetup,
		CallbackInfo ci
	) {
		if (this.level != null && TFDimension.isTwilightPortalDestination(this.level)) {
			if (TFSkyRenderer.renderSky(this.level, partialTick, frustumMatrix, camera, projectionMatrix, skyFogSetup)) {
				ci.cancel();
			}
		}
	}

	@Inject(
		method = "renderSnowAndRain",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$renderSnowAndRain(
		LightTexture lightTexture,
		float partialTick,
		double camX,
		double camY,
		double camZ,
		CallbackInfo ci
	) {
		if (this.level != null && TFDimension.isTwilightPortalDestination(this.level)) {
			if (TFWeatherRenderer.renderSnowAndRain(this.level, (int) this.level.getGameTime(), partialTick, lightTexture, new Vec3(camX, camY, camZ))) {
				ci.cancel();
			}
		}
	}

	@Inject(
		method = "tickRain",
		at = @At("HEAD"),
		cancellable = true
	)
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
	private void twilightforest$renderGiantBlockOutlines(
		PoseStack poseStack,
		VertexConsumer consumer,
		Entity entity,
		double camX,
		double camY,
		double camZ,
		BlockPos pos,
		BlockState state,
		CallbackInfo ci
	) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;

		HitResult hitResult = Minecraft.getInstance().hitResult;
		if (!(hitResult instanceof BlockHitResult)) return;

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