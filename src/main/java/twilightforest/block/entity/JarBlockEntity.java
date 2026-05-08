package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.JarLid;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class JarBlockEntity extends BlockEntity {
    public static final ResourceLocation JAR_LID = TwilightForestMod.prefix("jar_lid");
    public static final Map<Item, BooleanSupplier> REGISTERED_LOG_LIDS = new HashMap<>();

    private Item lid = TFBlocks.TWILIGHT_OAK_LOG.get().asItem();

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(TFBlockEntities.MASON_JAR, pos, state);
    }

    public Item getLid() {
        return this.lid;
    }

    public void setLid(Item lid) {
        this.lid = lid;
        this.setChanged();
    }

    public ItemStack getJarAsItem() {
        ItemStack stack = new ItemStack(this.getBlockState().getBlock());
        stack.set(TFDataComponents.JAR_LID, new JarLid(this.lid));
        return stack;
    }

    public void wobble(DecoratedPotBlockEntity.WobbleStyle style) {
        if (this.level != null) {
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, style.ordinal());
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        return id == 1 || super.triggerEvent(id, type);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("lid", this.lid.builtInRegistryHolder().key().location().toString());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("lid")) {
            this.lid = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(ResourceLocation.parse(tag.getString("lid")));
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(TFDataComponents.JAR_LID, new JarLid(this.lid));
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput input) {
        super.applyImplicitComponents(input);
        this.lid = input.getOrDefault(TFDataComponents.JAR_LID, new JarLid(TFBlocks.TWILIGHT_OAK_LOG.get().asItem())).lid();
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove("lid");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        this.saveAdditional(tag, registries);
        return tag;
    }

    static {
        REGISTERED_LOG_LIDS.put(TFBlocks.TWILIGHT_OAK_LOG.get().asItem(), () -> true);
        REGISTERED_LOG_LIDS.put(TFBlocks.CANOPY_LOG.get().asItem(), () -> true);
        REGISTERED_LOG_LIDS.put(TFBlocks.MANGROVE_LOG.get().asItem(), () -> true);
        REGISTERED_LOG_LIDS.put(TFBlocks.DARK_LOG.get().asItem(), () -> true);
        REGISTERED_LOG_LIDS.put(TFBlocks.TIME_LOG.get().asItem(), () -> true);
        REGISTERED_LOG_LIDS.put(TFBlocks.TRANSFORMATION_LOG.get().asItem(), () -> true);
        REGISTERED_LOG_LIDS.put(TFBlocks.MINING_LOG.get().asItem(), () -> true);
        REGISTERED_LOG_LIDS.put(TFBlocks.SORTING_LOG.get().asItem(), () -> true);
    }
}
