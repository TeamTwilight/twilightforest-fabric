package twilightforest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.DimensionSpecialEffectsManager;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.model.block.doors.CastleDoorModelLoader;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader;
import twilightforest.client.model.block.giantblock.GiantBlockModelLoader;
import twilightforest.client.model.block.patch.PatchModelLoader;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.compat.trinkets.TrinketsCompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.events.HostileMountEvents;
import twilightforest.fabric.models.TFModelLoadingPlugin;
import twilightforest.init.TFItems;
import twilightforest.item.*;
import twilightforest.mixin.client.LevelRendererAccessor;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TFClientEvents {

	public static void init() {
		TFItems.addItemModelProperties();
		ModelLoadingPlugin.register(TFModelLoadingPlugin.INSTANCE);
		MinecraftTailCallback.EVENT.register(ModBusEvents::registerDimEffects);
		WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(TFClientEvents::unrenderMiniStructureHitbox);
		WorldRenderEvents.AFTER_TRANSLUCENT.register(TFClientEvents::renderWeather);
		WorldRenderEvents.LAST.register(TFClientEvents::renderWorldLast);
		RenderTickStartCallback.EVENT.register(TFClientEvents::renderTick);
		ClientTickEvents.END_CLIENT_TICK.register(TFClientEvents::clientTick);
		ItemTooltipCallback.EVENT.register(TFClientEvents::tooltipEvent);
		FieldOfViewEvents.MODIFY.register(TFClientEvents::FOVUpdate);
		LivingEntityRenderEvents.PRE.register(TFClientEvents::unrenderHeadWithTrophies);
		ItemTooltipCallback.EVENT.register(TFClientEvents::translateBookAuthor);
		CameraSetupCallback.EVENT.register(TFClientEvents::camera);

	}

	public static class ModBusEvents {
		public static void registerLoaders(Consumer<ModelResolver> out) {
			out.accept(PatchModelLoader.INSTANCE);
			out.accept(GiantBlockModelLoader.INSTANCE);
			out.accept(ForceFieldModelLoader.INSTANCE);
			out.accept(CastleDoorModelLoader.INSTANCE);
		}

		public static void modelBake(Map<ResourceLocation, BakedModel> models, ModelBakery modelBakery) {
			// fabric: in init
//			TFItems.addItemModelProperties();

			// fabric: Handled by ItemBlockRenderTypesMixin
//			List<Map.Entry<ResourceLocation, BakedModel>> models =  bakedModels.entrySet().stream()
//					.filter(entry -> entry.getKey().getNamespace().equals(TwilightForestMod.ID) && entry.getKey().getPath().contains("leaves") && !entry.getKey().getPath().contains("dark")).toList();

//			models.forEach(entry -> bakedModels.put(entry.getKey(), new BakedLeavesModel(entry.getValue())));
		}

		public static void registerModels(Consumer<ResourceLocation> out) {
			out.accept(ShieldLayer.LOC);
			out.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy"), "inventory"));
			out.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory"));
			out.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy_quest"), "inventory"));

			out.accept(TwilightForestMod.prefix("block/casket_obsidian"));
			out.accept(TwilightForestMod.prefix("block/casket_stone"));
			out.accept(TwilightForestMod.prefix("block/casket_basalt"));
		}

		public static void registerDimEffects(Minecraft client) {
			new TFSkyRenderer();
			new TFWeatherRenderer();
			ResourceLocation id = TwilightForestMod.prefix("renderer");
			TwilightForestRenderInfo info = new TwilightForestRenderInfo(128.0F, false, DimensionSpecialEffects.SkyType.NONE, false, false);
			DimensionRenderingRegistry.registerDimensionEffects(id, info);
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
//	@SubscribeEvent TODO: PORT
//	public static void preOverlay(RenderGuiOverlayEvent.Pre event) {
//		if (event.getOverlay().id() == VanillaGuiOverlay.MOUNT_HEALTH.id()) {
//			if (HostileMountEvents.isRidingUnfriendly(Minecraft.getInstance().player)) {
//				event.setCanceled(true);
//			}
//		}
//	}

	/**
	 * Render effects in first-person perspective
	 */
	public static void renderWorldLast(WorldRenderContext context) {
		// fabric: we already render and the very end
//		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) { // after particles says its best for special rendering effects, and thats what I consider this
			if (!TFConfig.CLIENT_CONFIG.firstPersonEffects.get()) return;

			Options settings = Minecraft.getInstance().options;
			if (settings.getCameraType() != CameraType.FIRST_PERSON || settings.hideGui) return;

			Entity entity = Minecraft.getInstance().getCameraEntity();
			if (entity instanceof LivingEntity) {
				EntityRenderer<? extends Entity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
				if (renderer instanceof LivingEntityRenderer<?, ?>) {
					for (EffectRenders effect : EffectRenders.VALUES) {
						if (effect.shouldRender((LivingEntity) entity, true)) {
							effect.render((LivingEntity) entity, ((LivingEntityRenderer<?, ?>) renderer).getModel(), 0.0, 0.0, 0.0, context.tickDelta(), true);
						}
					}
				}
			}
		}
	}

	/**
	 * On the tick, we kill the vignette
	 */
	public static void renderTick() {
		Minecraft minecraft = Minecraft.getInstance();

		// only fire if we're in the twilight forest
		if (minecraft.level != null && TFGenerationSettings.DIMENSION_KEY.equals(minecraft.level.dimension())) {
			// vignette
			if (minecraft.gui != null) {
				minecraft.gui.vignetteBrightness = 0.0F;
			}
		}

		if (minecraft.player != null && HostileMountEvents.isRidingUnfriendly(minecraft.player)) {
			if (minecraft.gui != null) {
				minecraft.gui.setOverlayMessage(Component.empty(), false);
			}
		}
	}

	public static int time = 0;
	private static int rotationTickerI = 0;
	private static int sineTickerI = 0;
	public static float rotationTicker = 0;
	public static float sineTicker = 0;
	public static final float PI = (float) Math.PI;
	private static final int SINE_TICKER_BOUND = (int) ((PI * 200.0F) - 1.0F);
	private static float intensity = 0.0F;

	public static void clientTick(Minecraft client) {
		 if (Minecraft.getInstance().isPaused()) return;
		Minecraft mc = Minecraft.getInstance();
		float partial = mc.getFrameTime();

		if (!mc.isPaused()) {
			time++;

			rotationTickerI = (rotationTickerI >= 359 ? 0 : rotationTickerI + 1);
			sineTickerI = (sineTickerI >= SINE_TICKER_BOUND ? 0 : sineTickerI + 1);

			rotationTicker = rotationTickerI + partial;
			sineTicker = sineTicker + partial;
		}

		if (!mc.isPaused()) {
			BugModelAnimationHelper.animate();
			DimensionSpecialEffects info = DimensionRenderingRegistry.getDimensionEffects(TwilightForestMod.prefix("renderer"));

			// add weather box if needed
			if (mc.level != null && info instanceof TwilightForestRenderInfo) {
				TFWeatherRenderer.tick();
			}

			if (TFConfig.CLIENT_CONFIG.firstPersonEffects.get() && mc.level != null && mc.player != null) {
				HashSet<ChunkPos> chunksInRange = new HashSet<>();
				for (int x = -16; x <= 16; x += 16) {
					for (int z = -16; z <= 16; z += 16) {
						chunksInRange.add(new ChunkPos((int) (mc.player.getX() + x) >> 4, (int) (mc.player.getZ() + z) >> 4));
					}
				}
				for (ChunkPos pos : chunksInRange) {
					if (mc.level.getChunk(pos.x, pos.z, ChunkStatus.FULL, false) != null) {
						List<BlockEntity> beanstalksInChunk = mc.level.getChunk(pos.x, pos.z).getBlockEntities().values().stream()
								.filter(blockEntity -> blockEntity instanceof GrowingBeanstalkBlockEntity beanstalkBlock && beanstalkBlock.isBeanstalkRumbling())
								.toList();
						if (!beanstalksInChunk.isEmpty()) {
							BlockEntity beanstalk = beanstalksInChunk.get(0);
							Player player = mc.player;
							intensity = (float) (1.0F - mc.player.distanceToSqr(Vec3.atCenterOf(beanstalk.getBlockPos())) / Math.pow(16, 2));
							if (intensity > 0) {
								player.moveTo(player.getX(), player.getY(), player.getZ(),
										player.getYRot() + (player.getRandom().nextFloat() * 2.0F - 1.0F) * intensity,
										player.getXRot() + (player.getRandom().nextFloat() * 2.0F - 1.0F) * intensity);
								intensity = 0.0F;
								break;
							}
						}
					}
				}
			}
		}
	}

	public static boolean camera(CameraInfo info) {
		if (TFConfig.CLIENT_CONFIG.firstPersonEffects.get() && !Minecraft.getInstance().isPaused() && intensity > 0 && Minecraft.getInstance().player != null) {

			info.yaw = (float) Mth.lerp(info.partialTicks, info.yaw, info.yaw + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * intensity);
			info.pitch = (float) Mth.lerp(info.partialTicks, info.pitch, info.pitch + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * intensity);
			info.roll = (float) Mth.lerp(info.partialTicks, info.roll, info.roll + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * intensity);
			intensity = 0F;
		}
		return false; // we just modify, don't cancel
	}

	private static final MutableComponent WIP_TEXT_0 = Component.translatable("misc.twilightforest.wip0").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent WIP_TEXT_1 = Component.translatable("misc.twilightforest.wip1").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent NYI_TEXT = Component.translatable("misc.twilightforest.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));

	public static void tooltipEvent(ItemStack item, TooltipFlag context, List<Component> lines) {
		if (!item.is(ItemTagGenerator.WIP) && !item.is(ItemTagGenerator.NYI)) return;

		if (item.is(ItemTagGenerator.WIP)) {
			lines.add(WIP_TEXT_0);
			lines.add(WIP_TEXT_1);
		} else {
			lines.add(NYI_TEXT);
		}
	}

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	public static float FOVUpdate(Player player, float fov) {
		if (player.isUsingItem()) {
			Item useItem = player.getUseItem().getItem();
			if (useItem instanceof TripleBowItem || useItem instanceof EnderBowItem || useItem instanceof IceBowItem || useItem instanceof SeekerBowItem) {
				float f = player.getTicksUsingItem() / 20.0F;
				f = f > 1.0F ? 1.0F : f * f;
				return fov * (1.0F - f * 0.15F);
			}
		}
		return fov;
	}

	public static boolean unrenderHeadWithTrophies(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialRenderTick, PoseStack matrixStack, MultiBufferSource buffers, int light) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
		boolean visible = !(stack.getItem() instanceof TrophyItem) && !(stack.getItem() instanceof SkullCandleItem) && !areCuriosEquipped(entity);

		if (!visible && renderer.getModel() instanceof HeadedModel headedModel) {
			headedModel.getHead().visible = false;
			if (renderer.getModel() instanceof HumanoidModel<?> humanoidModel) {
				humanoidModel.hat.visible = false;
			}
		}
		return false;
	}

	private static boolean areCuriosEquipped(LivingEntity entity) {
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			return TrinketsCompat.isTrophyCurioEquipped(entity) || TrinketsCompat.isSkullCurioEquipped(entity);
		}
		return false;
	}

	public static void translateBookAuthor(ItemStack stack, TooltipFlag context, List<Component> lines) {
		if (stack.getItem() instanceof WrittenBookItem && stack.hasTag()) {
			CompoundTag tag = stack.getOrCreateTag();
			if (tag.contains(TwilightForestMod.ID + ":book")) {
				List<Component> components = lines;
				for (Component component : components) {
					if (component.toString().contains("book.byAuthor")) {
						components.set(components.indexOf(component), (Component.translatable("book.byAuthor", Component.translatable(TwilightForestMod.ID + ".book.author"))).withStyle(component.getStyle()));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRenderBlockHighlightEvent(RenderHighlightEvent.Block event) {
		BlockPos pos = event.getTarget().getBlockPos();
		BlockState state = event.getCamera().getEntity().level().getBlockState(pos);

		if (state.getBlock() instanceof MiniatureStructureBlock) {
			event.setCanceled(true);
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && (player.getMainHandItem().getItem() instanceof GiantPickItem || (player.getMainHandItem().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GiantBlock))) {
			event.setCanceled(true);
			if (!state.isAir() && player.level().getWorldBorder().isWithinBounds(pos)) {
				BlockPos offsetPos = new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
				renderGiantHitOutline(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.lines()), event.getCamera().getPosition(), offsetPos);
			}
		}
	}

	private static final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);

	private static void renderGiantHitOutline(PoseStack poseStack, VertexConsumer consumer, Vec3 cam, BlockPos pos) {
		PoseStack.Pose last = poseStack.last();
		float posX = pos.getX() - (float)cam.x();
		float posY = pos.getY() - (float)cam.y();
		float posZ = pos.getZ() - (float)cam.z();
		GIANT_BLOCK.forAllEdges((x, y, z, x1, y1, z1) -> {
			float xSize = (float)(x1 - x);
			float ySize = (float)(y1 - y);
			float zSize = (float)(z1 - z);
			float sqrt = Mth.sqrt(xSize * xSize + ySize * ySize + zSize * zSize);
			xSize /= sqrt;
			ySize /= sqrt;
			zSize /= sqrt;
			consumer.vertex(last.pose(), (float)(x + posX), (float)(y + posY), (float)(z + posZ)).color(0.0F, 0.0F, 0.0F, 0.45F).normal(last.normal(), xSize, ySize, zSize).endVertex();
			consumer.vertex(last.pose(), (float)(x1 + posX), (float)(y1 + posY), (float)(z1 + posZ)).color(0.0F, 0.0F, 0.0F, 0.45F).normal(last.normal(), xSize, ySize, zSize).endVertex();
		});
	}
}
