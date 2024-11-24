package twilightforest.client.model.block.patch;

import io.github.fabricators_of_create.porting_lib.models.geometry.IUnbakedGeometry;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.function.Function;

public record UnbakedPatchModel(Material material, boolean shaggify)
		implements IUnbakedGeometry<UnbakedPatchModel> {
	public UnbakedPatchModel(ResourceLocation texture, boolean shaggify) {
		this(new Material(InventoryMenu.BLOCK_ATLAS, texture), shaggify);
	}

	@Override
	public BakedModel bake(BlockModel context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation, boolean isGui3d) {
		return new PatchModel(modelLocation, spriteGetter.apply(this.material()), this.shaggify());
	}
}
