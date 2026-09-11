package twilightforest.client.event;

import carminite.events.modified.CarminiteRenderLevelStageEvent;
import carminite.events.neoforge.ComputeFovModifierEvent;
import carminite.events.neoforge.CustomizeGuiOverlayEvent;
import carminite.events.neoforge.RenderFrameEvent;
import carminite.events.neoforge.ViewportEvent;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.TFCommon;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.*;
import twilightforest.client.renderer.AuroraRenderer;
import twilightforest.client.renderer.entity.MagicPaintingRenderer;
import twilightforest.config.TFConfig;
import twilightforest.entity.boss.bar.ClientTFBossBar;
import twilightforest.tags.TFItemTags;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.*;
import twilightforest.item.*;
import twilightforest.util.HolderMatcher;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class ClientGameEvents {
	private static final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);
	private static final MutableComponent WIP_TEXT = Component.translatable("misc.twilightforest.wip").withStyle(ChatFormatting.RED);
	private static final MutableComponent EMPERORS_CLOTH_TOOLTIP = Component.translatable("item.twilightforest.emperors_cloth.desc").withStyle(ChatFormatting.GRAY);

	public static int time = 0;
	private static float shakeIntensity = 0.0F;

	private static int aurora = 0;
	private static int lastAurora = 0;
	private static final AuroraRenderer auroraRenderer = new AuroraRenderer();

	public static void customizeSplashes(Screen screen) {
		if (screen instanceof TitleScreen title) {
			SplashRenderer renderer = title.splash;
			if (renderer != null) {
				LocalDate date = LocalDate.now();
				if (date.getMonth() == Month.AUGUST && date.getDayOfMonth() == 19) {
					RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(Locale.US, RuleBasedNumberFormat.ORDINAL);
					renderer.splash = Component.literal(String.format("Happy %s birthday to the Twilight Forest!", formatter.format(date.getYear() - 2011)));
				}
			}
		}
	}

	public static void clearEntityRenderUtilMap() {
		EntityCache.clearCache();
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	public static void removeHostileMountHealth(HudElement hudElement, GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		if (HostileMountEvents.isRidingUnfriendly(Objects.requireNonNull(Minecraft.getInstance()).player)) {
			return;
		}
		hudElement.extractRenderState(graphics, deltaTracker);
	}

	public static void renderAurora(CarminiteRenderLevelStageEvent.AfterWeather event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;

		if (aurora > 0 || lastAurora > 0) {
			BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

			final float scale = 2048F * (mc.options.getEffectiveRenderDistance() * 16F / 32F);
			Vec3 pos = event.getLevelRenderState().cameraRenderState.pos;
			float y = (float) (256F - pos.y());
			buffer.addVertex(-scale, y, scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(-scale, y, -scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(scale, y, -scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(scale, y, scale).setColor(1F, 1F, 1F, 1F);

			float alpha = Mth.lerp(mc.getDeltaTracker().getGameTimeDeltaTicks(), lastAurora, aurora) / 60F * 0.5F;
			auroraRenderer.draw(
				buffer.buildOrThrow(),
				alpha,
				Mth.abs((int) mc.level.getBiomeManager().biomeZoomSeed),
				(float) pos.x(),
				(float) pos.y(),
				(float) pos.z()
			);
		}
	}

	public static void endAuroraFrame(RenderFrameEvent.Post event) {
		auroraRenderer.endFrame();
	}

	public static void killVignette(RenderFrameEvent.Pre event) {
		Minecraft minecraft = Minecraft.getInstance();
		// only fire if we're in the twilight forest
		if (minecraft.level != null && TFDimension.DIMENSION_KEY.equals(minecraft.level.dimension())) {
			minecraft.gui.vignetteBrightness = 0.0F;
		}

		if (minecraft.player != null && HostileMountEvents.isRidingUnfriendly(minecraft.player)) {
			minecraft.gui.setOverlayMessage(Component.empty(), false);
		}
	}

	public static void clientTick(Minecraft mc) {
		if (!mc.isPaused()) {
			time++;

			lastAurora = aurora;
			if (mc.level != null && mc.getCameraEntity() != null && !TFConfig.getValidAuroraBiomes(mc.level.registryAccess()).isEmpty()) {
				RegistryAccess access = mc.level.registryAccess();
				Holder<Biome> biome = mc.level.getBiome(mc.getCameraEntity().blockPosition());
				if (TFConfig.getValidAuroraBiomes(access).stream().anyMatch(c -> HolderMatcher.INSTANCE.match(c, biome)))
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
						if (mc.level.getChunk(pos.x(), pos.z(), ChunkStatus.FULL, false) != null) {
							List<BlockEntity> beanstalksInChunk = mc.level.getChunk(pos.x(), pos.z()).getBlockEntities().values().stream()
								.filter(blockEntity -> blockEntity instanceof GrowingBeanstalkBlockEntity beanstalkBlock && beanstalkBlock.isBeanstalkRumbling())
								.toList();
							if (!beanstalksInChunk.isEmpty()) {
								BlockEntity beanstalk = beanstalksInChunk.getFirst();
								Player player = mc.player;
								shakeIntensity = (float) (1.0F - mc.player.distanceToSqr(Vec3.atCenterOf(beanstalk.getBlockPos())) / Math.pow(16, 2));
								if (shakeIntensity > 0) {
									player.snapTo(player.getX(), player.getY(), player.getZ(),
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

	public static void shakeCamera(ViewportEvent.ComputeCameraAngles event) {
		if (TFConfig.firstPersonEffects && !Minecraft.getInstance().isPaused() && shakeIntensity > 0 && Minecraft.getInstance().player != null) {
			event.setYaw((float) Mth.lerp(event.getPartialTick(), event.getYaw(), event.getYaw() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setPitch((float) Mth.lerp(event.getPartialTick(), event.getPitch(), event.getPitch() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setRoll((float) Mth.lerp(event.getPartialTick(), event.getRoll(), event.getRoll() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			shakeIntensity = 0F;
		}
	}

	public static void addCustomTooltips(ItemStack item, List<Component> lines) {
		if (item.has(TFDataComponents.EMPERORS_CLOTH)) {
			lines.add(1, EMPERORS_CLOTH_TOOLTIP);
		}

		if (item.is(TFItemTags.WIP)) {
			lines.add(WIP_TEXT);
		}
	}

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	public static void updateBowFOV(ComputeFovModifierEvent event) {
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

	public static void translateBookAuthor(ItemStack stack, List<Component> lines) {
		if (stack.getItem() instanceof WrittenBookItem && stack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
			if (stack.has(TFDataComponents.TRANSLATABLE_BOOK)) {
				List<Component> components = lines;
				for (int i = 0; i < components.size(); i++) {
					Component component = components.get(i);
					if (component.toString().contains("book.byAuthor")) {
						components.set(i, (Component.translatable("book.byAuthor", Component.translatable(TFCommon.ID + ".book.author"))).withStyle(component.getStyle()));
					}
				}
			}
		}
	}

	public static boolean renderGiantBlockOutlines(LevelRenderContext context, BlockOutlineRenderState outlineState) {
		BlockPos pos = outlineState.pos();
		BlockState state = Minecraft.getInstance().level.getBlockState(pos);

		if (state.getBlock() instanceof MiniatureStructureBlock) {
			return false;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && (player.getMainHandItem().getItem() instanceof GiantPickItem || (player.getMainHandItem().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GiantBlock))) {
			if (!state.isAir() && player.level().getWorldBorder().isWithinBounds(pos)) {
				BlockPos offsetPos = new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
				VertexConsumer consumer = context.bufferSource().getBuffer(RenderTypes.lines());
				Vec3 xyz = Vec3.atLowerCornerOf(offsetPos).subtract(context.gameRenderer().getMainCamera().position());
				ShapeRenderer.renderShape(context.poseStack(), consumer, GIANT_BLOCK, xyz.x(), xyz.y(), xyz.z(), ARGB.colorFromFloat(0.0F, 0.0F, 0.0F, 0.45F), Minecraft.getInstance().gameRenderer.getGameRenderState().windowRenderState.appropriateLineWidth);
			}
			return false;
		}
		return true;
	}

	public static void renderCustomBossbars(CustomizeGuiOverlayEvent.BossEventProgress event) {
		if (event.getBossEvent() instanceof ClientTFBossBar bossEvent) {
			event.setCanceled(true);
			bossEvent.renderBossBar(event.getGuiGraphics(), event.getX(), event.getY());
		}
	}
}
