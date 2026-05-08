package twilightforest.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFLootModifiers;

@Mixin(LootTable.class)
public abstract class LootTableMixin {
	@Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("RETURN"), cancellable = true)
	private void codexTwilight$applyLootModifiers(LootParams params, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir) {
		cir.setReturnValue(TFLootModifiers.apply(cir.getReturnValue(), params));
	}

	@Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;J)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("RETURN"), cancellable = true)
	private void codexTwilight$applyLootModifiersSeeded(LootParams params, long seed, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir) {
		cir.setReturnValue(TFLootModifiers.apply(cir.getReturnValue(), params));
	}

	@Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;Lnet/minecraft/util/RandomSource;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("RETURN"), cancellable = true)
	private void codexTwilight$applyLootModifiersRandom(LootParams params, RandomSource random, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir) {
		cir.setReturnValue(TFLootModifiers.apply(cir.getReturnValue(), params));
	}
}
