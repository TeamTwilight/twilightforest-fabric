package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.init.BiomeKeys;

public class LockedBiomeListener {

	private static boolean shownToast = false;
	private static int timeUntilToast = 60;

	public static void clientTick(Minecraft client) {
		Player player = client.player;
		if (player == null || !(player.level instanceof ClientLevel world))
			return;

		//attempt to send a biome locked toast if our player is in a locked biome, only every 5 ticks
		if(world.isClientSide && player.tickCount % 5 == 0
				&& TFGenerationSettings.isProgressionEnforced(world)
				&& !player.isCreative() && !player.isSpectator() && !TFConfig.CLIENT_CONFIG.disableLockedBiomeToasts.get()) {
			if (!TFGenerationSettings.isBiomeSafeFor(world.getBiome(player.blockPosition()).value(), player)) {
				timeUntilToast--;
				ItemStack item;
				if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.DARK_FOREST))) {
					item = new ItemStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get());
				} else if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.DARK_FOREST_CENTER))) {
					item = new ItemStack(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem());
				} else if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.SNOWY_FOREST))) {
					item = new ItemStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get());
				} else if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.GLACIER))) {
					item = new ItemStack(TFItems.ALPHA_YETI_FUR.get());
				} else if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.SWAMP))) {
					item = new ItemStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get());
				} else if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.FIRE_SWAMP))) {
					item = new ItemStack(TFItems.MEEF_STROGANOFF.get());
				} else if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.HIGHLANDS))) {
					item = new ItemStack(TFBlocks.UBEROUS_SOIL.get());
				} else if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.THORNLANDS))) {
					item = new ItemStack(TFItems.LAMP_OF_CINDERS.get());
				} else if (world.getBiome(player.blockPosition()).value().equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.FINAL_PLATEAU))) {
					item = new ItemStack(TFItems.LAMP_OF_CINDERS.get());
				} else {
					item = new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get());
				}
				if(!shownToast && timeUntilToast <= 0) {
					Minecraft.getInstance().getToasts().addToast(new LockedBiomeToast(item));
					shownToast = true;
				}
			} else {
				if(shownToast) {
					timeUntilToast = 60;
					shownToast = false;
				}
			}
		}
	}
}
