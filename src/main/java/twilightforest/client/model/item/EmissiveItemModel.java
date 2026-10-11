package twilightforest.client.model.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.resources.model.ModelBaker;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

public final class EmissiveItemModel implements ItemModel {
	private final ItemModel baseModel;

	public EmissiveItemModel(ItemModel baseModel) {
		this.baseModel = baseModel;
	}

	@Override
	public void update(ItemStackRenderState output, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext context, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		this.baseModel.update(output, stack, resolver, context, level, owner, seed);
	}

	public record Unbaked(ItemModel.Unbaked baseModel, int lightEmission) implements ItemModel.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ItemModels.CODEC.fieldOf("base_model")
				.forGetter(Unbaked::baseModel),
			Codec.intRange(0, 15)
				.fieldOf("light_emission")
				.forGetter(Unbaked::lightEmission)
		).apply(instance, Unbaked::new));

		@Override
		public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
			if (!(this.baseModel instanceof CuboidItemModelWrapper.Unbaked cuboid)) {
				throw new IllegalArgumentException("EmissiveItemModel currently requires minecraft:model as its base_model");
			}

			ModelBaker baker = context.blockModelBaker();
			ResolvedModel resolvedModel = baker.getModel(cuboid.model());
			TextureSlots textureSlots = resolvedModel.getTopTextureSlots();

			QuadCollection originalQuads = resolvedModel.bakeTopGeometry(textureSlots, baker, BlockModelRotation.IDENTITY);
			QuadCollection.Builder builder = new QuadCollection.Builder();

			for (BakedQuad quad : originalQuads.getAll()) {
				BakedQuad.MaterialInfo old = quad.materialInfo();
				BakedQuad emissiveQuad = new BakedQuad(
					quad.position0(),
					quad.position1(),
					quad.position2(),
					quad.position3(),
					quad.packedUV0(),
					quad.packedUV1(),
					quad.packedUV2(),
					quad.packedUV3(),
					quad.direction(),
					new BakedQuad.MaterialInfo(
						old.sprite(),
						old.layer(),
						old.itemRenderType(),
						old.tintIndex(),
						old.shade(),
						lightEmission
					)
				);

				if (quad.direction() == null) {
					builder.addUnculledFace(emissiveQuad);
				} else {
					builder.addCulledFace(quad.direction(), emissiveQuad);
				}
			}

			QuadCollection emissiveQuads = builder.build();
			return new CuboidItemModelWrapper(
				cuboid.tints(),
				emissiveQuads,
				ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureSlots),
				transformation
			);
		}

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			this.baseModel.resolveDependencies(resolver);
		}

		@Override
		public MapCodec<? extends ItemModel.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
