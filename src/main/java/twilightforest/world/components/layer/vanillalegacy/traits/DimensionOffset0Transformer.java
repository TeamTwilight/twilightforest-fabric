package twilightforest.world.components.layer.vanillalegacy.traits;

public interface DimensionOffset0Transformer extends DimensionTransformer {
	@Override
	default int getParentX(int x) {
		return x;
	}

	@Override
	default int getParentY(int z) {
		return z;
	}
}
