package twilightforest.loot;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.TwilightForestMod;

import java.util.Set;

public class TFLootTables {
	// For easy testing:
	// /give @p chest{BlockEntityTag:{LootTable:"twilightforest:all_bosses",CustomName:'{"text":"Master Loot Crate"}'}} 1
	private static final Set<ResourceLocation> TF_LOOT_TABLES = Sets.newHashSet();
	private static final int DEFAULT_PLACE_FLAG = 2;
	
	public static final TFLootTables SMALL_HOLLOW_HILL = new TFLootTables("hill_1");
	public static final TFLootTables MEDIUM_HOLLOW_HILL = new TFLootTables("hill_2");
	public static final TFLootTables LARGE_HOLLOW_HILL = new TFLootTables("hill_3");
	public static final TFLootTables HEDGE_MAZE = new TFLootTables("hedge_maze");
	public static final TFLootTables FANCY_WELL = new TFLootTables("fancy_well");
	public static final TFLootTables WELL = new TFLootTables("well");
	public static final TFLootTables LABYRINTH_ROOM = new TFLootTables("labyrinth_room");
	public static final TFLootTables LABYRINTH_DEAD_END = new TFLootTables("labyrinth_dead_end");
	public static final TFLootTables TOWER_ROOM = new TFLootTables("tower_room");
	public static final TFLootTables TOWER_LIBRARY = new TFLootTables("tower_library");
	public static final TFLootTables BASEMENT = new TFLootTables("basement");
	public static final TFLootTables FOUNDATION_BASEMENT = new TFLootTables("foundation_basement");
	public static final TFLootTables LABYRINTH_VAULT = new TFLootTables("labyrinth_vault");
	public static final TFLootTables LABYRINTH_VAULT_JACKPOT = new TFLootTables("labyrinth_vault_jackpot");
	public static final TFLootTables DARKTOWER_CACHE = new TFLootTables("darktower_cache");
	public static final TFLootTables DARKTOWER_KEY = new TFLootTables("darktower_key");
	public static final TFLootTables DARKTOWER_BOSS = new TFLootTables("darktower_boss");
	public static final TFLootTables TREE_CACHE = new TFLootTables("tree_cache");
	public static final TFLootTables STRONGHOLD_CACHE = new TFLootTables("stronghold_cache");
	public static final TFLootTables STRONGHOLD_ROOM = new TFLootTables("stronghold_room");
	public static final TFLootTables STRONGHOLD_BOSS = new TFLootTables("stronghold_boss");
	public static final TFLootTables AURORA_CACHE = new TFLootTables("aurora_cache");
	public static final TFLootTables AURORA_ROOM = new TFLootTables("aurora_room");
//	public static final TFLootTables AURORA_BOSS = new TFLootTables("aurora_boss"); //unused
	public static final TFLootTables TROLL_GARDEN = new TFLootTables("troll_garden");
	public static final TFLootTables TROLL_VAULT = new TFLootTables("troll_vault");
	public static final TFLootTables TROLL_VAULT_WITH_LAMP = new TFLootTables("troll_vault_with_lamp");
	public static final TFLootTables GRAVEYARD = new TFLootTables("graveyard");
	public static final TFLootTables QUEST_GROVE = new TFLootTables("quest_grove_dropper");
	public static final TFLootTables USELESS_LOOT = new TFLootTables("useless");

