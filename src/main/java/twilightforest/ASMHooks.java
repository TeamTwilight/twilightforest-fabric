package twilightforest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Function4;
import com.mojang.math.Matrix4f;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import twilightforest.entity.TFEntities;
import twilightforest.entity.TFPartEntity;
import twilightforest.events.EntityJoinWorldEvent;
import twilightforest.events.EntityLeaveWorldEvent;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateTFMultipartPacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression", "deprecation"})
public class ASMHooks {

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.gui.MapRenderer.MapInstance#draw(PoseStack, MultiBufferSource, boolean, int)}<br>
	 * [BEFORE FIRST ISTORE]
	 */
	public static void mapRenderContext(PoseStack stack, MultiBufferSource buffer, int light) {
		TFMagicMapData.TFMapDecoration.RenderContext.stack = stack;
		TFMagicMapData.TFMapDecoration.RenderContext.buffer = buffer;
		TFMagicMapData.TFMapDecoration.RenderContext.light = light;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.sounds.MusicManager#tick()}<br>
	 * [AFTER FIRST INVOKEVIRTUAL]
	 */
	public static Music music(Music music) {
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == Musics.CREATIVE || music == Musics.UNDER_WATER) && Minecraft.getInstance().level.dimension().location().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get()))
			return Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition()).getBackgroundMusic().orElse(Musics.GAME);
		return music;
	}

	private static final WeakHashMap<Level, List<TFPartEntity<?>>> cache = new WeakHashMap<>();
	private static final Int2ObjectMap<TFPartEntity<?>> multiparts = new Int2ObjectOpenHashMap<>();

	// This only works on the client side in 1.17...
	public static void registerMultipartEvents(IEventBus bus) {
		bus.addListener((Consumer<EntityJoinWorldEvent>) event -> {
			if(event.getWorld().isClientSide() && event.getEntity().isMultipartEntity())
			synchronized (cache) {
				cache.computeIfAbsent(event.getWorld(), (w) -> new ArrayList<>());
				cache.get(event.getWorld()).addAll(Arrays.stream(Objects.requireNonNull(event.getEntity().getParts())).
						filter(TFPartEntity.class::isInstance).map(obj -> (TFPartEntity<?>) obj).
						collect(Collectors.toList()));

			}
		});
		bus.addListener((Consumer<EntityLeaveWorldEvent>) event -> {
			if(event.getWorld().isClientSide() && event.getEntity().isMultipartEntity())
			synchronized (cache) {
				cache.computeIfPresent(event.getWorld(), (world, list) -> {
					list.removeAll(Arrays.stream(Objects.requireNonNull(event.getEntity().getParts())).
							filter(TFPartEntity.class::isInstance).map(obj -> (TFPartEntity<?>) obj).
							collect(Collectors.toList()));
					return list;
				});
			}
		});
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerLevel#onTrackingStart(Entity)}<br>
	 * [FIRST INST]
	 */
	public static void trackingStart(Entity entity) {
		if (entity.isMultipartEntity()) {
			List<TFPartEntity<?>> list = Arrays.stream(Objects.requireNonNull(entity.getParts())).
					filter(TFPartEntity.class::isInstance).map(obj -> (TFPartEntity<?>) obj).
					collect(Collectors.toList());
			list.forEach(part -> multiparts.put(part.getId(), part));
			synchronized (cache) {
				cache.computeIfAbsent(entity.level, (w) -> new ArrayList<>());
				cache.get(entity.level).addAll(list);
			}
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerLevel#onTrackingEnd(Entity)}<br>
	 * [FIRST INST]
	 */
	public static void trackingEnd(Entity entity) {
		if (entity.isMultipartEntity()) {
			List<TFPartEntity<?>> list = Arrays.stream(Objects.requireNonNull(entity.getParts())).
					filter(TFPartEntity.class::isInstance).map(obj -> (TFPartEntity<?>) obj).
					collect(Collectors.toList());
			list.forEach(part -> multiparts.remove(part.getId()));
			synchronized (cache) {
				cache.computeIfPresent(entity.level, (world, parts) -> {
					parts.removeAll(list);
					return parts;
				});
			}
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.Level#getEntities(Entity, AABB, Predicate)}<br>
	 * [BEFORE ARETURN]
	 */
	public static synchronized List<Entity> multipartHitbox(List<Entity> list, Level world, @Nullable Entity entityIn, AABB boundingBox, @Nullable Predicate<? super Entity> predicate) {
		synchronized (cache) {
			List<TFPartEntity<?>> parts = cache.get(world);
			if(parts != null) {
				for (TFPartEntity<?> part : parts) {
					if (part != entityIn &&

							part.getBoundingBox().intersects(boundingBox) &&

							(predicate == null || predicate.test(part)) &&

							!list.contains(part))
						list.add(part);
				}
			}
			return list;
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerLevel#getEntityOrPart(int)}<br>
	 * [BEFORE ARETURN]
	 */
	public static Entity multipartFromID(@Nullable Entity o, int id) {
		return o == null ? multiparts.get(id) : o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerEntity#sendDirtyEntityData}<br>
	 * [AFTER GETFIELD]
	 */
	public static Entity updateMultiparts(Entity entity) {
		if (entity.isMultipartEntity())
			TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new UpdateTFMultipartPacket(entity));
		return entity;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(Entity)}<br>
	 * [BEFORE LAST ARETURN]
	 */
	@Nullable
	@OnlyIn(Dist.CLIENT)
	public static EntityRenderer<?> getMultipartRenderer(@Nullable EntityRenderer<?> renderer, Entity entity) {
		if(entity instanceof TFPartEntity<?>)
			return TFEntities.BakedMultiPartRenderers.lookup(((TFPartEntity<?>) entity).renderer());
		return renderer;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#onResourceManagerReload(ResourceManager)}<br>
	 * [AFTER FIRST INVOKESPECIAL]
	 */
	@OnlyIn(Dist.CLIENT)
	public static EntityRendererProvider.Context bakeMultipartRenders(EntityRendererProvider.Context context) {
		TFEntities.BakedMultiPartRenderers.bakeMultiPartRenderers(context);
		return context;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LevelRenderer#renderLevel(PoseStack, float, long, boolean, Camera, GameRenderer, LightTexture, Matrix4f)}<br>
	 * [AFTER {@link net.minecraft.client.multiplayer.ClientLevel#entitiesForRendering}]
	 */
	public static Iterable<Entity> renderMutiparts(Iterable<Entity> iter) {
		List<Entity> list = new ArrayList<>();
		iter.forEach(entity -> {
			list.add(entity);
			if(entity.isMultipartEntity() && entity.getParts() != null) {
				for (PartEntity<?> part : entity.getParts()) {
					if(part instanceof TFPartEntity)
						list.add(part);
				}
			}
		});
		return list;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.Minecraft#doLoadLevel(String, RegistryAccess.RegistryHolder, Function, Function4, boolean, Minecraft.ExperimentalDialogType, boolean)}<br>
	 * [AFTER ALL ALOAD 6]
	 */
	public static Minecraft.ExperimentalDialogType dragons(Minecraft.ExperimentalDialogType type) {
		return TFConfig.CLIENT_CONFIG.disableHereBeDragons.get() ? Minecraft.ExperimentalDialogType.NONE : type;
	}

}
