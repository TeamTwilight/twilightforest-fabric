package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.ItemDisplays;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

public class ItemDisplayContents implements TooltipComponent {
	public static final List<DeferredHolder<ItemDisplayType, ItemDisplayType>> LAYOUT = List.of(ItemDisplays.MAP, ItemDisplays.MAP, ItemDisplays.MAP, ItemDisplays.COMPASS, ItemDisplays.CLOCK, ItemDisplays.MOON_DIAL);
	private static final int FIRST_MAP_SLOT_INDEX = LAYOUT.indexOf(ItemDisplays.MAP);
	public static final ItemDisplayContents EMPTY = new ItemDisplayContents(LAYOUT.size());
	public static final Codec<ItemDisplayContents> CODEC = DisplaySlot.CODEC.listOf().xmap(ItemDisplayContents::fromSlots, ItemDisplayContents::asSlots);
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemDisplayContents> STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()).map(ItemDisplayContents::new, contents -> contents.items);
	final NonNullList<ItemStack> items;

	private ItemDisplayContents(int size) {
		this.items = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	private ItemDisplayContents(List<ItemStack> items) {
		this.items = NonNullList.copyOf(items);
	}

	private void copyInto(NonNullList<ItemStack> list) {
		for (int i = 0; i < list.size(); i++) {
			ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
			list.set(i, itemstack.copy());
		}
	}

	private static ItemDisplayContents fromSlots(List<DisplaySlot> slots) {
		OptionalInt optionalint = slots.stream().mapToInt(DisplaySlot::index).max();
		if (optionalint.isEmpty()) {
			return EMPTY;
		} else {
			ItemDisplayContents contents = new ItemDisplayContents(optionalint.getAsInt() + 1);

			for (DisplaySlot slot : slots) {
				contents.items.set(slot.index(), slot.item());
			}

			return contents;
		}
	}

	private List<DisplaySlot> asSlots() {
		List<DisplaySlot> list = new ArrayList<>();

		for (int i = 0; i < this.items.size(); i++) {
			ItemStack itemstack = this.items.get(i);
			if (!itemstack.isEmpty()) {
				list.add(new DisplaySlot(i, itemstack));
			}
		}

		return list;
	}

	public static int findActiveMapSlot(NonNullList<ItemStack> items, Entity player) {
		int slots = Math.min(ItemDisplayContents.LAYOUT.size(), items.size());
		int startSlot = player.getData(TFDataAttachments.ITEM_DISPLAY_CHOSEN_MAP_SLOT);
		if (slots == 0 || startSlot == -1) return -1;
		for (int i = 0; i < slots; i++) {
			int slot = (startSlot + i) % slots;
			boolean isMapSlot = ItemDisplayContents.LAYOUT.get(slot).get() == ItemDisplays.MAP.get();
			if (isMapSlot && !items.get(slot).isEmpty()) {
				player.setData(TFDataAttachments.ITEM_DISPLAY_CHOSEN_MAP_SLOT, slot);
				return slot;
			}
		}

		return -1;
	}

	public NonNullList<ItemStack> items() {
		return this.items;
	}

	public int size() {
		return this.items.size();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof ItemDisplayContents contents && ItemStack.listMatches(this.items, contents.items);
		}
	}

	@Override
	public int hashCode() {
		return ItemStack.hashStackList(this.items);
	}

	@Override
	public String toString() {
		return "ItemDisplayContents" + this.items;
	}

	public static class Mutable {
		private final NonNullList<ItemStack> items;

		public Mutable(ItemDisplayContents contents) {
			this.items = NonNullList.withSize(LAYOUT.size(), ItemStack.EMPTY);
			contents.copyInto(this.items);
		}

		private int findSwapSlot(ItemStack stack) {
			for (int i = 0; i < LAYOUT.size(); i++) {
				if (LAYOUT.get(i).get().validItems().test(stack)) {
					return i;
				}
			}
			return -1;
		}

		private int findInsertSlot(ItemStack stack) {
			for (int i = 0; i < LAYOUT.size(); i++) {
				if (LAYOUT.get(i).get().validItems().test(stack) && this.items.get(i).isEmpty()) {
					return i;
				}
			}
			return -1;
		}

		public boolean trySwap(SlotAccess source, Player player) {
			return this.trySwap(source,player, TFItemStackUtils::giveOrDrop);
		}

		public boolean trySwap(SlotAccess source, Player player, BiConsumer<ItemStack, Player> remainder) {
			ItemStack slottedStack = source.get();
			if (slottedStack.isEmpty() || !slottedStack.canFitInsideContainerItems()) {
				return false;
			}

			int slotForStack = this.findInsertSlot(slottedStack);
			if (slotForStack < 0) {
				slotForStack = this.findSwapSlot(slottedStack);
			}
			if (slotForStack < 0) {
				return false;
			}

			ItemStack targetStack = this.items.get(slotForStack);
			if (!targetStack.isEmpty() && ItemStack.isSameItemSameComponents(slottedStack, targetStack)) {
				return false;
			}

			ItemStack insert = slottedStack.split(1);
			ItemStack replaced = this.items.set(slotForStack, insert);

			if (replaced.isEmpty()) {
				tryResetChosenMapSlot(player, slotForStack);
				return source.set(slottedStack);
			} else {
				boolean ret = source.set(replaced);
				remainder.accept(slottedStack, player);
				return ret;
			}
		}

		// returns slot where it inserter, otherwise 0
		public int tryInsert(ItemStack stack, Player player) {
			if (!stack.isEmpty() && stack.canFitInsideContainerItems()) {
				int insertSlotIndex = this.findInsertSlot(stack);
				if (insertSlotIndex != -1) {
					this.items.set(insertSlotIndex, stack.split(1));
					tryResetChosenMapSlot(player, insertSlotIndex);
				}
				return insertSlotIndex;
			}
			return -1;
		}

		@Nullable
		public ItemStack removeFirstFree() {
			for (int i = 0; i < this.items.size(); i++) {
				if (!this.items.get(i).isEmpty()) {
					return this.items.set(i, ItemStack.EMPTY);
				}
			}
			return null;
		}

		public ItemDisplayContents toImmutable() {
			return new ItemDisplayContents(this.items);
		}

		private boolean tryResetChosenMapSlot(Player player, int index) {
			if (player.getData(TFDataAttachments.ITEM_DISPLAY_CHOSEN_MAP_SLOT) == -1 && index == FIRST_MAP_SLOT_INDEX && !hasOtherMaps(index)) {
				player.setData(TFDataAttachments.ITEM_DISPLAY_CHOSEN_MAP_SLOT, FIRST_MAP_SLOT_INDEX);
				return true;
			}
			return false;
		}

		private boolean hasOtherMaps(int mapIndex) {
			 for (int i = 0; i < Math.min(items.size(), LAYOUT.size()); i++) {
				 if (!items.get(i).isEmpty() && LAYOUT.get(i) == ItemDisplays.MAP && mapIndex != i)
					 return true;
			 }
			 return false;
		}
	}

	private record DisplaySlot(int index, ItemStack item) {
		public static final Codec<DisplaySlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("slot").forGetter(DisplaySlot::index),
			ItemStack.CODEC.fieldOf("item").forGetter(DisplaySlot::item)
		).apply(instance, DisplaySlot::new));
	}
}
