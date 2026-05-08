package twilightforest.components.block;

import java.util.List;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedSlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.Direction;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.init.TFBlockEntities;

public class ChiseledCanopyBookshelfWrapper extends CombinedSlottedStorage<ItemVariant, SingleSlotStorage<ItemVariant>> implements InventoryStorage {
	private final ChiseledCanopyShelfBlockEntity shelf;

	public ChiseledCanopyBookshelfWrapper(ChiseledCanopyShelfBlockEntity shelf, Direction side) {
		super(wrapSlots(shelf, side));
		this.shelf = shelf;
	}

	public static void register() {
		ItemStorage.SIDED.registerForBlockEntity(ChiseledCanopyBookshelfWrapper::new, TFBlockEntities.CHISELED_CANOPY_BOOKSHELF);
	}

	@Override
	public List<SingleSlotStorage<ItemVariant>> getSlots() {
		return this.parts;
	}

	@Override
	public boolean supportsExtraction() {
		return !this.isSpawnerShelf() && super.supportsExtraction();
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		return this.isSpawnerShelf() ? 0 : super.extract(resource, maxAmount, transaction);
	}

	private boolean isSpawnerShelf() {
		return this.shelf.getBlockState().hasProperty(ChiseledCanopyShelfBlock.SPAWNER)
				&& this.shelf.getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER);
	}

	private static List<SingleSlotStorage<ItemVariant>> wrapSlots(ChiseledCanopyShelfBlockEntity shelf, Direction side) {
		return InventoryStorage.of(shelf, side).getSlots().stream()
				.map(slot -> new SpawnerAwareSlot(shelf, slot))
				.<SingleSlotStorage<ItemVariant>>map(slot -> slot)
				.toList();
	}

	private record SpawnerAwareSlot(ChiseledCanopyShelfBlockEntity shelf, SingleSlotStorage<ItemVariant> delegate) implements SingleSlotStorage<ItemVariant> {
		@Override
		public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			return this.delegate.insert(resource, maxAmount, transaction);
		}

		@Override
		public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			if (this.shelf.getBlockState().hasProperty(ChiseledCanopyShelfBlock.SPAWNER)
					&& this.shelf.getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER)) {
				return 0;
			}
			return this.delegate.extract(resource, maxAmount, transaction);
		}

		@Override
		public boolean isResourceBlank() {
			return this.delegate.isResourceBlank();
		}

		@Override
		public ItemVariant getResource() {
			return this.delegate.getResource();
		}

		@Override
		public long getAmount() {
			return this.delegate.getAmount();
		}

		@Override
		public long getCapacity() {
			return this.delegate.getCapacity();
		}

		@Override
		public StorageView<ItemVariant> getUnderlyingView() {
			return this.delegate.getUnderlyingView();
		}
	}
}
