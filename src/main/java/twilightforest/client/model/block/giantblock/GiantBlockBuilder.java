package twilightforest.client.model.block.giantblock;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GiantBlockBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	public static <T extends ModelBuilder<T>> GiantBlockBuilder<T> begin(T parent, ExistingFileHelper helper) {
		return new GiantBlockBuilder<>(parent, helper);
	}

	private ResourceLocation parentBlock;
	private Map<Direction, ResourceLocation> textures = new HashMap<>();
	private String renderType = "minecraft:solid";

	protected GiantBlockBuilder(T parent, ExistingFileHelper helper) {
		super(TwilightForestMod.prefix("giant_block"), parent, helper);
	}

	public GiantBlockBuilder<T> parentBlock(Block block) {
		Preconditions.checkNotNull(block, "parent block must not be null");
		this.parentBlock = Registry.BLOCK.getKey(block);
		return this;
	}

	public GiantBlockBuilder<T> allSidedTexture(ResourceLocation texture) {
		Preconditions.checkNotNull(texture, "texture must not be null");
		for (Direction direction : Direction.values()) {
			this.textures.put(direction, texture);
		}
		return this;
	}

	public GiantBlockBuilder<T> columnTextures(ResourceLocation top, ResourceLocation side) {
		Preconditions.checkNotNull(top, "top texture must not be null");
		Preconditions.checkNotNull(side, "side texture must not be null");
		for (Direction direction : Direction.values()) {
			this.textures.put(direction, direction.getAxis().isVertical() ? top : side);
		}
		return this;
	}

	public GiantBlockBuilder<T> sidedTexture(Direction side, ResourceLocation texture) {
		Preconditions.checkNotNull(texture, "texture must not be null");
		this.textures.put(side, texture);
		return this;
	}

	public GiantBlockBuilder<T> renderType(String renderType) {
		Preconditions.checkNotNull(renderType, "rendertype must not be null");
		this.renderType = renderType;
		return this;
	}

	@Override
	public T end() {
		for (Direction direction : Direction.values()) {
			Preconditions.checkArgument(this.textures.containsKey(direction), "block must include texture for " + direction + " side");
		}
		Preconditions.checkNotNull(this.parentBlock, "giant block must have a parent block");
		return super.end();
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);
		JsonObject object = new JsonObject();
		for (Direction direction : Direction.values()) {
			object.addProperty(direction.getName().toLowerCase(Locale.ROOT), this.textures.get(direction).toString());
		}
		json.add("textures", object);
		json.addProperty("parent_block", this.parentBlock.toString());
		json.addProperty("render_type", this.renderType);
		return json;
	}
}
