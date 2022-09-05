package twilightforest.world.registration.surface_rules;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import twilightforest.init.BiomeKeys;
import twilightforest.init.TFBlocks;

public class TFSurfaceRules {

	private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
	private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
	private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
	private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
	private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
	private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
	private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
	private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
	private static final SurfaceRules.RuleSource SNOW = makeStateRule(Blocks.SNOW_BLOCK);
	private static final SurfaceRules.RuleSource WEATHERED_DEADROCK = makeStateRule(TFBlocks.WEATHERED_DEADROCK.get());
	private static final SurfaceRules.RuleSource CRACKED_DEADROCK = makeStateRule(TFBlocks.CRACKED_DEADROCK.get());
	private static final SurfaceRules.RuleSource DEADROCK = makeStateRule(TFBlocks.DEADROCK.get());

	private static SurfaceRules.RuleSource makeStateRule(Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}

	public static SurfaceRules.RuleSource tfSurface() {
		//surface is a normal overworld surface as the base
		//snowy forest is all snow on the top layers
		//glacier has gravel for a few layers, then stone
		//highlands has a noise-based mixture of podzol and coarse dirt
		//thornlands/plateau has no caves and deadrock instead of stone

		SurfaceRules.RuleSource highlandsNoise = SurfaceRules.sequence(
				//check if we're in the highlands
				SurfaceRules.ifTrue(
						SurfaceRules.isBiome(BiomeKeys.HIGHLANDS),
						SurfaceRules.ifTrue(
								//check if we're on the surface
								SurfaceRules.ON_FLOOR,
								SurfaceRules.sequence(
										//mix coarse dirt and podzol like we used to
										PODZOL,
										SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT),
										SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL)))));

		SurfaceRules.RuleSource deadrockLands = SurfaceRules.sequence(
				SurfaceRules.ifTrue(
						//check if we're in the deadrock biomes
						SurfaceRules.isBiome(BiomeKeys.THORNLANDS, BiomeKeys.FINAL_PLATEAU),
						//deadrock blocks replace everything
						SurfaceRules.sequence(
								SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, WEATHERED_DEADROCK),
								SurfaceRules.ifTrue(
										SurfaceRules.waterStartCheck(-6, -1),
										SurfaceRules.sequence(
												SurfaceRules.ifTrue(
														SurfaceRules.UNDER_FLOOR,
														CRACKED_DEADROCK))),
								DEADROCK)));

		SurfaceRules.RuleSource snowyForest = SurfaceRules.sequence(
				SurfaceRules.ifTrue(
						//check if we're in the snowy forest
						SurfaceRules.isBiome(BiomeKeys.SNOWY_FOREST),
						//surface is snow
						SurfaceRules.sequence(
								SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SNOW),
								SurfaceRules.ifTrue(
										SurfaceRules.waterStartCheck(-6, -1),
										SurfaceRules.sequence(
												SurfaceRules.ifTrue(
														SurfaceRules.UNDER_FLOOR,
														SNOW))))));

		SurfaceRules.RuleSource glacier = SurfaceRules.sequence(
				SurfaceRules.ifTrue(
						//check if we're in the glacier
						SurfaceRules.isBiome(BiomeKeys.GLACIER),
						SurfaceRules.sequence(
								//surface and under is gravel
								SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, GRAVEL),
								SurfaceRules.ifTrue(
										SurfaceRules.waterStartCheck(-6, -1),
										SurfaceRules.sequence(
												SurfaceRules.ifTrue(
														SurfaceRules.UNDER_FLOOR,
														GRAVEL))))));

		SurfaceRules.RuleSource overworldLike = SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
						SurfaceRules.sequence(
								SurfaceRules.ifTrue(
										//lakes and rivers get sand
										SurfaceRules.isBiome(BiomeKeys.LAKE, BiomeKeys.STREAM),
										SurfaceRules.sequence(
												SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE),
												SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), GRASS_BLOCK), SAND)),
								//make sure the swamps always get grass, they had weird stone patches sometimes
								SurfaceRules.ifTrue(
										SurfaceRules.isBiome(BiomeKeys.SWAMP, BiomeKeys.FIRE_SWAMP),
										SurfaceRules.sequence(
										SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), GRASS_BLOCK), DIRT)),
								//make everything else grass
								SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0),
										SurfaceRules.sequence(
												SurfaceRules.ifTrue(
														//check if we're above ground, so hollow hills dont have grassy floors
														SurfaceRules.yStartCheck(VerticalAnchor.absolute(-4), 1), GRASS_BLOCK))),

								//if we're around the area hollow hill floors are, check if we're underwater. If so place some dirt.
								//This fixes streams having weird stone patches
								SurfaceRules.ifTrue(
										SurfaceRules.not(
												SurfaceRules.yStartCheck(VerticalAnchor.absolute(-4), 1)),
										SurfaceRules.sequence(
												SurfaceRules.ifTrue(
														SurfaceRules.not(
																SurfaceRules.waterBlockCheck(-1, 0)), DIRT))))),
				//dirt goes under the grass of course!
				//check if we're above ground, so hollow hills dont have dirt floors
				SurfaceRules.ifTrue(SurfaceRules.waterStartCheck(-6, -1),
						SurfaceRules.sequence(
								SurfaceRules.ifTrue(
										SurfaceRules.yStartCheck(VerticalAnchor.absolute(-4), 1),
										SurfaceRules.sequence(
												SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT))))));


		ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
		builder

				.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK))
				.add(highlandsNoise)
				.add(deadrockLands)
				.add(snowyForest)
				.add(glacier)
				.add(overworldLike);

		return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
	}

	private static SurfaceRules.ConditionSource surfaceNoiseAbove(double p_194809_) {
		return SurfaceRules.noiseCondition(Noises.SURFACE, p_194809_ / 8.25D, Double.MAX_VALUE);
	}
}
