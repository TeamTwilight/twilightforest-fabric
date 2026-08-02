package twilightforest.client.model.block;

import com.google.common.base.MoreObjects;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import io.github.fabricators_of_create.porting_lib.models.data.ModelData;
import io.github.fabricators_of_create.porting_lib.models.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import twilightforest.block.entity.ReactorDebrisBlockEntity;
import twilightforest.client.renderer.block.ReactorDebrisRenderer;

import java.util.function.Supplier;

public class ReactorDebrisModel extends ForwardingBakedModel {
	public static final ModelProperty<ResourceLocation> TEXTURE_FOR_PARTICLE = new ModelProperty<>();

	public ReactorDebrisModel(BakedModel defaultModel) {
		this.wrapped = defaultModel;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		this.wrapped.emitBlockQuads(level, state, pos, randomSupplier, context);
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		this.wrapped.emitItemQuads(stack, randomSupplier, context);
	}

	public @NotNull ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
		if (!(level.getBlockEntity(pos) instanceof ReactorDebrisBlockEntity reactorDebrisBlockEntity)
			|| !(level instanceof ClientLevel clientLevel))
			return modelData.derive().with(TEXTURE_FOR_PARTICLE, ReactorDebrisBlockEntity.DEFAULT_TEXTURE).build();
		final ResourceLocation textureForParticle = reactorDebrisBlockEntity.textures[clientLevel.random.nextInt(reactorDebrisBlockEntity.textures.length)];
		return modelData.derive().with(TEXTURE_FOR_PARTICLE, textureForParticle).build();
	}

	public @NotNull TextureAtlasSprite getParticleIcon(ModelData data) {
		ResourceLocation texturePath = MoreObjects.firstNonNull(data.get(TEXTURE_FOR_PARTICLE), ReactorDebrisBlockEntity.DEFAULT_TEXTURE);
		return ReactorDebrisRenderer.getSprite(texturePath);
	}
}