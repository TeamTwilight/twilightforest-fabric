package twilightforest.client.model.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.TrollsteinnBlock;

import java.util.List;

public class TrollsteinnModel implements BakedModel {
	public static final ResourceLocation LIT_TROLLSTEINN_ID = TwilightForestMod.prefix("item/trollsteinn_light");
	public static final ModelResourceLocation LIT_TROLLSTEINN = new ModelResourceLocation(LIT_TROLLSTEINN_ID, "standalone");

	@Nullable
	private BakedModel litTrollsteinnModel;
	private final BakedModel originalModel;

	public TrollsteinnModel(BakedModel originalModel) {
		this.originalModel = originalModel;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
		return this.originalModel.getQuads(state, side, random);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.originalModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.originalModel.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return this.originalModel.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return this.originalModel.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.originalModel.getParticleIcon();
	}

	@Override
	public ItemTransforms getTransforms() {
		return this.originalModel.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.originalModel.getOverrides();
	}
}
