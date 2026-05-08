package twilightforest.block.entity.bookshelf;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.ChiseledCanopyShelfBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BookshelfSpawner {
	public static final List<Pair<Integer, BooleanProperty>> SLOT_PROPERTIES_AND_INDEXES = List.of(
			Pair.of(0, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_0_OCCUPIED),
			Pair.of(1, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_1_OCCUPIED),
			Pair.of(2, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_2_OCCUPIED),
			Pair.of(3, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_3_OCCUPIED),
			Pair.of(4, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_4_OCCUPIED),
			Pair.of(5, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_5_OCCUPIED));

	public int maxNearbyEntities = 4;
	public int spawnRange = 4;
	public int spawnCheckRange = 12;
	private int spawnDelay = 20;
	private SimpleWeightedRandomList<SpawnData> spawnPotentials = SimpleWeightedRandomList.empty();
	@Nullable
	private SpawnData nextSpawnData;
	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 400;
	private int requiredPlayerRange = 8;
	private final PlayerDetector detector = PlayerDetector.INCLUDING_CREATIVE_PLAYERS;

	public void setEntityId(EntityType<?> type, @Nullable Level level, RandomSource random, BlockPos pos) {
		this.getOrCreateNextSpawnData(level, random, pos)
				.getEntityToSpawn()
				.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
	}

	private boolean isNearPlayer(ServerLevel level, BlockPos pos) {
		return !this.detector.detect(level, PlayerDetector.EntitySelector.SELECT_FROM_LEVEL, pos, this.requiredPlayerRange, true).isEmpty();
	}

	public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
		if (!this.isNearPlayer(level, pos)) {
			return;
		}

		if (this.spawnDelay == -1) {
			this.delay(level, pos);
		}

		if (this.spawnDelay > 0) {
			this.spawnDelay--;
			return;
		}

		List<Pair<Integer, BooleanProperty>> filledSlots = new ArrayList<>(SLOT_PROPERTIES_AND_INDEXES);
		filledSlots.removeIf(pair -> !state.getValue(pair.getSecond()));
		Collections.shuffle(filledSlots);

		for (Pair<Integer, BooleanProperty> filledSlot : filledSlots) {
			BooleanProperty property = filledSlot.getSecond();
			if (state.hasProperty(property) && state.getValue(property) && this.attemptSpawnTome(filledSlot.getFirst(), level, pos, false, null, 0)) {
				this.delay(level, pos);
				break;
			}
		}

		int fullSlots = 0;
		for (BooleanProperty property : ChiseledCanopyShelfBlock.SLOT_OCCUPIED_PROPERTIES) {
			if (state.hasProperty(property) && state.getValue(property)) {
				fullSlots++;
			}
		}

		if (fullSlots == 0) {
			level.setBlockAndUpdate(pos, state.setValue(ChiseledCanopyShelfBlock.SPAWNER, false));
		}
	}

	private void delay(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();
		if (this.maxSpawnDelay <= this.minSpawnDelay) {
			this.spawnDelay = this.minSpawnDelay;
		} else {
			this.spawnDelay = this.minSpawnDelay + random.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
		}

		this.spawnPotentials.getRandom(random).ifPresent(entry -> this.setNextSpawnData(level, pos, entry.data()));
		this.broadcastEvent(level, pos, 1);
	}

	public void load(@Nullable Level level, BlockPos pos, CompoundTag tag) {
		this.spawnDelay = tag.getShort("Delay");
		boolean hasSpawnData = tag.contains("SpawnData", 10);
		if (hasSpawnData) {
			SpawnData spawnData = SpawnData.CODEC
					.parse(NbtOps.INSTANCE, tag.getCompound("SpawnData"))
					.resultOrPartial(message -> TwilightForestMod.LOGGER.warn("Death Tome Spawner: Invalid SpawnData: {}", message))
					.orElseGet(SpawnData::new);
			this.setNextSpawnData(level, pos, spawnData);
		}

		boolean hasSpawnPotentials = tag.contains("SpawnPotentials", 9);
		if (hasSpawnPotentials) {
			ListTag list = tag.getList("SpawnPotentials", 10);
			this.spawnPotentials = SpawnData.LIST_CODEC
					.parse(NbtOps.INSTANCE, list)
					.resultOrPartial(message -> TwilightForestMod.LOGGER.warn("Death Tome Spawner: Invalid SpawnPotentials list: {}", message))
					.orElseGet(SimpleWeightedRandomList::empty);
		} else {
			this.spawnPotentials = SimpleWeightedRandomList.single(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData());
		}

		if (tag.contains("MinSpawnDelay", 99)) {
			this.minSpawnDelay = tag.getShort("MinSpawnDelay");
			this.maxSpawnDelay = tag.getShort("MaxSpawnDelay");
		}

		if (tag.contains("MaxNearbyEntities", 99)) {
			this.maxNearbyEntities = tag.getShort("MaxNearbyEntities");
			this.requiredPlayerRange = tag.getShort("RequiredPlayerRange");
		}

		if (tag.contains("SpawnRange", 99)) {
			this.spawnRange = tag.getShort("SpawnRange");
		}

		if (tag.contains("SpawnCheckRange", 99)) {
			this.spawnCheckRange = tag.getShort("SpawnCheckRange");
		}
	}

	public CompoundTag save(CompoundTag tag) {
		tag.putShort("Delay", (short) this.spawnDelay);
		tag.putShort("MinSpawnDelay", (short) this.minSpawnDelay);
		tag.putShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
		tag.putShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
		tag.putShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
		tag.putShort("SpawnRange", (short) this.spawnRange);
		tag.putShort("SpawnCheckRange", (short) this.spawnCheckRange);
		if (this.nextSpawnData != null) {
			tag.put("SpawnData", SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, this.nextSpawnData)
					.getOrThrow(message -> new IllegalStateException("Invalid SpawnData: " + message)));
		}

		tag.put("SpawnPotentials", SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).getOrThrow());
		return tag;
	}

	public boolean onEventTriggered(Level level, int id) {
		if (id != 1) {
			return false;
		}

		if (level.isClientSide()) {
			this.spawnDelay = this.minSpawnDelay;
		}

		return true;
	}

	protected void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData data) {
		this.nextSpawnData = data;
	}

	@Nullable
	public SpawnData getNextSpawnData() {
		return this.nextSpawnData;
	}

	private SpawnData getOrCreateNextSpawnData(@Nullable Level level, RandomSource random, BlockPos pos) {
		if (this.nextSpawnData == null) {
			this.setNextSpawnData(level, pos, this.spawnPotentials.getRandom(random).map(WeightedEntry.Wrapper::data).orElseGet(SpawnData::new));
		}
		return this.nextSpawnData;
	}

	public abstract void broadcastEvent(Level level, BlockPos pos, int id);

	public boolean attemptSpawnTome(int slot, ServerLevel level, BlockPos pos, boolean fire, @Nullable LivingEntity assailant, int maxTries) {
		RandomSource random = level.getRandom();
		SpawnData data = this.getOrCreateNextSpawnData(level, random, pos);
		CompoundTag tag = data.entityToSpawn();
		BlockState shelf = level.getBlockState(pos);
		Direction facing = shelf.getValue(HorizontalDirectionalBlock.FACING);
		Optional<EntityType<?>> optional = EntityType.by(tag);

		if (optional.isEmpty() || !level.getBlockState(pos.relative(facing)).canBeReplaced()) {
			this.delay(level, pos);
			return false;
		}

		double x = pos.relative(facing).getX() + (random.nextDouble() - random.nextDouble()) * 2.0D;
		double y = (double) pos.getY() + (random.nextDouble() - random.nextDouble());
		double z = pos.relative(facing).getZ() + (random.nextDouble() - random.nextDouble()) * 2.0D;

		if (level.noCollision(optional.get().getSpawnAABB(x, y, z))) {
			boolean difficultyPreventsSpawn = !optional.get().getCategory().isFriendly() && level.getDifficulty() == Difficulty.PEACEFUL;
			BlockPos blockPos = BlockPos.containing(x, y, z);

			if (data.getCustomSpawnRules().isPresent()) {
				if (difficultyPreventsSpawn) {
					return false;
				}

				SpawnData.CustomSpawnRules rules = data.getCustomSpawnRules().get();
				if (!rules.isValidPosition(blockPos, level) && !fire) {
					return false;
				}
			} else if (difficultyPreventsSpawn) {
				this.delay(level, pos);
				return false;
			}

			Entity entity = EntityType.loadEntityRecursive(tag, level, processed -> {
				processed.moveTo(x, y, z, processed.getYRot(), processed.getXRot());
				if (fire) {
					processed.setRemainingFireTicks(200);
				}

				if (assailant != null && processed instanceof Mob mob) {
					mob.setTarget(assailant);
				}

				return processed;
			});
			if (entity == null) {
				this.delay(level, pos);
				return false;
			}

			int nearby = level.getEntities(EntityTypeTest.forExactClass(entity.getClass()), new AABB(pos).inflate(this.spawnCheckRange), EntitySelector.NO_SPECTATORS).size();
			if (nearby >= this.maxNearbyEntities && !fire) {
				this.delay(level, pos);
				return false;
			}

			entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), random.nextFloat() * 360.0F, 0.0F);
			if (entity instanceof Mob mob) {
				boolean onlyEntityId = data.getEntityToSpawn().size() == 1 && data.getEntityToSpawn().contains("id", 8);
				if (onlyEntityId) {
					mob.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null);
				}
				data.getEquipment().ifPresent(mob::equip);
			}

			if (!level.tryAddFreshEntityWithPassengers(entity)) {
				this.delay(level, pos);
				return false;
			}

			level.gameEvent(entity, GameEvent.ENTITY_PLACE, blockPos);
			if (entity instanceof Mob mob) {
				mob.spawnAnim();
			}

			if (level.getBlockEntity(pos) instanceof ChiseledCanopyShelfBlockEntity blockEntity) {
				blockEntity.setItem(slot, ItemStack.EMPTY);
			}
			return true;
		}

		if (maxTries != 0) {
			return this.attemptSpawnTome(slot, level, pos, fire, assailant, maxTries - 1);
		}
		return false;
	}
}
