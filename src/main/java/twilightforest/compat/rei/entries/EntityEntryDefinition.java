package twilightforest.compat.rei.entries;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.fabricators_of_create.porting_lib.entity.MultiPartEntity;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EntityEntryDefinition implements EntryDefinition<Entity>, EntrySerializer<Entity> {

	public static final EntryType<Entity> ENTITY_TYPE = EntryType.deferred(new ResourceLocation("entity"));

	@Environment(EnvType.CLIENT)
	private EntryRenderer<Entity> renderer;

	public EntityEntryDefinition() {
		EnvExecutor.runInEnv(Env.CLIENT, () -> () -> EntityEntryDefinition.Client.init(this));
	}

	@Environment(EnvType.CLIENT)
	private static class Client {
		private static void init(EntityEntryDefinition definition) {
			definition.renderer = definition.new EntityEntryRenderer();
		}
	}

	@Override
	public Class<Entity> getValueType() {
		return Entity.class;
	}

	@Override
	public EntryType<Entity> getType() {
		return ENTITY_TYPE;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public EntryRenderer<Entity> getRenderer() {
		return renderer;
	}

	@Override
	public @Nullable ResourceLocation getIdentifier(EntryStack<Entity> entry, Entity value) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(value.getType());
	}

	@Override
	public boolean isEmpty(EntryStack<Entity> entry, Entity value) {
		return false;//value.isAlive();
	}

	@Override
	public Entity copy(EntryStack<Entity> entry, Entity value) {
		CompoundTag tag = new CompoundTag();

		String string = value.getEncodeId();

		if (string != null) {
			tag.putString("id", string);
			value.saveWithoutId(tag);
		}

		Entity entity = value.getType().create(Minecraft.getInstance().level);

		entity.load(tag);

		return entity;
	}

	@Override
	public Entity normalize(EntryStack<Entity> entry, Entity value) {
		return copy(entry, value);
	}

	@Override
	public Entity wildcard(EntryStack<Entity> entry, Entity value) {
		return value.getType().create(Minecraft.getInstance().level);
	}

	@Override
	public long hash(EntryStack<Entity> entry, Entity value, ComparisonContext context) {
		int code = 1;
		code = 31 * code + System.identityHashCode(value.getType());
		code = 31 * code + Long.hashCode(EntityTypeComparatorRegistryImpl.INSTANCE.hashOf(context, value));
		return code;
	}

	@Override
	public boolean equals(Entity o1, Entity o2, ComparisonContext context) {
		if (o1.getType() != o2.getType())
			return false;
		return EntityTypeComparatorRegistryImpl.INSTANCE.hashOf(context, o1) == EntityTypeComparatorRegistryImpl.INSTANCE.hashOf(context, o2);
	}

	@Override
	public @Nullable EntrySerializer<Entity> getSerializer() {
		return null;
	}

	private static final ReferenceSet<EntityType<Entity>> SEARCH_BLACKLISTED = new ReferenceOpenHashSet<>();

	@Override
	public Component asFormattedText(EntryStack<Entity> entry, Entity value) {
		return asFormattedText(entry, value, TooltipContext.of());
	}

	@Override
	public Component asFormattedText(EntryStack<Entity> entry, Entity value, TooltipContext context) {
		if (!SEARCH_BLACKLISTED.contains(value.getType()))
			try {
				return value.getName();
			} catch (Throwable e) {
				if (context != null && context.isSearch()) throw e;
				TwilightForestMod.LOGGER.error(e);
				SEARCH_BLACKLISTED.add((EntityType<Entity>) value.getType());
			}
		try {
			return Component.literal(I18n.get("entity." + BuiltInRegistries.ENTITY_TYPE.getKey(value.getType()).toString().replace(":", ".")));
		} catch (Throwable e) {
			TwilightForestMod.LOGGER.error(e);
		}
		return Component.literal("ERROR");
	}

	@Environment(EnvType.CLIENT)
	private List<Component> tryGetEntityToolTip(EntryStack<Entity> entry, Entity value, TooltipContext context) {
		if (!SEARCH_BLACKLISTED.contains(value.getType()))
			try {
				return Lists.newArrayList(value.getName());
			} catch (Throwable e) {
				if (context.isSearch()) throw e;
				TwilightForestMod.LOGGER.error(e);
				SEARCH_BLACKLISTED.add((EntityType<Entity>) value.getType());
			}
		return Lists.newArrayList(asFormattedText(entry, value, context));
	}

	@Override
	public Stream<? extends TagKey<?>> getTagsFor(EntryStack<Entity> entry, Entity value) {
		return value.getType().builtInRegistryHolder().tags();
	}

	@Override
	public boolean supportSaving() {
		return true;
	}

	@Override
	public boolean supportReading() {
		return true;
	}

	@Override
	public CompoundTag save(EntryStack<Entity> entry, Entity value) {
		CompoundTag tag = new CompoundTag();

		String string = value.getEncodeId();

		if (string != null) {
			tag.putString("id", string);
			value.saveWithoutId(tag);
		}

		return tag;
	}

	@Override
	public Entity read(CompoundTag tag) {
		return EntityType.create(tag, Minecraft.getInstance().level).get();
	}

	@Environment(EnvType.CLIENT)
	public class EntityEntryRenderer implements EntryRenderer<Entity> {

		public final int size = 32;

		@Override
		public void render(EntryStack<Entity> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
			Entity entity = entry.getValue();

			if (!entry.isEmpty()) {
				int scale = this.size / 2;
				float height = entity.getBbHeight();
				float width = entity.getBbWidth();
				if (height > 2.25F || width > 2.25F) {
					scale = (int) (20 / Math.max(height, width));
				}

				graphics.pose().pushPose();
				graphics.pose().translate(bounds.getX(), bounds.getY(), entry.getValue().getZ());

				// catch exceptions drawing the entity to be safe, any caught exceptions blacklist the entity
				PoseStack modelView = RenderSystem.getModelViewStack();
				modelView.pushPose();
				modelView.mulPoseMatrix(graphics.pose().last().pose());

				try {
					this.renderTheEntity(this.size / 2, this.size - 2, scale, (LivingEntity) entity);
				} catch (Exception e) {
					TwilightForestMod.LOGGER.error("Error drawing entity {}", BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()), e);
//                    IGNORED_ENTITIES.add(type);
//                    this.ENTITY_MAP.remove(type);
				}

				modelView.popPose();
				RenderSystem.applyModelViewMatrix();

				graphics.pose().popPose();
			}
		}

		//[VanillaCopy] of InventoryScreen.renderEntityInInventory, with added rotations and some other modified values
		private void renderTheEntity(int x, int y, int scale, LivingEntity entity) {
			PoseStack posestack = RenderSystem.getModelViewStack();
			posestack.pushPose();
			posestack.translate(x, y, 1050.0D);
			if (entity.getType() == EntityType.GHAST) posestack.translate(0.0D, -8.5D, 0.0D);
			posestack.scale(1.0F, 1.0F, -1.0F);
			RenderSystem.applyModelViewMatrix();
			PoseStack posestack1 = new PoseStack();
			posestack1.translate(0.0D, 0.0D, 1000.0D);
			posestack1.scale((float) scale, (float) scale, (float) scale);
			Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
			Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
			quaternion.mul(quaternion1);
			posestack1.mulPose(quaternion);
			posestack1.mulPose(Axis.XN.rotationDegrees(35.0F));
			posestack1.mulPose(Axis.YN.rotationDegrees(145.0F));
			float f2 = entity.yBodyRot;
			float f3 = entity.getYRot();
			float f4 = entity.getXRot();
			float f5 = entity.yHeadRotO;
			float f6 = entity.yHeadRot;
			entity.yBodyRot = 0.0F;
			entity.setYRot(0.0F);
			entity.setXRot(0.0F);
			entity.yHeadRot = entity.getYRot();
			entity.yHeadRotO = entity.getYRot();
			Lighting.setupForEntityInInventory();
			EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
			quaternion1.conjugate();
			entityrenderdispatcher.overrideCameraOrientation(quaternion1);
			entityrenderdispatcher.setRenderShadow(false);
			MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
			RenderSystem.runAsFancy(() -> {
				entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
				if (entity instanceof MultiPartEntity multiPartEntity && multiPartEntity.isMultipartEntity()) {
					Arrays.stream(multiPartEntity.getParts())
							.filter(Objects::nonNull)
							.forEach(partEntity ->
									entityrenderdispatcher.render(partEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880));
				}
			});
			multibuffersource$buffersource.endBatch();
			entityrenderdispatcher.setRenderShadow(true);
			entity.yBodyRot = f2;
			entity.setYRot(f3);
			entity.setXRot(f4);
			entity.yHeadRotO = f5;
			entity.yHeadRot = f6;
			posestack.popPose();
			RenderSystem.applyModelViewMatrix();
			Lighting.setupFor3DItems();
		}

		@Override
		@Nullable
		public Tooltip getTooltip(EntryStack<Entity> entry, TooltipContext context) {
			if (entry.isEmpty()) return null;

			EntityType<Entity> entity = (EntityType<Entity>) entry.getValue().getType();

			Tooltip tooltip = Tooltip.create();

			List<Component> components = tryGetEntityToolTip(entry, entry.getValue(), context);

			if (context.getFlag().isAdvanced()) {
				components.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(entity)).toString()).withStyle(ChatFormatting.DARK_GRAY));
			}

			if (!components.isEmpty()) tooltip.addAll(components.get(0));

			for (int i = 1; i < components.size(); i++) {
				tooltip.add(components.get(i));
			}

			return tooltip;
		}
	}

	@Environment(EnvType.CLIENT)
	public static class ItemEntityRender implements EntryRenderer<Entity> {
		private final float bobOffs;

		public ItemEntityRender() {
			this.bobOffs = RandomSource.create().nextFloat() * (float) Math.PI * 2.0F;
		}

		@Override
		public void render(EntryStack<Entity> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
			//setupGL(entry, renderer);
			ItemStack item = ((ItemEntity) entry.getValue()).getItem();

			if (!entry.isEmpty()) {
				Level level = Minecraft.getInstance().level;

				graphics.pose().pushPose();

				graphics.pose().translate(bounds.x, bounds.y, 0);

				if (level != null) {
					PoseStack modelView = RenderSystem.getModelViewStack();

					modelView.pushPose();
					modelView.mulPoseMatrix(graphics.pose().last().pose());

					try {
						this.renderItemEntity(((ItemEntity) entry.getValue()), item, level, delta);
					} catch (Exception e) {
						TwilightForestMod.LOGGER.error("Error drawing item in REI!", e);
					}

					modelView.popPose();
					RenderSystem.applyModelViewMatrix();
				}

				graphics.pose().popPose();
			}
		}

		@Override
		public @Nullable Tooltip getTooltip(EntryStack<Entity> entry, TooltipContext context) {
			ItemStack item = ((ItemEntity) entry.getValue()).getItem();

			List<Component> tooltip = new ArrayList<>();

			tooltip.add(item.getItem().getDescription());

			if (context.getFlag().isAdvanced()) {
				tooltip.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.getItem())).toString()).withStyle(ChatFormatting.DARK_GRAY));
			}

			return Tooltip.create(context.getPoint(), tooltip);
		}

		private void renderItemEntity(ItemEntity item, ItemStack stack, Level level, float delta) {
			PoseStack posestack = RenderSystem.getModelViewStack();
			posestack.pushPose();
			posestack.translate(16.0D, 32.0D, 1050.0D);
			posestack.scale(1.0F, 1.0F, -1.0F);

			RenderSystem.applyModelViewMatrix();

			PoseStack posestack1 = new PoseStack();
			posestack1.translate(0.0D, 0.0D, 1000.0D);
			posestack1.scale(50.0F, 50.0F, 50.0F);

			Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
			Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
			quaternion.mul(quaternion1);

			posestack1.mulPose(quaternion);
			posestack1.mulPose(Axis.XN.rotationDegrees(35.0F));
			posestack1.mulPose(Axis.YN.rotationDegrees(145.0F));

			Lighting.setupForEntityInInventory();

			quaternion1.conjugate();

			MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
			Objects.requireNonNull(item).setItem(stack);
			RenderSystem.runAsFancy(() ->
					this.render(item, delta, posestack1, buffer, 15728880));

			buffer.endBatch();
			posestack.popPose();
			RenderSystem.applyModelViewMatrix();
			Lighting.setupFor3DItems();
		}

		//[VanillaCopy] of ItemEntityRenderer.render. I have to add my own bob offset and ticker since using the vanilla method has issues
		public void render(ItemEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
			stack.pushPose();
			ItemStack itemstack = entity.getItem();
			BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, entity.level(), null, entity.getId());
			float f1 = Mth.sin((Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + partialTicks) / 10.0F + this.bobOffs) * 0.1F + 0.1F;
			float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
			stack.translate(0.0D, f1 + 0.25F * f2, 0.0D);
			float f3 = this.getSpin(partialTicks);
			stack.mulPose(Axis.YP.rotation(f3));

			stack.pushPose();

			Minecraft.getInstance().getItemRenderer().render(itemstack, ItemDisplayContext.GROUND, false, stack, buffer, light, OverlayTexture.NO_OVERLAY, bakedmodel);
			stack.popPose();


			stack.popPose();
		}

		public float getSpin(float pPartialTicks) {
			return (Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + pPartialTicks) / 20.0F + this.bobOffs;
		}
	}
}
