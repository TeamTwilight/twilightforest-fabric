package twilightforest.init;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.components.entity.GiantPickaxeMiningAttachment;
import twilightforest.components.entity.MultiplayerInclusivityAttachment;
import twilightforest.components.entity.PotionFlaskTrackingAttachment;
import twilightforest.components.entity.SmashBlocksEnchantmentAttachment;
import twilightforest.components.entity.SlimySolesAttachment;
import twilightforest.components.entity.TFPortalAttachment;
import twilightforest.components.entity.TravellersWingsAnimAttachment;
import twilightforest.components.entity.TravellersWingsAttachment;
import twilightforest.components.entity.YetiThrowAttachment;

public final class TFDataAttachments {
    public static final AttachmentType<Boolean> FEATHER_FAN = bool("feather_fan_falling", false);
    public static final AttachmentType<Boolean> HAS_DOUBLE_JUMP = bool("has_double_jump", false);
    public static final AttachmentType<Boolean> IS_USING_GOGGLES_ZOOM_MODIFIER = bool("is_using_goggles_zoom_modifier", false);
    public static final AttachmentType<Boolean> TRAVELLERS_GOGGLES_RED_THREAD_VISION = bool("travellers_goggles_red_thread_vision", true);
    public static final AttachmentType<Boolean> IS_GRADUALLY_GLIDING = bool("is_gradually_gliding", false);

    public static final AttachmentType<Long> LAST_TICK_WATER_WALKING = longType("last_tick_water_walking", 0L);
    public static final AttachmentType<Long> LAST_DAMAGE_ARMOR_TIME = longType("last_damage_armor_time", 0L);

    public static final AttachmentType<Integer> DOUBLE_JUMP_VALIDATOR = intType("double_jump_validator", 0);
    public static final AttachmentType<Integer> DOUBLE_JUMP_VALIDATOR_LAST_CHECK = intType("double_jump_validator_last_check", 0);
    public static final AttachmentType<Integer> LAST_JUMP_KEY_PRESS_TIME = intType("last_jump_key_press_time", 0);
    public static final AttachmentType<Integer> LAST_HORIZONTAL_WALKING_TIME = intType("last_horizontal_walking_time", 0);
    public static final AttachmentType<Integer> SIDESTEP_VALIDATOR = intType("sidestep_validator", 0);
    public static final AttachmentType<Integer> SIDESTEP_VALIDATOR_LAST_CHECK = intType("sidestep_validator_last_check", 0);

    public static final AttachmentType<Double> TEMPORARY_SAVED_STRAIGHT_AHEAD = doubleType("temporary_saved_straight_ahead", 1.0D);
    public static final AttachmentType<Float> LAST_HORIZONTAL_IMPULSE = floatType("last_horizontal_impulse", 0.0F);
    public static final AttachmentType<Float> LAST_NON_ZERO_HORIZONTAL_IMPULSE = floatType("last_non_horizontal_impulse", 0.0F);

    public static final AttachmentType<Unit> LEASH_PATHFINDER_OVERRIDE = unit("leashed_pathfinder_override");
    public static final AttachmentType<Unit> BANISHED_TO_TWILIGHT_FOREST = unit("twilightforest_banished");
    public static final AttachmentType<CompoundTag> CHARM_PLAYER_DATA = AttachmentRegistry.create(
        TwilightForestMod.prefix("charm_player_data"),
        builder -> builder.initializer(CompoundTag::new).persistent(CompoundTag.CODEC));
    public static final AttachmentType<TravellersWingsAttachment> TRAVELLERS_WINGS = AttachmentRegistry.createDefaulted(
        TwilightForestMod.prefix("travellers_wings"), TravellersWingsAttachment::new);
    public static final AttachmentType<SlimySolesAttachment> SLIMY_SOLES_BOUNCE_INFO = AttachmentRegistry.create(
        TwilightForestMod.prefix("slimy_soles_bounce_info"),
        builder -> builder.initializer(SlimySolesAttachment::new).persistent(SlimySolesAttachment.CODEC));

