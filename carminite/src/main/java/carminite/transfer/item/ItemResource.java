package carminite.transfer.item;

import carminite.transfer.TransferPreconditions;
import carminite.transfer.resource.IDataComponentHolderResource;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public final class ItemResource implements IDataComponentHolderResource<Item> {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final ItemResource EMPTY = new ItemResource(ItemStack.EMPTY);

	public static final Codec<ItemResource> CODEC = Codec.lazyInitialized(
		() -> RecordCodecBuilder.create(
			i -> i.group(
					Item.CODEC.fieldOf("id").forGetter(ItemResource::typeHolder),
					DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemResource::getComponentsPatch))
				.apply(i, ItemResource::of)));

	public static final Codec<ItemResource> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC).xmap(
		optional -> optional.orElse(ItemResource.EMPTY),
		itemResource -> itemResource.isEmpty() ? Optional.empty() : Optional.of(itemResource));

	public static final StreamCodec<RegistryFriendlyByteBuf, ItemResource> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.holderRegistry(Registries.ITEM), ItemResource::typeHolder,
		DataComponentPatch.STREAM_CODEC, ItemResource::getComponentsPatch,
		ItemResource::of);

	public static ItemResource of(ItemStack stack) {
		if (stack.isEmpty() || stack.getComponentsPatch().isEmpty()) {
			return of(stack.getItem());
		}
		return new ItemResource(stack.copyWithCount(1));
	}

	public static ItemResource of(@Nullable ItemStackTemplate template) {
		if (template == null) {
			return EMPTY;
		}
		if (template.components().isEmpty()) {
			return of(template.item());
		}

		var stack = template.create();
		stack.setCount(1);
		return new ItemResource(stack);
	}

	public static ItemResource of(ItemLike item) {
		Item value = item.asItem();
		if (value == Items.AIR) return EMPTY;
		return value.carminite$computeDefaultResource(i -> new ItemResource(new ItemStack(i)));
	}

	public static ItemResource of(ItemLike item, DataComponentPatch patch) {
		return of(item.asItem().builtInRegistryHolder(), patch);
	}

	public static ItemResource of(Holder<Item> holder) {
		return of(holder.value());
	}

	public static ItemResource of(Holder<Item> holder, DataComponentPatch patch) {
		if (holder.value() == Items.AIR || patch.isEmpty()) {
			return of(holder.value());
		}

		var stack = new ItemStack(holder, 1, patch);
		var err = ItemStack.validateStrict(stack).error();

		if (err.isPresent()) {
			LOGGER.warn("Can't create item resource '{}' with components {}, error: {}", holder.getRegisteredName(), patch, err.get().message());
			return EMPTY;
		}

		return new ItemResource(stack);
	}

	private final ItemStack innerStack;

	private ItemResource(ItemStack stack) {
		this.innerStack = stack;
	}

	@Override
	public Item value() {
		return innerStack.getItem();
	}

	public Item getItem() {
		return value();
	}

	@Override
	public Holder<Item> typeHolder() {
		return innerStack.typeHolder();
	}

	@Override
	public boolean isEmpty() {
		return innerStack.isEmpty();
	}

	public boolean matches(ItemStack stack) {
		return ItemStack.isSameItemSameComponents(stack, innerStack);
	}

	public boolean matches(@Nullable ItemStackTemplate template) {
		return ItemStack.isSameItemSameComponents(innerStack, template.create());
	}

	public boolean is(ItemLike item) {
		return is(item.asItem());
	}

	public boolean test(Predicate<ItemStack> predicate) {
		return predicate.test(innerStack);
	}

	@Override
	public boolean isComponentsPatchEmpty() {
		return innerStack.isEmpty() || innerStack.getComponentsPatch().isEmpty();
	}

	@Override
	public ItemResource withMergedPatch(DataComponentPatch patch) {
		if (isEmpty() || patch.isEmpty())
			return this;

		ItemStack stack = innerStack.copy();
		stack.applyComponents(patch);

		return ItemResource.of(stack);
	}

	@Override
	public <D> ItemResource with(DataComponentType<D> type, @Nullable D data) {
		if (isEmpty()) return ItemResource.EMPTY;
		if (Objects.equals(get(type), data)) return this;

		ItemStack stack = innerStack.copy();
		stack.set(type, data);
		return ItemResource.of(stack);
	}

	@Override
	public <D> ItemResource with(Supplier<? extends DataComponentType<D>> type, @Nullable D data) {
		return with(type.get(), data);
	}

	@Override
	public ItemResource without(DataComponentType<?> type) {
		if (isEmpty()) return ItemResource.EMPTY;
		if (get(type) == null) return this;

		ItemStack stack = innerStack.copy();
		stack.remove(type);
		return ItemResource.of(stack);
	}

	@Override
	public ItemResource without(Supplier<? extends DataComponentType<?>> type) {
		return without(type.get());
	}

	@Override
	public DataComponentMap getComponents() {
		return innerStack.immutableComponents();
	}

	@Override
	public DataComponentPatch getComponentsPatch() {
		return innerStack.getComponentsPatch();
	}

	public ItemStack toStack(int count) {
		TransferPreconditions.checkNonNegative(count);
		if (count == 0) return ItemStack.EMPTY;
		return this.innerStack.copyWithCount(count);
	}

	public ItemStack toStack() {
		return this.innerStack.copyWithCount(1);
	}

	public int getMaxStackSize() {
		return innerStack.getMaxStackSize();
	}

	public Component getHoverName() {
		return innerStack.getHoverName();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		ItemResource other = (ItemResource) obj;
		return ItemStack.isSameItemSameComponents(this.innerStack, other.innerStack);
	}

	@Override
	public int hashCode() {
		return ItemStack.hashItemAndComponents(innerStack);
	}

	@Override
	public String toString() {
		return value() + " [" + getComponentsPatch().size() + "]";
	}
}