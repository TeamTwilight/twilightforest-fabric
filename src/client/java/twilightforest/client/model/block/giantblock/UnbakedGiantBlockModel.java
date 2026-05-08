package twilightforest.client.model.block.giantblock;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public record UnbakedGiantBlockModel(ResourceLocation parent, Map<String, Material> materials) implements UnbakedModel {
	public UnbakedGiantBlockModel(ResourceLocation parent, Map<String, ResourceLocation> textures, boolean byTexture) {
		this(parent, makeMaterials(textures));
	}

	private static Map<String, Material> makeMaterials(Map<String, ResourceLocation> textures) {
		Map<String, Material> materials = new LinkedHashMap<>();
		textures.forEach((key, value) -> materials.put(key, new Material(InventoryMenu.BLOCK_ATLAS, value)));
		return materials;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return List.of();
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter) {
	}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
		TextureAtlasSprite[] sprites;
		if (this.materials.containsKey("all")) {
			sprites = new TextureAtlasSprite[]{spriteGetter.apply(this.materials.get("all"))};
		} else {
			sprites = new TextureAtlasSprite[Direction.values().length];
			for (Direction dir : Direction.values()) {
				sprites[dir.ordinal()] = spriteGetter.apply(this.materials.get(dir.getName().toLowerCase(Locale.ROOT)));
			}
		}

		Material particle = this.materials.getOrDefault("particle", this.materials.containsKey("all") ? this.materials.get("all") : this.materials.get("north"));
		return new GiantBlockModel(sprites, spriteGetter.apply(particle), ItemOverrides.EMPTY, ItemTransforms.NO_TRANSFORMS);
	}
}
