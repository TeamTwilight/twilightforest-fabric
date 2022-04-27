package twilightforest;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.mojang.authlib.GameProfile;
import io.github.fabricators_of_create.porting_lib.event.*;
import io.github.fabricators_of_create.porting_lib.event.common.AdvancementCallback;
import io.github.fabricators_of_create.porting_lib.event.common.ItemAttributeModifierCallback;
import io.github.fabricators_of_create.porting_lib.event.common.MountEntityCallback;
import io.github.fabricators_of_create.porting_lib.extensions.EntityExtensions;
import io.github.fabricators_of_create.porting_lib.loot.GlobalLootModifierSerializer;
import io.github.fabricators_of_create.porting_lib.loot.LootModifier;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.*;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.enchantment.TFEnchantment;
import twilightforest.entity.CharmEffect;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.TFEntities;
import twilightforest.entity.monster.Kobold;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;
import twilightforest.entity.projectile.ITFProjectile;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.item.PhantomArmorItem;
import twilightforest.item.TFItems;
import twilightforest.network.AreaProtectionPacket;
import twilightforest.network.EnforceProgressionStatusPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateShieldPacket;
import twilightforest.potions.TFMobEffects;
import twilightforest.util.TFItemStackUtils;
import twilightforest.util.TFStats;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * So much of the mod logic in this one class
 */
public class TFEventListener {

	public static void init() {
		ServerEntityEvents.ENTITY_LOAD.register(TFEventListener::addPrey);
		ItemCraftedCallback.EVENT.register(TFEventListener::onCrafting);
		LivingEntityEvents.ACTUALLY_HURT.register(TFEventListener::entityHurts);
		LivingEntityEvents.TICK.register(TFEventListener::livingUpdate);
		LivingEntityEvents.ATTACK.register(TFEventListener::livingAttack);
		LivingEntityEvents.EQUIPMENT_CHANGE.register(TFEventListener::armorChanged);
		UseBlockCallback.EVENT.register(TFEventListener::createSkullCandle);
		UseBlockCallback.EVENT.register(TFEventListener::onPlayerRightClick);
		PlayerBlockBreakEvents.BEFORE.register(TFEventListener::onCasketBreak);
		PlayerBlockBreakEvents.BEFORE.register(TFEventListener::breakBlock);
		ServerPlayerCreationCallback.EVENT.register(TFEventListener::playerLogsIn);
		ServerPlayerEvents.AFTER_RESPAWN.register(TFEventListener::onPlayerRespawn);
		ServerPlayerEvents.ALLOW_DEATH.register(TFEventListener::applyDeathItems);
		ProjectileImpactCallback.EVENT.register(TFEventListener::throwableParry);
		ItemAttributeModifierCallback.EVENT.register(TFEventListener::addReach);
		EntityTrackingEvents.START_TRACKING.register(TFEventListener::onStartTracking);
		MountEntityCallback.EVENT.register(TFEventListener::preventMountDismount);
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(TFEventListener::playerPortals);
		AdvancementCallback.EVENT.register(TFEventListener::onAdvancementGet);
	}

	private static final ImmutableSet<String> SHIELD_DAMAGE_BLACKLIST = ImmutableSet.of(
			"inWall", "cramming", "drown", "starve", "fall", "flyIntoWall", "outOfWorld", "fallingBlock"
	);

	private static boolean isBreakingWithGiantPick = false;
	private static boolean shouldMakeGiantCobble = false;
	private static int amountOfCobbleToReplace = 0;

