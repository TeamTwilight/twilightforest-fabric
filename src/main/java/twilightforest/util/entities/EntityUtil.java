package twilightforest.util.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFSounds;
import twilightforest.util.features.FeaturePlacers;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class EntityUtil {
    private EntityUtil() {
    }

    public static <T extends Mob> void spawnEntity(EntityType<T> entityType, ServerLevelAccessor level, BlockPos pos) {
        FeaturePlacers.placeEntity(entityType, pos, level);
    }

    public static <T extends Entity> T createEntityIgnoreException(ServerLevelAccessor levelAccessor, EntityType<T> type) {
        try {
            return type.create(levelAccessor.getLevel());
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    /**
     * P5.e: drop-target location for {@code BaseTFBoss.postRemoval} celebratory chest.
     * Upstream picks the boss's home-anchor pos (or current entity pos if no anchor).
     */
    public static BlockPos bossChestLocation(Entity boss) {
        if (boss instanceof twilightforest.entity.EnforcedHomePoint home && home.isRestrictionPointValid(boss.level().dimension())) {
            return home.getRestrictionPoint().pos().below();
        }
        return boss.blockPosition();
    }

    public static void killLavaAround(Entity entity) {
        AABB bounds = entity.getBoundingBox().inflate(9.0D);
        BlockPos min = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos max = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
        if (!entity.level().hasChunksAt(min, max)) {
            return;
        }
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            BlockState state = entity.level().getBlockState(pos);
            if (state.is(Blocks.LAVA)) {
                entity.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    private static final MethodHandle LIVING_ENTITY_GET_DEATH_SOUND = findLivingEntityDeathSound();
    private static final MethodHandle MOB_GET_EQUIPMENT_DROP_CHANCE = findMobEquipmentDropChance();

    private static MethodHandle findLivingEntityDeathSound() {
        try {
            Method method = LivingEntity.class.getDeclaredMethod("getDeathSound");
            method.setAccessible(true);
            return MethodHandles.lookup().unreflect(method);
        } catch (ReflectiveOperationException | SecurityException ignored) {
            return null;
        }
    }

    public static SoundEvent getDeathSound(LivingEntity entity) {
        if (LIVING_ENTITY_GET_DEATH_SOUND != null) {
            try {
                return (SoundEvent) LIVING_ENTITY_GET_DEATH_SOUND.invoke(entity);
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    private static MethodHandle findMobEquipmentDropChance() {
        try {
            Method method = Mob.class.getDeclaredMethod("getEquipmentDropChance", EquipmentSlot.class);
            method.setAccessible(true);
            return MethodHandles.lookup().unreflect(method);
        } catch (ReflectiveOperationException | SecurityException ignored) {
            return null;
        }
    }

    private static float getEquipmentDropChance(Mob mob, EquipmentSlot slot) {
        if (MOB_GET_EQUIPMENT_DROP_CHANCE != null) {
            try {
                return (float) MOB_GET_EQUIPMENT_DROP_CHANCE.invoke(mob, slot);
            } catch (Throwable ignored) {
            }
        }
        return 0.085F;
    }

    public static boolean tryHangPainting(WorldGenLevel world, BlockPos pos, Direction direction, Holder<PaintingVariant> chosenPainting) {
        if (chosenPainting == null) {
            return false;
        }

        Painting painting = new Painting(world.getLevel(), pos, direction, chosenPainting);
        if (world.noCollision(painting, painting.getBoundingBox())) {
            world.addFreshEntity(painting);
            return true;
        }
        return false;
    }

    public static List<Holder<PaintingVariant>> getPaintingsOfSizeOrSmaller(WorldGenLevel level, TagKey<PaintingVariant> paintings, int width, int height) {
        List<Holder<PaintingVariant>> valid = new ArrayList<>();

        for (Holder<PaintingVariant> art : level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(paintings)) {
            if (art.value().width() <= width && art.value().height() <= height) {
                valid.add(art);
            }
        }

        return valid;
    }

    public static boolean canDestroyBlock(Level level, BlockPos pos, Entity entity) {
        return level.getWorldBorder().isWithinBounds(pos) && entity.mayInteract(level, pos);
    }

    public static Holder<PaintingVariant> getPaintingOfSize(WorldGenLevel level, RandomSource random, int minSize) {
        List<Holder<PaintingVariant>> valid = new ArrayList<>();
        level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT).holders().forEach(holder -> {
            if (holder.value().width() >= minSize || holder.value().height() >= minSize) {
                valid.add(holder);
            }
        });
        return valid.isEmpty() ? null : valid.get(random.nextInt(valid.size()));
    }

    /** Ports upstream {@code twilightforest.util.entities.EntityUtil.rayTrace} — basic block-only
     * ray cast from a player's eye position out to a 5-block reach (matches the upstream constant
     * implicitly used at every call site). Used by {@code HedgeBlock} to detect a player swinging
     * at a hedge from a distance. */
    public static BlockHitResult rayTrace(Player player) {
        return rayTrace(player, range -> range);
    }

    /** Overload that allows the caller to adjust the default 5-block reach (used by
     * {@code StrongholdShieldBlock} to extend the ray a tick further before deciding whether the
     * front-face is being hit). */
    public static BlockHitResult rayTrace(Player player, java.util.function.DoubleUnaryOperator rangeAdjuster) {
        double reach = rangeAdjuster.applyAsDouble(5.0D);
        Vec3 eye = player.getEyePosition(1.0F);
        Vec3 look = player.getViewVector(1.0F);
        Vec3 end = eye.add(look.x * reach, look.y * reach, look.z * reach);
        return player.level().clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }

    public static List<Entity> getEntitiesInAABB(WorldGenLevel world, AABB boundingBox) {
        List<Entity> entities = new ArrayList<>();
        int minChunkX = Mth.floor((boundingBox.minX - 2) / 16.0D);
        int maxChunkX = Mth.floor((boundingBox.maxX + 2) / 16.0D);
        int minChunkZ = Mth.floor((boundingBox.minZ - 2) / 16.0D);
        int maxChunkZ = Mth.floor((boundingBox.maxZ + 2) / 16.0D);

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                ChunkAccess chunk = world.getChunk(chunkX, chunkZ, ChunkStatus.STRUCTURE_STARTS);
                if (chunk instanceof ProtoChunk protoChunk) {
                    protoChunk.getEntities().forEach(tag -> {
                        Entity entity = EntityType.loadEntityRecursive(tag, world.getLevel(), value -> value);
                        if (entity != null && boundingBox.intersects(entity.getBoundingBox())) {
                            entities.add(entity);
                        }
                    });
                }
            }
        }

        return entities;
    }

    @SuppressWarnings("unchecked")
    public static boolean convertEntity(LivingEntity oldEntity, EntityType<?> newType) {
        if (!(oldEntity.level() instanceof ServerLevel level)) {
            return false;
        }

        Entity newEntity = newType.create(level);
        if (!(newEntity instanceof LivingEntity)) {
            return false;
        }

        List<Entity> passengerSave = oldEntity.getPassengers();
        if (oldEntity instanceof Mob mob && newEntity instanceof Mob newMob) {
            newEntity = mob.convertTo((EntityType<? extends Mob>) newMob.getType(), true);
            if (newEntity == null) {
                return false;
            }
        } else {
            newEntity.copyPosition(oldEntity);

            if (newEntity instanceof Mob mob) {
                if (oldEntity instanceof Mob oldMob) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        ItemStack stack = oldEntity.getItemBySlot(slot).copyAndClear();
                        if (!stack.isEmpty()) {
                            mob.setItemSlot(slot, stack.copyAndClear());
                            mob.setDropChance(slot, getEquipmentDropChance(oldMob, slot));
                        }
                    }
                }

                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(oldEntity.blockPosition()), MobSpawnType.CONVERSION, null);
            }

            oldEntity.level().addFreshEntity(newEntity);
            oldEntity.discard();
        }

        try {
            java.util.UUID uuid = newEntity.getUUID();
            newEntity.load(oldEntity.saveWithoutId(newEntity.saveWithoutId(new CompoundTag())));
            newEntity.setUUID(uuid);
            if (newEntity instanceof LivingEntity living) {
                living.setHealth(living.getMaxHealth());
            }
        } catch (Exception e) {
            TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
        }

        if (oldEntity instanceof Saddleable saddleable && saddleable.isSaddled() && !(newEntity instanceof Saddleable)) {
            newEntity.spawnAtLocation(Items.SADDLE);
        }

        if (newEntity instanceof Mob mob) {
            mob.spawnAnim();
            mob.spawnAnim();

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = mob.getItemBySlot(slot).copyAndClear();
                mob.spawnAtLocation(stack);
            }
        }

        if (!passengerSave.isEmpty()) {
            for (Entity entity : passengerSave) {
                entity.startRiding(newEntity, true);
            }
        }

        level.playSound(null, newEntity.blockPosition(), TFSounds.TRANSFORMATION_POWDER_USE, newEntity.getSoundSource());
        return true;
    }
}
