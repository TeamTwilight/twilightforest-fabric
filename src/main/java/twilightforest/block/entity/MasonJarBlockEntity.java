package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import twilightforest.util.TFAuxiliaryLightManager;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import twilightforest.network.PacketDistributor;
import io.github.fabricators_of_create.porting_lib.core.util.ServerLifecycleHooks;
import twilightforest.init.TFBlockEntities;
import twilightforest.network.SetMasonJarItemPacket;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;

public class MasonJarBlockEntity extends JarBlockEntity {
	public static final String TAG_ITEM = "item";
	public static final String TAG_ANGLE = "rotation";

	protected final MasonJarItemStackHandler item;
	protected int itemRotation = 0;

	public MasonJarBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.MASON_JAR.get(), pos, state);
		this.item = new MasonJarItemStackHandler(this);
	}

	public MasonJarItemStackHandler getItemHandler() {
		return this.item;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put(TAG_ITEM, this.item.serializeNBT(registries));
		tag.putInt(TAG_ANGLE, this.itemRotation);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.item.deserializeNBT(registries, tag.getCompound(TAG_ITEM));
		this.itemRotation = tag.getInt(TAG_ANGLE);
	}

	public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel level) {
		MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
		return this.fillFromLootTable(lootTableKey, seed, level, currentServer.reloadableRegistries());
	}

	public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel serverLevel, ReloadableServerRegistries.Holder holder) {
		LootTable lootTable = holder.getLootTable(lootTableKey);

		if (lootTable == LootTable.EMPTY) return false;

		LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.getBlockPos())).create(LootContextParamSets.CHEST);

		lootTable.getRandomItemsRaw(new LootContext.Builder(params).withOptionalRandomSeed(seed).create(Optional.of(lootTableKey.location())), this::acceptLootTable);

		return true;
	}

	private void acceptLootTable(ItemStack stack) {
		MasonJarItemStackHandler jarInv = this.getItemHandler();
		if (jarInv.isEmpty()) {
			jarInv.setItem(stack);
		} else {
			ItemStack contained = jarInv.peekItem();
			// Merge stack in if there's already an item inside
			if (ItemStack.isSameItemSameComponents(contained, stack)) {
				contained.setCount(Math.min(contained.getCount() + stack.getCount(), contained.getMaxStackSize()));
			}
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
	protected void applyImplicitComponents(DataComponentInput input) {
		super.applyImplicitComponents(input);
		this.item.setItem(input.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne());
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove(TAG_ITEM);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void setChanged() {
		super.setChanged();
		if (this.level != null) {
			BlockPos pos = this.getBlockPos();
			// getAuxLightManager is NeoForge-only. Vanilla light engine handles block light via getLightEngine().checkBlock()
			this.level.getLightEngine().checkBlock(pos);
		}
		if (this.level instanceof ServerLevel serverLevel) {
			PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(this.getBlockPos()), new SetMasonJarItemPacket(this.getBlockPos(), this.item.getItem(), this.itemRotation));
		}
	}

	public int getItemRotation() {
		return this.itemRotation;
	}

	public void setItemRotation(int itemRotation) {
		this.itemRotation = itemRotation;
	}

	public static class MasonJarItemStackHandler extends ItemStackHandler {
		protected final MasonJarBlockEntity jarEntity;

		public MasonJarItemStackHandler(MasonJarBlockEntity jarEntity) {
			super(1);
			this.jarEntity = jarEntity;
		}

		// Used for simple checks of what the one item is, without going through all the hoops. Used by the renderer and when saving contents to item
		public ItemStack getItem() {
			return this.getStackInSlot(0).copy();
		}

		// Peeks at the stored item, without cloning it
		private ItemStack peekItem() {
			return this.getStackInSlot(0);
		}

		// Used when syncing to client and when placing a jar that already has stored items
		public void setItem(ItemStack itemStack) {
			this.setStackInSlot(0, itemStack);
		}

		public boolean isItemValid(int slot, ItemStack stack) {
			return !stack.isEmpty();
		}

		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (amount <= 0) return ItemStack.EMPTY;
			ItemStack existing = this.getStackInSlot(slot);
			if (existing.isEmpty()) return ItemStack.EMPTY;
			int toExtract = Math.min(amount, existing.getCount());
			if (!simulate) {
				ItemStack remaining = existing.copyWithCount(existing.getCount() - toExtract);
				this.setStackInSlot(slot, remaining);
				this.jarEntity.wobble(WobbleStyle.NEGATIVE);
				this.jarEntity.setChanged();
			}
			return existing.copyWithCount(toExtract);
		}

		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (stack.isEmpty()) return ItemStack.EMPTY;
			if (!this.isItemValid(slot, stack)) return stack;
			ItemStack existing = this.getStackInSlot(slot);
			int limit = stack.getMaxStackSize();
			if (!existing.isEmpty()) {
				if (!ItemStack.isSameItemSameComponents(existing, stack)) return stack;
				limit -= existing.getCount();
			}
			if (limit <= 0) return stack;
			int toInsert = Math.min(limit, stack.getCount());
			ItemStack inserted = stack.copyWithCount(toInsert);
			ItemStack remainder = stack.copyWithCount(stack.getCount() - toInsert);
			if (!simulate) {
				if (existing.isEmpty()) {
					this.setStackInSlot(slot, inserted);
				} else {
					existing.grow(toInsert);
					this.setStackInSlot(slot, existing);
				}
				this.jarEntity.wobble(WobbleStyle.POSITIVE);
				this.jarEntity.setChanged();
			}
			return remainder.isEmpty() ? ItemStack.EMPTY : remainder;
		}

		public boolean isEmpty() {
			return this.getStackInSlot(0).isEmpty();
		}
	}
}