	public static void addReach(ItemStack stack, EquipmentSlot slotType, Multimap<Attribute, AttributeModifier> modifiers) {
		Item item = stack.getItem();
		if((item == TFItems.GIANT_PICKAXE.get() || item == TFItems.GIANT_SWORD.get()) && slotType == EquipmentSlot.MAINHAND) {
			modifiers.put(ReachEntityAttributes.REACH, new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Tool modifier", 2.5, AttributeModifier.Operation.ADDITION));
		}
	}

	public static void addPrey(Entity entity, ServerLevel world) {
		EntityType<?> type = entity.getType();
		if(entity instanceof Mob mob) {
			if (type == EntityType.CAT) {
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal)entity, DwarfRabbit.class, false, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal)entity, Squirrel.class, false, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal)entity, TinyBird.class, false, null));
			} else if(type == EntityType.OCELOT) {
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, false));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Squirrel.class, false));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, TinyBird.class, false));
			} else if (type == EntityType.FOX) {
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, false));
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, Squirrel.class, false));
			} else if(type == EntityType.WOLF) {
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal)entity, DwarfRabbit.class, false, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal)entity, Squirrel.class, false, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal)entity, Bighorn.class, false, null));
			}
		}
	}

	public static void onCrafting(Player player, ItemStack itemStack, Container container) {
		// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
		//TODO: Can this be an Ingredient?
		if (itemStack.getItem() == Item.byBlock(Blocks.OAK_PLANKS) && itemStack.getCount() == 64 && doesCraftMatrixHaveGiantLog(container)) {
			player.getInventory().placeItemBackInInventory(new ItemStack(Blocks.OAK_PLANKS, 64));
			player.getInventory().placeItemBackInInventory(new ItemStack(Blocks.OAK_PLANKS, 64));
			player.getInventory().placeItemBackInInventory(new ItemStack(Blocks.OAK_PLANKS, 64));
		}
	}

	private static boolean doesCraftMatrixHaveGiantLog(Container inv) {
		Item giantLogItem = Item.byBlock(TFBlocks.GIANT_LOG.get());
		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (inv.getItem(i).getItem() == giantLogItem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Also check if we need to transform 64 cobbles into a giant cobble
	 */
	public static class ManipulateDrops extends LootModifier {

		protected ManipulateDrops(LootItemCondition[] conditionsIn) {
			super(conditionsIn);
		}

		@Nonnull
		@Override
		protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
			List<ItemStack> newLoot = new ArrayList<>();
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
	}

	public static class Serializer extends GlobalLootModifierSerializer<ManipulateDrops> {

		@Override
		public ManipulateDrops read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
			return new ManipulateDrops(conditionsIn);
		}

		@Override
		public JsonObject write(ManipulateDrops instance) {
			return null;
		}
	}

	public static float entityHurts(DamageSource damageSource, LivingEntity living, float amount) {

		String damageType = damageSource.getMsgId();
		Entity trueSource = damageSource.getEntity();

		// fire aura
		if (living instanceof Player && (damageType.equals("mob") || damageType.equals("player")) && trueSource != null) {
			Player player = (Player) living;
			int fireLevel = TFEnchantment.getFieryAuraLevel(player.getInventory(), damageSource);

			if (fireLevel > 0 && player.getRandom().nextInt(25) < fireLevel) {
				trueSource.setSecondsOnFire(fireLevel / 2);
			}
		}

		// chill aura
		if (living instanceof Player && (damageType.equals("mob") || damageType.equals("player")) && trueSource instanceof LivingEntity) {
			Player player = (Player) living;
			int chillLevel = TFEnchantment.getChillAuraLevel(player.getInventory(), damageSource);

			if (chillLevel > 0) {
				((LivingEntity) trueSource).addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), chillLevel * 5 + 5, chillLevel));
			}
		}

		// triple bow strips hurtResistantTime
		if (damageType.equals("arrow") && trueSource instanceof Player) {
			Player player = (Player) trueSource;

			if (player.getMainHandItem().getItem() == TFItems.TRIPLE_BOW.get() || player.getOffhandItem().getItem() == TFItems.TRIPLE_BOW.get()) {
				living.invulnerableTime = 0;
			}
		}

		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource == DamageSource.IN_WALL) {
			return 0;
		}
		return amount;
	}

	//I wanted to make sure absolutely nothing broke, so I also check against the namespaces of the item to make sure theyre vanilla.
	//Worst case some stupid mod adds their own stuff to the minecraft namespace and breaks this, then you can disable this via config.
	public static InteractionResult createSkullCandle(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack stack = player.getItemInHand(hand);
		BlockPos pos = hitResult.getBlockPos();
		BlockState state = world.getBlockState(pos);
		if(!TFConfig.COMMON_CONFIG.disableSkullCandles.get()) {
			if (stack.is(ItemTags.CANDLES) && Registry.ITEM.getKey(stack.getItem()).getNamespace().equals("minecraft") && !player.isShiftKeyDown()) {
				if (state.getBlock() instanceof AbstractSkullBlock && Registry.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
					SkullBlock.Types type = (SkullBlock.Types) ((AbstractSkullBlock) state.getBlock()).getType();
					boolean wall = state.getBlock() instanceof WallSkullBlock;
					switch (type) {
						case SKELETON -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.SKELETON_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.SKELETON_SKULL_CANDLE.get());
						}
						case WITHER_SKELETON -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.WITHER_SKELE_SKULL_CANDLE.get());
						}
						case PLAYER -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.PLAYER_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.PLAYER_SKULL_CANDLE.get());
						}
						case ZOMBIE -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.ZOMBIE_SKULL_CANDLE.get());
						}
						case CREEPER -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.CREEPER_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.CREEPER_SKULL_CANDLE.get());
						}
						default -> {
							return InteractionResult.PASS;
						}
					}
					if(!player.getAbilities().instabuild) stack.shrink(1);
					player.swing(hand);
					if(player instanceof ServerPlayer) player.awardStat(TFStats.SKULL_CANDLES_MADE.get());
					//this is to prevent anything from being placed afterwords
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	private static void makeFloorSkull(ItemStack stack, BlockPos pos, Level world, Block newBlock) {
		GameProfile profile = null;
		if(world.getBlockEntity(pos) instanceof SkullBlockEntity skull) profile = skull.getOwnerProfile();
		world.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		world.setBlockAndUpdate(pos, newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
				.setValue(SkullCandleBlock.ROTATION, world.getBlockState(pos).getValue(SkullBlock.ROTATION)));
		world.setBlockEntity(new SkullCandleBlockEntity(pos,
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
						.setValue(SkullCandleBlock.ROTATION, world.getBlockState(pos).getValue(SkullBlock.ROTATION)),
				AbstractSkullCandleBlock.candleToCandleColor(stack.getItem()).getValue(), 1));
		if(world.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	private static void makeWallSkull(ItemStack stack, BlockPos pos, Level world, Block newBlock) {
		GameProfile profile = null;
		if(world.getBlockEntity(pos) instanceof SkullBlockEntity skull) profile = skull.getOwnerProfile();
		world.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		world.setBlockAndUpdate(pos, newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
				.setValue(WallSkullCandleBlock.FACING, world.getBlockState(pos).getValue(WallSkullBlock.FACING)));
		world.setBlockEntity(new SkullCandleBlockEntity(pos,
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
						.setValue(WallSkullCandleBlock.FACING, world.getBlockState(pos).getValue(WallSkullBlock.FACING)),
				AbstractSkullCandleBlock.candleToCandleColor(stack.getItem()).getValue(), 1));
		if(world.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	private static boolean hasCharmCurio(Item item, Player player) {
		if(FabricLoader.getInstance().isModLoaded("curios")) {
//			ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(item, player).map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
//
//			if (!stack.isEmpty()) {
//				stack.shrink(1);
//				return true;
//			}
		}

		return false;
	}

	// For when the player dies
	public static boolean applyDeathItems(ServerPlayer player, DamageSource damageSource, float damageAmount) {
		if (player.isCreative()) return true;

		if (charmOfLife(player)) {
			return false; // Executes if the player had charms
		} else if (!player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
			// Did the player recover? No? Let's give them their stuff based on the keeping charms
			charmOfKeeping(player);

			// Then let's store the rest of their stuff in the casket
			keepsakeCasket(player);
		}
		return false;
	}

	private static boolean casketExpiration = false;
	private static void keepsakeCasket(Player player) {
		boolean casketConsumed = TFItemStackUtils.consumeInventoryItem(player, TFBlocks.KEEPSAKE_CASKET.get().asItem());

		if (casketConsumed) {
			Level world = player.getCommandSenderWorld();
			BlockPos.MutableBlockPos pos = player.blockPosition().mutable();

			if (pos.getY() < 2) {
				pos.setY(2);
			} else {
				int logicalHeight = player.getCommandSenderWorld().dimensionType().logicalHeight();

				if (pos.getY() > logicalHeight) {
					pos.setY(logicalHeight - 1);
				}
			}

			// TODO determine if block was air or better yet make a tag list of blocks that are OK to place the casket in
			BlockPos immutablePos = pos.immutable();
			FluidState fluidState = world.getFluidState(immutablePos);

			if (world.setBlockAndUpdate(immutablePos, TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState().setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType())).setValue(KeepsakeCasketBlock.BREAKAGE, TFItemStackUtils.damage))) {
				BlockEntity te = world.getBlockEntity(immutablePos);

				if (te instanceof KeepsakeCasketBlockEntity casket) {

					if (TFConfig.COMMON_CONFIG.casketUUIDLocking.get()) {
						//make it so only the player who died can open the chest if our config allows us
						casket.playeruuid = player.getGameProfile().getId();
					} else {
						casket.playeruuid = null;
					}

					//some names are way too long for the casket so we'll cut them down
					String modifiedName;
					if (player.getName().getString().length() > 12)
						modifiedName = player.getName().getString().substring(0, 12);
					else modifiedName = player.getName().getString();
					casket.name = player.getName().getString();
					casket.casketname = modifiedName;
					casket.setCustomName(new TextComponent(modifiedName + "'s " + (world.random.nextInt(10000) == 0 ? "Costco Casket" : casket.getDisplayName().getString())));
					int damage = world.getBlockState(immutablePos).getValue(KeepsakeCasketBlock.BREAKAGE);
					if (world.random.nextFloat() <= 0.15F) {
						if (damage >= 2) {
							player.getInventory().dropAll();
							world.setBlockAndUpdate(immutablePos, Blocks.AIR.defaultBlockState());
							casketExpiration = true;
							TwilightForestMod.LOGGER.debug("{}'s Casket damage value was too high, alerting the player and dropping extra items", player.getName().getString());
						} else {
							damage = damage + 1;
							world.setBlockAndUpdate(immutablePos, TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState().setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType())).setValue(KeepsakeCasketBlock.BREAKAGE, damage));
							TwilightForestMod.LOGGER.debug("{}'s Casket was randomly damaged, applying new damage", player.getName().getString());
						}
					}
					int casketCapacity = casket.getContainerSize();
					List<ItemStack> list = new ArrayList<>(casketCapacity);
					NonNullList<ItemStack> filler = NonNullList.withSize(4, ItemStack.EMPTY);

					// lets add our inventory exactly how it was on us
					list.addAll(TFItemStackUtils.sortArmorForCasket(player));
					player.getInventory().armor.clear();
					list.addAll(filler);
					list.addAll(player.getInventory().offhand);
					player.getInventory().offhand.clear();
					list.addAll(TFItemStackUtils.sortInvForCasket(player));
					player.getInventory().items.clear();

					casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
				}
			} else {
				TwilightForestMod.LOGGER.error("Could not place Keepsake Casket at " + pos);
			}
		}
	}

	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	public static boolean onCasketBreak(Level world, Player player, BlockPos pos, BlockState state, /* Nullable */ BlockEntity te) {
		Block block = state.getBlock();
		UUID checker;
		if(block == TFBlocks.KEEPSAKE_CASKET.get()) {
			if(te instanceof KeepsakeCasketBlockEntity casket) {
				checker = casket.playeruuid;
			} else checker = null;
			if(checker != null) {
				if (!((KeepsakeCasketBlockEntity) te).isEmpty()) {
					if(!player.hasPermissions(3) || !player.getGameProfile().getId().equals(checker)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean charmOfLife(Player player) {
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_2.get()) || hasCharmCurio(TFItems.CHARM_OF_LIFE_2.get(), player);
		boolean charm1 = !charm2 && (TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_1.get()) || hasCharmCurio(TFItems.CHARM_OF_LIFE_1.get(), player));

		if (charm2 || charm1) {
			if (charm1) {
				player.setHealth(8);
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
			}

			if (charm2) {
				player.setHealth(player.getMaxHealth());

				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0));
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));
			}

			// spawn effect thingers
			CharmEffect effect = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level, player, charm1 ? TFItems.CHARM_OF_LIFE_1.get() : TFItems.CHARM_OF_LIFE_2.get());
			player.level.addFreshEntity(effect);

			CharmEffect effect2 = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level, player, charm1 ? TFItems.CHARM_OF_LIFE_1.get() : TFItems.CHARM_OF_LIFE_2.get());
			effect2.offset = (float) Math.PI;
			player.level.addFreshEntity(effect2);

			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.CHARM_LIFE, player.getSoundSource(), 1, 1);

			if(player instanceof ServerPlayer) player.awardStat(TFStats.LIFE_CHARMS_ACTIVATED.get());

			return true;
		}

		return false;
	}
	
	private static final String PERSISTED_NBT_TAG = "PlayerPersisted";

	private static CompoundTag getPlayerData(Player player) {
		if (!((EntityExtensions)player).getExtraCustomData().contains(PERSISTED_NBT_TAG)) {
			((EntityExtensions)player).getExtraCustomData().put(PERSISTED_NBT_TAG, new CompoundTag());
		}
		return ((EntityExtensions)player).getExtraCustomData().getCompound(PERSISTED_NBT_TAG);
	}

	//if we have any curios and die with a charm of keeping on us, keep our curios instead of dropping them
