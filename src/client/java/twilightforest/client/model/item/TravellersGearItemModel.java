package twilightforest.client.model.item;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TravellersGearItemModel implements UnbakedModel {
	private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
	private static final Function<Float, Transformation> TRANSFORM = f -> new Transformation(null, null, new Vector3f(1.0F + f), null);

	private final List<Holder.Reference<TravellersModifier>> modifiers;
	private final String directory;
	private final boolean broken;
	private final String brokenDirectory;
	private final boolean showGloves;
	private final Map<String, Material> materials;

	TravellersGearItemModel(List<Holder.Reference<TravellersModifier>> modifiers, String directory, boolean broken, String brokenDirectory, boolean showGloves, Map<String, Material> materials) {
		this.modifiers = modifiers;
		this.directory = directory;
		this.broken = broken;
		this.brokenDirectory = brokenDirectory;
		this.showGloves = showGloves;
		this.materials = materials;
	}

	public TravellersGearItemModel withModifiers(List<Holder.Reference<TravellersModifier>> modifiers, boolean broken, boolean showGloves) {
		return new TravellersGearItemModel(modifiers, this.directory, broken, this.brokenDirectory, showGloves, this.materials);
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
		TextureAtlasSprite baseSprite = this.getSprite(spriteGetter);
		List<net.minecraft.client.renderer.block.model.BakedQuad> quads = new ArrayList<>();
		int layers = 0;

		if (baseSprite != null) {
			quads.addAll(bakeLayer(this.getBaseMaterial(), baker, spriteGetter, modelState).getQuads(null, null, RandomSource.create(42L)));
			layers++;
		}

		for (Holder.Reference<TravellersModifier> modifier : this.modifiers) {
			Material material = this.getModifierMaterial(modifier.key());
			TextureAtlasSprite modSprite = spriteGetter.apply(material);
			if (!modSprite.contents().name().equals(MissingTextureAtlasSprite.getLocation())) {
				quads.addAll(bakeLayer(material, baker, spriteGetter, scaled(modelState, layers * 0.001F)).getQuads(null, null, RandomSource.create(42L)));
				layers++;
			}
		}

		if (this.showGloves) {
			Material glovesMaterial = new Material(InventoryMenu.BLOCK_ATLAS, TwilightForestMod.prefix("item/" + (this.broken ? this.brokenDirectory : this.directory) + "gloves"));
			TextureAtlasSprite glovesSprite = spriteGetter.apply(glovesMaterial);
			if (!glovesSprite.contents().name().equals(MissingTextureAtlasSprite.getLocation())) {
				quads.addAll(bakeLayer(glovesMaterial, baker, spriteGetter, scaled(modelState, layers * 0.001F)).getQuads(null, null, RandomSource.create(42L)));
			}
		}

		TextureAtlasSprite particle = baseSprite != null ? baseSprite : spriteGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, MissingTextureAtlasSprite.getLocation()));
		return new SimpleBakedModel(quads, Map.of(), false, false, false, particle, net.minecraft.client.renderer.block.model.ItemTransforms.NO_TRANSFORMS, ItemOverrides.EMPTY);
	}

	@Nullable
	private TextureAtlasSprite getSprite(Function<Material, TextureAtlasSprite> spriteGetter) {
		Material material = this.getBaseMaterial();
		return material != null ? spriteGetter.apply(material) : null;
	}

	@Nullable
	private Material getBaseMaterial() {
		if (this.broken && this.materials.containsKey("broken")) {
			return this.materials.get("broken");
		}
		return this.materials.get("base");
	}

	private Material getModifierMaterial(ResourceKey<TravellersModifier> modifier) {
		return new Material(InventoryMenu.BLOCK_ATLAS, modifier.location().withPrefix("item/" + (this.broken ? this.brokenDirectory : this.directory)));
	}

	private static BakedModel bakeLayer(Material material, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
		BlockModel model = BlockModel.fromString("{\"parent\":\"minecraft:item/generated\",\"textures\":{\"layer0\":\"" + material.texture() + "\"}}");
		model.resolveParents(baker::getModel);
		BlockModel generated = ITEM_MODEL_GENERATOR.generateBlockModel(spriteGetter, model);
		return generated.bake(baker, spriteGetter, modelState);
	}

	private static ModelState scaled(ModelState parent, float scale) {
		return new ModelState() {
			@Override
			public Transformation getRotation() {
				return parent.getRotation().compose(TRANSFORM.apply(scale));
			}

			@Override
			public boolean isUvLocked() {
				return parent.isUvLocked();
			}
		};
	}

	public static final class Loader {
		public static final TravellersGearItemModel.Loader INSTANCE = new TravellersGearItemModel.Loader();

		private Loader() {
		}

		public TravellersGearItemModel read(JsonObject object) {
			Map<String, Material> materials = new LinkedHashMap<>();
			JsonObject textures = GsonHelper.getAsJsonObject(object, "textures", new JsonObject());
			for (Map.Entry<String, JsonElement> entry : textures.entrySet()) {
				materials.put(entry.getKey(), new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.parse(entry.getValue().getAsString())));
			}
			return new TravellersGearItemModel(List.of(), GsonHelper.getAsString(object, "modifier_directory"), false, GsonHelper.getAsString(object, "broken_modifier_directory"), false, materials);
		}
	}

}
