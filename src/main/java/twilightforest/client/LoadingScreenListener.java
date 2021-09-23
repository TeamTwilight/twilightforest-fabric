package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

@Environment(EnvType.CLIENT)
public class LoadingScreenListener {

	private static final Minecraft client = Minecraft.getInstance();
	public static void onOpenGui(Screen event) {
		if (event instanceof ReceivingLevelScreen && client.player != null) {
			ResourceKey<Level> tfDimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TwilightForestMod.COMMON_CONFIG.dimension.portalDestinationID));
			if (client.player.getCommandSenderWorld().getBlockState(client.player.blockPosition().below()) == TFBlocks.twilight_portal.defaultBlockState() || client.player.getCommandSenderWorld().dimension() == tfDimension) {
				LoadingScreenGui guiLoading = new LoadingScreenGui();
				guiLoading.setEntering(client.player.getCommandSenderWorld().dimension() == Level.OVERWORLD);
				Minecraft.getInstance().setScreen(guiLoading);
				//event.setGui(guiLoading);
			}
		}
	}
}