//	@SubscribeEvent
//	public static void keepCurios(DropRulesEvent event) {
//		if (event.getEntityLiving() instanceof Player player) {
//			CompoundTag playerData = getPlayerData(player);
//			if (!player.level.isClientSide && playerData.contains("TfCharmInventory")) { //Keep all Curios items
//				CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(modifiable -> {
//					for (int i = 0; i < modifiable.getSlots(); ++i) {
//						int finalI = i;
//						event.addOverride(stack -> stack == modifiable.getStackInSlot(finalI), ICurio.DropRule.ALWAYS_KEEP);
//					}
//				});
//			}
//		}
//	}

	//stores the charm that was used for the effect later
	private static ItemStack charmUsed;
	private static void charmOfKeeping(Player player) {

		//check our inventory for any charms of keeping. We also want to check curio slots (if the mod is installed)
		// TODO also consider situations where the actual slots may be empty, and charm gets consumed anyway. Usually won't happen.
		boolean tier3 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_3.get()) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_3.get(), player);
		boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_2.get()) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_2.get(), player);
		boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_1.get()) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_1.get(), player);

		//create a fake inventory to organize our kept inventory in
		Inventory keepInventory = new Inventory(null);
		ListTag tagList = new ListTag();

		//if we have any charm of keeping, all armor and offhand items are kept, so add those
		if (tier1) {
			keepWholeList(keepInventory.armor, player.getInventory().armor);
			keepWholeList(keepInventory.offhand, player.getInventory().offhand);
		}

		if (tier3) {
			//tier 3 keeps our entire inventory
			keepWholeList(keepInventory.items, player.getInventory().items);
			charmUsed = new ItemStack(TFItems.CHARM_OF_KEEPING_3.get());
		} else if (tier2) {
			//tier 2 keeps our hotbar only
			for (int i = 0; i < 9; i++) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			charmUsed = new ItemStack(TFItems.CHARM_OF_KEEPING_2.get());
		} else if (tier1) {
			//tier 1 keeps our selected item only
			int i = player.getInventory().selected;
			if (Inventory.isHotbarSlot(i)) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			charmUsed = new ItemStack(TFItems.CHARM_OF_KEEPING_1.get());
		}

		// always keep tower keys and held phantom armor
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack stack = player.getInventory().items.get(i);
			if (stack.getItem() == TFItems.TOWER_KEY.get()) {
				keepInventory.items.set(i, stack.copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
			if (stack.getItem() instanceof PhantomArmorItem) {
				keepInventory.items.set(i, stack.copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
		}

		// Keep phantom equipment
		for (int i = 0; i < player.getInventory().armor.size(); i++) {
			ItemStack armor = player.getInventory().armor.get(i);
			if (armor.getItem() instanceof PhantomArmorItem) {
				keepInventory.armor.set(i, armor.copy());
				player.getInventory().armor.set(i, ItemStack.EMPTY);
			}
		}

		//take our fake inventory and save it to the persistent player data.
		//by saving it there we can guarantee we will always get all of our items back, even if the player logs out and back in.
		keepInventory.save(tagList);
		getPlayerData(player).put("TfCharmInventory", tagList);
	}

	//transfers a list of items to another
	private static void keepWholeList(NonNullList<ItemStack> transferTo, NonNullList<ItemStack> transferFrom) {
		for (int i = 0; i < transferFrom.size(); i++) {
			transferTo.set(i, transferFrom.get(i).copy());
		}
		transferFrom.clear();
	}

	public static void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
//		if (event.isEndConquered()) {
//			updateCapabilities((ServerPlayer) event.getPlayer(), event.getPlayer());
//		} else {
			if(casketExpiration) {
				newPlayer.sendMessage(new TranslatableComponent("block.twilightforest.casket.broken").withStyle(ChatFormatting.DARK_RED), newPlayer.getUUID());
			}
			returnStoredItems(newPlayer);
//		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(Player player) {

		TwilightForestMod.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUUID());

		//check if our tag is in the persistent player data. If so, copy that inventory over to our own. Cloud storage at its finest!
		CompoundTag playerData = getPlayerData(player);
		if (!player.level.isClientSide && playerData.contains("TfCharmInventory")) {
			ListTag tagList = playerData.getList("TfCharmInventory", 10);
			player.getInventory().load(tagList);
			getPlayerData(player).remove("TfCharmInventory");
		}

		// spawn effect thingers
		if (charmUsed != null) {
			CharmEffect effect = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level, player, charmUsed.getItem());
			player.level.addFreshEntity(effect);

			CharmEffect effect2 = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level, player, charmUsed.getItem());
			effect2.offset = (float) Math.PI;
			player.level.addFreshEntity(effect2);

			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.CHARM_KEEP, player.getSoundSource(), 1.5F, 1.0F);
			if(player instanceof ServerPlayer) player.awardStat(TFStats.KEEPING_CHARMS_ACTIVATED.get());
			charmUsed = null;
		}

	}

	public static void livingUpdate(LivingEntity entity) {
		CapabilityList.SHIELDS.maybeGet(entity).ifPresent(IShieldCapability::update);
		if (entity instanceof IHostileMount)
			entity.getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static volatile boolean allowDismount = false;

	public static InteractionResult preventMountDismount(Entity mounted, Entity mounting, boolean isMounting) {
		if (!mounted.level.isClientSide() && !isMounting && mounted.isAlive() && mounting instanceof LivingEntity living && isRidingUnfriendly(living) && !allowDismount)
			return InteractionResult.FAIL;
		return InteractionResult.PASS;
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}

	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 * Also check for breaking blocks with the giant's pickaxe and maybe break nearby blocks
	 */
	public static boolean breakBlock(Level world, Player player, BlockPos pos, BlockState state, /* Nullable */ BlockEntity blockEntity) {
		if (world.isClientSide) return true;

		boolean cancelled = false;
		if (isBlockProtectedFromBreaking(world, pos) && isAreaProtected(world, player, pos)) {
			cancelled = true;

		} else if (!isBreakingWithGiantPick && canHarvestWithGiantPick(player, state)) {

			isBreakingWithGiantPick = true;

			// check nearby blocks for same block or same drop

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
		return !cancelled;
	}

	private static boolean canHarvestWithGiantPick(Player player, BlockState state) {
		ItemStack heldStack = player.getMainHandItem();
		Item heldItem = heldStack.getItem();
		return heldItem == TFItems.GIANT_PICKAXE.get() && heldItem.isCorrectToolForDrops(state);
	}

	public static InteractionResult onPlayerRightClick(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
		if (!world.isClientSide && isBlockProtectedFromInteraction(world, hitResult.getBlockPos()) && isAreaProtected(world, player, hitResult.getBlockPos())) {
			return InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	private static boolean isBlockProtectedFromInteraction(Level world, BlockPos pos) {
		return world.getBlockState(pos).is(BlockTagGenerator.STRUCTURE_BANNED_INTERACTIONS);
	}

	private static boolean isBlockProtectedFromBreaking(Level world, BlockPos pos) {
		// todo improve
		return !Registry.BLOCK.getKey(world.getBlockState(pos).getBlock()).getPath().contains("grave") || !world.getBlockState(pos).is(TFBlocks.KEEPSAKE_CASKET.get());
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private static boolean isAreaProtected(Level world, Player player, BlockPos pos) {

		if (player.getAbilities().instabuild || !TFGenerationSettings.isProgressionEnforced(world)/* || player instanceof FakePlayer*/) {
			return false;
		}

		ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(world);


		if (chunkGenerator != null) {
			Optional<StructureStart> struct = TFGenerationSettings.locateTFStructureInRange((ServerLevel) world, pos, 0);
			if(struct.isPresent()) {
				StructureStart structure = struct.get();
				if(structure.getBoundingBox().isInside(pos)) {
					// what feature is nearby?  is it one the player has not unlocked?
					TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), (ServerLevel) world);

					if (!nearbyFeature.doesPlayerHaveRequiredAdvancements(player)/* && chunkGenerator.isBlockProtected(pos)*/) {

						// TODO: This is terrible but *works* for now.. proper solution is to figure out why the stronghold bounding box is going so high
						if (nearbyFeature == TFFeature.KNIGHT_STRONGHOLD && pos.getY() >= TFGenerationSettings.SEALEVEL)
							return false;

						// send protection packet
						BoundingBox bb = structure.getBoundingBox();//new MutableBoundingBox(pos, pos.add(16, 16, 16)); // todo 1.15 get from structure
						sendAreaProtectionPacket(world, pos, bb);

						// send a hint monster?
						nearbyFeature.trySpawnHintMonster(world, player, pos);

						return true;
					}
				}
			}
		}
		return false;
	}

	private static void sendAreaProtectionPacket(Level world, BlockPos pos, BoundingBox sbb) {
		TFPacketHandler.CHANNEL.sendToClientsAround(new AreaProtectionPacket(sbb, pos), world, pos, 64);
	}

	public static boolean livingAttack(LivingEntity living, DamageSource source, float amount) {
		// cancel attacks in protected areas
		if (!living.level.isClientSide && living instanceof Enemy && source.getEntity() instanceof Player && !(living instanceof Kobold)
				&& isAreaProtected(living.level, (Player) source.getEntity(), new BlockPos(living.blockPosition()))) {

			return true;
		}
		// shields
		AtomicBoolean cancelled = new AtomicBoolean(false);
		if (!living.level.isClientSide && !SHIELD_DAMAGE_BLACKLIST.contains(source.msgId)) {
			CapabilityList.SHIELDS.maybeGet(living).ifPresent(cap -> {
				if (cap.shieldsLeft() > 0) {
					cap.breakShield();
					cancelled.set(true);
				}
			});
		}
		return cancelled.get();
	}

	/**
	 * When player logs in, report conflict status, set enforced_progression rule
	 */
	public static void playerLogsIn(ServerPlayer player) {
		sendEnforcedProgressionStatus(player, TFGenerationSettings.isProgressionEnforced(player.level));
		updateCapabilities(player, player);
		banishNewbieToTwilightZone(player);
	}

	/**
	 * When player changes dimensions, send the rule status if they're moving to the Twilight Forest
	 */
	public static void playerPortals(ServerPlayer player, ServerLevel origin, ServerLevel destination) {
		if (TFGenerationSettings.usesTwilightChunkGenerator(player.getLevel())) {
			sendEnforcedProgressionStatus(player, TFGenerationSettings.isProgressionEnforced(player.getLevel()));
		}

		updateCapabilities(player, player);
	}

	public static void onStartTracking(Entity trackedEntity, ServerPlayer player) {
		updateCapabilities(player, trackedEntity);
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer clientTarget, Entity shielded) {
		CapabilityList.SHIELDS.maybeGet(shielded).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.sendToClient(new UpdateShieldPacket(shielded, cap), clientTarget);
			}
		});
	}

	private static void sendEnforcedProgressionStatus(ServerPlayer player, boolean isEnforced) {
		TFPacketHandler.CHANNEL.sendToClient(new EnforceProgressionStatusPacket(isEnforced), player);
	}

	// Teleport first-time players to Twilight Forest

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";

	private static void banishNewbieToTwilightZone(Player player) {
		CompoundTag tagCompound = ((EntityExtensions)player).getExtraCustomData();
		CompoundTag playerData = tagCompound.getCompound(PERSISTED_NBT_TAG);

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.putBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.put(PERSISTED_NBT_TAG, playerData); // commit

		if (shouldBanishPlayer) TFPortalBlock.attemptSendEntity(player, true, TFConfig.COMMON_CONFIG.DIMENSION.portalForNewPlayerSpawn.get()); // See ya hate to be ya
	}

	// Advancement Trigger
	public static void onAdvancementGet(Player player, Advancement advancement) {
		if (player instanceof ServerPlayer) {
			TFAdvancements.ADVANCEMENT_UNLOCKED.trigger((ServerPlayer) player, advancement);
		}
	}

	public static void armorChanged(LivingEntity living, EquipmentSlot slot, @Nonnull ItemStack from, @Nonnull ItemStack to) {
		if (!living.level.isClientSide && living instanceof ServerPlayer) {
			TFAdvancements.ARMOR_CHANGED.trigger((ServerPlayer) living, from, to);
		}
	}

	// Parrying

	private static final boolean globalParry = !FabricLoader.getInstance().isModLoaded("parry");

	/*@SubscribeEvent
	public static void arrowParry(ProjectileImpactEvent<AbstractArrow> event) {
		final AbstractArrow projectile = event.getProjectile();

		if (!projectile.getCommandSenderWorld().isClientSide && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityHitResult) {
				Entity entity = ((EntityHitResult) event.getRayTraceResult()).getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.isDamageSourceBlocked(new DamageSource("parry_this") {
						@Override
						public Vec3 getSourcePosition() {
							return projectile.position();
						}
					}) && (entityBlocking.getUseItem().getItem().getUseDuration(entityBlocking.getUseItem()) - entityBlocking.getUseItemRemainingTicks()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksArrow.get()) {
						Vec3 playerVec3 = entityBlocking.getLookAngle();

						projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

						projectile.setOwner(entityBlocking); //TODO: Verify

						event.setCanceled(true);
					}
				}
			}
		}
	}*/

	/*@SubscribeEvent
	public static void fireballParry(ProjectileImpactEvent<Fireball> event) {
		final AbstractHurtingProjectile projectile = event.getProjectile();

		if (!projectile.getCommandSenderWorld().isClientSide && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityHitResult) {
				Entity entity = ((EntityHitResult) event.getRayTraceResult()).getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.isDamageSourceBlocked(new DamageSource("parry_this") {
						@Override
						public Vec3 getSourcePosition() {
							return projectile.position();
						}
					}) && (entityBlocking.getUseItem().getItem().getUseDuration(entityBlocking.getUseItem()) - entityBlocking.getUseItemRemainingTicks()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksFireball.get()) {
						Vec3 playerVec3 = entityBlocking.getLookAngle();

						projectile.setDeltaMovement(new Vec3(playerVec3.x, playerVec3.y, playerVec3.z));
						projectile.xPower = projectile.getDeltaMovement().x() * 0.1D;
						projectile.yPower = projectile.getDeltaMovement().y() * 0.1D;
						projectile.zPower = projectile.getDeltaMovement().z() * 0.1D;

						projectile.setOwner(entityBlocking); //TODO: Verify

						event.setCanceled(true);
					}
				}
			}
		}
	}*/

	public static boolean throwableParry(final Projectile projectile, HitResult hitResult) {
		if (!projectile.getCommandSenderWorld().isClientSide && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (hitResult instanceof EntityHitResult) {
				Entity entity = ((EntityHitResult) hitResult).getEntity();


				if (projectile != null && entity instanceof LivingEntity entityBlocking) {
					if (entityBlocking.isDamageSourceBlocked(new DamageSource("parry_this") {
						@Override
						public Vec3 getSourcePosition() {
							return projectile.position();
						}
					}) && (entityBlocking.getUseItem().getItem().getUseDuration(entityBlocking.getUseItem()) - entityBlocking.getUseItemRemainingTicks()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksThrowable.get()) {
						Vec3 playerVec3 = entityBlocking.getLookAngle();

						projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

						projectile.setOwner(entityBlocking); //TODO: Verify

						return true;
					}
				}
			}
		}
		return false;
	}
}
