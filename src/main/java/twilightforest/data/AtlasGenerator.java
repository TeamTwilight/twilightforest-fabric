package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.SpriteSourceProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.resources.model.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;
import twilightforest.TwilightForestMod;
import twilightforest.client.MagicPaintingTextureManager;
import twilightforest.client.renderer.tileentity.TwilightChestRenderer;
import twilightforest.util.MagicPaintingVariant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AtlasGenerator extends SpriteSourceProvider {
	public static final Map<ResourceLocation, MagicPaintingVariant> MAGIC_PAINTING_HELPER = new HashMap<>();

	public AtlasGenerator(FabricDataOutput output) {
		super(output, TwilightForestMod.ID);
	}

	@Override
	protected void addSources() {
		TwilightChestRenderer.MATERIALS.values().stream().flatMap(e -> e.values().stream()).map(Material::texture)
				.forEach(resourceLocation -> this.atlas(CHESTS_ATLAS).addSource(new SingleFile(resourceLocation, Optional.empty())));
		this.atlas(SHIELD_PATTERNS_ATLAS).addSource(new SingleFile(TwilightForestMod.prefix("model/knightmetal_shield"), Optional.empty()));

		this.atlas(MagicPaintingTextureManager.ATLAS_INFO_LOCATION).addSource(new SingleFile(MagicPaintingTextureManager.BACK_SPRITE_LOCATION, Optional.empty()));

		MAGIC_PAINTING_HELPER.forEach((location, parallaxVariant) -> {
			location = location.withPrefix(MagicPaintingTextureManager.MAGIC_PAINTING_PATH + "/");
			for (MagicPaintingVariant.Layer layer : parallaxVariant.layers()) {
				this.atlas(MagicPaintingTextureManager.ATLAS_INFO_LOCATION).addSource(new SingleFile(location.withSuffix("/" + layer.path()), Optional.empty()));
			}
		});
	}
}
