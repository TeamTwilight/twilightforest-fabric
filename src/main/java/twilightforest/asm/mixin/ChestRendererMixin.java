package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.renderer.block.TFChestRenderer;

@Mixin(ChestRenderer.class)
public class ChestRendererMixin<T extends BlockEntity & LidBlockEntity> {

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
		at = @At("TAIL")
	)
	private void twilightforest$addState(
		T blockEntity,
		ChestRenderState state,
		float partialTicks,
		Vec3 cameraPosition,
		ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress,
		CallbackInfo ci
	) {
		state.setData(TFChestRenderer.CUSTOM_CHEST_SPRITE_KEY, this.twilightforest$getCustomSprite(blockEntity, state));
	}

	@ModifyExpressionValue(
		method = "submit(Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/Sheets;chooseSprite(Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState$ChestMaterialType;Lnet/minecraft/world/level/block/state/properties/ChestType;)Lnet/minecraft/client/resources/model/sprite/SpriteId;"
		)
	)
	private SpriteId twilightforest$setCustomSprite(
		SpriteId original,
		@Local(argsOnly = true, name = "state") ChestRenderState state
	) {
		return state.getData(TFChestRenderer.CUSTOM_CHEST_SPRITE_KEY) != null ? state.getData(TFChestRenderer.CUSTOM_CHEST_SPRITE_KEY) : original;
	}

	@Unique
	@Nullable
	private  <E extends BlockEntity & LidBlockEntity> SpriteId twilightforest$getCustomSprite(E blockEntity, ChestRenderState renderState) {
		var materials = TFChestRenderer.MATERIALS.get(blockEntity.getBlockState().getBlock());
		return materials != null ? materials.get(renderState.type) : null;
	}
}