package twilightforest.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Drullkus on 12/12/17.
 */
public class NagastonePillarBlock extends DirectionalRotatedPillarBlock {

    public NagastonePillarBlock() {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE));
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.Y).setValue(REVERSED, false));
    }
}
