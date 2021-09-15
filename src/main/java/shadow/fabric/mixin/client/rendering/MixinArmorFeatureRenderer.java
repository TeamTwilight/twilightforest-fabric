/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 /*
 This file is a refactor from fabric-api, based on the work of shedaniel.
 */


package shadow.fabric.mixin.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;

import java.util.Map;
import java.util.Objects;

@Mixin(HumanoidArmorLayer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinArmorFeatureRenderer extends RenderLayer {
	@Shadow
	@Final
	private static Map<String, ResourceLocation> ARMOR_LOCATION_CACHE;
	
	public MixinArmorFeatureRenderer(RenderLayerParent context) {
		super(context);
		
	}
	
	@Unique
	private LivingEntity storedEntity;
	@Unique
	private EquipmentSlot storedSlot;
	
	@Inject(method = "render", at = @At("HEAD"))
	private void storeEntity(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		// We store the living entity wearing the armor before we render
		this.storedEntity = livingEntity;
	}
	
	@Inject(method = "renderArmorPiece", at = @At("HEAD"))
	private void storeSlot(PoseStack matrices, MultiBufferSource vertexConsumers, LivingEntity livingEntity, EquipmentSlot slot, int i, HumanoidModel bipedEntityModel, CallbackInfo ci) {
		// We store the current armor slot that is rendering before we render each armor piece
		this.storedSlot = slot;
	}
	
	@Inject(method = "render", at = @At("RETURN"))
	private void removeStored(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		// We remove the stored data after we render
		this.storedEntity = null;
		this.storedSlot = null;
	}
	
	@Inject(method = "getArmorModel", at = @At("RETURN"), cancellable = true)
	private void selectArmorModel(EquipmentSlot slot, CallbackInfoReturnable<HumanoidModel<LivingEntity>> cir) {
		ItemStack stack = storedEntity.getItemBySlot(slot);
		
		HumanoidModel<LivingEntity> defaultModel = cir.getReturnValue();
		HumanoidModel<LivingEntity> model = ArmorRenderingRegistry.getArmorModel(storedEntity, stack, slot, defaultModel);
		
		if (model != defaultModel) {
			cir.setReturnValue(model);
		}
	}
	
	@Inject(method = "getArmorLocation", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private void getArmorTexture(ArmorItem armorItem, boolean secondLayer, /* @Nullable */ String suffix, CallbackInfoReturnable<ResourceLocation> cir, String vanillaIdentifier) {
		String texture = ArmorRenderingRegistry.getArmorTexture(storedEntity, storedEntity.getItemBySlot(storedSlot), storedSlot, secondLayer, suffix, new ResourceLocation(vanillaIdentifier)).toString();
		
		if (!Objects.equals(texture, vanillaIdentifier)) {
			cir.setReturnValue(ARMOR_LOCATION_CACHE.computeIfAbsent(texture, ResourceLocation::new));
		}
	}
}
