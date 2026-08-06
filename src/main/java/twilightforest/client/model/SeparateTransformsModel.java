package twilightforest.client.model;

import com.google.gson.*;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.models.TransformTypeDependentItemBakedModel;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryBakingContext;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;
import io.github.fabricators_of_create.porting_lib.models.geometry.IUnbakedGeometry;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.TwilightForestMod;

import java.util.*;
import java.util.function.Function;

// FIXME: Try to get Porting Lib model working
public class SeparateTransformsModel {

	public static final ResourceLocation ID = TwilightForestMod.prefix("separate_transforms");

	public static class Unbaked implements IUnbakedGeometry<Unbaked> {
		private final UnbakedModel base;
		private final Map<ItemDisplayContext, UnbakedModel> perspectives;

		public Unbaked(UnbakedModel base, Map<ItemDisplayContext, UnbakedModel> perspectives) {
			this.base = base;
			this.perspectives = perspectives;
		}

		@Override
		public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
			if (this.base instanceof BlockModel blockModel) {
				blockModel.resolveParents(modelGetter);
			}
			for (UnbakedModel model : this.perspectives.values()) {
				if (model instanceof BlockModel blockModel) {
					blockModel.resolveParents(modelGetter);
				}
			}
		}

		@Override
		public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
			BakedModel bakedBase = this.base.bake(baker, spriteGetter, modelState);
			Map<ItemDisplayContext, BakedModel> bakedPerspectives = new HashMap<>();
			for (var entry : this.perspectives.entrySet()) {
				bakedPerspectives.put(entry.getKey(), entry.getValue().bake(baker, spriteGetter, modelState));
			}
			return new Baked(bakedBase, bakedPerspectives, context);
		}
	}

	public static class Baked extends ForwardingBakedModel implements TransformTypeDependentItemBakedModel {
		private final Map<ItemDisplayContext, BakedModel> perspectiveModels;
		private final IGeometryBakingContext context;

		public Baked(BakedModel baseModel, Map<ItemDisplayContext, BakedModel> perspectiveModels, IGeometryBakingContext context) {
			this.wrapped = baseModel;
			this.perspectiveModels = perspectiveModels;
			this.context = context;
		}

		@Override
		public boolean isVanillaAdapter() {
			return false;
		}

		@Override
		public ItemTransforms getTransforms() {
			return this.context.getTransforms();
		}

		@Override
		public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform, DefaultTransform transform) {
			BakedModel perspectiveModel = this.perspectiveModels.get(transformType);
			if (perspectiveModel != null) {
				if (perspectiveModel instanceof TransformTypeDependentItemBakedModel td) {
					return td.applyTransform(transformType, poseStack, applyLeftHandTransform, transform);
				}
				transform.apply(perspectiveModel);
				return perspectiveModel;
			}
			transform.apply(this);
			return this;
		}
	}

	public static class Loader implements IGeometryLoader<Unbaked> {
		public static final Loader INSTANCE = new Loader();

		@Override
		public Unbaked read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
			UnbakedModel base = context.deserialize(json.get("base"), BlockModel.class);
			Map<ItemDisplayContext, UnbakedModel> perspectives = new HashMap<>();
			if (json.has("perspectives")) {
				JsonObject perspectivesJson = json.getAsJsonObject("perspectives");
				for (var entry : perspectivesJson.entrySet()) {
					ItemDisplayContext displayContext = ItemDisplayContext.valueOf(entry.getKey().toUpperCase(Locale.ROOT));
					UnbakedModel model = context.deserialize(entry.getValue(), BlockModel.class);
					perspectives.put(displayContext, model);
				}
			}
			return new Unbaked(base, perspectives);
		}
	}
}