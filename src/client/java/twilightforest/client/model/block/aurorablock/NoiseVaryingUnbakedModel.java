package twilightforest.client.model.block.aurorablock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Fabric port of upstream {@code twilightforest.client.model.block.aurorablock.UnbakedNoiseVaryingModel}.
 * NeoForge handled this via {@code IGeometryLoader} reading the JSON's {@code variants} array;
 * Fabric has no JSON {@code "loader"} hook, so we instead intercept the model at
 * {@link net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver} time and feed the
 * variant list in directly (registered in {@code CodexTwilightClient}).
 */
@Environment(EnvType.CLIENT)
public class NoiseVaryingUnbakedModel implements UnbakedModel {
	private final List<ResourceLocation> variantIds;
	private final List<UnbakedModel> resolvedVariants;

	public NoiseVaryingUnbakedModel(List<ResourceLocation> variantIds) {
		this.variantIds = List.copyOf(variantIds);
		this.resolvedVariants = new ArrayList<>(variantIds.size());
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return this.variantIds;
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelLookup) {
		this.resolvedVariants.clear();
		for (ResourceLocation id : this.variantIds) {
			UnbakedModel um = modelLookup.apply(id);
			if (um != null) {
				um.resolveParents(modelLookup);
				this.resolvedVariants.add(um);
			} else {
				this.resolvedVariants.add(modelLookup.apply(ModelBakery.MISSING_MODEL_LOCATION));
			}
		}
	}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
		BakedModel[] baked = new BakedModel[this.variantIds.size()];
		for (int i = 0; i < baked.length; i++) {
			ResourceLocation id = this.variantIds.get(i);
			BakedModel b = baker.bake(id, modelState);
			baked[i] = b != null ? b : baker.bake(ModelBakery.MISSING_MODEL_LOCATION, modelState);
		}
		return new NoiseVaryingModel(baked);
	}

	// We override the legacy 4-arg bake signature too in case mod / mixin paths invoke it.
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
		return this.bake(baker, spriteGetter, modelState);
	}

	@SuppressWarnings("unused")
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		return this.bake(baker, spriteGetter, modelState);
	}
}
