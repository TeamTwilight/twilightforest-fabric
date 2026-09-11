package twilightforest.datagen.assets;

import carminite.datagen.SpriteSourceProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import twilightforest.TFCommon;
import twilightforest.client.MagicPaintingAtlasInfo;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AtlasGenerator extends SpriteSourceProvider {
	public AtlasGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, TFCommon.ID);
	}

	@Override
	protected void gather() {
		this.atlas(AtlasIds.SHIELD_PATTERNS).addSource(new SingleFile(TFCommon.prefix("entity/knightmetal_shield"), Optional.of(TFCommon.prefix("entity/shield/knightmetal_shield"))));
		this.atlas(MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION).addSource(new SingleFile(MagicPaintingAtlasInfo.BACK_SPRITE_LOCATION, Optional.empty()));

		MagicPaintingVariants.MAGIC_PAINTING_ATLAS_HELPER.forEach((location, parallaxVariant) -> {
			location = location.withPrefix(MagicPaintingAtlasInfo.MAGIC_PAINTING_PATH + "/");
			for (MagicPaintingVariant.Layer layer : parallaxVariant.layers()) {
				this.atlas(MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION).addSource(new SingleFile(location.withSuffix("/" + layer.path()), Optional.empty()));
			}
		});
	}
}