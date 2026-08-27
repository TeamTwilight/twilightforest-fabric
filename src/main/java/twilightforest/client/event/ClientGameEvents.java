package twilightforest.client.event;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.TFMain;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.EntityCache;
import twilightforest.client.renderer.entity.MagicPaintingRenderer;
import twilightforest.config.TFConfig;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.TFDataComponents;
import twilightforest.item.GiantPickItem;
import twilightforest.tags.TFItemTags;
import twilightforest.util.HolderMatcher;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ClientGameEvents {

	private static final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);
	private static final MutableComponent WIP_TEXT = Component.translatable("misc.twilightforest.wip").withStyle(ChatFormatting.RED);
	private static final MutableComponent EMPERORS_CLOTH_TOOLTIP = Component.translatable("item.twilightforest.emperors_cloth.desc").withStyle(ChatFormatting.GRAY);

	public static int time = 0;
	private static float shakeIntensity = 0.0F;

	private static int aurora = 0;
	private static int lastAurora = 0;

	private static final HolderMatcher holderMatcher = HolderMatcher.INSTANCE;

	public static void init() {
		ScreenEvents.AFTER_INIT.register((minecraft, screen, width, height) -> {
			customizeSplashes(screen);
			ScreenEvents.remove(screen).register(s -> EntityCache.clearCache());
		});

		ClientTickEvents.END_CLIENT_TICK.register(ClientGameEvents::clientTick);

		ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
			addCustomTooltips(stack, lines);
			translateBookAuthor(stack, lines);
		});

		LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register(ClientGameEvents::renderGiantBlockOutlines);

		HudElementRegistry.replaceElement(VanillaHudElements.MOUNT_HEALTH, previous -> (extractor, delta) -> {
			if (!HostileMountEvents.isRidingUnfriendly(Minecraft.getInstance().player)) {
				previous.extractRenderState(extractor, delta);
			}
		});

		// TODO [Fabric] pending mixin/shader batches:
		// - setMusicInDimension: mixin Minecraft#getSituationalMusic
		// - renderAurora: needs TFShaders#AURORA ported to RenderPipeline
		// - killVignette / shakeCamera / updateBowFOV / unrenderHeadWithTrophies / renderCustomBossbars: mixins
	}

	private static void customizeSplashes(Screen screen) {
		if (screen instanceof TitleScreen title) {
			SplashRenderer renderer = title.splash;
			if (renderer != null) {
				LocalDate date = LocalDate.now();
				if (date.getMonth() == Month.AUGUST && date.getDayOfMonth() == 19) {
					RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(Locale.US, RuleBasedNumberFormat.ORDINAL);
					title.splash = new SplashRenderer(Component.literal(String.format("Happy %s birthday to the Twilight Forest!", formatter.format(date.getYear() - 2011))));
				}
			}
		}
	}

	private static void clientTick(Minecraft mc) {
		if (!mc.isPaused()) {
			time++;

			lastAurora = aurora;
			if (mc.level != null && mc.getCameraEntity() != null && !TFConfig.getValidAuroraBiomes(mc.level.registryAccess()).isEmpty()) {
				var access = mc.level.registryAccess();
				var biome = mc.level.getBiome(mc.getCameraEntity().blockPosition());
				if (TFConfig.getValidAuroraBiomes(access).stream().anyMatch(c -> holderMatcher.match(c, biome)))
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
									player.setYRot(player.getYRot() + (player.getRandom().nextFloat() - 0.5F) * shakeIntensity);
									player.setXRot(player.getXRot() + (player.getRandom().nextFloat() * 2.5F - 1.25F) * shakeIntensity);
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

	private static void addCustomTooltips(ItemStack item, List<Component> lines) {
		if (item.has(TFDataComponents.EMPERORS_CLOTH)) {
			lines.add(1, EMPERORS_CLOTH_TOOLTIP);
		}

		if (item.is(TFItemTags.WIP)) {
			lines.add(WIP_TEXT);
		}
	}

	private static void translateBookAuthor(ItemStack stack, List<Component> lines) {
		if (stack.getItem() instanceof WrittenBookItem && stack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
			if (stack.has(TFDataComponents.TRANSLATABLE_BOOK)) {
				for (int i = 0; i < lines.size(); i++) {
					Component component = lines.get(i);
					if (component.toString().contains("book.byAuthor")) {
						lines.set(i, (Component.translatable("book.byAuthor", Component.translatable(TFMain.ID + ".book.author"))).withStyle(component.getStyle()));
					}
				}
			}
		}
	}

	private static boolean renderGiantBlockOutlines(LevelRenderContext context, BlockOutlineRenderState outline) {
		BlockPos pos = outline.pos();
		BlockState state = context.gameRenderer().getMainCamera().entity().level().getBlockState(pos);

		if (state.getBlock() instanceof MiniatureStructureBlock) {
			return false;
		}

		Player player = Minecraft.getInstance().player;
		if (player != null && (player.getMainHandItem().getItem() instanceof GiantPickItem || (player.getMainHandItem().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GiantBlock))) {
			if (!state.isAir() && player.level().getWorldBorder().isWithinBounds(pos)) {
				BlockPos offsetPos = new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
				VertexConsumer consumer = context.bufferSource().getBuffer(RenderTypes.lines());
				Vec3 xyz = Vec3.atLowerCornerOf(offsetPos).subtract(context.gameRenderer().getMainCamera().position());
				ShapeRenderer.renderShape(context.poseStack(), consumer, GIANT_BLOCK, xyz.x(), xyz.y(), xyz.z(), ARGB.color(0, 0, 0), 0.45F);
			}
			return false;
		}
		return true;
	}
}
