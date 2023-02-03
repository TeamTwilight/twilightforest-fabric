package twilightforest.client.model.block.patch;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public record UnbakedPatchModel(Material material, boolean shaggify) implements UnbakedModel {
    public UnbakedPatchModel(ResourceLocation texture, boolean shaggify) {
        this(new Material(InventoryMenu.BLOCK_ATLAS, texture), shaggify);
    }

    @Override
    public BakedModel bake(ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {
        return new PatchModel(modelLocation, spriteGetter.apply(this.material()), this.shaggify());
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptySet();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> models) {}
}
