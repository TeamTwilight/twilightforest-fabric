package twilightforest.block.entity;

import twilightforest.fabric.network.PacketDistributor;
import twilightforest.fabric.util.ServerLifecycleHooks;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFBlockEntities;
import twilightforest.network.SetMasonJarItemPacket;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;

public class MasonJarBlockEntity extends JarBlockEntity {
	private static final String TAG_ITEM = "item";
	public static final String TAG_ANGLE = "rotation";

	protected ItemStack itemStack = ItemStack.EMPTY;
	protected final MasonJarItemStorage item;
	protected int itemRotation = 0;

	public MasonJarBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.MASON_JAR, pos, state);
		this.item = new MasonJarItemStorage(this);
	}

	public MasonJarItemStorage getItemHandler() {
		return this.item;
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.store(TAG_ITEM, ItemStack.CODEC, this.itemStack);
		output.putInt(TAG_ANGLE, this.itemRotation);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.itemStack = input.read(TAG_ITEM, ItemStack.CODEC).orElse(ItemStack.EMPTY);
		this.itemRotation = input.getIntOr(TAG_ANGLE, 0);
	}

	public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel level) {
		MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
		return this.fillFromLootTable(lootTableKey, seed, level, currentServer.reloadableRegistries());
	}

	public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel serverLevel, ReloadableServerRegistries.Holder holder) {
		LootTable lootTable = holder.getLootTable(lootTableKey);

		if (lootTable == LootTable.EMPTY) return false;

		LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.getBlockPos())).create(LootContextParamSets.CHEST);
		lootTable.getRandomItems(new LootContext.Builder(params).withOptionalRandomSeed(seed).create(Optional.of(lootTableKey.identifier())), this::acceptLootTable);
		return true;
	}

	private void acceptLootTable(ItemStack stack) {
		ItemStack contained = this.item.getItem();

		if (contained.isEmpty()) {
			this.item.setItem(stack);
		} else if (ItemStack.isSameItemSameComponents(contained, stack)) {
			contained.setCount(Math.min(
				contained.getCount() + stack.getCount(),
				contained.getMaxStackSize()
			));

			this.item.setItem(contained);
		}
	}

	public void setFromItem(ItemStack stack) {
		this.applyComponentsFromItemStack(stack);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(this.item.getItem())));
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.item.setItem(components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne());
	}

	@Override
	public void removeComponentsFromTag(ValueOutput output) {
		super.removeComponentsFromTag(output);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.level != null) {
			BlockPos pos = this.getBlockPos();
			this.level.getLightEngine().checkBlock(pos);
		}
		if (this.level instanceof ServerLevel serverLevel) {
			PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(this.getBlockPos()), new SetMasonJarItemPacket(this.getBlockPos(), this.item.getItem(), this.itemRotation));
		}
	}

	public int getItemRotation() {
		return this.itemRotation;
	}

	public void setItemRotation(int itemRotation) {
		this.itemRotation = itemRotation;
	}

	public static class MasonJarItemStorage extends SingleStackStorage {
		private final MasonJarBlockEntity jarEntity;

		public MasonJarItemStorage(MasonJarBlockEntity jarEntity) {
			this.jarEntity = jarEntity;
		}

		public ItemStack getItem() {
			return this.getStack().copy();
		}

		public void setItem(ItemStack stack) {
			this.setStack(stack.copy());
		}

		@Override
		protected ItemStack getStack() {
			return this.jarEntity.itemStack;
		}

		@Override
		protected void setStack(ItemStack stack) {
			this.jarEntity.itemStack = stack;
		}

		@Override
		protected boolean canInsert(ItemVariant variant) {
			return variant.toStack().getItem().canFitInsideContainerItems();
		}

		@Override
		public long insert(
			ItemVariant resource,
			long maxAmount,
			TransactionContext transaction
		) {
			long inserted = super.insert(resource, maxAmount, transaction);

			if (inserted > 0) {
				transaction.addOuterCloseCallback(result -> {
					if (result.wasCommitted()) {
						this.jarEntity.wobble(WobbleStyle.POSITIVE);
						this.jarEntity.setChanged();
					}
				});
			}

			return inserted;
		}

		@Override
		public long extract(
			ItemVariant resource,
			long maxAmount,
			TransactionContext transaction
		) {
			long extracted = super.extract(resource, maxAmount, transaction);

			if (extracted > 0) {
				transaction.addOuterCloseCallback(result -> {
					if (result.wasCommitted()) {
						this.jarEntity.wobble(WobbleStyle.NEGATIVE);
						this.jarEntity.setChanged();
					}
				});
			}

			return extracted;
		}
	}
}