package twilightforest.client.model.block.giantblock;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.block.GiantBlock;
import twilightforest.util.Vec2i;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GiantBlockModel implements BakedModel, FabricBakedModel {
	private static final FaceBakery FACE_BAKERY = new FaceBakery();

	private final TextureAtlasSprite[] textures;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;
	private final ItemTransforms transforms;

	public GiantBlockModel(TextureAtlasSprite[] textures, TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms) {
		this.textures = textures;
		this.particle = particle;
		this.overrides = overrides;
		this.transforms = transforms;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().find();
		for (Direction side : Direction.values()) {
			BakedQuad quad = this.getQuad(pos, side);
			if (quad != null) {
				context.getEmitter().fromVanilla(quad, material, side).emit();
			}
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().find();
		for (BakedQuad quad : this.getQuads(null, null, randomSupplier.get())) {
			context.getEmitter().fromVanilla(quad, material, quad.getDirection()).emit();
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
		List<BakedQuad> quads = new ArrayList<>();
		BlockPos pos = BlockPos.ZERO;
		if (side != null) {
			BakedQuad quad = this.getQuad(pos, side);
			if (quad != null) {
				quads.add(quad);
			}
			return quads;
		}

		for (Direction direction : Direction.values()) {
			BakedQuad quad = this.getQuad(pos, direction);
			if (quad != null) {
				quads.add(quad);
			}
		}
		return quads;
	}

	@Nullable
	private BakedQuad getQuad(BlockPos pos, Direction side) {
		BlockPos shifted = pos.offset(this.magicOffsetFromDir(side));
		Vec2i coords = this.calculateOffset(side, shifted);
		TextureAtlasSprite sprite = this.textures[this.textures.length > 1 ? side.ordinal() : 0];

		if (Iterables.contains(GiantBlock.getVolume(pos), pos.offset(side.getNormal()))) {
			return null;
		}

		return FACE_BAKERY.bakeQuad(
				new Vector3f(0.0F, 0.0F, 0.0F),
				new Vector3f(16.0F, 16.0F, 16.0F),
				new BlockElementFace(side, side.ordinal(), side.name(), new BlockFaceUV(new float[]{coords.x, coords.z, 4.0F + coords.x, 4.0F + coords.z}, 0)),
				sprite,
				side,
				BlockModelRotation.X0_Y0,
				null,
				true);
	}

	private BlockPos magicOffsetFromDir(Direction side) {
		return switch (side) {
			default -> new BlockPos(0, 0, -1);
			case DOWN -> new BlockPos(0, 0, 2);
			case NORTH, SOUTH -> new BlockPos(0, 1, 0);
			case WEST, EAST -> new BlockPos(0, 1, -1);
		};
	}

	private Vec2i calculateOffset(Direction side, BlockPos pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int offsetX;
		int offsetY;

		if (side.getAxis().isVertical()) {
			offsetX = x % 4;
			offsetY = (side.getStepY() * z + 1) % 4;
		} else if (side.getAxis() == Direction.Axis.Z) {
			offsetX = x % 4;
			offsetY = -y % 4;
		} else {
			offsetX = (z + 1) % 4;
			offsetY = -y % 4;
		}

		if (side == Direction.NORTH || side == Direction.EAST) {
			offsetX = (4 - offsetX - 1) % 4;
		}

		if (offsetX < 0) {
			offsetX += 16;
		}
		if (offsetY < 0) {
			offsetY += 16;
		}

		return new Vec2i((offsetX % 4) * 4, (offsetY % 4) * 4);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return true;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.particle;
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.overrides;
	}

	@Override
	public ItemTransforms getTransforms() {
		return this.transforms;
	}
}
