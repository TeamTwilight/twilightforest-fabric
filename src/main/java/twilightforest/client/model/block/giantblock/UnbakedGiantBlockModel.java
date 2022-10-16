package twilightforest.client.model.block.giantblock;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.*;
import java.util.function.Function;

public record UnbakedGiantBlockModel(Map<ResourceLocation, Map<Direction, ResourceLocation>> textures, String renderType, ResourceLocation parent) implements UnbakedModel {

	@Override
	public BakedModel bake(ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
		Map<Direction, TextureAtlasSprite> texturesReal = new HashMap<>();
		for (Direction direction : Direction.values()) {
			texturesReal.put(direction, spriteGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, this.textures().get(this.parent()).get(direction))));
		}
		return new GiantBlockModel(texturesReal);
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return Collections.emptyList();
	}
}
