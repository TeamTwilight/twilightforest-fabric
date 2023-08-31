package twilightforest.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.item.ItemStack;
import twilightforest.compat.trinkets.TrinketsCompat;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.network.CreateMovingCicadaSoundPacket;
import twilightforest.network.TFPacketHandler;

import javax.annotation.Nonnull;

public class MiscEvents {

	public static void init() {
		ServerEntityEvents.ENTITY_LOAD.register(MiscEvents::addPrey);
		ServerEntityEvents.EQUIPMENT_CHANGE.register(MiscEvents::armorChanged);
	}

	public static void addPrey(Entity entity, ServerLevel world) {
		if (entity instanceof Mob mob) {
			EntityType<?> type = mob.getType();
			if (type == EntityType.CAT) {
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, DwarfRabbit.class, false, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Squirrel.class, false, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, TinyBird.class, false, null));
			} else if (type == EntityType.OCELOT) {
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, false));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Squirrel.class, false));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, TinyBird.class, false));
			} else if (type == EntityType.FOX) {
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, false));
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, Squirrel.class, false));
			} else if (type == EntityType.WOLF) {
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, DwarfRabbit.class, false, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Squirrel.class, false, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Bighorn.class, false, null));
			}
		}
	}

	public static void armorChanged(LivingEntity living, EquipmentSlot slot, @Nonnull ItemStack from, @Nonnull ItemStack to) {
		// from what I can see, vanilla doesnt have a hook for this in the item class. So this will have to do.
		// we only have to check equipping, when its unequipped the sound instance handles the rest

		//if we have a cicada in our curios slot, dont try to run this
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			if (TrinketsCompat.isCicadaEquipped(living)) {
				return;
			}
		}

		if (living != null && !living.level().isClientSide() && slot == EquipmentSlot.HEAD && to.is(TFBlocks.CICADA.get().asItem())) {
			TFPacketHandler.CHANNEL.sendToClientsTrackingAndSelf(new CreateMovingCicadaSoundPacket(living.getId()), living);
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());
		if (stack.getItem() instanceof SpawnEggItem spawnEggItem && spawnEggItem.getType(stack.getTag()) == TFEntities.DEATH_TOME.get()) {
			BlockPos pos = event.getPos();
			BlockState state = event.getLevel().getBlockState(pos);
			if (state.getBlock() instanceof LecternBlock && !state.getValue(BlockStateProperties.HAS_BOOK)) {
				event.setCanceled(true);
				event.getLevel().playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);

				if (event.getLevel() instanceof ServerLevel serverLevel) {
					DeathTome tome = TFEntities.DEATH_TOME.get().spawn(serverLevel, stack, player, pos.below(), MobSpawnType.SPAWN_EGG, true, false);
					if (tome != null) {
						if (!player.getAbilities().instabuild) stack.shrink(1);
						serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
						tome.setOnLectern(true);
					}
				}

			}
		}
	}
}
