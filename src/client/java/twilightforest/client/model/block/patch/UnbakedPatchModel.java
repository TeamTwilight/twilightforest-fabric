package twilightforest.client.model.block.patch;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public record UnbakedPatchModel(Material material, boolean shaggify) implements UnbakedModel {
	public UnbakedPatchModel(ResourceLocation texture, boolean shaggify) {
		this(new Material(InventoryMenu.BLOCK_ATLAS, texture), shaggify);
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return List.of();
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter) {
	}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform) {
		return new PatchModel(spriteGetter.apply(this.material()), this.shaggify());
	}
}
