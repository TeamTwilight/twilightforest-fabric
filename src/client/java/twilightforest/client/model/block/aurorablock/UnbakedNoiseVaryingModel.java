package twilightforest.client.model.block.aurorablock;

import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

public class UnbakedNoiseVaryingModel extends NoiseVaryingUnbakedModel {
	public UnbakedNoiseVaryingModel(String[] variants) {
		super(Arrays.stream(variants).map(ResourceLocation::parse).toList());
	}
}
