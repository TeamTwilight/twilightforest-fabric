package twilightforest.client.model.block.giantblock;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.RenderTypeGroup;

import java.util.*;
import java.util.function.Function;

public record UnbakedGiantBlockModel(ResourceLocation parent) implements UnbakedModel {

	@Override
	public BakedModel bake(ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
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

		ResourceLocation renderTypeHint = context.getRenderTypeHint();
		RenderTypeGroup renderTypes = renderTypeHint != null ? context.getRenderType(renderTypeHint) : RenderTypeGroup.EMPTY;
		return new GiantBlockModel(sprites, spriteGetter.apply(context.getMaterial("particle")), overrides, context.getTransforms(), renderTypes);
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
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
