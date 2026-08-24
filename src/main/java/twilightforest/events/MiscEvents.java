package twilightforest.events;

import carminite.network.PacketDistributor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.entity.monster.DeathTome;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEntities;
import twilightforest.network.CreateMovingCicadaSoundPacket;

public class MiscEvents {
	public static final MiscEvents INSTANCE = new MiscEvents();

	private void init() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, _) -> INSTANCE.addPrey(entity));
		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, _, currentStack) -> INSTANCE.updateCicadaSoundsOnHead(livingEntity, equipmentSlot, currentStack));
		UseBlockCallback.EVENT.register(INSTANCE::addTomesToLecterns);
		UseBlockCallback.EVENT.register(INSTANCE::washOffCloth);
	}

	private void addPrey(Entity entity) {
		if (entity instanceof Mob mob) {
			EntityType<?> type = mob.getType();
			if (type == EntityType.CAT) {
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, DwarfRabbit.class, true, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Squirrel.class, true, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, TinyBird.class, true, null));
			} else if (type == EntityType.OCELOT) {
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, true));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Squirrel.class, true));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, TinyBird.class, true));
			} else if (type == EntityType.FOX) {
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, true));
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, Squirrel.class, true));
			} else if (type == EntityType.WOLF) {
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, DwarfRabbit.class, true, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Squirrel.class, true, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Bighorn.class, true, null));
			}
		}
	}

	private void updateCicadaSoundsOnHead(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack currentStack) {
		// from what I can see, vanilla doesn't have a hook for this in the item class. So this will have to do.
		// we only have to check equipping, when its unequipped the sound instance handles the rest

		//if we have a cicada in our curios slot, don't try to run this
		 if (FabricLoader.getInstance().isModLoaded("trinkets")) {
		 	//if (CuriosCompat.isCurioEquipped(living, stack -> stack.is(TFBlocks.CICADA.asItem()))) return;
		 }

		if (!livingEntity.level().isClientSide() && equipmentSlot == EquipmentSlot.HEAD && currentStack.is(TFBlocks.CICADA.asItem())) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, new CreateMovingCicadaSoundPacket(livingEntity.getId()));
		}
	}

	private InteractionResult addTomesToLecterns(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isSpectator()) {
			return InteractionResult.PASS;
		}

		ItemStack stack = player.getItemInHand(hand);

		if (!(stack.getItem() instanceof SpawnEggItem) || SpawnEggItem.getType(stack) != TFEntities.DEATH_TOME)
			return InteractionResult.PASS;

		BlockPos pos = hitResult.getBlockPos();
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof LecternBlock && !state.getValue(BlockStateProperties.HAS_BOOK)) {
			level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);

			if (level instanceof ServerLevel serverLevel) {
				DeathTome tome = TFEntities.DEATH_TOME.spawn(serverLevel, stack, player, pos.below(), EntitySpawnReason.SPAWN_ITEM_USE, true, false);
				if (tome != null) {
					stack.consume(1, player);
					serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
					tome.setOnLectern(true);
				}
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	private InteractionResult washOffCloth(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isSpectator()) {
			return InteractionResult.PASS;
		}

		BlockPos pos = hitResult.getBlockPos();
		BlockState state = level.getBlockState(pos);
		ItemStack usedItem = player.getItemInHand(hand);

		if (!state.is(Blocks.WATER_CAULDRON) || state.getValue(LayeredCauldronBlock.LEVEL) <= 0) {
			return InteractionResult.PASS;
		}

		if (usedItem.has(TFDataComponents.EMPERORS_CLOTH)) {
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
			usedItem.remove(TFDataComponents.EMPERORS_CLOTH);
			player.awardStat(Stats.CLEAN_ARMOR);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
