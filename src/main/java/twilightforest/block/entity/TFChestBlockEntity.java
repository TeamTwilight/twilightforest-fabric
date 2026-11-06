package twilightforest.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;

import java.util.List;
import java.util.Optional;

public class TFChestBlockEntity extends ChestBlockEntity {

	private final ContainerStorage itemHandler = ContainerStorage.of(this, null);

	public TFChestBlockEntity(BlockPos pos, BlockState state) {
		this(TFBlockEntities.TF_CHEST, pos, state);
	}

	protected TFChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static class ItemHandlerProvider implements DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Storage<ItemVariant>>> {

		@Override
		public Optional<Storage<ItemVariant>> acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
			if (first instanceof TFChestBlockEntity tfFirst && second instanceof TFChestBlockEntity tfSecond) {
				return Optional.of(new CombinedStorage<>(List.of(tfFirst.itemHandler, tfSecond.itemHandler)));
			}
			return Optional.empty();
		}

		@Override
		public Optional<Storage<ItemVariant>> acceptSingle(ChestBlockEntity single) {
			return single instanceof TFChestBlockEntity tfSingle
				? Optional.of(tfSingle.itemHandler)
				: Optional.empty();
		}

		@Override
		public Optional<Storage<ItemVariant>> acceptNone() {
			return Optional.empty();
		}
	}
}
