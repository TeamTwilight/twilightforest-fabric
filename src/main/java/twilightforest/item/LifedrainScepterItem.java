package twilightforest.item;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;
import twilightforest.network.LifedrainParticlePacket;
import twilightforest.network.ParticlePacket;
import twilightforest.util.TFItemStackUtils;
import twilightforest.util.entities.EntityUtil;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LifedrainScepterItem extends CodexItem {
    private static final TagKey<EntityType<?>> COMMON_BOSSES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", "bosses"));

    public LifedrainScepterItem(Properties properties, Item fallback, int cmd) {
        super(properties, fallback, cmd);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getDamageValue() == stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity.tickCount % 20 == 0 && level instanceof ServerLevel serverLevel && stack.has(DataComponents.ENCHANTMENTS) && !isSelected) {
            int renewal = stack.get(DataComponents.ENCHANTMENTS).getLevel(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(TFEnchantments.RENEWAL));
            if (renewal > 0) {
                RechargeScepterEffect.applyRecharge(serverLevel, stack, entity);
            }
        }
    }

    public static void animateTargetShatter(ServerLevel level, Entity target) {
        ParticleOptions options = new ItemParticleOption(ParticleTypes.ITEM, Items.ROTTEN_FLESH.getDefaultInstance());
        boolean big = level.getRandom().nextInt(100) == 0;
        double explosionPower = big ? 1.0D : 0.3D;
        double gaussFactor = 5.0D;
        ParticlePacket packet = new ParticlePacket();

        for (int i = 0; i < 50 + (int) (target.getBbWidth() * (big ? 75 : 25)); ++i) {
            double gaussX = level.getRandom().nextGaussian() * 0.01D;
            double gaussY = level.getRandom().nextGaussian() * 0.01D;
            double gaussZ = level.getRandom().nextGaussian() * 0.01D;
            double speed = level.getRandom().nextFloat() * explosionPower;
            double x = level.getRandom().nextFloat() * target.getBbWidth() * 1.5F - target.getBbWidth() - gaussX * gaussFactor + level.random.nextGaussian() * gaussX;
            double y = level.getRandom().nextFloat() * target.getBbHeight() - gaussY * gaussFactor + level.random.nextGaussian() * gaussY;
            double z = level.getRandom().nextFloat() * target.getBbWidth() * 1.5F - target.getBbWidth() - gaussZ * gaussFactor + level.random.nextGaussian() * gaussZ;

            packet.queueParticle(options, false, target.getX() + x, target.getY() + y, target.getZ() + z, x * speed, y * speed, z * speed);
        }

        for (ServerPlayer viewer : PlayerLookup.tracking(target)) {
            if (ServerPlayNetworking.canSend(viewer, ParticlePacket.TYPE)) {
                ServerPlayNetworking.send(viewer, packet);
            }
        }
    }

    @Nullable
    private Entity getPlayerLookTarget(Level level, LivingEntity living) {
        Entity pointedEntity = null;
        double range = 20.0D;
        Vec3 srcVec = living.getEyePosition();
        Vec3 lookVec = living.getViewVector(1.0F);
        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
        float inflate = 1.0F;
        List<Entity> possibleList = level.getEntities(living, living.getBoundingBox().expandTowards(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range).inflate(inflate, inflate, inflate));
        double hitDist = 0.0D;

        for (Entity possibleEntity : possibleList) {
            if (possibleEntity.isPickable()) {
                float borderSize = possibleEntity.getPickRadius();
                AABB collisionBox = possibleEntity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vec3> interceptPos = collisionBox.clip(srcVec, destVec);

                if (collisionBox.contains(srcVec)) {
                    if (0.0D < hitDist || hitDist == 0.0D) {
                        pointedEntity = possibleEntity;
                        hitDist = 0.0D;
                    }
                } else if (interceptPos.isPresent()) {
                    double possibleDist = srcVec.distanceTo(interceptPos.get());

                    if (possibleDist < hitDist || hitDist == 0.0D) {
                        pointedEntity = possibleEntity;
                        hitDist = possibleDist;
                    }
                }
            }
        }

        return pointedEntity;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        if (stack.getDamageValue() == stack.getMaxDamage()) {
            living.stopUsingItem();
            return;
        }

        if (count % 5 != 0) {
            return;
        }

        Entity pointedEntity = this.getPlayerLookTarget(level, living);
        if (!(pointedEntity instanceof LivingEntity target) || target instanceof ArmorStand) {
            return;
        }

        if (!level.isClientSide() && !target.isDeadOrDying()) {
            sendLifedrainParticles(living, target);
            level.playSound(null, living.blockPosition(), TFSounds.LIFE_SCEPTER_DRAIN, SoundSource.PLAYERS);
        }

        DamageSource damageSource = TFDamageTypes.entitySource(level, TFDamageTypes.LIFEDRAIN, living);
        if (!target.hurt(damageSource, 1.0F)) {
            return;
        }

        if (!level.isClientSide()) {
            handleSuccessfulDrain(level, living, stack, count, target, damageSource);
        }

        if (!level.isClientSide() && target.getHealth() <= living.getHealth()) {
            target.setDeltaMovement(0.0D, 0.15D, 0.0D);
        }
    }

    private static void sendLifedrainParticles(LivingEntity living, LivingEntity target) {
        LifedrainParticlePacket packet = new LifedrainParticlePacket(living.getId(), target.getEyePosition());
        if (living instanceof ServerPlayer self && ServerPlayNetworking.canSend(self, LifedrainParticlePacket.TYPE)) {
            ServerPlayNetworking.send(self, packet);
        }
        for (ServerPlayer viewer : PlayerLookup.tracking(living)) {
            if (ServerPlayNetworking.canSend(viewer, LifedrainParticlePacket.TYPE)) {
                ServerPlayNetworking.send(viewer, packet);
            }
        }
    }

    private static void handleSuccessfulDrain(Level level, LivingEntity living, ItemStack stack, int count, LivingEntity target, DamageSource damageSource) {
        if (target.getHealth() <= 1.0F && !isBoss(target)) {
            handleLifedrainKill(level, living, target, damageSource);
        } else {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
            if (count % 10 == 0) {
                living.heal(1.0F);
                if (living instanceof Player player) {
                    player.getFoodData().eat(1, 0.1F);
                }
            }
        }

        if (living instanceof Player player && !player.getAbilities().instabuild
                && (!player.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN.get()) || level.getRandom().nextFloat() > 0.05F)
                && level instanceof ServerLevel serverLevel) {
            TFItemStackUtils.hurtButDontBreak(stack, 1, serverLevel, player);
        }
    }

    private static void handleLifedrainKill(Level level, LivingEntity living, LivingEntity target, DamageSource damageSource) {
        if (!target.getType().is(EntityTagGenerator.LIFEDRAIN_DROPS_NO_FLESH) && level instanceof ServerLevel serverLevel && living instanceof Player player) {
            LootParams ctx = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, target)
                    .withParameter(LootContextParams.ORIGIN, target.getEyePosition())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                    .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                    .withParameter(LootContextParams.ATTACKING_ENTITY, player)
                    .withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, player)
                    .create(LootContextParamSets.ENTITY);
            serverLevel.getServer().reloadableRegistries().getLootTable(TFLootTables.LIFEDRAIN_SCEPTER_KILL_BONUS).getRandomItems(ctx).forEach(target::spawnAtLocation);
            animateTargetShatter(serverLevel, target);
        }

        if (target instanceof Mob mob) {
            mob.spawnAnim();
        }

        SoundEvent deathSound = EntityUtil.getDeathSound(target);
        if (deathSound != null) {
            level.playSound(null, target.blockPosition(), deathSound, SoundSource.HOSTILE, 1.0F, target.getVoicePitch());
        }

        if (!target.isDeadOrDying()) {
            if (target instanceof Player) {
                target.hurt(TFDamageTypes.entitySource(level, TFDamageTypes.LIFEDRAIN, living), Float.MAX_VALUE);
            } else {
                target.die(TFDamageTypes.entitySource(level, TFDamageTypes.LIFEDRAIN, living));
                target.discard();
            }
        }
    }

    private static boolean isBoss(LivingEntity target) {
        return target.getType().is(EntityTagGenerator.BOSSES) || target.getType().is(COMMON_BOSSES);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() == newStack.getItem();
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, context, tooltip, flags);
        tooltip.add(Component.translatable("item.twilightforest.scepter.desc", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
    }
}
