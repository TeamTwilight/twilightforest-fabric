package twilightforest.mixin;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$4")
public class GrindstoneMenu$4Mixin {

	@Shadow
	@Final
	GrindstoneMenu field_16780;

	@Inject(
		method = "onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",
		at = @At("HEAD")
	)
	private void twilightforest$onTake(
		Player player,
		ItemStack stack,
		CallbackInfo ci
	) {
		GrindstoneMenuAccessor accessor = (GrindstoneMenuAccessor) field_16780;

		ItemStack top = accessor.twilightforest$repairSlots().getItem(0);
		ItemStack bottom = accessor.twilightforest$repairSlots().getItem(1);

		twilightforest$extractItemsFromSwapHotbarModifier(player, top, bottom);
	}

	@Unique
	private static void twilightforest$extractItemsFromSwapHotbarModifier(Player player, ItemStack top, ItemStack bottom) {
		twilightforest$returnModifierItems(
			player,
			top,
			bottom,
			TravellersModifiersManager.SWAP_HOTBAR_MODIFIER,
			DataComponents.CONTAINER,
			ItemContainerContents::nonEmptyStream
		);

		twilightforest$returnModifierItems(
			player,
			top,
			bottom,
			TravellersModifiersManager.ITEM_DISPLAY_MODIFIER,
			TFDataComponents.ITEM_DISPLAY.get(),
			contents -> contents.items().stream()
		);
	}

	@Unique
	private static <T> void twilightforest$returnModifierItems(Player player, ItemStack top, ItemStack bottom, ResourceKey<TravellersModifier> modifierKey, DataComponentType<T> componentType, Function<T, Stream<ItemStack>> itemStreamExtractor) {
		twilightforest$getUniqueTravellersGear(top, bottom, stack ->
			TravellersModifiersManager.hasTravellersModifier(
				player.registryAccess(),
				stack,
				modifierKey
			)
		).map(stack -> stack.get(componentType))
			.ifPresent(component ->
				itemStreamExtractor.apply(component)
					.filter(stack -> !stack.isEmpty())
					.forEach(itemStack ->
						ItemHandlerHelper.giveItemToPlayer(player, itemStack.copy())
					)
			);
	}

	@Unique
	private static Optional<ItemStack> twilightforest$getUniqueTravellersGear(ItemStack top, ItemStack bottom, Predicate<ItemStack> predicate) {
		List<ItemStack> travellersItemStacks = Stream.of(top, bottom)
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()))
			.filter(predicate)
			.toList();
		return travellersItemStacks.size() == 1 ? Optional.of(travellersItemStacks.getFirst()) : Optional.empty();
	}
}