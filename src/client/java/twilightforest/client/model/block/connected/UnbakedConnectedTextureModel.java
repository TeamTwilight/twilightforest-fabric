package twilightforest.client.model.block.connected;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
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
import net.minecraft.world.level.block.Block;
import org.joml.Vector3f;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UnbakedConnectedTextureModel implements UnbakedModel {
	private static final FaceBakery FACE_BAKERY = new FaceBakery();

	private final boolean renderOnDisabledFaces;
	private final EnumSet<Direction> enabledFaces;
	private final List<Block> connectableBlocks;
	private final int baseTintIndex;
	private final int tintIndex;
	private final Map<String, Material> materials;

	public UnbakedConnectedTextureModel(EnumSet<Direction> enabledFaces, boolean renderOnDisabledFaces, List<Block> connectableBlocks, int baseTintIndex, int baseEmissivity, int tintIndex, int emissivity, Map<String, Material> materials) {
		this.enabledFaces = enabledFaces;
		this.renderOnDisabledFaces = renderOnDisabledFaces;
		this.connectableBlocks = connectableBlocks;
		this.baseTintIndex = baseTintIndex;
		this.tintIndex = tintIndex;
		this.materials = materials;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return List.of();
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter) {
	}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
		@SuppressWarnings("unchecked")
		List<BakedQuad>[] baseQuads = this.materials.containsKey("base_texture") ? (List<BakedQuad>[]) Array.newInstance(List.class, 6) : null;
		TextureAtlasSprite baseTexture = baseQuads != null ? spriteGetter.apply(this.materials.get("base_texture")) : null;
		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{
				spriteGetter.apply(this.materials.get("overlay_texture")),
				spriteGetter.apply(this.materials.get("overlay_connected")),
				spriteGetter.apply(this.materials.getOrDefault("particle", this.materials.get("overlay_texture")))
		};
		BakedQuad[][][] quads = new BakedQuad[6][4][ConnectionLogic.values().length];

		for (Direction face : Direction.values()) {
			int faceIndex = face.get3DDataValue();
			if (baseQuads != null) {
				baseQuads[faceIndex] = new ArrayList<>();
			}

			for (int quad = 0; quad < 4; ++quad) {
				Vector3f[] box = boxFor(face, quad);
				float[] baseUvs = uvsByFace(face, box[0], box[1]);
				if (baseQuads != null) {
					baseQuads[faceIndex].add(this.bakeFace(face, box[0], box[1], baseTexture, ConnectionLogic.NONE.remapUVs(baseUvs), this.baseTintIndex, modelState));
				}

				for (ConnectionLogic logic : ConnectionLogic.values()) {
					quads[faceIndex][quad][logic.ordinal()] = this.bakeFace(face, box[0], box[1], logic.chooseTexture(sprites), logic.remapUVs(baseUvs), this.tintIndex, modelState);
				}
			}
		}

		return new ConnectedTextureModel(this.enabledFaces, this.renderOnDisabledFaces, this.connectableBlocks, baseQuads, quads, sprites[2], ItemOverrides.EMPTY, ItemTransforms.NO_TRANSFORMS);
	}

	private BakedQuad bakeFace(Direction face, Vector3f from, Vector3f to, TextureAtlasSprite texture, float[] uvs, int tintIndex, ModelState modelState) {
		BlockElementFace blockFace = new BlockElementFace(face, tintIndex, texture.atlasLocation().toString(), new BlockFaceUV(uvs, 0));
		return FACE_BAKERY.bakeQuad(from, to, blockFace, texture, face, modelState, null, true);
	}

	private static Vector3f[] boxFor(Direction face, int quad) {
		Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];
		Direction first = planeDirections[quad];
		Direction second = planeDirections[(quad + 1) % 4];
		int x = (face.getStepX() + first.getStepX() + second.getStepX() + 1) * 8;
		int y = (face.getStepY() + first.getStepY() + second.getStepY() + 1) * 8;
		int z = (face.getStepZ() + first.getStepZ() + second.getStepZ() + 1) * 8;
		return new Vector3f[]{
				new Vector3f(Math.min(8, x), Math.min(8, y), Math.min(8, z)),
				new Vector3f(Math.max(8, x), Math.max(8, y), Math.max(8, z))
		};
	}

	private static float[] uvsByFace(Direction face, Vector3f from, Vector3f to) {
		return switch (face) {
			case DOWN -> new float[]{from.x(), 16.0F - to.z(), to.x(), 16.0F - from.z()};
			case UP -> new float[]{from.x(), from.z(), to.x(), to.z()};
			case NORTH -> new float[]{16.0F - to.x(), 16.0F - to.y(), 16.0F - from.x(), 16.0F - from.y()};
			case SOUTH -> new float[]{from.x(), 16.0F - to.y(), to.x(), 16.0F - from.y()};
			case WEST -> new float[]{from.z(), 16.0F - to.y(), to.z(), 16.0F - from.y()};
			case EAST -> new float[]{16.0F - to.z(), 16.0F - to.y(), 16.0F - from.z(), 16.0F - from.y()};
		};
	}
}
