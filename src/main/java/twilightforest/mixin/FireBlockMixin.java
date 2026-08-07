package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;

@Mixin(FireBlock.class)
public class FireBlockMixin {

	@WrapOperation(
		method = "checkBurnOut",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
		)
	)
	private boolean twilightforest$onCaughtFire(
		Level level,
		BlockPos pos,
		BlockState newState,
		int flags,
		Operation<Boolean> original
	) {
		BlockState currentState = level.getBlockState(pos);
		if (currentState.getBlock() instanceof ChiseledCanopyShelfBlock) {
			if (currentState.getValue(ChiseledCanopyShelfBlock.SPAWNER) && level instanceof ServerLevel serverLevel) {
				if (level.getBlockEntity(pos) instanceof ChiseledCanopyShelfBlockEntity shelf) {
					for (int i = 0; i < ChiseledCanopyShelfBlock.SLOT_OCCUPIED_PROPERTIES.size(); i++) {
						BooleanProperty property = ChiseledCanopyShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(i);
						if (currentState.hasProperty(property) && currentState.getValue(property)) {
							shelf.getSpawner().attemptSpawnTome(i, serverLevel, pos, true, null, 5);
						}
					}
				}
			}
			level.destroyBlock(pos, false);
			return true;
		}

		return original.call(level, pos, newState, flags);
	}
}
