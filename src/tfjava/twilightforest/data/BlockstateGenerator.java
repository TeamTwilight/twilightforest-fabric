package twilightforest.data;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import twilightforest.data.helpers.BlockModelBuilders;

import java.util.concurrent.CompletableFuture;

public class BlockstateGenerator extends BlockModelBuilders {
	public BlockstateGenerator(PackOutput output) {
		super(output);
	}

	public BlockstateGenerator(PackOutput output, Object existingFileHelper) {
		this(output);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return CompletableFuture.allOf(
			this.copyJsonTree(cache, "blockstates"),
			this.copyJsonTree(cache, "models/block")
		);
	}

	@Override
	public String getName() {
		return "Twilight Forest blockstates and block models";
	}
}
