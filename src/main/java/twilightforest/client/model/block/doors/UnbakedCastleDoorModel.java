package twilightforest.client.model.block.doors;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import org.joml.Vector3f;
import io.github.fabricators_of_create.porting_lib.models.geometry.IGeometryBakingContext;
import io.github.fabricators_of_create.porting_lib.models.geometry.IUnbakedGeometry;
import io.github.fabricators_of_create.porting_lib.models.UnbakedGeometryHelper;

import java.util.Map;
import java.util.function.Function;

public class UnbakedCastleDoorModel implements IUnbakedGeometry<UnbakedCastleDoorModel> {

	private final BlockElement[][] baseElements;
	private final BlockElement[][][] faceElements;

	public UnbakedCastleDoorModel() {
		this.baseElements = new BlockElement[6][4];
		this.faceElements = new BlockElement[6][4][5];
		Vec3i center = new Vec3i(8, 8, 8);

		for (Direction face : Direction.values()) {
			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];

			for (int quad = 0; quad < 4; quad++) {
				Vec3i corner = face.getNormal().offset(planeDirections[quad].getNormal()).offset(planeDirections[(quad + 1) % 4].getNormal()).offset(1, 1, 1).multiply(8);
				BlockElement element = new BlockElement(new Vector3f((float) Math.min(center.getX(), corner.getX()), (float) Math.min(center.getY(), corner.getY()), (float) Math.min(center.getZ(), corner.getZ())), new Vector3f((float) Math.max(center.getX(), corner.getX()), (float) Math.max(center.getY(), corner.getY()), (float) Math.max(center.getZ(), corner.getZ())), Map.of(), null, true);
				this.baseElements[face.get3DDataValue()][quad] = new BlockElement(element.from, element.to, Map.of(face, new BlockElementFace(face, -1, "", new BlockFaceUV(ConnectionLogic.NONE.remapUVs(element.uvsByFace(face)), 0))), null, true);

				for (ConnectionLogic connectionType : ConnectionLogic.values()) {
					this.faceElements[face.get3DDataValue()][quad][connectionType.ordinal()] = new BlockElement(element.from, element.to, Map.of(face, new BlockElementFace(face, 0, "", new BlockFaceUV(connectionType.remapUVs(element.uvsByFace(face)), 0))), null, true);
				}
			}
		}
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		RenderMaterial material = renderer.materialFinder().blendMode(0, BlendMode.CUTOUT).emissive(0, false).find();

		Mesh[] baseQuads = new Mesh[6];
		TextureAtlasSprite baseTexture = spriteGetter.apply(context.getMaterial("base"));

		for (int dir = 0; dir < 6; dir++) {
			MeshBuilder builder = renderer.meshBuilder();

			for (BlockElement element : this.baseElements[dir]) {
				QuadEmitter emitter = builder.getEmitter();
				BakedQuad quad = UnbakedGeometryHelper.bakeElementFace(element, element.faces.values().iterator().next(), baseTexture, Direction.values()[dir], modelState);
				emitter.fromVanilla(quad, material, Direction.values()[dir]);
				emitter.emit();
			}

			baseQuads[dir] = builder.build();
		}

		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{spriteGetter.apply(context.getMaterial("overlay")), spriteGetter.apply(context.getMaterial("overlay_connected"))};

		BakedQuad[][][] quads = new BakedQuad[6][4][5];

		for (int dir = 0; dir < 6; dir++) {
			for (int quad = 0; quad < 4; quad++) {
				for (int type = 0; type < 5; type++) {
					BlockElement element = this.faceElements[dir][quad][type];
					quads[dir][quad][type] = UnbakedGeometryHelper.bakeElementFace(element, element.faces.values().iterator().next(), ConnectionLogic.values()[type].chooseTexture(sprites), Direction.values()[dir], modelState);
				}
			}
		}

		TextureAtlasSprite particle = spriteGetter.apply(context.getMaterial("particle"));
		return new CastleDoorModel(baseQuads, quads, particle, overrides, context.getTransforms());
	}
}
