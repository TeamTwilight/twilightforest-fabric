package twilightforest.client.model.block.giantblock;

import com.mojang.datafixers.util.Pair;
import io.github.fabricators_of_create.porting_lib.model.geometry.IGeometryBakingContext;
import io.github.fabricators_of_create.porting_lib.model.geometry.IUnbakedGeometry;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Function;

public record UnbakedGiantBlockModel(ResourceLocation parent) implements IUnbakedGeometry<UnbakedGiantBlockModel> {

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
		TextureAtlasSprite[] sprites;
		if (context.hasMaterial("all")) {
			sprites = new TextureAtlasSprite[]{spriteGetter.apply(context.getMaterial("all"))};
		} else {
			ArrayList<TextureAtlasSprite> materials = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				materials.add(spriteGetter.apply(context.getMaterial(dir.getName().toLowerCase(Locale.ROOT))));
			}
			sprites = materials.toArray(new TextureAtlasSprite[]{});
		}

		return new GiantBlockModel(sprites, spriteGetter.apply(context.getMaterial("particle")), overrides, context.getTransforms());
	}

	@Override
	public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		if (context.hasMaterial("all")) {
			return List.of(context.getMaterial("all"), context.getMaterial("particle"));
		} else {
			ArrayList<Material> materials = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				materials.add(context.getMaterial(dir.getName().toLowerCase(Locale.ROOT)));
			}
			materials.add(context.getMaterial("particle"));
			return materials;
		}
	}
}