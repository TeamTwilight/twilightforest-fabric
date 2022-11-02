package twilightforest.client.model.block.giantblock;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Function;

public record UnbakedGiantBlockModel(ResourceLocation parent, BlockModel ownerModel) implements UnbakedModel {

	@Override
	public BakedModel bake(ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
		ItemOverrides overrides = ownerModel().getOverrides(bakery, ownerModel, spriteGetter);
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

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		if (ownerModel.hasTexture("all")) {
			return List.of(ownerModel.getMaterial("all"), ownerModel.getMaterial("particle"));
		} else {
			ArrayList<Material> materials = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				materials.add(ownerModel.getMaterial(dir.getName().toLowerCase(Locale.ROOT)));
			}
			materials.add(ownerModel.getMaterial("particle"));
			return materials;
		}
	}
}