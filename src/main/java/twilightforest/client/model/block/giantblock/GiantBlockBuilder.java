package twilightforest.client.model.block.giantblock;

import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import twilightforest.TFMain;

public class GiantBlockBuilder extends CustomLoaderBuilder {

	public static GiantBlockBuilder begin() {
		return new GiantBlockBuilder();
	}

	public GiantBlockBuilder() {
		super(TFMain.prefix("giant_block"), false);
	}

	@Override
	protected CustomLoaderBuilder copyInternal() {
		return new GiantBlockBuilder();
	}
}
