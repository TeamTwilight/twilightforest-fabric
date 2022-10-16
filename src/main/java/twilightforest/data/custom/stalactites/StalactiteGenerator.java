package twilightforest.data.custom.stalactites;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.stalactites.entry.Stalactite;

public class StalactiteGenerator extends StalactiteProvider {
	public StalactiteGenerator(DataGenerator generator) {
		super(generator, TwilightForestMod.ID);
	}

	@Override
	protected void createStalactites() {
		this.makeStalactite(new Stalactite(Blocks.IRON_ORE, 0.7F, 8, 24), Stalactite.HollowHillType.SMALL);
		this.makeStalactite(new Stalactite(Blocks.COAL_ORE, 0.8F, 12, 24), Stalactite.HollowHillType.SMALL);
		this.makeStalactite(new Stalactite(Blocks.COPPER_ORE, 0.6F, 12, 12), Stalactite.HollowHillType.SMALL);
		this.makeStalactite(new Stalactite(Blocks.GLOWSTONE, 0.5F, 8, 12), Stalactite.HollowHillType.SMALL);

		this.makeStalactite(new Stalactite(Blocks.GOLD_ORE, 0.6F, 6, 20), Stalactite.HollowHillType.MEDIUM);
		this.makeStalactite(new Stalactite(Blocks.REDSTONE_ORE, 0.8F, 8, 40), Stalactite.HollowHillType.MEDIUM);

		this.makeStalactite(new Stalactite(Blocks.DIAMOND_ORE, 0.5F, 4, 30), Stalactite.HollowHillType.LARGE);
		this.makeStalactite(new Stalactite(Blocks.LAPIS_ORE, 0.8F, 8, 30), Stalactite.HollowHillType.LARGE);
		this.makeStalactite(new Stalactite(Blocks.EMERALD_ORE, 0.5F, 3, 15), Stalactite.HollowHillType.LARGE);
	}

	@Override
	public String getName() {
		return "Twilight Forest Hollow Hill Stalactites";
	}
}
