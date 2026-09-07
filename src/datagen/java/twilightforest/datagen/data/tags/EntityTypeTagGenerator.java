package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.init.TFEntities;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends EntityTypeTagsProvider {

	public EntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		this.tag(EntityTypeTags.SKELETONS).add(TFEntities.SKELETON_DRUID, TFEntities.LICH, TFEntities.KNIGHT_PHANTOM);
		this.tag(EntityTypeTags.ZOMBIES).add(TFEntities.LICH_MINION, TFEntities.LOYAL_ZOMBIE, TFEntities.RISING_ZOMBIE);
		this.tag(EntityTypeTags.ARROWS).add(TFEntities.ICE_ARROW, TFEntities.SEEKER_ARROW);
		this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.FIRE_BEETLE);
		this.tag(EntityTypeTags.FROG_FOOD).add(TFEntities.MAZE_SLIME);

		this.tag(TFEntityTypeTags.BOSSES).add(
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

		this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(
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
			TFEntities.CHAIN_BLOCK
		);

		this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
			TFEntities.PENGUIN,
			TFEntities.STABLE_ICE_CORE,
			TFEntities.UNSTABLE_ICE_CORE,
			TFEntities.SNOW_GUARDIAN,
			TFEntities.ICE_CRYSTAL
		).add(
			TFEntities.RAVEN,
			TFEntities.SQUIRREL,
			TFEntities.DWARF_RABBIT,
			TFEntities.TINY_BIRD,
			TFEntities.KOBOLD,
			TFEntities.DEATH_TOME,
			TFEntities.MOSQUITO_SWARM,
			TFEntities.TOWERWOOD_BORER
		);

		this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
			TFEntities.PENGUIN,
			TFEntities.STABLE_ICE_CORE,
			TFEntities.UNSTABLE_ICE_CORE,
			TFEntities.SNOW_GUARDIAN,
			TFEntities.ICE_CRYSTAL
		).add(
			TFEntities.WRAITH,
			TFEntities.KNIGHT_PHANTOM,
			TFEntities.WINTER_WOLF,
			TFEntities.YETI
		).addTag(TFEntityTypeTags.BOSSES);

		this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(
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

		this.tag(TFEntityTypeTags.LICH_POPPABLES)
			.addTag(EntityTypeTags.SKELETONS)
			.add(EntityType.ZOMBIE, EntityType.ENDERMAN, EntityType.SPIDER, EntityType.CREEPER, TFEntities.SWARM_SPIDER)
			.removeTag(ConventionalEntityTypeTags.BOSSES);

		this.tag(TFEntityTypeTags.LIFEDRAIN_DROPS_NO_FLESH).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.FROG_FOOD).add(
			EntityType.BLAZE,
			EntityType.BREEZE,
			EntityType.IRON_GOLEM,
			EntityType.PHANTOM,
			EntityType.SHULKER,
			EntityType.SKELETON_HORSE,
			EntityType.SNOW_GOLEM,
			EntityType.VEX,
			EntityType.WITHER,
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
		this.tag(TFEntityTypeTags.RIDES_OBSTRUCT_SNATCHING).add(TFEntities.PINCH_BEETLE, TFEntities.YETI, TFEntities.ALPHA_YETI);

		this.tag(TFEntityTypeTags.DONT_KILL_BUGS).add(TFEntities.MOONWORM_SHOT);

		this.tag(TFEntityTypeTags.SORTABLE_ENTITIES).add(
			EntityType.CHEST_MINECART,
			EntityType.HOPPER_MINECART,
			EntityType.LLAMA,
			EntityType.TRADER_LLAMA,
			EntityType.DONKEY,
			EntityType.MULE);

		this.tag(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES).add(
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
		this.tag(EntityTypeTags.ARTHROPOD).add(
			TFEntities.CARMINITE_BROODLING,
			TFEntities.FIRE_BEETLE,
			TFEntities.HEDGE_SPIDER,
			TFEntities.HELMET_CRAB,
			TFEntities.KING_SPIDER,
			TFEntities.PINCH_BEETLE,
			TFEntities.SLIME_BEETLE,
			TFEntities.SWARM_SPIDER,
			TFEntities.TOWERWOOD_BORER);
		this.tag(EntityTypeTags.UNDEAD).add(TFEntities.WRAITH);
		this.tag(EntityTypeTags.IMMUNE_TO_OOZING).add(TFEntities.MAZE_SLIME);
		this.tag(EntityTypeTags.IMMUNE_TO_INFESTED).add(TFEntities.TOWERWOOD_BORER);
		this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).add(TFEntities.HYDRA_MORTAR, TFEntities.LICH_BOLT);
		this.tag(TFEntityTypeTags.LICH_DEFLECTS_PHASE_2).add(TFEntities.WAND_BOLT, TFEntities.LICH_BOLT, TFEntities.LICH_BOMB);
	}


	@Override
	public String getName() {
		return "Twilight Forest Entity Tags";
	}
}
