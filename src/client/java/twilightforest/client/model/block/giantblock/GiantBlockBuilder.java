package twilightforest.client.model.block.giantblock;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;

public class GiantBlockBuilder {
	private ResourceLocation parentBlock;

	public GiantBlockBuilder parentBlock(Block block) {
		Preconditions.checkNotNull(block, "parent block must not be null");
		this.parentBlock = BuiltInRegistries.BLOCK.getKey(block);
		return this;
	}

	public JsonObject toJson(JsonObject json) {
		Preconditions.checkNotNull(this.parentBlock, "giant block must have a parent block");
		json.addProperty("loader", TwilightForestMod.prefix("giant_block").toString());
		json.addProperty("parent_block", this.parentBlock.toString());
		return json;
	}
}
