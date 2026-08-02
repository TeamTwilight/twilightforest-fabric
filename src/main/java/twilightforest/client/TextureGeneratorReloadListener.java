package twilightforest.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.world.entity.vehicle.Boat;
import twilightforest.TwilightForestMod;
import twilightforest.util.TFBoatTypes;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

public class TextureGeneratorReloadListener implements IdentifiableResourceReloadListener {
	public static final TextureGeneratorReloadListener INSTANCE = new TextureGeneratorReloadListener();
	private static final EnumMap<Boat.Type, AbstractTexture> BOAT_CACHE = new EnumMap<>(Boat.Type.class);
	private static final AtomicReference<NativeImage> ref = new AtomicReference<>();

	@Override
	public ResourceLocation getFabricId() {
		return TwilightForestMod.prefix("texture_generator");
	}

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
		return barrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
			onResourceManagerReload(manager);
		}, gameExecutor);
	}

	private void onResourceManagerReload(ResourceManager manager) {
		// Get a default boat chest texture (vanilla oak)
		ResourceLocation oak = ResourceLocation.withDefaultNamespace("textures/entity/chest_boat/oak.png");

		manager.getResource(oak).ifPresent(vanillaResource -> {
			try (InputStream vanillaStream = vanillaResource.open()) {
				try (NativeImage vanillaImage = NativeImage.read(vanillaStream)) {
					int defaultScale = 128;
					int vanillaScale = vanillaImage.getWidth() / defaultScale;
					for (Boat.Type type : Boat.Type.values()) {
						String name = getTFTextureName(type);
						if (name == null) continue; // Only process TF custom types
						ResourceLocation location = TwilightForestMod.prefix("textures/entity/chest_boat/" + name + ".png");
						manager.getResource(location).ifPresent(tfResource -> {
								try (InputStream tfStream = tfResource.open()) {
									try (NativeImage tfImage = NativeImage.read(tfStream)) {
										int tfScale = tfImage.getWidth() / defaultScale;

										for (int x = 0; x < 48 * tfScale; x++) {
											for (int y = 58 * tfScale; y < 96 * tfScale; y++) {
												// If the loaded tf boat chest texture has non-transparent pixels below the boat section of the texture, return
												if (tfImage.getPixelRGBA(x, y) != 0x00000000) return;
											}
										}

										if (vanillaScale > tfScale) {
											try (NativeImage newImage = new NativeImage(defaultScale * vanillaScale, defaultScale * vanillaScale, false)) {
												newImage.copyFrom(vanillaImage);
												for (int x = 0; x < 102 * vanillaScale; x++) {
													for (int y = 0; y < 52 * vanillaScale; y++) {
														newImage.setPixelRGBA(x, y, tfImage.getPixelRGBA(x / (vanillaScale / tfScale), y / (vanillaScale / tfScale)));
													}
												}

												ref.set(newImage);

												if (BOAT_CACHE.containsKey(type)) {
													BOAT_CACHE.get(type).load(manager);
												} else {
													AbstractTexture texture = new AbstractTexture() {
														@Override
														public void load(ResourceManager resourceManager) {
															if (ref.get() == null)
																return;
															TextureUtil.prepareImage(this.getId(), 0, ref.get().getWidth(), ref.get().getHeight());
															ref.get().upload(0, 0, 0, 0, 0, ref.get().getWidth(), ref.get().getHeight(), false, false, false, true);
														}
													};
													Minecraft.getInstance().getTextureManager().register(location, texture);
													BOAT_CACHE.put(type, texture);
												}
											}
										} else {
											for (int x = 0; x < 48 * tfScale; x++) {
												for (int y = 58 * tfScale; y < 96 * tfScale; y++) {
													tfImage.setPixelRGBA(x, y, vanillaImage.getPixelRGBA(x / (tfScale / vanillaScale), y / (tfScale / vanillaScale)));
												}
											}

											ref.set(tfImage);

											if (BOAT_CACHE.containsKey(type)) {
												BOAT_CACHE.get(type).load(manager);
											} else {
												AbstractTexture texture = new AbstractTexture() {
													@Override
													public void load(ResourceManager resourceManager) {
														if (ref.get() == null)
															return;
														TextureUtil.prepareImage(this.getId(), 0, ref.get().getWidth(), ref.get().getHeight());
														ref.get().upload(0, 0, 0, 0, 0, ref.get().getWidth(), ref.get().getHeight(), false, false, false, true);
													}
												};
												Minecraft.getInstance().getTextureManager().register(location, texture);
												BOAT_CACHE.put(type, texture);
											}
										}
									}
								} catch (IOException e) {
									// Fail silently, no boat texture bullshit here
								}
							});
					}
				}
			} catch (IOException e) {
				// Fail silently, no boat texture bullshit here
			}
		});
		ref.set(null);
	}

	private static String getTFTextureName(Boat.Type type) {
		if (type == TFBoatTypes.TWILIGHT_OAK) return "twilight_oak";
		if (type == TFBoatTypes.CANOPY) return "canopy";
		if (type == TFBoatTypes.MANGROVE_TYPE) return "mangrove";
		if (type == TFBoatTypes.DARK) return "dark";
		if (type == TFBoatTypes.TIME) return "time";
		if (type == TFBoatTypes.TRANSFORMATION) return "transformation";
		if (type == TFBoatTypes.MINING) return "mining";
		if (type == TFBoatTypes.SORTING) return "sorting";
		return null;
	}
}
