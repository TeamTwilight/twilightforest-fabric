package twilightforest.asm.mixin.enums;

import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import twilightforest.world.components.BiomeColorAlgorithms;

@Mixin(BiomeSpecialEffects.GrassColorModifier.class)
public enum GrassColorModifierMixin {
	TWILIGHTFOREST_ENCHANTED_FOREST("twilightforest:enchanted_forest") {
		@Override
		public int modifyColor(double x, double z, int baseColor) {
			return biomeColorAlgorithms.enchanted(baseColor, (int) x, (int) z);
		}
	},
	TWILIGHTFOREST_SWAMP("twilightforest:swamp") {
		@Override
		public int modifyColor(double x, double z, int baseColor) {
			return biomeColorAlgorithms.swamp(BiomeColorAlgorithms.Type.Grass);
		}
	},
	TWILIGHTFOREST_DARK_FOREST("twilightforest:dark_forest") {
		@Override
		public int modifyColor(double x, double z, int baseColor) {
			return biomeColorAlgorithms.darkForest(BiomeColorAlgorithms.Type.Grass);
		}
	},
	TWILIGHTFOREST_DARK_FOREST_CENTER("twilightforest:dark_forest_center") {
		@Override
		public int modifyColor(double x, double z, int baseColor) {
			return biomeColorAlgorithms.darkForestCenterGrass(x, z);
		}
	},
	TWILIGHTFOREST_SPOOKY_FOREST("twilightforest:spooky_forest") {
		@Override
		public int modifyColor(double x, double z, int baseColor) {
			return biomeColorAlgorithms.spookyGrass(x, z);
		}
	}
	;

	@Unique
	private static final BiomeColorAlgorithms biomeColorAlgorithms = BiomeColorAlgorithms.INSTANCE;

	@Shadow
	GrassColorModifierMixin(String name) {
	}

	@Shadow
	@SuppressWarnings("unused")
	public int modifyColor(final double x, final double z, final int baseColor) {
		throw new AssertionError();
	}
}