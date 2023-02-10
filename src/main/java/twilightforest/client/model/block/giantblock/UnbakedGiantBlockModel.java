package twilightforest.client.model.block.giantblock;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import twilightforest.mixin.BlockModelAccessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.function.Function;

public record UnbakedGiantBlockModel(ResourceLocation parent, BlockModel ownerModel) implements UnbakedModel {

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> models) {
		ownerModel.resolveParents(models);
	}

	@Override
	public BakedModel bake(ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
		ItemOverrides overrides = ((BlockModelAccessor)ownerModel()).tf$callGetItemOverrides(bakery, ownerModel);
		TextureAtlasSprite[] sprites;
		if (ownerModel.hasTexture("all")) {
			sprites = new TextureAtlasSprite[]{spriteGetter.apply(ownerModel.getMaterial("all"))};
		} else {
			ArrayList<TextureAtlasSprite> materials = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				materials.add(spriteGetter.apply(ownerModel.getMaterial(dir.getName().toLowerCase(Locale.ROOT))));
			}
			sprites = materials.toArray(new TextureAtlasSprite[]{});
		}

		return new GiantBlockModel(sprites, spriteGetter.apply(ownerModel.getMaterial("particle")), overrides, ownerModel.getTransforms());
	}
}