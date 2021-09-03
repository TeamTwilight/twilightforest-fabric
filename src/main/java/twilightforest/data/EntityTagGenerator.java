//package twilightforest.data;
//
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.tags.EntityTypeTagsProvider;
//import net.minecraft.tags.EntityTypeTags;
//import net.minecraft.tags.Tag;
//import net.minecraft.world.entity.EntityType;
//import net.minecraftforge.common.data.ExistingFileHelper;
//import twilightforest.TwilightForestMod;
//import twilightforest.entity.TFEntities;
//
//import javax.annotation.Nullable;
//
//public class EntityTagGenerator extends EntityTypeTagsProvider {
//    public static final Tag.Named<EntityType<?>> BOSSES = EntityTypeTags.bind(TwilightForestMod.prefix("bosses").toString());
//
//    public EntityTagGenerator(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
//        super(dataGenerator, TwilightForestMod.ID, existingFileHelper);
//    }
//
//    @Override
//    protected void addTags() {
//        tag(EntityTypeTags.SKELETONS).add(TFEntities.skeleton_druid);
//        tag(EntityTypeTags.ARROWS).add(TFEntities.ice_arrow, TFEntities.seeker_arrow);
//        tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.fire_beetle);
//
//        tag(BOSSES).add(
//                TFEntities.naga,
//                TFEntities.lich,
//                TFEntities.minoshroom,
//                TFEntities.hydra,
//                TFEntities.knight_phantom,
//                TFEntities.ur_ghast,
//                TFEntities.yeti_alpha,
//                TFEntities.snow_queen,
//                TFEntities.plateau_boss
//        );
//
//        tag(EntityTypeTags.IMPACT_PROJECTILES).add(
//                TFEntities.nature_bolt,
//                TFEntities.lich_bolt,
//                TFEntities.wand_bolt,
//                TFEntities.lich_bomb,
//                TFEntities.cicada_shot,
//                TFEntities.moonworm_shot,
//                TFEntities.slime_blob,
//                TFEntities.thrown_wep,
//                TFEntities.thrown_ice,
//                TFEntities.falling_ice,
//                TFEntities.ice_snowball
//        );
//
//        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
//                TFEntities.penguin,
//                TFEntities.stable_ice_core,
//                TFEntities.unstable_ice_core,
//                TFEntities.snow_guardian,
//                TFEntities.ice_crystal
//        ).add(
//                TFEntities.raven,
//                TFEntities.squirrel,
//                TFEntities.bunny,
//                TFEntities.tiny_bird,
//                TFEntities.kobold,
//                TFEntities.death_tome,
//                TFEntities.mosquito_swarm,
//                TFEntities.tower_termite
//        );
//
//        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
//                TFEntities.penguin,
//                TFEntities.stable_ice_core,
//                TFEntities.unstable_ice_core,
//                TFEntities.snow_guardian,
//                TFEntities.ice_crystal
//        ).add(
//                TFEntities.wraith,
//                TFEntities.knight_phantom,
//                TFEntities.winter_wolf,
//                TFEntities.yeti,
//                TFEntities.yeti_alpha,
//                TFEntities.snow_queen
//        );
//    }
//}
