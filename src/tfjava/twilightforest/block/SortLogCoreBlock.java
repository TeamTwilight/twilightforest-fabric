package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFParticleType;
import twilightforest.util.WorldUtil;

import java.util.*;

public class SortLogCoreBlock extends SpecialMagicLogBlock {
	public SortLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.COMMON_CONFIG.MAGIC_TREES.disableSorting;
	}

	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		Map<List<Container>, Vec3> inputMap = new HashMap<>();
		Map<Container, Vec3> outputMap = new HashMap<>();

		for (BlockPos blockPos : WorldUtil.getAllAround(pos, TFConfig.COMMON_CONFIG.MAGIC_TREES.sortingRange)) {
			if (blockPos.equals(pos)) {
				continue;
			}
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			Container container = blockEntity instanceof Container blockContainer ? blockContainer : null;
			if (container == null) continue;

			boolean nearCore = Math.abs(blockPos.getX() - pos.getX()) <= 2
				&& Math.abs(blockPos.getY() - pos.getY()) <= 2
				&& Math.abs(blockPos.getZ() - pos.getZ()) <= 2;
			if (nearCore) {
				inputMap.put(List.of(container), Vec3.upFromBottomCenterOf(blockPos, 1.9D));
			} else {
				outputMap.put(container, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
			}
		}

		List<Entity> alreadyUsedForInput = new ArrayList<>();
		level.getEntities((Entity) null, new AABB(pos).inflate(2), this::isSortableEntity).forEach(entity -> {
			Container container = getEntityContainer(entity);
			if (container != null) {
				inputMap.put(List.of(container), entity.position().add(0.0D, entity.getBbHeight() + 0.9D, 0.0D));
				alreadyUsedForInput.add(entity);
			}
		});

		if (inputMap.isEmpty()) {
			return;
		}

		level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> this.isSortableEntity(entity) && !alreadyUsedForInput.contains(entity)).forEach(entity -> {
			Container container = getEntityContainer(entity);
			if (container != null) {
				outputMap.put(container, entity.position().add(0.0D, entity.getBbHeight() + 0.9D, 0.0D));
			}
		});

		if (outputMap.isEmpty()) {
			return;
		}

		for (Map.Entry<List<Container>, Vec3> inputContainers : inputMap.entrySet()) {
			boolean transferred = this.transferOneItem(level, rand, inputContainers, outputMap);
			if (transferred) {
				return;
			}
		}
	}

	private boolean transferOneItem(ServerLevel level, RandomSource rand, Map.Entry<List<Container>, Vec3> inputContainers, Map<Container, Vec3> outputMap) {
		for (Container inputContainer : inputContainers.getKey()) {
			for (int inputSlot = 0; inputSlot < inputContainer.getContainerSize(); inputSlot++) {
				ItemStack inputStack = inputContainer.getItem(inputSlot);
				if (inputStack.isEmpty()) {
					continue;
				}

				Map<Integer, Container> outputsByCount = new HashMap<>();
				for (Container outputContainer : outputMap.keySet()) {
					int count = countMatchingItems(outputContainer, inputStack);
					if (count > 0) {
						outputsByCount.put(count, outputContainer);
					}
				}

				for (Integer count : outputsByCount.keySet().stream().sorted(Comparator.reverseOrder()).toList()) {
					Container outputContainer = outputsByCount.get(count);
					int targetSlot = findTargetSlot(outputContainer, inputStack);
					if (targetSlot == -1) {
						continue;
					}

					ItemStack extracted = inputStack.split(1);
					if (extracted.isEmpty()) {
						continue;
					}
					if (!canInsert(outputContainer, targetSlot, extracted)) {
						inputStack.grow(1);
						continue;
					}
					insertInto(outputContainer, targetSlot, extracted);
					inputContainer.setChanged();
					outputContainer.setChanged();
					this.sendSortingParticles(level, rand, inputContainers.getValue(), outputMap.get(outputContainer));
					return true;
				}
			}
		}
		return false;
	}

	private static int countMatchingItems(Container container, ItemStack inputStack) {
		int count = 0;
		for (int slot = 0; slot < container.getContainerSize(); slot++) {
			ItemStack stack = container.getItem(slot);
			if (stack.is(inputStack.getItem())) {
				count += stack.getCount();
			}
		}
		return count;
	}

	private static int findTargetSlot(Container container, ItemStack inputStack) {
		int firstEmpty = -1;
		for (int slot = 0; slot < container.getContainerSize(); slot++) {
			if (!container.canPlaceItem(slot, inputStack)) {
				continue;
			}
			ItemStack outputStack = container.getItem(slot);
			if (firstEmpty == -1 && outputStack.isEmpty()) {
				firstEmpty = slot;
			} else if (ItemStack.isSameItemSameComponents(inputStack, outputStack)
				&& outputStack.getCount() < outputStack.getMaxStackSize()
				&& outputStack.getCount() < container.getMaxStackSize()) {
				return slot;
			}
		}
		return firstEmpty;
	}

	private static boolean canInsert(Container container, int slot, ItemStack stack) {
		if (!container.canPlaceItem(slot, stack)) return false;
		ItemStack existing = container.getItem(slot);
		return existing.isEmpty() || (ItemStack.isSameItemSameComponents(existing, stack)
			&& existing.getCount() < Math.min(existing.getMaxStackSize(), container.getMaxStackSize()));
	}

	private static void insertInto(Container container, int slot, ItemStack stack) {
		ItemStack existing = container.getItem(slot);
		if (existing.isEmpty()) {
			container.setItem(slot, stack);
		} else {
			existing.grow(stack.getCount());
		}
	}

	private void sendSortingParticles(ServerLevel level, RandomSource rand, Vec3 input, Vec3 output) {
		Vec3 diff = input.subtract(output);
		double length = Math.max(diff.length(), 0.001D);
		double x = diff.x - 0.25D + rand.nextDouble() * 0.5D;
		double y = diff.y - 1.75D + rand.nextDouble() * 0.5D;
		double z = diff.z - 0.25D + rand.nextDouble() * 0.5D;
		level.sendParticles(TFParticleType.SORTING_PARTICLE, output.x(), output.y(), output.z(), 1, x / length, y / length, z / length, 0.0D);
	}

	private boolean isSortableEntity(Entity entity) {
		return entity.isAlive() && (entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES) || entity instanceof ContainerEntity || entity instanceof Container);
	}

	private static Container getEntityContainer(Entity entity) {
		if (entity instanceof Container container) {
			return container;
		}
		return null;
	}
}
