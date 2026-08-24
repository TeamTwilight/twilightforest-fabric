package twilightforest.events;

import carminite.events.hooks.EventHooks;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.effect.ServerMobEffectEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import twilightforest.block.GiantBlock;
import twilightforest.components.entity.GiantPickaxeMiningAttachment;
import twilightforest.tags.TFBlockTags;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.item.*;

import java.util.List;

public class ToolEvents {
	public static final ToolEvents INSTANCE = new ToolEvents();

	private void init() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, _) -> INSTANCE.fieryToolSetFire(entity, source));
		PlayerBlockBreakEvents.BEFORE.register((_, player, _, state, _) -> INSTANCE.damageNonMazebreakerToolsMore(player, state));
		ServerMobEffectEvents.ALLOW_ADD.register((effectInstance, entity, _) -> INSTANCE.preventFatigueWithPocketWatch(effectInstance, entity));
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, _) -> INSTANCE.handleGiantPickaxeMining(level, player, pos, state));
		CommonLifecycleEvents.TAGS_LOADED.register((_, _) -> INSTANCE.refreshOreMagnetCache());
	}

	private boolean fieryToolSetFire(LivingEntity entity, DamageSource source) {
		if (source.getEntity() instanceof LivingEntity living && (living.getMainHandItem().is(TFItems.FIERY_SWORD) || living.getMainHandItem().is(TFItems.FIERY_PICKAXE)) && !entity.fireImmune()) {
			entity.igniteForSeconds(1);
		}
		return true;
	}

	private boolean damageNonMazebreakerToolsMore(Player player, BlockState state) {
		ItemStack stack = player.getMainHandItem();
		if (state.is(TFBlockTags.MAZEBREAKER_ACCELERATED)) {
			if (stack.isDamageableItem() && !(stack.getItem() instanceof MazebreakerPickItem)) {
				stack.hurtAndBreak(16, player, EquipmentSlot.MAINHAND);
			}
		}
		return true;
	}

	private boolean preventFatigueWithPocketWatch(MobEffectInstance effectInstance, LivingEntity entity) {
		return !effectInstance.is(MobEffects.MINING_FATIGUE) || !entity.isHolding(TFItems.POCKET_WATCH);
	}

	private boolean handleGiantPickaxeMining(Level level, Player player, BlockPos pos, BlockState state) {
		if (player instanceof ServerPlayer serverPlayer && canHarvestWithGiantPick(player, state, pos)) {
			var attachment = serverPlayer.getAttached(TFDataAttachments.GIANT_PICKAXE_MINING);

			if (shouldBreakGiantBlock(serverPlayer, attachment)) {
				attachment.setBreaking(true); // Tell the capability that a block breaking loop is happening, so it knows to fail the if check above. Otherwise, this would go on forever

				LootParams.Builder builder = new LootParams.Builder(serverPlayer.level())
					.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
					.withParameter(LootContextParams.BLOCK_STATE, state)
					.withOptionalParameter(LootContextParams.THIS_ENTITY, serverPlayer)
					.withParameter(LootContextParams.TOOL, serverPlayer.getMainHandItem());

				List<ItemStack> drops = state.getDrops(builder);

				if (!drops.isEmpty() && drops.getFirst().getItem() instanceof BlockItem block) {
					boolean allTheSame = LootEvents.GIANT_BLOCK_CONVERSIONS.containsKey(block.getBlock()); //check if the block drops can be converted instead of the block itself so things like stone can make giant cobble
					if (allTheSame) {
						for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
							if (!serverPlayer.level().getBlockState(offsetPos).is(state.getBlock())) {
								allTheSame = false;
								break; //end early: we have determined we arent getting a giant block from this. No need to keep checking positions
							}
						}
					}
					attachment.setGiantBlockConversion(allTheSame ? 64 : 0); // NO IN-BETWEEN! Either the whole 64 get converted, or none do
				}
				serverPlayer.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				serverPlayer.gameMode.destroyBlock(pos); // Break the block we broke, for real this time

				// Break all the other blocks, if they're the same type
				for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
					if (!offsetPos.equals(pos) && serverPlayer.level().getBlockState(offsetPos).is(state.getBlock())) {
						BlockPos newPos = new BlockPos(offsetPos); // This feels dumb, but without it, the client thinks the last block in the iterator is broken too
						serverPlayer.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(serverPlayer.level().getBlockState(newPos)));
						serverPlayer.gameMode.destroyBlock(newPos);
					}
				}
				attachment.setBreaking(false); // Tell the capability that the loop is over, and all is good in the world
				return false;
			}
		}
		return true;
	}

	private static boolean canHarvestWithGiantPick(Player player, BlockState state, BlockPos pos) {
		return player.getMainHandItem().getItem() instanceof GiantPickItem && EventHooks.doPlayerHarvestCheck(player, state, player.level(), pos);
	}

	private static boolean shouldBreakGiantBlock(Player player, GiantPickaxeMiningAttachment attachment) {
		return attachment.getMining() == player.level().getGameTime() && !attachment.getBreaking();
	}

	private void refreshOreMagnetCache() {
		OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.clear();
		OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.clear();

		//collect all tags
		for (TagKey<Block> tag : BuiltInRegistries.BLOCK.listTagIds().filter(location -> location.location().getNamespace().equals("c")).toList()) {
			//check if the tag is a valid ore tag
			if (tag.location().getPath().contains("ores_in_ground/")) {
				//grab the part after the slash for use later
				String oreground = tag.location().getPath().substring(15);
				//check if a tag for ore grounds matches up with our ores in ground tag
				if (BuiltInRegistries.BLOCK.listTagIds().filter(location -> location.location().getNamespace().equals("c")).anyMatch(blockTagKey -> blockTagKey.location().getPath().equals("ore_bearing_ground/" + oreground))) {
					//add each ground type to each ore
					BuiltInRegistries.BLOCK.get(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ore_bearing_ground/" + oreground))).get().forEach(ground ->
						BuiltInRegistries.BLOCK.get(tag).get().forEach(ore -> {
							//exclude ignored ores
							if (!ore.value().defaultBlockState().is(TFBlockTags.ORE_MAGNET_IGNORE)) {
								OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.put(ore.value(), ground.value());
							}
							if (!ore.value().defaultBlockState().is(TFBlockTags.MINING_CORE_EXCLUDED)) {
								OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.put(ore.value(), ground.value());
							}
						}));
				}
			}
		}

		//Gonna need to special case this one as it isn't covered by tags.
		//Ancient debris isn't exactly an ore, so it makes sense that the tag doesn't include it
		if (!Blocks.ANCIENT_DEBRIS.defaultBlockState().is(TFBlockTags.ORE_MAGNET_IGNORE) && !OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.containsKey(Blocks.ANCIENT_DEBRIS)) {
			OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERRACK);
		}

		if (!Blocks.ANCIENT_DEBRIS.defaultBlockState().is(TFBlockTags.MINING_CORE_EXCLUDED) && !OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.containsKey(Blocks.ANCIENT_DEBRIS)) {
			OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERRACK);
		}
	}
}