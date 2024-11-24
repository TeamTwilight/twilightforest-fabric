package twilightforest.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.util.MagicPaintingVariant;

@Environment(EnvType.CLIENT)
public class MagicPaintingTextureManager extends TextureAtlasHolder {
	public final static String MAGIC_PAINTING_PATH = "magic_paintings";
	public static final ResourceLocation ATLAS_LOCATION = TwilightForestMod.prefix("textures/atlas/magic_paintings.png");
	public static final ResourceLocation ATLAS_INFO_LOCATION = new ResourceLocation(MAGIC_PAINTING_PATH);
	public static final ResourceLocation BACK_SPRITE_LOCATION = TwilightForestMod.prefix(MAGIC_PAINTING_PATH + "/back");

	private static MagicPaintingTextureManager instance;
	public static void setInstance(MagicPaintingTextureManager value) {
		instance = value;
	}
	public static MagicPaintingTextureManager getInstance() {
		return instance;
	}

	public MagicPaintingTextureManager(TextureManager textureManager) {
		super(textureManager, ATLAS_LOCATION, ATLAS_INFO_LOCATION);
	}

	public TextureAtlasSprite getLayerSprite(ResourceLocation variant, MagicPaintingVariant.Layer layer) {
		return this.getSprite(variant.withPrefix(MAGIC_PAINTING_PATH + "/").withSuffix("/" + layer.path()));
	}

	public TextureAtlasSprite getBackSprite() {
		return this.getSprite(BACK_SPRITE_LOCATION);
	}
}