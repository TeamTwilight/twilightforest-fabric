package twilightforest.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import io.github.fabricators_of_create.porting_lib.client.armor.ArmorRenderer;
import io.github.fabricators_of_create.porting_lib.client_extensions.IClientItemExtensions;
import io.github.fabricators_of_create.porting_lib.core.util.Lazy;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.init.TFDataAttachments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class TFArmorRenderer implements IClientItemExtensions, ArmorRenderer {
	public static final List<TFArmorRenderer> INSTANCES = new ArrayList<>();
	protected final Map<ModelLayerLocation, Lazy<ModelPart>> ARMOR_MODELS = new HashMap<>();

	public TFArmorRenderer(ModelLayerLocation... layerLocations) {
		for (ModelLayerLocation layerLocation : layerLocations) {
			this.ARMOR_MODELS.put(
				layerLocation,
				Lazy.of(() -> Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation))
			);
		}
		INSTANCES.add(this);
	}

	public void resetModelCache() {
		ARMOR_MODELS.values().forEach(Lazy::invalidate);
	}

	public static void resetAllModelCache() {
		INSTANCES.forEach(TFArmorRenderer::resetModelCache);
	}

	protected ModelPart getModelPart(ModelLayerLocation layerLocation) {
		return ARMOR_MODELS.get(layerLocation).get();
	}

	/**
	 * Returns the custom armor model for the given slot.
	 * Replaces the old getHumanoidArmorModel which was removed from IClientItemExtensions in 1.21.1.
	 */
	protected abstract HumanoidModel<?> getArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> contextModel);

	@Override
	@SuppressWarnings("unchecked")
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel, HumanoidModel<LivingEntity> armorModel) {
		if (!(stack.getItem() instanceof ArmorItem armorItem)) return;

		HumanoidModel<?> customModel = this.getArmorModel(entity, stack, slot, contextModel);

		// Call setupModelAnimations first (sets wing animation), then copyPropertiesTo overrides
		// the standard limb/head positions from the context model without touching wing parts.
		if (customModel instanceof TravellersWingsModel wingsModel) {
			float limbSwing = entity.walkAnimation.position();
			float limbSwingAmount = Math.min(entity.walkAnimation.speed(), 1.0F);
			float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
			wingsModel.setupModelAnimations(entity, limbSwing, limbSwingAmount, entity.tickCount + partialTick, 0, 0);
		}

		((HumanoidModel) contextModel).copyPropertiesTo(customModel);
		this.setPartVisibility(customModel, slot);

		ArmorMaterial material = armorItem.getMaterial().value();
		boolean innerModel = slot == EquipmentSlot.LEGS;
		boolean useGogglesTexture = !innerModel
			&& slot == EquipmentSlot.HEAD
			&& entity.getAttachedOrCreate(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER);

		// Mimics vanilla HumanoidArmorLayer#renderArmorPiece: every material layer is
		// rendered, with dyeable layers tinted by the item's dye color (falling back to
		// the renderer's default dye color) and non-dyeable layers (e.g. the arctic fur
		// overlay) rendered untinted.
		int dyeColor = stack.is(ItemTags.DYEABLE)
			? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, this.getDefaultDyeColor(stack)))
			: -1;

		for (ArmorMaterial.Layer layer : material.layers()) {
			ResourceLocation texture = useGogglesTexture
				? TwilightForestMod.prefix("textures/models/armor/travellers_layer_1_down.png")
				: layer.texture(innerModel);

			VertexConsumer consumer = vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(texture));
			((HumanoidModel) customModel).renderToBuffer(matrices, consumer, light, OverlayTexture.NO_OVERLAY, layer.dyeable() ? dyeColor : -1);
		}

		if (stack.hasFoil()) {
			((HumanoidModel) customModel).renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY);
		}
	}

	/**
	 * Returns the color used to tint dyeable armor layers when the stack carries no
	 * dye_color component. Mirrors NeoForge's IClientItemExtensions#getDefaultDyeColor.
	 * Defaults to vanilla leather's dye color.
	 */
	public int getDefaultDyeColor(ItemStack stack) {
		return 0xFF626262;
	}

	private void setPartVisibility(HumanoidModel<?> model, EquipmentSlot slot) {
		model.setAllVisible(false);
		switch (slot) {
			case HEAD -> {
				model.head.visible = true;
				model.hat.visible = true;
			}
			case CHEST -> {
				model.body.visible = true;
				model.rightArm.visible = true;
				model.leftArm.visible = true;
			}
			case LEGS -> {
				model.body.visible = true;
				model.rightLeg.visible = true;
				model.leftLeg.visible = true;
			}
			case FEET -> {
				model.rightLeg.visible = true;
				model.leftLeg.visible = true;
			}
		}
	}

	public static final class ResourceReloadListener implements IdentifiableResourceReloadListener {
		@Override
		public ResourceLocation getFabricId() {
			return TwilightForestMod.prefix("armor_renderer");
		}

		@Override
		public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
			return barrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
				TFArmorRenderer.resetAllModelCache();
			}, gameExecutor);
		}
	}
}