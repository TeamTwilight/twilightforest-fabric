package twilightforest.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
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
import twilightforest.entity.monster.DeathTome;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEntities;
import twilightforest.mixin.MobAccessor;
import twilightforest.network.CreateMovingCicadaSoundPacket;

public final class MiscEvents {
	private static boolean bootstrapped;

	private MiscEvents() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof Mob mob) {
				addPrey(mob);
			}
		});
		ServerEntityEvents.EQUIPMENT_CHANGE.register((living, slot, previous, current) -> {
			if (slot == EquipmentSlot.HEAD && current.is(TFBlocks.CICADA.get().asItem())) {
				sendCicadaSound(living);
			}
		});
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			InteractionResult result = addTomesToLecterns(player, level, hand == null ? ItemStack.EMPTY : player.getItemInHand(hand), hitResult.getBlockPos());
			if (result != InteractionResult.PASS) {
				return result;
			}
			return washOffCloth(player, level, hand == null ? ItemStack.EMPTY : player.getItemInHand(hand), hitResult.getBlockPos());
		});
	}

	private static void addPrey(Mob mob) {
		EntityType<?> type = mob.getType();
		if (type == EntityType.CAT && mob instanceof TamableAnimal tameable) {
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(1, new NonTameRandomTargetGoal<>(tameable, DwarfRabbit.class, true, null));
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(1, new NonTameRandomTargetGoal<>(tameable, Squirrel.class, true, null));
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(1, new NonTameRandomTargetGoal<>(tameable, TinyBird.class, true, null));
		} else if (type == EntityType.OCELOT) {
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(1, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, true));
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(1, new NearestAttackableTargetGoal<>(mob, Squirrel.class, true));
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(1, new NearestAttackableTargetGoal<>(mob, TinyBird.class, true));
		} else if (type == EntityType.FOX) {
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(6, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, true));
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(6, new NearestAttackableTargetGoal<>(mob, Squirrel.class, true));
		} else if (type == EntityType.WOLF && mob instanceof TamableAnimal tameable) {
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(7, new NonTameRandomTargetGoal<>(tameable, DwarfRabbit.class, true, null));
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(7, new NonTameRandomTargetGoal<>(tameable, Squirrel.class, true, null));
			((MobAccessor) mob).codex_twilight$getTargetSelector().addGoal(7, new NonTameRandomTargetGoal<>(tameable, Bighorn.class, true, null));
		}
	}

	private static void sendCicadaSound(net.minecraft.world.entity.LivingEntity living) {
		if (living.level().isClientSide()) return;
		CreateMovingCicadaSoundPacket packet = new CreateMovingCicadaSoundPacket(living.getId());
		for (ServerPlayer watcher : PlayerLookup.tracking(living)) {
			if (ServerPlayNetworking.canSend(watcher, CreateMovingCicadaSoundPacket.TYPE)) {
				ServerPlayNetworking.send(watcher, packet);
			}
		}
		if (living instanceof ServerPlayer self && ServerPlayNetworking.canSend(self, CreateMovingCicadaSoundPacket.TYPE)) {
			ServerPlayNetworking.send(self, packet);
		}
	}

	private static InteractionResult addTomesToLecterns(Player player, Level level, ItemStack stack, BlockPos pos) {
		if (!(stack.getItem() instanceof SpawnEggItem spawnEggItem) || spawnEggItem.getType(stack) != TFEntities.DEATH_TOME.get()) {
			return InteractionResult.PASS;
		}
		BlockState state = level.getBlockState(pos);
		if (!(state.getBlock() instanceof LecternBlock) || state.getValue(BlockStateProperties.HAS_BOOK)) {
			return InteractionResult.PASS;
		}
		level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);
		if (level instanceof ServerLevel serverLevel) {
			DeathTome tome = TFEntities.DEATH_TOME.get().spawn(serverLevel, stack, player, pos.below(), MobSpawnType.SPAWN_EGG, true, false);
			if (tome != null) {
				stack.consume(1, player);
				serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
				tome.setOnLectern(true);
			}
		}
		return InteractionResult.SUCCESS;
	}

	private static InteractionResult washOffCloth(Player player, Level level, ItemStack stack, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (!state.is(Blocks.WATER_CAULDRON) || state.getValue(LayeredCauldronBlock.LEVEL) <= 0 || !stack.has(TFDataComponents.EMPERORS_CLOTH)) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide()) {
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
			stack.remove(TFDataComponents.EMPERORS_CLOTH);
			player.awardStat(Stats.CLEAN_ARMOR);
		}
		return InteractionResult.SUCCESS;
	}
}
