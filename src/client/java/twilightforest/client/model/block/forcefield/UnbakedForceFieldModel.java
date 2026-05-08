package twilightforest.client.model.block.forcefield;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record UnbakedForceFieldModel(Map<net.minecraft.client.renderer.block.model.BlockElement, ForceFieldModelLoader.Condition> parts, Map<String, Material> materials) implements UnbakedModel {
	@Override
	public Collection<ResourceLocation> getDependencies() {
		return List.of();
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter) {
	}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
		return new ForceFieldModel(this.parts, this.materials, spriteGetter, ItemOverrides.EMPTY, ItemTransforms.NO_TRANSFORMS);
	}
}
