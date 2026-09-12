package twilightforest.init;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import twilightforest.TFCommon;
import twilightforest.components.entity.*;
import twilightforest.util.Codecs;

import static com.mojang.serialization.MapCodec.unitCodec;

public class TFDataAttachments {

	public static final AttachmentType<Boolean> FEATHER_FAN = AttachmentRegistry.create(TFCommon.prefix("feather_fan_falling"), b -> b.initializer(() -> false).persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()));
	public static final AttachmentType<PotionFlaskTrackingAttachment> FLASK_DOSES = AttachmentRegistry.create(TFCommon.prefix("flask_doses"), b -> b.initializer(PotionFlaskTrackingAttachment::new).persistent(PotionFlaskTrackingAttachment.CODEC.codec()));
	public static final AttachmentType<FortificationShieldAttachment> FORTIFICATION_SHIELDS = AttachmentRegistry.create(TFCommon.prefix("fortification_shields"), b -> b.initializer(FortificationShieldAttachment::new).persistent(FortificationShieldAttachment.CODEC.codec()).syncWith(FortificationShieldAttachment.STREAM_CODEC, AttachmentSyncPredicate.all()));
	public static final AttachmentType<GiantPickaxeMiningAttachment> GIANT_PICKAXE_MINING = AttachmentRegistry.create(TFCommon.prefix("giant_pickaxe_mining"), b -> b.initializer(GiantPickaxeMiningAttachment::new));
	public static final AttachmentType<YetiThrowAttachment> YETI_THROWING = AttachmentRegistry.create(TFCommon.prefix("yeti_throwing"), b -> b.initializer(YetiThrowAttachment::new));
	public static final AttachmentType<MultiplayerInclusivityAttachment> MULTIPLAYER_FIGHT = AttachmentRegistry.create(TFCommon.prefix("multiplayer_fight"), b -> b.initializer(MultiplayerInclusivityAttachment::new));
	public static final AttachmentType<TFPortalAttachment> TF_PORTAL_COOLDOWN = AttachmentRegistry.create(TFCommon.prefix("tf_portal_cooldown"), b -> b.initializer(TFPortalAttachment::new));
	public static final AttachmentType<SmashBlocksEnchantmentAttachment> SMASH_BLOCKS = AttachmentRegistry.create(TFCommon.prefix("smash_blocks"), b -> b.initializer(SmashBlocksEnchantmentAttachment::new).persistent(SmashBlocksEnchantmentAttachment.CODEC.codec()));
	public static final AttachmentType<GameProfile> ZOMBIFIED_PLAYER = AttachmentRegistry.create(TFCommon.prefix("zombified_player"), b -> b.initializer(() -> UUIDUtil.createOfflineProfile("GizmoTheMoonPig")).persistent(Codecs.SIMPLE_GAME_PROFILE.codec()));
	public static final AttachmentType<Unit> LEASH_PATHFINDER_OVERRIDE = AttachmentRegistry.create(TFCommon.prefix("leashed_pathfinder_override"), b -> b.initializer(() -> Unit.INSTANCE).persistent(unitCodec(Unit.INSTANCE)));
	public static final AttachmentType<Unit> BANISHED_TO_TWILIGHT_FOREST = AttachmentRegistry.create(TFCommon.prefix("twilightforest_banished"), b -> b.initializer(() -> Unit.INSTANCE).persistent(unitCodec(Unit.INSTANCE)).copyOnDeath());
	public static final AttachmentType<TravellersWingsAttachment> TRAVELLERS_WINGS = AttachmentRegistry.create(TFCommon.prefix("travellers_wings"), b -> b.initializer(TravellersWingsAttachment::new));
	public static final AttachmentType<TravellersWingsAnimAttachment> TRAVELLERS_WINGS_ANIM = AttachmentRegistry.create(TFCommon.prefix("travellers_wings_anim"), b -> b.initializer(TravellersWingsAnimAttachment::new));
	public static final AttachmentType<Boolean> IS_USING_GOGGLES_ZOOM_MODIFIER = AttachmentRegistry.create(TFCommon.prefix("is_using_goggles_zoom_modifier"), b -> b.initializer(() -> false).persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()));
	public static final AttachmentType<Boolean> TRAVELLERS_GOGGLES_RED_THREAD_VISION = AttachmentRegistry.create(TFCommon.prefix("travellers_goggles_red_thread_vision"), b -> b.initializer(() -> true).persistent(Codec.BOOL));
	public static final AttachmentType<Long> LAST_TICK_WATER_WALKING = AttachmentRegistry.create(TFCommon.prefix("last_tick_water_walking"), b -> b.initializer(() -> 0L).persistent(Codec.LONG));
	public static final AttachmentType<Boolean> HAS_DOUBLE_JUMP = AttachmentRegistry.create(TFCommon.prefix("has_double_jump"), b -> b.initializer(() -> false).persistent(Codec.BOOL));
	public static final AttachmentType<Integer> DOUBLE_JUMP_VALIDATOR = AttachmentRegistry.create(TFCommon.prefix("double_jump_validator"), b -> b.initializer(() -> 0).persistent(Codec.INT));
	public static final AttachmentType<Integer> DOUBLE_JUMP_VALIDATOR_LAST_CHECK = AttachmentRegistry.create(TFCommon.prefix("double_jump_validator_last_check"), b -> b.initializer(() -> 0).persistent(Codec.INT));
	public static final AttachmentType<Double> TEMPORARY_SAVED_STRAIGHT_AHEAD = AttachmentRegistry.create(TFCommon.prefix("temporary_saved_straight_ahead"), b -> b.initializer(() -> 1D).persistent(Codec.DOUBLE));
	public static final AttachmentType<Long> LAST_DAMAGE_ARMOR_TIME = AttachmentRegistry.create(TFCommon.prefix("last_damage_armor_time"), b -> b.initializer(() -> 0L).persistent(Codec.LONG));
	public static final AttachmentType<Integer> LAST_JUMP_KEY_PRESS_TIME = AttachmentRegistry.create(TFCommon.prefix("last_jump_key_press_time"), b -> b.initializer(() -> 0).persistent(Codec.INT));
	public static final AttachmentType<Float> LAST_HORIZONTAL_IMPULSE = AttachmentRegistry.create(TFCommon.prefix("last_horizontal_impulse"), b -> b.initializer(() -> 0F).persistent(Codec.FLOAT));
	public static final AttachmentType<Float> LAST_NON_ZERO_HORIZONTAL_IMPULSE = AttachmentRegistry.create(TFCommon.prefix("last_non_horizontal_impulse"), b -> b.initializer(() -> 0F).persistent(Codec.FLOAT));
	public static final AttachmentType<Integer> LAST_HORIZONTAL_WALKING_TIME = AttachmentRegistry.create(TFCommon.prefix("last_horizontal_walking_time"), b -> b.initializer(() -> 0).persistent(Codec.INT));
	public static final AttachmentType<Integer> SIDESTEP_VALIDATOR = AttachmentRegistry.create(TFCommon.prefix("sidestep_validator"), b -> b.initializer(() -> 0).persistent(Codec.INT));
	public static final AttachmentType<Integer> SIDESTEP_VALIDATOR_LAST_CHECK = AttachmentRegistry.create(TFCommon.prefix("sidestep_validator_last_check"), b -> b.initializer(() -> 0).persistent(Codec.INT));
	public static final AttachmentType<Boolean> IS_GRADUALLY_GLIDING = AttachmentRegistry.create(TFCommon.prefix("is_gradually_gliding"), b -> b.initializer(() -> false).persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()));
	public static final AttachmentType<SlimySolesAttachment> SLIMY_SOLES_BOUNCE_INFO = AttachmentRegistry.create(TFCommon.prefix("slimy_soles_bounce_info"), b -> b.initializer(SlimySolesAttachment::new).persistent(SlimySolesAttachment.CODEC.codec()));
	public static final AttachmentType<CharmAttachment> CHARM_INVENTORY = AttachmentRegistry.create(TFCommon.prefix("charm_inventory"), b -> b.initializer(CharmAttachment::new).persistent(CharmAttachment.CODEC.codec()).copyOnDeath());

	// Added specifically for Fabric
	public static final AttachmentType<Boolean> ENDER_ARROW = AttachmentRegistry.create(TFCommon.prefix("is_ender_arrow"), b -> b.initializer(() -> false).persistent(Codec.BOOL));
	public static final AttachmentType<CompoundTag> CHARM_DATA = AttachmentRegistry.create(TFCommon.prefix("charm_data"), b -> b.initializer(CompoundTag::new).persistent(CompoundTag.CODEC).copyOnDeath());

	public static void init() {
		TFCommon.LOGGER.info("Initializing attachment types...");
	}
}