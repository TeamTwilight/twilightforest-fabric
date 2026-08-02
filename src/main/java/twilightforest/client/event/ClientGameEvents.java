package twilightforest.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.SelectMusicEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ViewportEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ComputeFovModifierEvent;
import io.github.fabricators_of_create.porting_lib.level.events.LevelEvent;



import twilightforest.TwilightForestMod;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.ISTER;
import twilightforest.client.OptifineWarningScreen;
import twilightforest.client.TFClientSetup;
import twilightforest.client.TFShaders;
import twilightforest.client.renderer.entity.MagicPaintingRenderer;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.entity.boss.bar.ClientTFBossBar;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.*;
import twilightforest.item.*;
import twilightforest.util.HolderMatcher;
import twilightforest.util.TFBeanRegistry;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Environment(EnvType.CLIENT)
public class ClientGameEvents {
	public static final ClientGameEvents INSTANCE = new ClientGameEvents();

	private final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);
	private final MutableComponent WIP_TEXT = Component.translatable("misc.twilightforest.wip").withStyle(ChatFormatting.RED);
	private final MutableComponent EMPERORS_CLOTH_TOOLTIP = Component.translatable("item.twilightforest.emperors_cloth.desc").withStyle(ChatFormatting.GRAY);

	private boolean firstTitleScreenShown = false;

	public static int time = 0;
	private float shakeIntensity = 0.0F;

	private int aurora = 0;
	private int lastAurora = 0;

	private HolderMatcher holderMatcher;

	private HolderMatcher getHolderMatcher() {
		if (holderMatcher == null) {
			holderMatcher = TFBeanRegistry.get(HolderMatcher.class);
		}
		return holderMatcher;
	}

	public ClientGameEvents() {
		TFBeanRegistry.register(ClientGameEvents.class, this);
		TFBeanRegistry.addPostInit(this::init);
	}

	private void init() {
		// Available in Porting-Lib:
		SelectMusicEvent.EVENT.register(this::setMusicInDimension);
		ViewportEvent.ComputeCameraAngles.EVENT.register(this::shakeCamera);
		ComputeFovModifierEvent.EVENT.register(this::updateBowFOV);

		// Fabric API: Client tick events
		ClientTickEvents.END_CLIENT_TICK.register(this::clientTick);
		ClientTickEvents.END_CLIENT_TICK.register(this::killVignette);

		// Fabric API: Item tooltip events
		ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> tooltip) -> addCustomTooltips(stack, context, flag, tooltip));
		ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> tooltip) -> translateBookAuthor(stack, context, flag, tooltip));

		// Fabric API: World render events
		WorldRenderEvents.LAST.register((WorldRenderContext context) -> {
			if (Minecraft.getInstance().level == null) return;

			if ((aurora > 0 || lastAurora > 0) && TFShaders.AURORA != null) {
				Tesselator tesselator = Tesselator.getInstance();
				BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

				float tickDelta = context.tickCounter().getGameTimeDeltaPartialTick(false);
				final float scale = 2048F * (Minecraft.getInstance().gameRenderer.getRenderDistance() / 32F);
				Vec3 pos = context.camera().getPosition();
				float y = (float) (256F - pos.y());
				buffer.addVertex(-scale, y, scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(-scale, y, -scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(scale, y, -scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(scale, y, scale).setColor(1F, 1F, 1F, 1F);

				RenderSystem.enableBlend();
				RenderSystem.enableDepthTest();
				RenderSystem.setShaderColor(1F, 1F, 1F, Mth.lerp(tickDelta, (float) lastAurora, (float) aurora) / 60F * 0.5F);
				TFShaders.AURORA.invokeThenEndTesselator(
					Minecraft.getInstance().level == null ? 0 : Math.abs((int) Minecraft.getInstance().level.getBiomeManager().biomeZoomSeed),
					(float) pos.x(), (float) pos.y(), (float) pos.z(), buffer);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				RenderSystem.disableDepthTest();
				RenderSystem.disableBlend();
			}
		});

		// removeHostileMountHealth is handled by GuiMixin
		// HudRenderCallback.EVENT.register(this::removeHostileMountHealth);

		// Fabric API: Screen events
		ScreenEvents.AFTER_INIT.register(this::handleGameBootup);
		// Splash customization handled via SplashRendererMixin
		// clearEntityRenderUtilMap - ScreenEvents.REMOVE not available in Fabric API 1.21.1
		// ScreenEvents.remove(screen -> clearEntityRenderUtilMap(screen));
		ViewportEvent.RenderFog.EVENT.register(FogHandler::renderFog);
		// FogHandler.unloadFog → LevelEvent.Unload (Porting-Lib level_events)
		LevelEvent.Unload.EVENT.register(FogHandler::unloadFog);
		// LockedBiomeToastHandler.tickLockedToastLogic → ClientTickEvents.END_CLIENT_TICK
	}

	private void handleGameBootup(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
		if (firstTitleScreenShown || !(screen instanceof TitleScreen)) return;

		// Registering this resource listener earlier than the main screen will cause a crash
		// Yes, crashing happens if registered to RegisterClientReloadListenersEvent
		if (client.getResourceManager() instanceof ReloadableResourceManager resourceManager) {
			resourceManager.registerReloadListener(ISTER.INSTANCE.get());
			TwilightForestMod.LOGGER.debug("Registered ISTER listener");
		}

		if (TFClientSetup.isOptifinePresent() && !TFConfig.disableOptifineNagScreen) {
			client.setScreen(new OptifineWarningScreen(screen));
		}

		firstTitleScreenShown = true;
	}

	private void setMusicInDimension(SelectMusicEvent event) {
		Music music = event.getOriginalMusic();
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == Musics.CREATIVE || music == Musics.UNDER_WATER) && TFDimension.isTwilightWorldOnClient(Minecraft.getInstance().level)) {
			event.setMusic(Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition()).value().getBackgroundMusic().orElse(Musics.GAME));
		}
	}

	// removeHostileMountHealth is handled by GuiMixin

	private void killVignette(Minecraft minecraft) {
		// only fire if we're in the twilight forest
		if (minecraft.level != null && TFDimension.DIMENSION_KEY.equals(minecraft.level.dimension())) {
			minecraft.gui.vignetteBrightness = 0.0F;
		}

		if (minecraft.player != null && HostileMountEvents.isRidingUnfriendly(minecraft.player)) {
			minecraft.gui.setOverlayMessage(Component.empty(), false);
		}
	}

	private void clientTick(Minecraft mc) {
		if (!mc.isPaused()) {
			time++;

			lastAurora = aurora;
			if (mc.level != null && mc.cameraEntity != null && !TFConfig.getValidAuroraBiomes(mc.level.registryAccess()).isEmpty()) {
				RegistryAccess access = mc.level.registryAccess();
				Holder<Biome> biome = mc.level.getBiome(mc.cameraEntity.blockPosition());
				if (TFConfig.getValidAuroraBiomes(access).stream().anyMatch(c -> getHolderMatcher().match(c, biome)))
					aurora++;
				else
					aurora--;
				aurora = Mth.clamp(aurora, 0, 60);
			} else {
				aurora = 0;
			}

			BugModelAnimationHelper.animate();

			if (mc.level != null) {
				if (mc.level.getSkyFlashTime() > 0) {
					MagicPaintingRenderer.lastLightning = mc.level.getGameTime();
				}

				if (TFConfig.firstPersonEffects && mc.player != null) {
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
								BlockEntity beanstalk = beanstalksInChunk.getFirst();
								Player player = mc.player;
								shakeIntensity = (float) (1.0F - mc.player.distanceToSqr(Vec3.atCenterOf(beanstalk.getBlockPos())) / Math.pow(16, 2));
								if (shakeIntensity > 0) {
									player.moveTo(player.getX(), player.getY(), player.getZ(),
										player.getYRot() + (player.getRandom().nextFloat() - 0.5F) * shakeIntensity,
										player.getXRot() + (player.getRandom().nextFloat() * 2.5F - 1.25F) * shakeIntensity);
									shakeIntensity = 0.0F;
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	private void shakeCamera(ViewportEvent.ComputeCameraAngles event) {
		if (TFConfig.firstPersonEffects && !Minecraft.getInstance().isPaused() && shakeIntensity > 0 && Minecraft.getInstance().player != null) {
			event.setYaw((float) Mth.lerp(event.getPartialTick(), event.getYaw(), event.getYaw() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setPitch((float) Mth.lerp(event.getPartialTick(), event.getPitch(), event.getPitch() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setRoll((float) Mth.lerp(event.getPartialTick(), event.getRoll(), event.getRoll() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			shakeIntensity = 0F;
		}
	}

	private void addCustomTooltips(ItemStack item, Item.TooltipContext tooltipContext, TooltipFlag flag, List<Component> tooltip) {
		if (item.has(TFDataComponents.EMPERORS_CLOTH.get())) {
			tooltip.add(1, EMPERORS_CLOTH_TOOLTIP);
		}

		if (item.is(ItemTagGenerator.WIP)) {
			tooltip.add(WIP_TEXT);
		}
	}

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	private void updateBowFOV(ComputeFovModifierEvent event) {
		Player player = event.getPlayer();
		if (player.isUsingItem()) {
			Item useItem = player.getUseItem().getItem();
			if (useItem instanceof TripleBowItem || useItem instanceof EnderBowItem || useItem instanceof IceBowItem || useItem instanceof SeekerBowItem) {
				float f = player.getTicksUsingItem() / 20.0F;
				f = f > 1.0F ? 1.0F : f * f;
				event.setNewFovModifier((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, (event.getFovModifier() * (1.0F - f * 0.15F))));
			}
		}
	}

	// unrenderHeadWithTrophies is handled by LivingEntityRendererMixin

	private static void clearEntityRenderUtilMap(Screen screen) {
		if (!EntityRenderingUtil.ENTITY_MAP.isEmpty()) EntityRenderingUtil.ENTITY_MAP.clear();
	}

	private void translateBookAuthor(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag flag, List<Component> tooltip) {
		if (stack.getItem() instanceof WrittenBookItem && stack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
			if (stack.has(TFDataComponents.TRANSLATABLE_BOOK.get())) {
				for (int i = 0; i < tooltip.size(); i++) {
					Component component = tooltip.get(i);
					if (component.toString().contains("book.byAuthor")) {
						tooltip.set(i, (Component.translatable("book.byAuthor", Component.translatable(TwilightForestMod.ID + ".book.author"))).withStyle(component.getStyle()));
					}
				}
			}
		}
	}

	// renderGiantBlockOutlines is handled by LevelRendererHitOutlineMixin
	// renderCustomBossbars is handled by BossHealthOverlayMixin
}
