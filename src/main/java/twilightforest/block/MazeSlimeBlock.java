package twilightforest.block;

import net.minecraft.world.level.block.SlimeBlock;

public class MazeSlimeBlock extends SlimeBlock {
	public MazeSlimeBlock(Properties properties) {
		super(properties);
	}

	// canStickTo is NeoForge-only; PistonBaseBlock.isSticky() handled via PistonBaseBlockMixin
	// isStickyBlock is inherited from SlimeBlock (returns true)
}