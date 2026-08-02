package twilightforest.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import io.github.fabricators_of_create.porting_lib.client_extensions.IClientItemExtensions;
import twilightforest.client.ISTER;

import java.util.function.Consumer;

public class TrophyItem extends StandingAndWallBlockItem {

	public TrophyItem(Block floorBlock, Block wallBlock, Properties properties) {
		super(floorBlock, wallBlock, properties, Direction.DOWN);
	}
}