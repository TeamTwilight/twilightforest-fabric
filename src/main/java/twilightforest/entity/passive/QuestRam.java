package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.entity.ai.goal.QuestRamEatWoolGoal;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;

public class QuestRam extends Animal {
    private static final EntityDataAccessor<Integer> DATA_COLOR_FLAGS =
            SynchedEntityData.defineId(QuestRam.class, EntityDataSerializers.INT);
    private static final DyeColor[] COLOR_ORDER = {
            DyeColor.WHITE,
            DyeColor.LIGHT_GRAY,
            DyeColor.GRAY,
            DyeColor.BLACK,
            DyeColor.RED,
            DyeColor.ORANGE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.GREEN,
            DyeColor.LIGHT_BLUE,
            DyeColor.CYAN,
            DyeColor.BLUE,
            DyeColor.PURPLE,
            DyeColor.MAGENTA,
            DyeColor.PINK,
            DyeColor.BROWN
    };

    private boolean rewarded;
    @Nullable
    private GlobalPos homePoint;
    private int randomTickDivider;

    public QuestRam(EntityType<? extends QuestRam> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 70.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_COLOR_FLAGS, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.38D));
        this.goalSelector.addGoal(2, new QuestRamEatWoolGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(ItemTags.WOOL), false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    public boolean isItemTempting(ItemStack stack) {
        if (!stack.is(ItemTags.WOOL)) {
            return false;
        }
        DyeColor color = this.guessColor(stack);
        return color != null && !this.isColorPresent(color);
    }

    @Nullable
    @Override
    public Animal getBreedOffspring(ServerLevel level, AgeableMob mate) {
        return null;
    }

    @Override
    protected void customServerAiStep() {
        if (--this.randomTickDivider <= 0) {
            this.randomTickDivider = 70 + this.getRandom().nextInt(50);
            if (this.countColorsSet() > 15 && !this.getRewarded()) {
                this.rewardQuest();
                this.setRewarded(true);
            }
        }
        super.customServerAiStep();
    }

    private void rewardQuest() {
        if (this.level() instanceof ServerLevel serverLevel) {
            LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.PIGLIN_BARTER);
            serverLevel.getServer().reloadableRegistries().getLootTable(TFLootTables.QUESTING_RAM_REWARDS).getRandomItems(params).forEach(stack -> this.spawnAtLocation(stack, 1.0F));
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.tryAccept(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.interactAt(player, vec, hand);
    }

    public boolean tryAccept(ItemStack stack) {
        if (stack.is(ItemTags.WOOL)) {
            DyeColor color = this.guessColor(stack);
            if (color != null && !this.isColorPresent(color)) {
                this.setColorPresent(color);
                this.animateAddColor(color, 50);
                this.playAmbientSound();
                return true;
            }
        }
        return false;
    }

    @Nullable
    public DyeColor guessColor(ItemStack stack) {
        if (stack.is(ItemTags.WOOL) && stack.getItem() instanceof BlockItem blockItem) {
            MapColor mapColor = blockItem.getBlock().defaultMapColor();
            for (DyeColor dye : DyeColor.values()) {
                if (mapColor == dye.getMapColor()) {
                    return dye;
                }
            }
        }
        return null;
    }

    private void animateAddColor(DyeColor color, int count) {
        int packed = color.getTextureDiffuseColor();
        float red = ((packed >> 16) & 255) / 255.0F;
        float green = ((packed >> 8) & 255) / 255.0F;
        float blue = (packed & 255) / 255.0F;
        DustParticleOptions particle = new DustParticleOptions(new Vector3f(red, green, blue), 1.0F);
        for (int i = 0; i < count; i++) {
            this.level().addParticle(particle, this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 1.5D, this.getY() + this.getRandom().nextDouble() * this.getBbHeight() * 1.5D, this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 1.5D, 0.0D, 0.0D, 0.0D);
        }
    }

    public int getColorFlags() {
        return this.getEntityData().get(DATA_COLOR_FLAGS);
    }

    private void setColorFlags(int flags) {
        this.getEntityData().set(DATA_COLOR_FLAGS, flags);
    }

    public boolean isColorPresent(DyeColor color) {
        return (this.getColorFlags() & (1 << color.getId())) > 0;
    }

    public void setColorPresent(DyeColor color) {
        this.setColorFlags(this.getColorFlags() | (1 << color.getId()));
    }

    public int countColorsSet() {
        return Integer.bitCount(this.getColorFlags());
    }

    public boolean getRewarded() {
        return this.rewarded;
    }

    public void setRewarded(boolean rewarded) {
        this.rewarded = rewarded;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public float getVoicePitch() {
        return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.7F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.QUEST_RAM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.QUEST_RAM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.QUEST_RAM_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(TFSounds.QUEST_RAM_STEP, 0.15F, 1.0F);
    }

    public @Nullable GlobalPos getRestrictionPoint() {
        return this.homePoint;
    }

    public void setRestrictionPoint(@Nullable GlobalPos pos) {
        this.homePoint = pos;
    }

    public int getHomeRadius() {
        return 13;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ColorFlags", this.getColorFlags());
        tag.putBoolean("Rewarded", this.getRewarded());
        GlobalPos home = this.getRestrictionPoint();
        if (home != null) {
            tag.putInt("HomeX", home.pos().getX());
            tag.putInt("HomeY", home.pos().getY());
            tag.putInt("HomeZ", home.pos().getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setColorFlags(tag.getInt("ColorFlags"));
        this.setRewarded(tag.getBoolean("Rewarded"));
        if (tag.contains("HomeX")) {
            this.setRestrictionPoint(GlobalPos.of(this.level().dimension(), new BlockPos(tag.getInt("HomeX"), tag.getInt("HomeY"), tag.getInt("HomeZ"))));
        }
    }
}
