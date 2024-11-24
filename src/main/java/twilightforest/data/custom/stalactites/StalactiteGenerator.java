package twilightforest.data.custom.stalactites;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.stalactites.entry.Stalactite;

import java.util.Map;

public class StalactiteGenerator extends StalactiteProvider {
	public StalactiteGenerator(FabricDataOutput output) {
		super(output, TwilightForestMod.ID);
	}

	@Override
	protected void createStalactites() {
		this.makeStalactite(new Stalactite(Map.of(Blocks.IRON_ORE, 20, Blocks.RAW_IRON_BLOCK, 1), 0.7F, 8, 24), Stalactite.HollowHillType.SMALL);
		this.makeStalactite(new Stalactite(Map.of(Blocks.COAL_ORE, 1), 0.8F, 12, 24), Stalactite.HollowHillType.SMALL);
		this.makeStalactite(new Stalactite(Map.of(Blocks.COPPER_ORE, 20, Blocks.RAW_COPPER_BLOCK, 1), 0.6F, 12, 12), Stalactite.HollowHillType.SMALL);
		this.makeStalactite(new Stalactite(Map.of(Blocks.GLOWSTONE, 1), 0.5F, 8, 12), Stalactite.HollowHillType.SMALL);

		this.makeStalactite(new Stalactite(Map.of(Blocks.GOLD_ORE, 10, Blocks.RAW_GOLD_BLOCK, 1), 0.6F, 6, 20), Stalactite.HollowHillType.MEDIUM);
		this.makeStalactite(new Stalactite(Map.of(Blocks.REDSTONE_ORE, 1), 0.8F, 8, 40), Stalactite.HollowHillType.MEDIUM);

		this.makeStalactite(new Stalactite(Map.of(Blocks.DIAMOND_ORE, 1), 0.5F, 4, 30), Stalactite.HollowHillType.LARGE);
		this.makeStalactite(new Stalactite(Map.of(Blocks.LAPIS_ORE, 1), 0.8F, 8, 30), Stalactite.HollowHillType.LARGE);
		this.makeStalactite(new Stalactite(Map.of(Blocks.EMERALD_ORE, 1), 0.5F, 3, 15), Stalactite.HollowHillType.LARGE);
	}

	@Override
	public String getName() {
		return "Twilight Forest Hollow Hill Stalactites";
	}
}
