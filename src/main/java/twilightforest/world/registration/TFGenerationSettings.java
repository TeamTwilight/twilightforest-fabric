package twilightforest.world.registration;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.PlayerHelper;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class TFGenerationSettings /*extends GenerationSettings*/ {

	private static final Map<ResourceLocation, ResourceLocation[]> BIOME_ADVANCEMENTS = new HashMap<>();
	private static final Map<ResourceLocation, BiConsumer<Player, ServerLevel>> BIOME_PROGRESSION_ENFORCEMENT = new HashMap<>();

	static {
		// TODO make this data-driven
		registerBiomeAdvancementRestriction(TFBiomes.DARK_FOREST, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(TFBiomes.DARK_FOREST_CENTER, TwilightForestMod.prefix("progress_knights"));
		registerBiomeAdvancementRestriction(TFBiomes.FINAL_PLATEAU, TwilightForestMod.prefix("progress_troll"));
		registerBiomeAdvancementRestriction(TFBiomes.FIRE_SWAMP, TwilightForestMod.prefix("progress_labyrinth"));
		registerBiomeAdvancementRestriction(TFBiomes.GLACIER, TwilightForestMod.prefix("progress_yeti"));
		registerBiomeAdvancementRestriction(TFBiomes.HIGHLANDS, TwilightForestMod.prefix("progress_merge"));
		registerBiomeAdvancementRestriction(TFBiomes.SNOWY_FOREST, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(TFBiomes.SWAMP, TwilightForestMod.prefix("progress_lich"));
		registerBiomeAdvancementRestriction(TFBiomes.THORNLANDS, TwilightForestMod.prefix("progress_troll"));

		registerBiomeProgressionEnforcement(TFBiomes.DARK_FOREST, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0, false, true));
				StructureHints.tryHintForStructure(player, world, TFStructures.KNIGHT_STRONGHOLD);
			}
		});
		registerBiomeProgressionEnforcement(TFBiomes.DARK_FOREST_CENTER, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0, false, true));
				StructureHints.tryHintForStructure(player, world, TFStructures.DARK_TOWER);
			}
		});
		registerBiomeProgressionEnforcement(TFBiomes.FINAL_PLATEAU, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 1.5F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
				// TODO: change this when there's a book for the castle
				StructureHints.tryHintForStructure(player, world, TFStructures.TROLL_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(TFBiomes.FIRE_SWAMP, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.setSecondsOnFire(8);
			}
			StructureHints.tryHintForStructure(player, world, TFStructures.HYDRA_LAIR);
		});
		registerBiomeProgressionEnforcement(TFBiomes.GLACIER, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), 100, 3, false, true));
			}
			StructureHints.tryHintForStructure(player, world, TFStructures.AURORA_PALACE);
		});
		registerBiomeProgressionEnforcement(TFBiomes.HIGHLANDS, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 0.5F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
				StructureHints.tryHintForStructure(player, world, TFStructures.TROLL_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(TFBiomes.SNOWY_FOREST, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				player.addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), 100, 2, false, true));
				StructureHints.tryHintForStructure(player, world, TFStructures.YETI_CAVE);
			}
		});
		registerBiomeProgressionEnforcement(TFBiomes.SWAMP, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 60 == 0) {
				MobEffectInstance currentHunger = player.getEffect(MobEffects.HUNGER);

				int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + 1 : 1;

				player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, hungerLevel, false, true));

				StructureHints.tryHintForStructure(player, world, TFStructures.LABYRINTH);
			}
		});
		registerBiomeProgressionEnforcement(TFBiomes.THORNLANDS, (player, world) -> {
			if (!world.isClientSide && player.tickCount % 5 == 0) {
				player.hurt(DamageSource.MAGIC, 1.0F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

				// hint monster?
				StructureHints.tryHintForStructure(player, world, TFStructures.TROLL_CAVE);
			}
		});
	}

	private static void registerBiomeAdvancementRestriction(ResourceKey<Biome> biome, ResourceLocation... advancements) {
		BIOME_ADVANCEMENTS.put(biome.location(), advancements);
	}

	private static void registerBiomeProgressionEnforcement(ResourceKey<Biome> biome, BiConsumer<Player, ServerLevel> exec) {
		BIOME_PROGRESSION_ENFORCEMENT.put(biome.location(), exec);
	}

	public static void enforceBiomeProgression(Player player, ServerLevel world) {
		Biome currentBiome = world.getBiome(player.blockPosition()).value();
		if (isBiomeSafeFor(currentBiome, player))
			return;
		BiConsumer<Player, ServerLevel> exec = BIOME_PROGRESSION_ENFORCEMENT.get(world.registryAccess().registryOrThrow(Registries.BIOME).getKey(currentBiome));
		if (exec != null)
			exec.accept(player, world);
	}

	@Deprecated // Used in places where we can't access the sea level FIXME Resolve
	public static final int SEALEVEL = 0;

	public static final ResourceLocation DIMENSION = TwilightForestMod.prefix("twilight_forest");
	public static final ResourceKey<LevelStem> WORLDGEN_KEY = ResourceKey.create(Registries.LEVEL_STEM, DIMENSION);
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION);

	// Checks if the world is linked by the default Twilight Portal.
	// If you want to check if the world is a Twilight world, use usesTwilightChunkGenerator instead
	// Only use this method if you need to know if a world is a destination for portals!
	public static boolean isTwilightPortalDestination(Level world) {
		return DIMENSION.equals(world.dimension().location());
	}

	// Checks if the world is a qualified Twilight world by checking against its namespace or if it's a portal destination
	@Environment(EnvType.CLIENT)
	public static boolean isTwilightWorldOnClient(Level world) {
		return TwilightForestMod.ID.equals(Minecraft.getInstance().level.dimension().location().getNamespace()) || isTwilightPortalDestination(world);
	}

	// Checks if the world is *a* Twilight world on the Server side.
	public static boolean usesTwilightChunkGenerator(ServerLevel world) {
		return world.getChunkSource().getGenerator() instanceof ChunkGeneratorTwilight;
	}

	public static boolean isBiomeSafeFor(Biome biome, Entity entity) {
		ResourceLocation[] advancements = BIOME_ADVANCEMENTS.get(entity.getLevel().registryAccess().registryOrThrow(Registries.BIOME).getKey(biome));
		if (advancements != null && entity instanceof Player)
			return PlayerHelper.doesPlayerHaveRequiredAdvancements((Player) entity, advancements);
		return true;
	}
}