    public static final AttachmentType<PotionFlaskTrackingAttachment> FLASK_DOSES = AttachmentRegistry.create(
        TwilightForestMod.prefix("flask_doses"),
        builder -> builder.initializer(PotionFlaskTrackingAttachment::new).persistent(PotionFlaskTrackingAttachment.CODEC));
    public static final AttachmentType<FortificationShieldAttachment> FORTIFICATION_SHIELDS = AttachmentRegistry.create(
        TwilightForestMod.prefix("fortification_shields"),
        builder -> builder.initializer(FortificationShieldAttachment::new).persistent(FortificationShieldAttachment.CODEC));
    public static final AttachmentType<GiantPickaxeMiningAttachment> GIANT_PICKAXE_MINING = AttachmentRegistry.create(
        TwilightForestMod.prefix("giant_pickaxe_mining"),
        builder -> builder.initializer(GiantPickaxeMiningAttachment::new).persistent(GiantPickaxeMiningAttachment.CODEC));
    public static final AttachmentType<YetiThrowAttachment> YETI_THROWING = AttachmentRegistry.createDefaulted(
        TwilightForestMod.prefix("yeti_throwing"), YetiThrowAttachment::new);
    public static final AttachmentType<MultiplayerInclusivityAttachment> MULTIPLAYER_FIGHT = AttachmentRegistry.createDefaulted(
        TwilightForestMod.prefix("multiplayer_fight"), MultiplayerInclusivityAttachment::new);
    public static final AttachmentType<TFPortalAttachment> TF_PORTAL_COOLDOWN = AttachmentRegistry.create(
        TwilightForestMod.prefix("tf_portal_cooldown"),
        builder -> builder.initializer(TFPortalAttachment::new).persistent(TFPortalAttachment.CODEC));
    public static final AttachmentType<SmashBlocksEnchantmentAttachment> SMASH_BLOCKS = AttachmentRegistry.create(
        TwilightForestMod.prefix("smash_blocks"),
        builder -> builder.initializer(SmashBlocksEnchantmentAttachment::new).persistent(SmashBlocksEnchantmentAttachment.CODEC));
    public static final AttachmentType<Object> ZOMBIFIED_PLAYER = AttachmentRegistry.create(TwilightForestMod.prefix("zombified_player"));
    public static final AttachmentType<TravellersWingsAnimAttachment> TRAVELLERS_WINGS_ANIM = AttachmentRegistry.createDefaulted(
        TwilightForestMod.prefix("travellers_wings_anim"), TravellersWingsAnimAttachment::new);

    private TFDataAttachments() {
    }

    public static <T> T get(Entity entity, AttachmentType<T> type) {
        return ((AttachmentTarget) entity).getAttachedOrCreate(type);
    }

    public static <T> void set(Entity entity, AttachmentType<T> type, T value) {
        ((AttachmentTarget) entity).setAttached(type, value);
    }

    private static AttachmentType<Boolean> bool(String path, boolean value) {
        return AttachmentRegistry.create(TwilightForestMod.prefix(path), builder -> builder.initializer(() -> value).persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, (target, player) -> true));
    }

    private static AttachmentType<Integer> intType(String path, int value) {
        return AttachmentRegistry.create(TwilightForestMod.prefix(path), builder -> builder.initializer(() -> value).persistent(Codec.INT));
    }

    private static AttachmentType<Long> longType(String path, long value) {
        return AttachmentRegistry.create(TwilightForestMod.prefix(path), builder -> builder.initializer(() -> value).persistent(Codec.LONG));
    }

    private static AttachmentType<Double> doubleType(String path, double value) {
        return AttachmentRegistry.create(TwilightForestMod.prefix(path), builder -> builder.initializer(() -> value).persistent(Codec.DOUBLE));
    }

    private static AttachmentType<Float> floatType(String path, float value) {
        return AttachmentRegistry.create(TwilightForestMod.prefix(path), builder -> builder.initializer(() -> value).persistent(Codec.FLOAT));
    }

    private static AttachmentType<Unit> unit(String path) {
        return AttachmentRegistry.create(TwilightForestMod.prefix(path), builder -> builder.initializer(() -> Unit.INSTANCE).persistent(Codec.unit(Unit.INSTANCE)));
    }
}