	public static final ResourceLocation BIGHORN_SHEEP_WHITE = register("entities/bighorn_sheep/white");
	public static final ResourceLocation BIGHORN_SHEEP_ORANGE = register("entities/bighorn_sheep/orange");
	public static final ResourceLocation BIGHORN_SHEEP_MAGENTA = register("entities/bighorn_sheep/magenta");
	public static final ResourceLocation BIGHORN_SHEEP_LIGHT_BLUE = register("entities/bighorn_sheep/light_blue");
	public static final ResourceLocation BIGHORN_SHEEP_YELLOW = register("entities/bighorn_sheep/yellow");
	public static final ResourceLocation BIGHORN_SHEEP_LIME = register("entities/bighorn_sheep/lime");
	public static final ResourceLocation BIGHORN_SHEEP_PINK = register("entities/bighorn_sheep/pink");
	public static final ResourceLocation BIGHORN_SHEEP_GRAY = register("entities/bighorn_sheep/gray");
	public static final ResourceLocation BIGHORN_SHEEP_LIGHT_GRAY = register("entities/bighorn_sheep/light_gray");
	public static final ResourceLocation BIGHORN_SHEEP_CYAN = register("entities/bighorn_sheep/cyan");
	public static final ResourceLocation BIGHORN_SHEEP_PURPLE = register("entities/bighorn_sheep/purple");
	public static final ResourceLocation BIGHORN_SHEEP_BLUE = register("entities/bighorn_sheep/blue");
	public static final ResourceLocation BIGHORN_SHEEP_BROWN = register("entities/bighorn_sheep/brown");
	public static final ResourceLocation BIGHORN_SHEEP_GREEN = register("entities/bighorn_sheep/green");
	public static final ResourceLocation BIGHORN_SHEEP_RED = register("entities/bighorn_sheep/red");
	public static final ResourceLocation BIGHORN_SHEEP_BLACK = register("entities/bighorn_sheep/black");

	public static final ResourceLocation QUESTING_RAM_REWARDS = register("entities/questing_ram_rewards");
	public static final ResourceLocation DEATH_TOME_HURT = register("entities/death_tome_hurt");
	public static final ResourceLocation DEATH_TOME_BOOKS = register("entities/death_tome_books");

	public static final ResourceLocation CICADA_SQUISH_DROPS = register("blocks/cicada_squish");
	public static final ResourceLocation FIREFLY_SQUISH_DROPS = register("blocks/firefly_squish");
	public static final ResourceLocation MOONWORM_SQUISH_DROPS = register("blocks/moonworm_squish");

	public static final ResourceLocation ALL_BOSSES = register("entities/all_bosses");

	public final ResourceLocation lootTable;

	private TFLootTables(String path) {
		this.lootTable = TwilightForestMod.prefix(String.format("structures/%s", path));
	}

	public void generateChest(WorldGenLevel world, BlockPos pos, Direction dir, boolean trapped) {
		this.generateLootContainer(world, pos, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).defaultBlockState().setValue(ChestBlock.FACING, dir), DEFAULT_PLACE_FLAG);
	}

	public void generateLootContainer(WorldGenLevel world, BlockPos pos, BlockState state, int flags) {
		world.setBlock(pos, state, flags);

		this.generateChestContents(world, pos);
	}

	public void generateLootContainer(LevelAccessor world, BlockPos pos, BlockState state, int flags, long seed) {
		world.setBlock(pos, state, flags);

		this.generateChestContents(world, pos, seed);
	}

	public void generateChestContents(WorldGenLevel world, BlockPos pos) {
		this.generateChestContents(world, pos, world.getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
	}

	public void generateChestContents(LevelAccessor world, BlockPos pos, long seed) {
		if (world.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity lootContainer)
			lootContainer.setLootTable(this.lootTable, seed);
	}

	private static ResourceLocation register(String id) {
		return register(TwilightForestMod.prefix(id));
	}

	private static ResourceLocation register(ResourceLocation id) {
		if (TF_LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException(id + " is already a registered built-in loot table");
		}
	}

	public static void entityDropsIntoContainer(LivingEntity entity, LootContext lootContext, BlockState blockContaining, BlockPos placement) {
		if (entity.getLevel() instanceof ServerLevel serverLevel
				&& serverLevel.setBlock(placement, blockContaining, DEFAULT_PLACE_FLAG)
				&& serverLevel.getBlockEntity(placement) instanceof Container container) {
			LootTable table = serverLevel.getServer().getLootTables().get(entity.getLootTable());
			ObjectArrayList<ItemStack> stacks = table.getRandomItems(lootContext);
			table.fill(container, lootContext);
			//if our loot stack size is bigger than the chest, drop everything else outside of it. Dont want to lose any loot now do we?
			if (stacks.size() > 27) {
				for (ItemStack stack : stacks.subList(28, stacks.size())) {
					ItemEntity item = new ItemEntity(serverLevel, placement.above().getX(), placement.above().getY(), placement.above().getZ(), stack);
					item.setExtendedLifetime();
					item.setNoPickUpDelay();
					serverLevel.addFreshEntity(item);
				}
			}
		}
	}
}
