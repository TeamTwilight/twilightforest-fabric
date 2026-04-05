package twilightforest.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.entity.MagicPaintingVariant;

public class MagicPaintingTextureManager extends TextureAtlasHolder {
	public final static String MAGIC_PAINTING_PATH = "magic_paintings";
	public static final Identifier ATLAS_LOCATION = TwilightForestMod.prefix("textures/atlas/magic_paintings.png");
	public static final Identifier ATLAS_INFO_LOCATION = Identifier.withDefaultNamespace(MAGIC_PAINTING_PATH);
	public static final Identifier BACK_SPRITE_LOCATION = TwilightForestMod.prefix(MAGIC_PAINTING_PATH + "/back");

	public static MagicPaintingTextureManager instance;

	public MagicPaintingTextureManager(TextureManager textureManager) {
		super(textureManager, ATLAS_LOCATION, ATLAS_INFO_LOCATION);
	}

	public TextureAtlasSprite getLayerSprite(Identifier variant, MagicPaintingVariant.Layer layer) {
		return this.getSprite(variant.withPrefix(MAGIC_PAINTING_PATH + "/").withSuffix("/" + layer.path()));
	}

	public TextureAtlasSprite getBackSprite(MagicPaintingVariant variant) {
		return this.getSprite(variant.backTexture());
	}
}