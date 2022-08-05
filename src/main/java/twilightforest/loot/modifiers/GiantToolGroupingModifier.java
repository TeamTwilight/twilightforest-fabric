package twilightforest.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.LootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.block.GiantBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

//FIXME I simply migrated this out of TFEventListener, it somehow needs to be redone in a more sane way.
public class GiantToolGroupingModifier extends LootModifier {
	public static final Codec<GiantToolGroupingModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, GiantToolGroupingModifier::new));

	static {
		PlayerBlockBreakEvents.BEFORE.register(GiantToolGroupingModifier::breakBlock);
	}

	private static boolean isBreakingWithGiantPick = false;
	private static boolean shouldMakeGiantCobble = false;
	private static int amountOfCobbleToReplace = 0;

	public GiantToolGroupingModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
		boolean flag = false;
		if (shouldMakeGiantCobble && generatedLoot.size() > 0) {
			// turn the next 64 cobblestone drops into one giant cobble
			if (generatedLoot.get(0).getItem() == Item.byBlock(Blocks.COBBLESTONE)) {
				generatedLoot.remove(0);
				if (amountOfCobbleToReplace == 64) {
					newLoot.add(new ItemStack(TFBlocks.GIANT_COBBLESTONE.get()));
					flag = true;
				}
				amountOfCobbleToReplace--;
				if (amountOfCobbleToReplace <= 0) {
					shouldMakeGiantCobble = false;
				}
			}
		}
		return flag ? newLoot : generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return GiantToolGroupingModifier.CODEC;
	}

	public static boolean breakBlock(Level world, Player player, BlockPos pos, BlockState state, /* Nullable */ BlockEntity blockEntity) {
		if (!isBreakingWithGiantPick && canHarvestWithGiantPick(player, state)) {

			isBreakingWithGiantPick = true;

			// pre-check for cobble!
			Item cobbleItem = Blocks.COBBLESTONE.asItem();
			boolean allCobble = state.getBlock().asItem() == cobbleItem;

			if (allCobble) {
				for (BlockPos dPos : GiantBlock.getVolume(pos)) {
					if (dPos.equals(pos))
						continue;
					BlockState stateThere = world.getBlockState(dPos);
					if (stateThere.getBlock().asItem() != cobbleItem) {
						allCobble = false;
						break;
					}
				}
			}

			if (allCobble && !player.getAbilities().instabuild) {
				shouldMakeGiantCobble = true;
				amountOfCobbleToReplace = 64;
			} else {
				shouldMakeGiantCobble = false;
				amountOfCobbleToReplace = 0;
			}

			// break all nearby blocks
			if (player instanceof ServerPlayer playerMP) {
				for (BlockPos dPos : GiantBlock.getVolume(pos)) {
					if (!dPos.equals(pos) && state.getBlock() == world.getBlockState(dPos).getBlock()) {
						// try to break that block too!
						playerMP.gameMode.destroyBlock(dPos);
					}
				}
			}

			isBreakingWithGiantPick = false;
		}
		return true;
	}

	private static boolean canHarvestWithGiantPick(Player player, BlockState state) {
		ItemStack heldStack = player.getMainHandItem();
		Item heldItem = heldStack.getItem();
		return heldItem == TFItems.GIANT_PICKAXE.get() && player.hasCorrectToolForDrops(state);
	}
}
