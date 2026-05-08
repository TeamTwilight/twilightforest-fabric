package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFBlockEntities;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;

public class MasonJarBlockEntity extends JarBlockEntity {
    public static final String TAG_ITEM = "item";
    public static final String TAG_ANGLE = "rotation";

    private final MasonJarItemStackHandler item = new MasonJarItemStackHandler(this);
    private int itemRotation = 0;

    public MasonJarBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public net.minecraft.world.level.block.entity.BlockEntityType<?> getType() {
        return TFBlockEntities.MASON_JAR;
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

    public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel serverLevel, ReloadableServerRegistries.Holder holder) {
        LootTable lootTable = holder.getLootTable(lootTableKey);
        if (lootTable == LootTable.EMPTY) {
            return false;
        }
        LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.getBlockPos())).create(LootContextParamSets.CHEST);
        lootTable.getRandomItemsRaw(new LootContext.Builder(params).withOptionalRandomSeed(seed).create(Optional.of(lootTableKey.location())), this::acceptLootTable);
        return true;
    }

    public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel serverLevel) {
        return this.fillFromLootTable(lootTableKey, seed, serverLevel, serverLevel.getServer().reloadableRegistries());
    }

    private void acceptLootTable(ItemStack stack) {
        MasonJarItemStackHandler jarInv = this.getItemHandler();
        if (jarInv.isEmpty()) {
            jarInv.setItem(stack);
        } else {
            ItemStack contained = jarInv.peekItem();
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
    protected void applyImplicitComponents(BlockEntity.DataComponentInput input) {
        super.applyImplicitComponents(input);
        this.item.setItem(input.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne());
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(TAG_ITEM);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            BlockPos pos = this.getBlockPos();
            this.level.getLightEngine().checkBlock(pos);
            this.level.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public int getItemRotation() {
        return this.itemRotation;
    }

    public void setItemRotation(int itemRotation) {
        this.itemRotation = itemRotation;
    }

    public int getStoredLightEmission() {
        ItemStack stack = this.item.getItem();
        return stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock().defaultBlockState().getLightEmission() : 0;
    }

    public static class MasonJarItemStackHandler {
        private final MasonJarBlockEntity jarEntity;
        private ItemStack stack = ItemStack.EMPTY;

        public MasonJarItemStackHandler(MasonJarBlockEntity jarEntity) {
            this.jarEntity = jarEntity;
        }

        public ItemStack getItem() {
            return this.stack.copy();
        }

        private ItemStack peekItem() {
            return this.stack;
        }

        public void setItem(ItemStack itemStack) {
            this.stack = itemStack.copy();
            this.jarEntity.setChanged();
        }

        public boolean isItemValid(ItemStack stack) {
            return stack.getItem().canFitInsideContainerItems();
        }

        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != 0 || amount <= 0 || this.stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            ItemStack extracted = this.stack.copyWithCount(Math.min(amount, this.stack.getCount()));
            if (!simulate) {
                this.stack.shrink(extracted.getCount());
                if (this.stack.isEmpty()) {
                    this.stack = ItemStack.EMPTY;
                }
                this.jarEntity.wobble(WobbleStyle.NEGATIVE);
                this.jarEntity.setChanged();
            }
            return extracted;
        }

        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot != 0 || stack.isEmpty() || !this.isItemValid(stack)) {
                return stack;
            }
            if (this.stack.isEmpty()) {
                int insertedCount = Math.min(stack.getCount(), stack.getMaxStackSize());
                ItemStack inserted = stack.copyWithCount(insertedCount);
                ItemStack remainder = stack.copy();
                remainder.shrink(insertedCount);
                if (!simulate) {
                    this.stack = inserted;
                    this.jarEntity.wobble(WobbleStyle.POSITIVE);
                    this.jarEntity.setChanged();
                }
                return remainder.isEmpty() ? ItemStack.EMPTY : remainder;
            }
            if (!ItemStack.isSameItemSameComponents(this.stack, stack) || this.stack.getCount() >= this.stack.getMaxStackSize()) {
                return stack;
            }
            int room = this.stack.getMaxStackSize() - this.stack.getCount();
            int insertedCount = Math.min(room, stack.getCount());
            ItemStack remainder = stack.copy();
            remainder.shrink(insertedCount);
            if (!simulate && insertedCount > 0) {
                this.stack.grow(insertedCount);
                this.jarEntity.wobble(WobbleStyle.POSITIVE);
                this.jarEntity.setChanged();
            }
            return remainder.isEmpty() ? ItemStack.EMPTY : remainder;
        }

        public boolean isEmpty() {
            return this.stack.isEmpty();
        }

        private CompoundTag serializeNBT(HolderLookup.Provider registries) {
            CompoundTag tag = new CompoundTag();
            if (!this.stack.isEmpty()) {
                tag.put("item", this.stack.save(registries, new CompoundTag()));
            }
            return tag;
        }

        private void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag) {
            this.stack = tag.contains("item") ? ItemStack.parse(registries, tag.getCompound("item")).orElse(ItemStack.EMPTY) : ItemStack.EMPTY;
        }
    }
}
