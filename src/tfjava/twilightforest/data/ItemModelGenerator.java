package twilightforest.data;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import twilightforest.data.helpers.BlockModelHelpers;

import java.util.concurrent.CompletableFuture;

public class ItemModelGenerator extends BlockModelHelpers {
	public ItemModelGenerator(PackOutput output) {
		super(output);
	}

	public ItemModelGenerator(PackOutput output, Object existingFileHelper) {
		this(output);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return this.copyJsonTree(cache, "models/item");
	}

	@Override
	public String getName() {
		return "Twilight Forest item models";
	}
}
