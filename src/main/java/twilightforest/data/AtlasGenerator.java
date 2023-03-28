package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.SpriteSourceProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.resources.model.Material;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.tileentity.TwilightChestRenderer;

import java.util.Optional;

public class AtlasGenerator extends SpriteSourceProvider {
	public AtlasGenerator(FabricDataOutput output) {
		super(output, TwilightForestMod.ID);
	}

	@Override
	protected void addSources() {
		TwilightChestRenderer.MATERIALS.values().stream().flatMap(e -> e.values().stream()).map(Material::texture)
				.forEach(resourceLocation -> this.atlas(CHESTS_ATLAS).addSource(new SingleFile(resourceLocation, Optional.empty())));
		this.atlas(SHIELD_PATTERNS_ATLAS).addSource(new SingleFile(TwilightForestMod.prefix("model/knightmetal_shield"), Optional.empty()));
	}
}
