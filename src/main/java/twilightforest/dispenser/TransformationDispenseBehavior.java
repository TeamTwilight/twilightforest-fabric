package twilightforest.dispenser;

import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public class TransformationDispenseBehavior extends DefaultDispenseItemBehavior {

    boolean fired = false;
    private final Map<EntityType<?>, EntityType<?>> transformMap = new HashMap<>();

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        this.initTransformations();
        Level world = source.getLevel();
        Random random = world.getRandom();
        BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
       if(!world.isClientSide) {
           for (LivingEntity livingentity : world.getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS)) {
               if(transformMap.containsValue(livingentity.getType())) {
                   EntityType<?> type = transformMap.get(livingentity.getType());
                   Entity newEntity = type.create(world);
                   if(type != null && newEntity != null) {
                       newEntity.moveTo(livingentity.getX(), livingentity.getY(), livingentity.getZ(), livingentity.getYRot(), livingentity.getXRot());
                       if (newEntity instanceof Mob && livingentity.level instanceof ServerLevelAccessor) {
                           ServerLevelAccessor sworld = (ServerLevelAccessor) livingentity.level;
                           ((Mob) newEntity).finalizeSpawn(sworld, livingentity.level.getCurrentDifficultyAt(livingentity.blockPosition()), MobSpawnType.CONVERSION, null, null);
                       }

                       try {
                           UUID uuid = newEntity.getUUID();
                           newEntity.load(livingentity.saveWithoutId(newEntity.saveWithoutId(new CompoundTag())));
                           newEntity.setUUID(uuid);
                       } catch (Exception e) {
                           TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
                       }

                       livingentity.level.addFreshEntity(newEntity);
                       livingentity.discard();

                       if (livingentity instanceof Mob) {
                           ((Mob) livingentity).spawnAnim();
                           ((Mob) livingentity).spawnAnim();
                       }
                       livingentity.playSound(TFSounds.POWDER_USE, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
                       stack.shrink(1);
                       fired = true;
                   }
               }
           }
       }
        return stack;
    }

    @Override
    protected void playSound(BlockSource source) {
        if(fired) {
            super.playSound(source);
        } else {
            source.getLevel().levelEvent(1001, source.getPos(), 0);
        }
    }

    public void initTransformations() {
        addTwoWayTransformation(TFEntities.minotaur,       EntityType.ZOMBIFIED_PIGLIN);
        addTwoWayTransformation(TFEntities.deer,           EntityType.COW);
        addTwoWayTransformation(TFEntities.bighorn_sheep,  EntityType.SHEEP);
        addTwoWayTransformation(TFEntities.wild_boar,      EntityType.PIG);
        addTwoWayTransformation(TFEntities.bunny,          EntityType.RABBIT);
        addTwoWayTransformation(TFEntities.tiny_bird,      EntityType.PARROT);
        addTwoWayTransformation(TFEntities.raven,          EntityType.BAT);
        addTwoWayTransformation(TFEntities.hostile_wolf,   EntityType.WOLF);
        addTwoWayTransformation(TFEntities.penguin,        EntityType.CHICKEN);
        addTwoWayTransformation(TFEntities.hedge_spider,   EntityType.SPIDER);
        addTwoWayTransformation(TFEntities.swarm_spider,   EntityType.CAVE_SPIDER);
        addTwoWayTransformation(TFEntities.wraith,         EntityType.BLAZE);
        addTwoWayTransformation(TFEntities.redcap,         EntityType.VILLAGER);
        addTwoWayTransformation(TFEntities.skeleton_druid, EntityType.WITCH);
    }

    private void addTwoWayTransformation(EntityType<?> from, EntityType<?> to) {
        transformMap.put(from, to);
        transformMap.put(to, from);
    }
}
