package twilightforest.client.model.block.patch;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import twilightforest.client.model.PatchModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public record UnbakedPatchModel(Material material, boolean shaggify) implements UnbakedModel {
    public UnbakedPatchModel(ResourceLocation texture, boolean shaggify) {
        this(new Material(InventoryMenu.BLOCK_ATLAS, texture), shaggify);
    }

    @Override
    public BakedModel bake(ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {
        return new PatchModel(modelLocation, spriteGetter.apply(this.material()), this.shaggify());
    }

    @Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return Collections.singleton(this.material());
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptySet();
    }
}
