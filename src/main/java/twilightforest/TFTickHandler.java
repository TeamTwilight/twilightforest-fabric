package twilightforest;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.data.ItemTagGenerator;
import twilightforest.item.BrittleFlaskItem;
import twilightforest.network.StructureProtectionPacket;
import twilightforest.network.StructureProtectionClearPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.PlayerHelper;
import twilightforest.util.StructureBoundingBoxUtils;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.List;
import java.util.Random;

public class TFTickHandler {


	public static void playerTick(Player player) {
		if (!(player.level instanceof ServerLevel))
			return;

		ServerLevel world = (ServerLevel) player.level;

		// check for portal creation, at least if it's not disabled
		if (!world.isClientSide && !TFConfig.COMMON_CONFIG.disablePortalCreation.get() && player.tickCount % (TFConfig.COMMON_CONFIG.checkPortalDestination.get() ? 100 : 20) == 0) {
			// skip non admin players when the option is on
			if (TFConfig.COMMON_CONFIG.adminOnlyPortals.get()) {
				if (world.getServer().getProfilePermissions(player.getGameProfile()) != 0) {
					// reduce range to 4.0 when the option is on
					checkForPortalCreation(player, world, 4.0F);
				}
			} else {
				// normal check, no special options
				checkForPortalCreation(player, world, 32.0F);
			}
		}

		//tick every second for the advancement bit of the flask, but only if we dont have the advancement
		if(!world.isClientSide && player.tickCount % 20 == 0 && !PlayerHelper.doesPlayerHaveRequiredAdvancements(player, TwilightForestMod.prefix("full_mettle_alchemist"))) {
			BrittleFlaskItem.ticker();
		}

		// check the player for being in a forbidden progression area, only every 20 ticks
		if (!world.isClientSide && player.tickCount % 20 == 0
				&& TFGenerationSettings.isProgressionEnforced(world)
				&& TFGenerationSettings.usesTwilightChunkGenerator(world)
				&& !player.isCreative() && !player.isSpectator()) {

			TFGenerationSettings.enforceBiomeProgression(player, world);
		}

		// check and send nearby forbidden structures, every 100 ticks or so
		if (!world.isClientSide && player.tickCount % 100 == 0 && TFGenerationSettings.isProgressionEnforced(world)) {
			if (TFGenerationSettings.usesTwilightChunkGenerator(world)) {
				if (player.isCreative() || player.isSpectator()) {
					sendAllClearPacket(world, player);
				} else {
					checkForLockedStructuresSendPacket(player, world);
				}
			}
		}
	}

	private static void sendStructureProtectionPacket(Level world, Player player, BoundingBox sbb) {
		if (player instanceof ServerPlayer serverPlayer) {
			TFPacketHandler.CHANNEL.send(serverPlayer, new StructureProtectionPacket(sbb));
		}
	}

	private static void sendAllClearPacket(Level world, Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			TFPacketHandler.CHANNEL.send(serverPlayer, new StructureProtectionClearPacket());
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static boolean checkForLockedStructuresSendPacket(Player player, Level world) {

		ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(world);
		if (chunkGenerator == null)
			return false;


		return TFGenerationSettings.locateTFStructureInRange((ServerLevel) world, player.blockPosition(), 100).map(structure -> {
			BoundingBox fullSBB = structure.getBoundingBox();
			Vec3i center = StructureBoundingBoxUtils.getCenter(fullSBB);

			TFFeature nearFeature = TFFeature.getFeatureForRegionPos(center.getX(), center.getZ(), (ServerLevel) world);

			if (!nearFeature.hasProtectionAura || nearFeature.doesPlayerHaveRequiredAdvancements(player)) {
				sendAllClearPacket(world, player);
				return false;
			} else {
				sendStructureProtectionPacket(world, player, fullSBB);
				return true;
			}
		}).orElse(false);
	}

	private static void checkForPortalCreation(Player player, Level world, float rangeToCheck) {
		if (world.dimension().location().equals(new ResourceLocation(TFConfig.COMMON_CONFIG.originDimension.get()))
				|| world.dimension().location().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get())
				|| TFConfig.COMMON_CONFIG.allowPortalsInOtherDimensions.get()) {

			List<ItemEntity> itemList = world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(rangeToCheck));

			for (ItemEntity entityItem : itemList) {
				if (ItemTagGenerator.PORTAL_ACTIVATOR.contains(entityItem.getItem().getItem())) {
					BlockPos pos = new BlockPos(entityItem.position().subtract(0, -0.1d, 0)); //TODO Quick fix, find if there's a more performant fix than this
					BlockState state = world.getBlockState(pos);
					if (TFBlocks.twilight_portal.canFormPortal(state)) {
						Random rand = new Random();
						for (int i = 0; i < 2; i++) {
							double vx = rand.nextGaussian() * 0.02D;
							double vy = rand.nextGaussian() * 0.02D;
							double vz = rand.nextGaussian() * 0.02D;

							world.addParticle(ParticleTypes.EFFECT, entityItem.getX(), entityItem.getY() + 0.2, entityItem.getZ(), vx, vy, vz);
						}

						if (TFBlocks.twilight_portal.tryToCreatePortal(world, pos, entityItem, player)) {
							TFAdvancements.MADE_TF_PORTAL.trigger((ServerPlayer) player);
							return;
						}
					}
				}
			}
		}
	}

}
