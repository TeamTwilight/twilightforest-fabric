package twilightforest.loot;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.TwilightForestMod;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class TFLootTables {
    private static final Set<ResourceKey<LootTable>> TF_LOOT_TABLES = new HashSet<>();
    private static final Set<ResourceKey<LootTable>> TF_IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(TF_LOOT_TABLES);
    public static final int DEFAULT_PLACE_FLAG = Block.UPDATE_CLIENTS;

    /**
     * P5.e: builder helper for upstream-style boss-loot post-mortem rolls.
     * Returns a {@link net.minecraft.world.level.storage.loot.LootParams.Builder} pre-populated
     * with the boss as both ATTACKER + ORIGIN + (optionally) DAMAGE_SOURCE so
     * {@code IBossLootBuffer.saveDropsIntoBoss} can roll the loot table identically to upstream.
     */
    public static net.minecraft.world.level.storage.loot.LootParams.Builder createLootParams(
            net.minecraft.world.entity.LivingEntity boss,
            boolean wasKilled,
            net.minecraft.world.damagesource.DamageSource damageSource) {
        net.minecraft.world.level.storage.loot.LootParams.Builder builder = new net.minecraft.world.level.storage.loot.LootParams.Builder((net.minecraft.server.level.ServerLevel) boss.level())
                .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY, boss)
                .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, boss.position())
                .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.DAMAGE_SOURCE, damageSource);
        if (damageSource.getEntity() != null) builder.withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ATTACKING_ENTITY, damageSource.getEntity());
        if (damageSource.getDirectEntity() != null) builder.withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());
        if (wasKilled && boss.getKillCredit() instanceof net.minecraft.world.entity.player.Player player) {
            builder.withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
        }
        return builder;
    }

    public static final ResourceKey<LootTable> AURORA_CACHE = register("aurora_cache");
    public static final ResourceKey<LootTable> AURORA_ROOM = register("aurora_room");
    public static final ResourceKey<LootTable> BASEMENT = register("chests/basement");
    public static final ResourceKey<LootTable> CAMP_ARMOR_RACK = register("camp_armor_rack");
    public static final ResourceKey<LootTable> CAMP_DRYING_RACK = register("camp_drying_rack");
    public static final ResourceKey<LootTable> CAMP_POT = register("camp_pot");
    public static final ResourceKey<LootTable> DARKTOWER_CACHE = register("darktower_cache");
    public static final ResourceKey<LootTable> DARKTOWER_KEY = register("darktower_key");
    public static final ResourceKey<LootTable> FALLEN_TRUNK_LOOT = register("fallen_trunk_loot");
    public static final ResourceKey<LootTable> FANCY_WELL = register("fancy_well");
    public static final ResourceKey<LootTable> FOUNDATION_BASEMENT = register("foundation_basement");
    public static final ResourceKey<LootTable> GRAVEYARD = register("graveyard");
    public static final ResourceKey<LootTable> HEDGE_CLOTH = register("hedge_cloth");
    public static final ResourceKey<LootTable> HEDGE_MAZE = register("hedge_maze");
    public static final ResourceKey<LootTable> HUT_JUNK = register("chests/hut_junk");
    public static final ResourceKey<LootTable> LABYRINTH_DEAD_END = register("labyrinth_dead_end");
    public static final ResourceKey<LootTable> LABYRINTH_ROOM = register("labyrinth_room");
    public static final ResourceKey<LootTable> LABYRINTH_VAULT = register("labyrinth_vault");
    public static final ResourceKey<LootTable> LABYRINTH_VAULT_JACKPOT = register("labyrinth_vault_jackpot");
    public static final ResourceKey<LootTable> LARGE_HOLLOW_HILL = register("hill_3");
    public static final ResourceKey<LootTable> LIFEDRAIN_SCEPTER_KILL_BONUS = register("items/lifedrain_scepter_kill_bonus");
    public static final ResourceKey<LootTable> MEDIUM_HOLLOW_HILL = register("hill_2");
    public static final ResourceKey<LootTable> QUEST_GROVE = register("quest_grove_dropper");
    public static final ResourceKey<LootTable> SUSPICIOUS_STEW = register("chests/suspicious_stew");
    public static final ResourceKey<LootTable> JUST_BONES = register("chests/just_bones");
    public static final ResourceKey<LootTable> TOWER_GRAVE_LOWER = register("chests/tower_grave_lower");
    public static final ResourceKey<LootTable> TOWER_GRAVE_UPPER = register("chests/tower_grave_upper");
    public static final ResourceKey<LootTable> CASKET_LOOT = register("chests/casket_loot");
    public static final ResourceKey<LootTable> DARKTOWER_BOSS = register("darktower_boss");
    public static final ResourceKey<LootTable> USELESS_LOOT = register("useless");
    public static final ResourceKey<LootTable> CAMP_TENT = register("camp_tent");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_WHITE = register("entities/bighorn_sheep/white");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_ORANGE = register("entities/bighorn_sheep/orange");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_MAGENTA = register("entities/bighorn_sheep/magenta");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_LIGHT_BLUE = register("entities/bighorn_sheep/light_blue");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_YELLOW = register("entities/bighorn_sheep/yellow");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_LIME = register("entities/bighorn_sheep/lime");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_PINK = register("entities/bighorn_sheep/pink");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_GRAY = register("entities/bighorn_sheep/gray");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_LIGHT_GRAY = register("entities/bighorn_sheep/light_gray");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_CYAN = register("entities/bighorn_sheep/cyan");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_PURPLE = register("entities/bighorn_sheep/purple");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_BLUE = register("entities/bighorn_sheep/blue");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_BROWN = register("entities/bighorn_sheep/brown");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_GREEN = register("entities/bighorn_sheep/green");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_RED = register("entities/bighorn_sheep/red");
    public static final ResourceKey<LootTable> BIGHORN_SHEEP_BLACK = register("entities/bighorn_sheep/black");
    public static final ResourceKey<LootTable> QUESTING_RAM_REWARDS = register("entities/questing_ram_rewards");
    public static final ResourceKey<LootTable> QUESTING_RAM_REWARD_BLOCKS = register("entities/questing_ram_reward_blocks");
    public static final ResourceKey<LootTable> DEATH_TOME_HURT = register("entities/death_tome_hurt");
    public static final ResourceKey<LootTable> DEATH_TOME_BOOKS = register("entities/death_tome_books");
    public static final ResourceKey<LootTable> SMALL_HOLLOW_HILL = register("hill_1");
    public static final ResourceKey<LootTable> STRONGHOLD_CACHE = register("stronghold_cache");
    public static final ResourceKey<LootTable> STRONGHOLD_ROOM = register("stronghold_room");
    public static final ResourceKey<LootTable> TOWER_FOYER = register("chests/tower_foyer");
    public static final ResourceKey<LootTable> TOWER_POTION = register("chests/tower_potion");
    public static final ResourceKey<LootTable> TOWER_ENCHANTING = register("chests/tower_enchanting");
    public static final ResourceKey<LootTable> TOWER_JARS = register("chests/tower_jars");
    public static final ResourceKey<LootTable> TOWER_LIBRARY = register("chests/tower_library");
    public static final ResourceKey<LootTable> TOWER_ROOM = register("chests/tower_room");
    public static final ResourceKey<LootTable> TREE_CACHE = register("tree_cache");
    public static final ResourceKey<LootTable> TROLL_GARDEN = register("troll_garden");
    public static final ResourceKey<LootTable> TROLL_VAULT = register("troll_vault");
    public static final ResourceKey<LootTable> TROLL_VAULT_WITH_LAMP = register("troll_vault_with_lamp");
    public static final ResourceKey<LootTable> WELL = register("well");
    public static final ResourceKey<LootTable> CICADA_SQUISH_DROPS = register("blocks/cicada_squish");
    public static final ResourceKey<LootTable> FIREFLY_SQUISH_DROPS = register("blocks/firefly_squish");
    public static final ResourceKey<LootTable> MOONWORM_FAILED_TO_PLACE_DROPS = register("blocks/moonworm_failed_to_place");
    public static final ResourceKey<LootTable> MOONWORM_SQUISH_DROPS = register("blocks/moonworm_squish");
    public static final ResourceKey<LootTable> KNIGHT_PHANTOM_DEFEATED = register("entities/knight_phantom_defeated");
    public static final ResourceKey<LootTable> BLACKBERRY_BUSH_BERRIES = register("blocks/blackberry_bush_berries");
    public static final ResourceKey<LootTable> BLUEBERRY_BUSH_BERRIES = register("blocks/blueberry_bush_berries");
    public static final ResourceKey<LootTable> RASPBERRY_BUSH_BERRIES = register("blocks/raspberry_bush_berries");
    public static final ResourceKey<LootTable> MALOBERRY_BUSH_BERRIES = register("blocks/maloberry_bush_berries");
    public static final ResourceKey<LootTable> BLIGHTBERRY_BUSH_BERRIES = register("blocks/blightberry_bush_berries");
    public static final ResourceKey<LootTable> DUSKBERRY_BUSH_BERRIES = register("blocks/duskberry_bush_berries");
    public static final ResourceKey<LootTable> SKYBERRY_BUSH_BERRIES = register("blocks/skyberry_bush_berries");
    public static final ResourceKey<LootTable> STINGBERRY_BUSH_BERRIES = register("blocks/stingberry_bush_berries");
    public static final ResourceKey<LootTable> BLACKBERRY_BUSH_DROPS = BLACKBERRY_BUSH_BERRIES;
    public static final ResourceKey<LootTable> BLUEBERRY_BUSH_DROPS = BLUEBERRY_BUSH_BERRIES;
    public static final ResourceKey<LootTable> RASPBERRY_BUSH_DROPS = RASPBERRY_BUSH_BERRIES;
    public static final ResourceKey<LootTable> MALOBERRY_BUSH_DROPS = MALOBERRY_BUSH_BERRIES;
    public static final ResourceKey<LootTable> BLIGHTBERRY_BUSH_DROPS = BLIGHTBERRY_BUSH_BERRIES;
    public static final ResourceKey<LootTable> DUSKBERRY_BUSH_DROPS = DUSKBERRY_BUSH_BERRIES;
    public static final ResourceKey<LootTable> SKYBERRY_BUSH_DROPS = SKYBERRY_BUSH_BERRIES;
    public static final ResourceKey<LootTable> STINGBERRY_BUSH_DROPS = STINGBERRY_BUSH_BERRIES;
    public static final ResourceKey<LootTable> COPPER_OREBERRY_BUSH_DROPS = register("blocks/copper_oreberry_bush_berries");
    public static final ResourceKey<LootTable> IRON_OREBERRY_BUSH_DROPS = register("blocks/iron_oreberry_bush_berries");
    public static final ResourceKey<LootTable> GOLD_OREBERRY_BUSH_DROPS = register("blocks/gold_oreberry_bush_berries");
    public static final ResourceKey<LootTable> ESSENCE_BERRY_BUSH_DROPS = register("blocks/essence_berry_bush_berries");
    public static final ResourceKey<LootTable> OMINOUS_SPAWNER_DROPS = register("blocks/ominous_spawner_drops");

    private TFLootTables() {
    }

    public static void generateChest(WorldGenLevel world, BlockPos pos, Direction dir, boolean trapped, ResourceKey<LootTable> lootTable) {
        generateLootContainer(world, pos, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).defaultBlockState().setValue(ChestBlock.FACING, dir), DEFAULT_PLACE_FLAG, lootTable);
    }

    public static void generateLootContainer(WorldGenLevel world, BlockPos pos, BlockState state, int flags, ResourceKey<LootTable> lootTable) {
        world.setBlock(pos, state, flags);
        generateChestContents(world, pos, lootTable);
    }

    public static void generateChestContents(WorldGenLevel level, BlockPos pos, ResourceKey<LootTable> lootTable) {
        generateChestContents(level, pos, level.getSeed() * pos.getX() + pos.getY() ^ pos.getZ(), lootTable);
    }

    public static void generateChestContents(LevelAccessor level, BlockPos pos, long seed, ResourceKey<LootTable> lootTable) {
        if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity lootContainer) {
            lootContainer.setLootTable(lootTable, seed);
        }
    }

    public static Set<ResourceKey<LootTable>> allBuiltin() {
        return TF_IMMUTABLE_LOCATIONS;
    }

    private static ResourceKey<LootTable> register(String id) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, TwilightForestMod.prefix(id)));
    }

    private static ResourceKey<LootTable> register(ResourceKey<LootTable> id) {
        if (TF_LOOT_TABLES.add(id)) {
            return id;
        }
        throw new IllegalArgumentException(id + " is already a registered built-in loot table");
    }
}
