package twilightforest.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.models.TransformTypeDependentItemBakedModel;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryLoader;
import io.github.fabricators_of_create.porting_lib.models.geometry.IUnbakedGeometry;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
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
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

/**
 * An unfinished port of forge:separate_transforms
 */
public class SeparateTransformsModel implements IUnbakedGeometry<SeparateTransformsModel>
{
	public static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "separate_transforms");

	private final BlockModel baseModel;
	private final ImmutableMap<ItemDisplayContext, BlockModel> perspectives;

	public SeparateTransformsModel(BlockModel baseModel, ImmutableMap<ItemDisplayContext, BlockModel> perspectives)
	{
		this.baseModel = baseModel;
		this.perspectives = perspectives;
	}

	@Override
	public BakedModel bake(BlockModel context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation, boolean isGui3d) {
		return new Baked(
				context.hasAmbientOcclusion(), isGui3d, context.getGuiLight().lightLikeBlock(),
				spriteGetter.apply(context.getMaterial("particle")), overrides,
				baseModel.bake(baker, baseModel, spriteGetter, modelState, modelLocation, context.getGuiLight().lightLikeBlock()),
				ImmutableMap.copyOf(Maps.transformValues(perspectives, value -> {
					return value.bake(baker, value, spriteGetter, modelState, modelLocation, context.getGuiLight().lightLikeBlock());
				}))
		);
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, BlockModel context)
	{
		baseModel.resolveParents(modelGetter);
		perspectives.values().forEach(model -> model.resolveParents(modelGetter));
	}

	public static class Baked implements BakedModel, FabricBakedModel, TransformTypeDependentItemBakedModel {
		private final boolean isAmbientOcclusion;
		private final boolean isGui3d;
		private final boolean isSideLit;
		private final TextureAtlasSprite particle;
		private final ItemOverrides overrides;
		private final BakedModel baseModel;
		private final ImmutableMap<ItemDisplayContext, BakedModel> perspectives;

		public Baked(boolean isAmbientOcclusion, boolean isGui3d, boolean isSideLit, TextureAtlasSprite particle, ItemOverrides overrides, BakedModel baseModel, ImmutableMap<ItemDisplayContext, BakedModel> perspectives)
		{
			this.isAmbientOcclusion = isAmbientOcclusion;
			this.isGui3d = isGui3d;
			this.isSideLit = isSideLit;
			this.particle = particle;
			this.overrides = overrides;
			this.baseModel = baseModel;
			this.perspectives = perspectives;
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
			return baseModel.getQuads(state, direction, random);
		}

		@Override
		public boolean useAmbientOcclusion()
		{
			return isAmbientOcclusion;
		}

		@Override
		public boolean isGui3d()
		{
			return isGui3d;
		}

		@Override
		public boolean usesBlockLight()
		{
			return isSideLit;
		}

		@Override
		public boolean isCustomRenderer()
		{
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleIcon()
		{
			return particle;
		}

		@Override
		public ItemOverrides getOverrides()
		{
			return overrides;
		}

		@Override
		public ItemTransforms getTransforms()
		{
			return ItemTransforms.NO_TRANSFORMS;
		}

		@Override
		public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform, DefaultTransform defaultTransform) {
			if (perspectives.containsKey(cameraTransformType))
			{
				BakedModel p = perspectives.get(cameraTransformType);
				defaultTransform.apply(p);
				return p;
			} else {
				defaultTransform.apply(baseModel);
				return baseModel;
			}
		}
	}

	public static final class Loader implements IGeometryLoader<SeparateTransformsModel>
	{
		public static final Loader INSTANCE = new Loader();

		private Loader() {
		}

		@Override
		public SeparateTransformsModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
		{
			BlockModel baseModel = deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "base"), BlockModel.class);

			JsonObject perspectiveData = GsonHelper.getAsJsonObject(jsonObject, "perspectives");

			Map<ItemDisplayContext, BlockModel> perspectives = new HashMap<>();
			for (ItemDisplayContext transform : ItemDisplayContext.values())
			{
				if (perspectiveData.has(transform.getSerializedName()))
				{
					BlockModel perspectiveModel = deserializationContext.deserialize(GsonHelper.getAsJsonObject(perspectiveData, transform.getSerializedName()), BlockModel.class);
					perspectives.put(transform, perspectiveModel);
				}
			}

			return new SeparateTransformsModel(baseModel, ImmutableMap.copyOf(perspectives));
		}
	}
}