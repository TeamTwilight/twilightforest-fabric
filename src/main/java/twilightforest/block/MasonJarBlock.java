package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.init.TFSounds;

import java.util.List;

public class MasonJarBlock extends JarBlock implements SimpleWaterloggedBlock {
	public static final MapCodec<MasonJarBlock> CODEC = simpleCodec(MasonJarBlock::new);

	public MasonJarBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MasonJarBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (super.useItemOn(stack, state, level, pos, player, hand, hit) != InteractionResult.PASS) {
			return InteractionResult.SUCCESS;
		}
		if (!(level.getBlockEntity(pos) instanceof MasonJarBlockEntity jar)) {
			return InteractionResult.CONSUME;
		}
		if (!(level instanceof ServerLevel server)) {
			return InteractionResult.SUCCESS;
		}
		MasonJarBlockEntity.MasonJarItemStorage storage = jar.getItemHandler();
		if (stack.isEmpty()) {
			handleEmptyHand(server, pos, player, hand, jar, storage);
		} else {
			handleInsert(server, pos, player, hand, jar, storage, stack);
		}
		return InteractionResult.SUCCESS;
	}

	private static void handleEmptyHand(ServerLevel server, BlockPos pos, Player player, InteractionHand hand, MasonJarBlockEntity jar, MasonJarBlockEntity.MasonJarItemStorage storage) {
		ItemStack contained = storage.getItem();
		if (contained.isEmpty()) {
			wiggle(server, pos, jar);
			return;
		}
		if (player.isSecondaryUseActive()) {
			player.sendOverlayMessage(Component.literal(contained.getHoverName().getString() + " x" + contained.getCount()));
			wiggle(server, pos, jar);
			return;
		}
		ItemVariant variant = ItemVariant.of(contained);
		int maxAmount = Math.min(
			Item.ABSOLUTE_MAX_STACK_SIZE,
			contained.getMaxStackSize()
		);
		try (Transaction transaction = Transaction.openOuter()) {
			long extracted = storage.extract(
				variant,
				maxAmount,
				transaction
			);
			if (extracted <= 0) {
				wiggle(server, pos, jar);
				return;
			}
			transaction.commit();
			ItemStack result = variant.toStack((int) extracted);
			server.sendBlockUpdated(pos, jar.getBlockState(), jar.getBlockState(), Block.UPDATE_ALL);
			player.setItemInHand(hand, result);
			server.playSound(null, pos, TFSounds.JAR_REMOVE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
			server.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
	}

	private static void handleInsert(ServerLevel server, BlockPos pos, Player player, InteractionHand hand, MasonJarBlockEntity jar, MasonJarBlockEntity.MasonJarItemStorage storage, ItemStack stack) {
		ItemVariant variant = ItemVariant.of(stack);
		int amountToInsert = stack.getCount();
		int oldRotation = jar.getItemRotation();
		jar.setItemRotation(RotationSegment.convertToSegment(player.getYRot() + 180.0F));
		try (Transaction transaction = Transaction.openOuter()) {
			long inserted = storage.insert(
				variant,
				amountToInsert,
				transaction
			);
			if (inserted <= 0) {
				jar.setItemRotation(oldRotation);
				wiggle(server, pos, jar);
				return;
			}
			transaction.commit();
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			ItemStack before = stack.copy();
			ItemStack remainder = stack.copy();
			remainder.shrink((int) inserted);
			player.setItemInHand(hand, player.hasInfiniteMaterials() ? before : remainder);
			float filledRatio = (float) inserted / (float) before.getMaxStackSize();
			server.playSound(
				null,
				pos,
				TFSounds.JAR_INSERT.value(),
				SoundSource.BLOCKS,
				1.0F,
				0.7F + 0.5F * filledRatio
			);
			server.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
	}

	private static void wiggle(ServerLevel server, BlockPos pos, MasonJarBlockEntity jar) {
		server.playSound(null, pos, TFSounds.JAR_WIGGLE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
		jar.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY)
			instanceof MasonJarBlockEntity jarBlockEntity) {
			params = params.withDynamicDrop(
				ShulkerBoxBlock.CONTENTS,
				stackConsumer -> stackConsumer.accept(
					jarBlockEntity.getItemHandler().getItem()
				)
			);
		}
		return super.getDrops(state, params);
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction
	) {
		if (level.getBlockEntity(pos)
			instanceof MasonJarBlockEntity jarBlockEntity) {
			ItemStack itemStack = jarBlockEntity.getItemHandler().getItem();
			return Mth.lerpDiscrete(itemStack.isEmpty() ? 0 : (float) itemStack.getCount() / itemStack.getMaxStackSize(), 0, 15);
		}
		return 0;
	}
}