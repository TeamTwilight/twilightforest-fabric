package twilightforest.client.model.block.doors;

import com.mojang.math.Transformation;
import io.github.fabricators_of_create.porting_lib.models.geometry.SimpleModelState;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import org.joml.Vector3f;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import twilightforest.mixin.BlockModelAccessor;

import java.util.*;
import java.util.function.Function;

//for now, im keeping this hardcoded to a 2 layer block, with the overlay layer being fullbright and tinted.
//It might be worth expanding this in the future to be more flexible for other kinds of blocks (1 layer blocks, determining emissivity and tinting per layer, maybe >2 layer blocks?) but for now, I see no point.
//I only wanted this system for castle doors after all!
public class UnbakedCastleDoorModel extends BlockModel {

	private final BlockElement[][] baseElements;
	private final BlockElement[][][] faceElements;

	private final BlockModel ownerModel;

	public UnbakedCastleDoorModel(BlockModel ownerModel) {
		super(ownerModel.parentLocation, ownerModel.getElements(), ownerModel.textureMap, ownerModel.hasAmbientOcclusion(), ownerModel.getGuiLight(), ownerModel.getTransforms(), ownerModel.getOverrides());
		this.ownerModel = ownerModel;
		//base elements - the base block. No Connected Textures on this bit.
		//the array is made of the directions and quads
		this.baseElements = new BlockElement[6][4];

		//face elements - the connected bit of the model.
		//the array is made of the directions, quads, and each logic value in the ConnectionLogic class
		//645 shows up quite a few times, I call it the magic number
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
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
		Transformation transformation = Transformation.identity();
		if (!transformation.isIdentity()) {
			modelState = new SimpleModelState(modelState.getRotation().compose(transformation), modelState.isUvLocked());
		}

		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		RenderMaterial material = renderer.materialFinder().blendMode(0, BlendMode.CUTOUT).find();
		//making an array list like this is cursed, would not recommend
		@SuppressWarnings("unchecked") //this is fine, I hope
		Mesh[] baseQuads = new Mesh[6];
		TextureAtlasSprite baseTexture = spriteGetter.apply(ownerModel.getMaterial("base"));

		for (int dir = 0; dir < 6; dir++) {
			MeshBuilder builder = renderer.meshBuilder();

			for (BlockElement element : this.baseElements[dir]) {
				QuadEmitter emitter = builder.getEmitter();
				emitter.fromVanilla(BlockModel.bakeFace(element, element.faces.values().iterator().next(), baseTexture, Direction.values()[dir], modelState, modelLocation), material, Direction.values()[dir]);
				emitter.emit();
			}

			baseQuads[dir] = builder.build();
		}

		//we'll use this to figure out which texture to use with the Connected Texture logic
		//NONE uses the first one, everything else uses the 2nd one
		TextureAtlasSprite[] sprites = new TextureAtlasSprite[]{spriteGetter.apply(ownerModel.getMaterial("overlay")), spriteGetter.apply(ownerModel.getMaterial("overlay_connected"))};

		BakedQuad[][][] quads = new BakedQuad[6][4][5];

		for (int dir = 0; dir < 6; dir++) {
			for (int quad = 0; quad < 4; quad++) {
				for (int type = 0; type < 5; type++) {
					BlockElement element = this.faceElements[dir][quad][type];
					quads[dir][quad][type] = BlockModel.bakeFace(element, element.faces.values().iterator().next(), ConnectionLogic.values()[type].chooseTexture(sprites), Direction.values()[dir], modelState, modelLocation);
				}
			}
		}

		return new CastleDoorModel(baseQuads, quads, spriteGetter.apply(ownerModel.getMaterial("particle")), ((BlockModelAccessor)ownerModel).tf$callGetItemOverrides(baker, ownerModel), ownerModel.getTransforms());
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> models) {
	}
}