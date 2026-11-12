package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.init.TFEntities;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends TagsProvider<EntityType<?>> {

	public EntityTypeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.ENTITY_TYPE, provider);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(EntityTypeTags.SKELETONS).twilightforest$add(TFEntities.SKELETON_DRUID, TFEntities.LICH, TFEntities.KNIGHT_PHANTOM);
		this.tag(EntityTypeTags.ZOMBIES).twilightforest$add(TFEntities.LICH_MINION, TFEntities.LOYAL_ZOMBIE, TFEntities.RISING_ZOMBIE);
		this.tag(EntityTypeTags.UNDEAD).twilightforest$add(TFEntities.WRAITH);
		this.tag(EntityTypeTags.BURN_IN_DAYLIGHT).twilightforest$add(TFEntities.SKELETON_DRUID);
		this.tag(EntityTypeTags.ARROWS).twilightforest$add(TFEntities.ICE_ARROW, TFEntities.SEEKER_ARROW);

		this.tag(EntityTypeTags.IMPACT_PROJECTILES).twilightforest$add(
			TFEntities.NATURE_BOLT,
			TFEntities.LICH_BOLT,
			TFEntities.WAND_BOLT,
			TFEntities.LICH_BOMB,
			TFEntities.MOONWORM_SHOT,
			TFEntities.SLIME_BLOB,
			TFEntities.THROWN_WEP,
			TFEntities.THROWN_ICE,
			TFEntities.FALLING_ICE,
			TFEntities.ICE_SNOWBALL,
			TFEntities.CHAIN_BLOCK);

		this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).twilightforest$add(
			TFEntities.PENGUIN,
			TFEntities.STABLE_ICE_CORE,
			TFEntities.UNSTABLE_ICE_CORE,
			TFEntities.SNOW_GUARDIAN,
			TFEntities.ICE_CRYSTAL,
			TFEntities.RAVEN,
			TFEntities.SQUIRREL,
			TFEntities.DWARF_RABBIT,
			TFEntities.TINY_BIRD,
			TFEntities.KOBOLD,
			TFEntities.DEATH_TOME,
			TFEntities.MOSQUITO_SWARM,
			TFEntities.TOWERWOOD_BORER);

		this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).twilightforest$add(
			TFEntities.PENGUIN,
			TFEntities.STABLE_ICE_CORE,
			TFEntities.UNSTABLE_ICE_CORE,
			TFEntities.SNOW_GUARDIAN,
			TFEntities.ICE_CRYSTAL,
			TFEntities.WRAITH,
			TFEntities.KNIGHT_PHANTOM,
			TFEntities.WINTER_WOLF,
			TFEntities.YETI
		).addTag(TFEntityTypeTags.BOSSES);

		this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).twilightforest$add(TFEntities.FIRE_BEETLE);
		this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER).addTag(TFEntityTypeTags.BOSSES);
		this.tag(EntityTypeTags.FROG_FOOD).twilightforest$add(TFEntities.MAZE_SLIME);
		this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).twilightforest$add(
			TFEntities.NAGA,
			TFEntities.SQUIRREL,
			TFEntities.WRAITH,
			TFEntities.CARMINITE_GOLEM,
			TFEntities.DEATH_TOME,
			TFEntities.UR_GHAST,
			TFEntities.CARMINITE_GHASTLING,
			TFEntities.KNIGHT_PHANTOM,
			TFEntities.SNOW_QUEEN,
			TFEntities.PENGUIN,
			TFEntities.RAVEN,
			TFEntities.SNOW_GUARDIAN,
			TFEntities.STABLE_ICE_CORE,
			TFEntities.MOSQUITO_SWARM,
			TFEntities.UNSTABLE_ICE_CORE,
			TFEntities.ICE_CRYSTAL,
			TFEntities.CARMINITE_GHASTGUARD,
			TFEntities.TINY_BIRD);

		this.tag(EntityTypeTags.NON_CONTROLLING_RIDER).twilightforest$add(TFEntities.MAZE_SLIME);
		this.tag(EntityTypeTags.ARTHROPOD).twilightforest$add(
			TFEntities.CARMINITE_BROODLING,
			TFEntities.FIRE_BEETLE,
			TFEntities.HEDGE_SPIDER,
			TFEntities.HELMET_CRAB,
			TFEntities.KING_SPIDER,
			TFEntities.PINCH_BEETLE,
			TFEntities.SLIME_BEETLE,
			TFEntities.SWARM_SPIDER,
			TFEntities.TOWERWOOD_BORER,
			TFEntities.MOSQUITO_SWARM);

		this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).twilightforest$add(TFEntities.HYDRA_MORTAR, TFEntities.LICH_BOLT);
		this.tag(EntityTypeTags.IMMUNE_TO_INFESTED).twilightforest$add(TFEntities.TOWERWOOD_BORER);
		this.tag(EntityTypeTags.IMMUNE_TO_OOZING).twilightforest$add(TFEntities.MAZE_SLIME);
		this.tag(EntityTypeTags.BOAT).twilightforest$add(
			TFEntities.TWILIGHT_OAK_BOAT,
			TFEntities.CANOPY_BOAT,
			TFEntities.MANGROVE_BOAT,
			TFEntities.DARK_BOAT,
			TFEntities.TIME_BOAT,
			TFEntities.TRANSFORMATION_BOAT,
			TFEntities.MINING_BOAT,
			TFEntities.SORTING_BOAT);

		this.tag(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS).twilightforest$add(
			TFEntities.DEER,
			TFEntities.TINY_BIRD,
			TFEntities.RAVEN,
			TFEntities.BOAR,
			TFEntities.BIGHORN_SHEEP,
			TFEntities.DWARF_RABBIT,
			TFEntities.PENGUIN,
			TFEntities.SQUIRREL
		);

		this.tag(EntityTypeTags.CANNOT_BE_PUSHED_ONTO_BOATS).twilightforest$add(
			TFEntities.ADHERENT,
			TFEntities.CARMINITE_GHASTLING,
			TFEntities.CARMINITE_GHASTGUARD,
			TFEntities.DEATH_TOME,
			TFEntities.MOSQUITO_SWARM,
			TFEntities.QUEST_RAM,
			TFEntities.RISING_ZOMBIE,
			TFEntities.SNOW_GUARDIAN,
			TFEntities.WRAITH
		).addTag(TFEntityTypeTags.BOSSES);

		this.tag(TFEntityTypeTags.BOSSES).twilightforest$add(
			TFEntities.NAGA,
			TFEntities.LICH,
			TFEntities.MINOSHROOM,
			TFEntities.HYDRA,
			TFEntities.KNIGHT_PHANTOM,
			TFEntities.UR_GHAST,
			TFEntities.ALPHA_YETI,
			TFEntities.SNOW_QUEEN,
			TFEntities.PLATEAU_BOSS
		);

		this.tag(TFEntityTypeTags.LICH_POPPABLES)
			.addTag(EntityTypeTags.SKELETONS)
			.twilightforest$add(EntityTypes.ZOMBIE, EntityTypes.ENDERMAN, EntityTypes.SPIDER, EntityTypes.CREEPER, TFEntities.SWARM_SPIDER)
			.removeTag(ConventionalEntityTypeTags.BOSSES);

		this.tag(TFEntityTypeTags.LIFEDRAIN_DROPS_NO_FLESH).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.FROG_FOOD).twilightforest$add(
			EntityTypes.BLAZE,
			EntityTypes.BREEZE,
			EntityTypes.IRON_GOLEM,
			EntityTypes.PHANTOM,
			EntityTypes.SHULKER,
			EntityTypes.SKELETON_HORSE,
			EntityTypes.SNOW_GOLEM,
			EntityTypes.VEX,
			EntityTypes.WITHER,
			TFEntities.CARMINITE_GOLEM,
			TFEntities.DEATH_TOME,
			TFEntities.ICE_CRYSTAL,
			TFEntities.KNIGHT_PHANTOM,
			TFEntities.LICH,
			TFEntities.MOSQUITO_SWARM,
			TFEntities.SNOW_GUARDIAN,
			TFEntities.STABLE_ICE_CORE,
			TFEntities.UNSTABLE_ICE_CORE,
			TFEntities.WRAITH);

		// These entities forcefully take players from the entity they're riding
		this.tag(TFEntityTypeTags.RIDES_OBSTRUCT_SNATCHING).twilightforest$add(TFEntities.PINCH_BEETLE, TFEntities.YETI, TFEntities.ALPHA_YETI);
		this.tag(TFEntityTypeTags.DONT_KILL_BUGS).twilightforest$add(TFEntities.MOONWORM_SHOT);
		this.tag(TFEntityTypeTags.SORTABLE_ENTITIES).twilightforest$add(
			EntityTypes.CHEST_MINECART,
			EntityTypes.HOPPER_MINECART,
			EntityTypes.LLAMA,
			EntityTypes.TRADER_LLAMA,
			EntityTypes.DONKEY,
			EntityTypes.MULE);

		this.tag(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES).twilightforest$add(
			TFEntities.NAGA,
			TFEntities.LICH,
			TFEntities.MINOSHROOM,
			TFEntities.HYDRA,
			TFEntities.UR_GHAST,
			TFEntities.ALPHA_YETI,
			TFEntities.SNOW_QUEEN,
			TFEntities.PLATEAU_BOSS
		);

		this.tag(ConventionalEntityTypeTags.BOSSES).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.LICH_DEFLECTS_PHASE_2).twilightforest$add(TFEntities.WAND_BOLT, TFEntities.LICH_BOLT, TFEntities.LICH_BOMB);

		this.tag(TFEntityTypeTags.HEDGE_DAMAGE_IMMUNE).twilightforest$add(
			EntityTypes.SPIDER,
			EntityTypes.CAVE_SPIDER,
			TFEntities.HEDGE_SPIDER,
			TFEntities.SWARM_SPIDER,
			TFEntities.KING_SPIDER,
			TFEntities.CARMINITE_BROODLING,
			EntityTypes.ITEM
		);

		this.tag(TFEntityTypeTags.PROGRESSION_LOCK_VULNERABLE).twilightforest$add(TFEntities.KOBOLD);

		// Mod Compatibility Tags

		this.tag(TFEntityTypeTags.AC_RESISTS_ACID).twilightforest$add(TFEntities.HYDRA, TFEntities.NAGA);
		this.tag(TFEntityTypeTags.AC_RESISTS_MAGNETS).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.AC_RESISTS_TREMORSAURUS_ROAR).twilightforest$add(TFEntities.HYDRA, TFEntities.UR_GHAST);

		this.tag(TFEntityTypeTags.AETHER_DEFLECTABLE_PROJECTILES).twilightforest$add(
			TFEntities.NATURE_BOLT,
			TFEntities.LICH_BOLT,
			TFEntities.WAND_BOLT,
			TFEntities.SLIME_BLOB,
			TFEntities.ICE_SNOWBALL);

		this.tag(TFEntityTypeTags.AETHER_FIRE_MOB).twilightforest$add(TFEntities.FIRE_BEETLE);
		this.tag(TFEntityTypeTags.AETHER_PIGS).twilightforest$add(TFEntities.BOAR);

		this.tag(TFEntityTypeTags.AN_JAR_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);
		this.tag(TFEntityTypeTags.AN_JAR_RELEASE_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);

		this.tag(TFEntityTypeTags.IE_SHADER_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);
	}


	@Override
	public String getName() {
		return "Twilight Forest Entity Tags";
	}
}
