package twilightforest.client.model.block.giantblock;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import io.github.fabricators_of_create.porting_lib.models.data.ModelData;
import io.github.fabricators_of_create.porting_lib.render_types.RenderTypeGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.block.GiantBlock;
import twilightforest.client.model.block.IDynamicBakedModel;
import twilightforest.util.Vec2i;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Iterables;

public class GiantBlockModel implements IDynamicBakedModel, FabricBakedModel {

	private static final FaceBakery FACE_BAKERY = new FaceBakery();
	private static final RenderMaterial SOLID = RendererAccess.INSTANCE.getRenderer().materialFinder().find();

	private final TextureAtlasSprite[] textures;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;
	private final ItemTransforms transforms;
	private final RenderMaterial material;

	public GiantBlockModel(TextureAtlasSprite[] texture, TextureAtlasSprite particle, ItemOverrides overrides, ItemTransforms transforms, RenderTypeGroup group) {
		this.textures = texture;
		this.particle = particle;
		this.overrides = overrides;
		this.transforms = transforms;
		this.material = group.isEmpty() ? SOLID : group.material();
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
		return List.of();
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		QuadEmitter emitter = context.getEmitter();

		for (Direction side : Direction.values()) {
			// Cull faces that are inside the 4x4x4 giant block volume
			if (Iterables.contains(GiantBlock.getVolume(pos), pos.offset(side.getStepX(), side.getStepY(), side.getStepZ()))) {
				continue;
			}

			Vec2i coords = this.calculateOffset(side, pos.offset(this.magicOffsetFromDir(side)));

			TextureAtlasSprite sprite = this.textures[this.textures.length > 1 ? side.ordinal() : 0];

			BakedQuad quad = FACE_BAKERY.bakeQuad(
				new Vector3f(0.0F, 0.0F, 0.0F),
				new Vector3f(16.0F, 16.0F, 16.0F),
				new BlockElementFace(side, side.ordinal(), side.name(), new BlockFaceUV(new float[]{0.0F + coords.x, 0.0F + coords.z, 4.0F + coords.x, 4.0F + coords.z}, 0)),
				sprite,
				side,
				BlockModelRotation.X0_Y0,
				null,
				true
			);
			emitter.fromVanilla(quad, this.material, quad.getDirection());
			emitter.emit();
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
	}

	//based on the offsets written in the original giant block json
	private BlockPos magicOffsetFromDir(Direction side) {
		return switch (side) {
			default -> new BlockPos(0, 0, -1);
			case DOWN -> new BlockPos(0, 0, 2);
			case NORTH, SOUTH -> new BlockPos(0, 1, 0);
			case WEST, EAST -> new BlockPos(0, 1, -1);
		};
	}

	//an offset is calculated between 0 and 15 based on which side its on.
	//it then grabs the remainder after dividing by 4 and then multiply by 4 to scale correctly
	private Vec2i calculateOffset(Direction side, BlockPos pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int offsetX, offsetY;

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

	@NotNull
	@Override
	public ItemTransforms getTransforms() {
		return this.transforms;
	}
}