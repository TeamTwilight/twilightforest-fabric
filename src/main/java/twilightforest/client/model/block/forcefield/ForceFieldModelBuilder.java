package twilightforest.client.model.block.forcefield;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import io.github.fabricators_of_create.porting_lib.models.generators.CustomLoaderBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelBuilder;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ForceFieldModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
	protected final List<ForceFieldElementBuilder> elements = new ArrayList<>();

	public static <T extends ModelBuilder<T>> ForceFieldModelBuilder<T> begin(T parent, ExistingFileHelper helper) {
		return new ForceFieldModelBuilder<>(parent, helper);
	}

	private ForceFieldModelBuilder<T> self() {
		return this;
	}

	protected ForceFieldModelBuilder(T parent, ExistingFileHelper helper) {
		super(TwilightForestMod.prefix("force_field"), parent, helper, false);
	}

	public ForceFieldElementBuilder forceFieldElement() {
		ForceFieldElementBuilder ret = new ForceFieldElementBuilder();
		elements.add(ret);
		return ret;
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);
		if (!this.elements.isEmpty()) {
			JsonArray elements = new JsonArray();
			this.elements.forEach(forceFieldElementBuilder -> {
				BlockElement part = forceFieldElementBuilder.build();
				JsonObject partObj = new JsonObject();

				if (forceFieldElementBuilder.condition != null) {
					JsonObject condition = new JsonObject();
					condition.addProperty("if", forceFieldElementBuilder.condition.getSecond());
					condition.addProperty("direction", forceFieldElementBuilder.condition.getFirst().getSerializedName());

					JsonArray parents = new JsonArray();
					for (ForceFieldModel.ExtraDirection parent : forceFieldElementBuilder.parents) {
						parents.add(parent.getSerializedName());
					}
					condition.add("parents", parents);

					partObj.add("condition", condition);
				}

				partObj.add("from", serializeVector3f(part.from));
				partObj.add("to", serializeVector3f(part.to));

				if (part.rotation != null) {
					JsonObject rotation = new JsonObject();
					rotation.add("origin", serializeVector3f(part.rotation.origin()));
					rotation.addProperty("axis", part.rotation.axis().getSerializedName());
					rotation.addProperty("angle", part.rotation.angle());
					if (part.rotation.rescale()) {
						rotation.addProperty("rescale", part.rotation.rescale());
					}
					partObj.add("rotation", rotation);
				}

				if (!part.shade) {
					partObj.addProperty("shade", part.shade);
				}

				JsonObject faces = new JsonObject();
				for (Direction dir : Direction.values()) {
					BlockElementFace face = part.faces.get(dir);
					if (face == null) continue;

					JsonObject faceObj = new JsonObject();
					faceObj.addProperty("texture", serializeLocOrKey(face.texture()));
					if (!Arrays.equals(face.uv().uvs, part.uvsByFace(dir))) {
						faceObj.add("uv", new Gson().toJsonTree(face.uv().uvs));
					}
					if (face.cullForDirection() != null) {
						faceObj.addProperty("cullface", face.cullForDirection().getSerializedName());
					}
					if (face.uv().rotation != 0) {
						faceObj.addProperty("rotation", face.uv().rotation);
					}
					if (face.tintIndex() != -1) {
						faceObj.addProperty("tintindex", face.tintIndex());
					}
					faces.add(dir.getSerializedName(), faceObj);
				}
				if (!part.faces.isEmpty()) {
					partObj.add("faces", faces);
				}
				elements.add(partObj);
			});
			json.add("elements", elements);
		}
		return json;
	}

	private String serializeLocOrKey(String tex) {
		if (tex.charAt(0) == '#') {
			return tex;
		}
		return ResourceLocation.parse(tex).toString();
	}

	private JsonArray serializeVector3f(Vector3f vec) {
		JsonArray ret = new JsonArray();
		ret.add(serializeFloat(vec.x()));
		ret.add(serializeFloat(vec.y()));
		ret.add(serializeFloat(vec.z()));
		return ret;
	}

	private Number serializeFloat(float f) {
		if ((int) f == f) return (int) f;
		return f;
	}

	public class ForceFieldElementBuilder {
		private Vector3f from = new Vector3f();
		private Vector3f to = new Vector3f(16, 16, 16);
		private final Map<Direction, FaceBuilder> faces = new LinkedHashMap<>();
		private RotationBuilder rotation;
		private boolean shade = true;
		private Pair<ForceFieldModel.ExtraDirection, Boolean> condition = null;
		private final List<ForceFieldModel.ExtraDirection> parents = new ArrayList<>();

		private void validateCoordinate(float coord, char name) {
			Preconditions.checkArgument(!(coord < -16.0F) && !(coord > 32.0F), "Position " + name + " out of range, must be within [-16, 32]. Found: %d", coord);
		}

		private void validatePosition(Vector3f pos) {
			validateCoordinate(pos.x(), 'x');
			validateCoordinate(pos.y(), 'y');
			validateCoordinate(pos.z(), 'z');
		}

		public ForceFieldElementBuilder from(float x, float y, float z) {
			this.from = new Vector3f(x, y, z);
			validatePosition(this.from);
			return this;
		}

		public ForceFieldElementBuilder to(float x, float y, float z) {
			this.to = new Vector3f(x, y, z);
			validatePosition(this.to);
			return this;
		}

		public FaceBuilder face(Direction dir) {
			Preconditions.checkNotNull(dir, "Direction must not be null");
			return faces.computeIfAbsent(dir, FaceBuilder::new);
		}

		public RotationBuilder rotation() {
			if (this.rotation == null) {
				this.rotation = new RotationBuilder();
			}
			return this.rotation;
		}

		public ForceFieldElementBuilder shade(boolean shade) {
			this.shade = shade;
			return this;
		}

		public ForceFieldElementBuilder allFaces(BiConsumer<Direction, FaceBuilder> action) {
			Arrays.stream(Direction.values())
				.forEach(d -> action.accept(d, face(d)));
			return this;
		}

		public ForceFieldElementBuilder faces(BiConsumer<Direction, FaceBuilder> action) {
			faces.forEach(action);
			return this;
		}

		public ForceFieldElementBuilder textureAll(String texture) {
			return allFaces(addTexture(texture));
		}

		public ForceFieldElementBuilder texture(String texture) {
			return faces(addTexture(texture));
		}

		public ForceFieldElementBuilder cube(String texture) {
			return allFaces(addTexture(texture).andThen((dir, f) -> f.cullface(dir)));
		}

		public ForceFieldElementBuilder emissivity(int blockLight, int skyLight) {
			// Emissive rendering handled at runtime by ForceFieldModel, all force field faces are emissive
			return this;
		}

		public ForceFieldElementBuilder color(int color) {
			return this;
		}

		public ForceFieldElementBuilder ao(boolean ao) {
			return this;
		}

		public ForceFieldElementBuilder ifState(ForceFieldModel.ExtraDirection condition, boolean b) {
			this.condition = Pair.of(condition, b);
			return this;
		}

		public ForceFieldElementBuilder ifSame() {
			ForceFieldElementBuilder newBuilder = this.end().forceFieldElement();
			newBuilder.condition = Pair.of(this.condition.getFirst(), this.condition.getSecond());
			return newBuilder;
		}

		public ForceFieldElementBuilder ifElse() {
			ForceFieldElementBuilder newBuilder = this.end().forceFieldElement();
			newBuilder.condition = Pair.of(this.condition.getFirst(), !this.condition.getSecond());
			return newBuilder;
		}

		public ForceFieldElementBuilder parents(ForceFieldModel.ExtraDirection... parents) {
			Collections.addAll(this.parents, parents);
			return this;
		}

		private BiConsumer<Direction, FaceBuilder> addTexture(String texture) {
			return ($, f) -> f.texture(texture);
		}

		BlockElement build() {
			Map<Direction, BlockElementFace> faces = this.faces.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().build(), (k1, k2) -> {
					throw new IllegalArgumentException();
				}, LinkedHashMap::new));
			return new BlockElement(from, to, faces, rotation == null ? null : rotation.build(), shade);
		}

		public ForceFieldModelBuilder<T> end() {
			return self();
		}

		public class FaceBuilder {
			private Direction cullface;
			private int tintindex = -1;
			private String texture = MissingTextureAtlasSprite.getLocation().toString();
			private float[] uvs;
			private FaceRotation rotation = FaceRotation.ZERO;

			FaceBuilder(Direction dir) {
			}

			public FaceBuilder cullface(@Nullable Direction dir) {
				this.cullface = dir;
				return this;
			}

			public FaceBuilder tintindex(int index) {
				this.tintindex = index;
				return this;
			}

			public FaceBuilder texture(String texture) {
				Preconditions.checkNotNull(texture, "Texture must not be null");
				this.texture = texture;
				return this;
			}

			public FaceBuilder uvs(float u1, float v1, float u2, float v2) {
				this.uvs = new float[]{u1, v1, u2, v2};
				return this;
			}

			public FaceBuilder rotation(FaceRotation rot) {
				Preconditions.checkNotNull(rot, "Rotation must not be null");
				this.rotation = rot;
				return this;
			}

			public FaceBuilder emissivity(int blockLight, int skyLight) {
				return this;
			}

			public FaceBuilder color(int color) {
				return this;
			}

			public FaceBuilder ao(boolean ao) {
				return this;
			}

			BlockElementFace build() {
				if (this.texture == null) {
					throw new IllegalStateException("A model face must have a texture");
				}
				return new BlockElementFace(cullface, tintindex, texture, new BlockFaceUV(uvs, rotation.rotation));
			}

			public ForceFieldElementBuilder end() {
				return ForceFieldElementBuilder.this;
			}
		}

		public class RotationBuilder {

			private Vector3f origin;
			private Direction.Axis axis;
			private float angle;
			private boolean rescale;

			public RotationBuilder origin(float x, float y, float z) {
				this.origin = new Vector3f(x, y, z);
				return this;
			}

			public RotationBuilder axis(Direction.Axis axis) {
				Preconditions.checkNotNull(axis, "Axis must not be null");
				this.axis = axis;
				return this;
			}

			public RotationBuilder angle(float angle) {
				Preconditions.checkArgument(angle == 0.0F || Mth.abs(angle) == 22.5F || Mth.abs(angle) == 45.0F, "Invalid rotation %f found, only -45/-22.5/0/22.5/45 allowed", angle);
				this.angle = angle;
				return this;
			}

			public RotationBuilder rescale(boolean rescale) {
				this.rescale = rescale;
				return this;
			}

			BlockElementRotation build() {
				return new BlockElementRotation(origin, axis, angle, rescale);
			}

			public ForceFieldElementBuilder end() {
				return ForceFieldElementBuilder.this;
			}
		}
	}

	public enum FaceRotation {
		ZERO(0),
		CLOCKWISE_90(90),
		UPSIDE_DOWN(180),
		COUNTERCLOCKWISE_90(270),
		;

		final int rotation;

		FaceRotation(int rotation) {
			this.rotation = rotation;
		}
	}

}
