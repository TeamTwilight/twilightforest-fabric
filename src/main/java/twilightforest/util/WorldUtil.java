package twilightforest.util;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import twilightforest.client.model.entity.PartEntity;
import twilightforest.extensions.IEntityEx;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Random;

public final class WorldUtil {
	private WorldUtil() {}

	/**
	 * Inclusive of edges
	 */
	public static Iterable<BlockPos> getAllAround(BlockPos center, int range) {
		return BlockPos.betweenClosed(center.offset(-range, -range, -range), center.offset(range, range, range));
	}

	/**
	 * Floors both corners of the bounding box to integers
	 * Inclusive of edges
	 */
	public static Iterable<BlockPos> getAllInBB(AABB bb) {
		return BlockPos.betweenClosed((int) bb.minX, (int) bb.minY, (int) bb.minZ, (int) bb.maxX, (int) bb.maxY, (int) bb.maxZ);
	}

	public static BlockPos randomOffset(Random random, BlockPos pos, int range) {
		return randomOffset(random, pos, range, range, range);
	}

	public static BlockPos randomOffset(Random random, BlockPos pos, int rx, int ry, int rz) {
		int dx = random.nextInt(rx * 2 + 1) - rx;
		int dy = random.nextInt(ry * 2 + 1) - ry;
		int dz = random.nextInt(rz * 2 + 1) - rz;
		return pos.offset(dx, dy, dz);
	}

	@Nullable
	public static ChunkGeneratorTwilight getChunkGenerator(LevelAccessor level) {
		if (level instanceof ServerLevel serverLevel && serverLevel.getChunkSource().getGenerator() instanceof ChunkGeneratorTwilight chunkGenerator)
			return chunkGenerator;

		return null;
	}

	public static int getSeaLevel(Level world) {
		return getSeaLevel(getChunkGenerator(world));
	}

	public static int getSeaLevel(ChunkSource source) {
		return source instanceof ServerChunkCache serverCache ? getSeaLevel(serverCache.getGenerator()) : TFGenerationSettings.SEALEVEL;
	}

	public static int getSeaLevel(ChunkGenerator generator) {
		if (generator instanceof ChunkGeneratorTwilight) {
			return generator.getSeaLevel();
		} else return TFGenerationSettings.SEALEVEL;
	}

	public static void removeEntityComplete(ServerLevel level, Entity entity, boolean keepData) {
		if(((IEntityEx)entity).isMultipartEntity()) {
			for(PartEntity<?> parts : Objects.requireNonNull(((IEntityEx) entity).getParts())) {
				parts.discard();
			}
		}

		level.getChunkSource().removeEntity(entity);
		if (entity instanceof ServerPlayer) {
			ServerPlayer serverplayerentity = (ServerPlayer)entity;
			level.players.remove(serverplayerentity);
		}

		level.getScoreboard().entityRemoved(entity);
		if (entity instanceof Mob) {
			level.navigatingMobs.remove(((Mob)entity).getNavigation());
		}

		entity.discard();
		//net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.EntityLeaveWorldEvent(p_8865_, this));
	}

	public static boolean isAreaLoaded(LevelReader world, BlockPos center, int range) {
		return world.hasChunksAt(center.offset(-range, -range, -range), center.offset(range, range, range));
	}

	// Not much of a world util but its fine here for now
	public static ItemStack getItemStack(JsonObject json, boolean readNBT)
	{
		String itemName = GsonHelper.getAsString(json, "item");

		Item item = Registry.ITEM.get(new ResourceLocation(itemName));

		if (item == null)
			throw new JsonSyntaxException("Unknown item '" + itemName + "'");

		if (readNBT && json.has("nbt"))
		{
			// Lets hope this works? Needs test
			try
			{
				JsonElement element = json.get("nbt");
				CompoundTag nbt;
				if(element.isJsonObject())
					nbt = TagParser.parseTag(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(element));
				else
					nbt = TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));

				CompoundTag tmp = new CompoundTag();

				tmp.put("tag", nbt);
				tmp.putString("id", itemName);
				tmp.putInt("Count", GsonHelper.getAsInt(json, "count", 1));

				return ItemStack.of(tmp);
			}
			catch (CommandSyntaxException e)
			{
				throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
			}
		}

		return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
	}
}
