package twilightforest.asm.hooks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

// TODO [Fabric] : Integrate these hooks into mixins once the project compiles and can be tested
public final class ProgressionEventHooks {
	/*
	private void preventLockedAreaMultiblocks(BlockEvent.EntityMultiPlaceEvent event) {
		Entity entity = event.getEntity();

		if (!(event.getLevel() instanceof ServerLevel level) || !(entity instanceof Player player) || event.isCanceled()) return;

		for (BlockSnapshot snapshot : event.getReplacedBlockSnapshots()) {
			BlockPos pos = snapshot.getPos();

			if (isBlockProtectedFromBreaking(level, pos) && isAreaProtected(level, player, pos)) {
				event.setCanceled(true);
				player.inventoryMenu.sendAllDataToRemote();
				break;
			}
		}
	}

	private void preventLockedAreaBlockInteracting(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		Level level = player.level();

		if (!level.isClientSide() && level instanceof ServerLevel serverLevel && isBlockProtectedFromInteraction(level, event.getPos()) && isAreaProtected(serverLevel, player, event.getPos())) {
			event.setUseBlock(TriState.FALSE);
		}
	}

	private void performProtectionAndPortalChecks(PlayerTickEvent.Post event) {
		Player eventPlayer = event.getEntity();

		if (!(eventPlayer instanceof ServerPlayer player)) return;
		ServerLevel level = player.level();

		// check for portal creation, at least if it's not disabled
		if (!TFConfig.disablePortalCreation && player.tickCount % (!TFConfig.checkPortalPlacement ? 100 : 20) == 0) {
			// skip non admin players when the option is on
			if (level.getServer().getProfilePermissions(player.nameAndId()).level().isEqualOrHigherThan(TFConfig.portalCreationPermission)) {
				// reduce range to 4.0 if config is set to admins/owners only
				checkForPortalCreation(player, level, TFConfig.portalCreationPermission.isEqualOrHigherThan(PermissionLevel.ADMINS)  ? 4.0F : 32.0F);
			}
		}

		// check the player for being in a forbidden progression area, only every 20 ticks
		if (player.tickCount % 20 == 0 && LandmarkUtil.isProgressionEnforced(level) && !player.isCreative() && !player.isSpectator()) {
			Enforcement.enforceBiomeProgression(player, level);
		}

		// check and send nearby forbidden structures, every 100 ticks or so
		if (player.tickCount % 100 == 0 && LandmarkUtil.isProgressionEnforced(level)) {
			if (player.isCreative() || player.isSpectator()) {
				sendAllClearPacket(player);
			} else {
				checkForLockedStructuresSendPacket(player, level);
			}
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static boolean checkForLockedStructuresSendPacket(Player player, ServerLevel world) {
		ChunkPos chunkPlayer = player.chunkPosition();
		return LandmarkUtil.locateNearestLandmarkStart(world, chunkPlayer.x(), chunkPlayer.z()).map(structureStart -> {
			if (structureStart.getStructure() instanceof AdvancementLockedStructure advancementLockedStructure && !advancementLockedStructure.doesPlayerHaveRequiredAdvancements(player)) {
				List<Pair<BoundingBox, Boolean>> boundingBoxesData = structureStart.getPieces().stream()
					.map(piece -> Pair.of(isPieceProtected(piece) ? piece.getBoundingBox().inflatedBy(4) : piece.getBoundingBox(), isPieceProtected(piece)))
					.toList();

				sendStructureProtectionPacket(player, boundingBoxesData);
				return true;
			}

			sendAllClearPacket(player);
			return false;
		}).orElse(false);
	}

	private static void checkForPortalCreation(ServerPlayer player, ServerLevel level, float rangeToCheck) {
		if (level.dimension().identifier().equals(Identifier.parse(TFConfig.originDimension))
			|| TFDimension.isTwilightPortalDestination(level)
			|| TFConfig.allowPortalsInOtherDimensions) {

			List<ItemEntity> itemList = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(rangeToCheck));
			ItemEntity qualified = null;

			for (ItemEntity entityItem : itemList) {
				if (entityItem.getItem().is(TFItemTags.PORTAL_ACTIVATOR) &&
					TFBlocks.TWILIGHT_PORTAL.canFormPortal(level.getBlockState(entityItem.blockPosition())) &&
					Objects.equals(entityItem.getOwner(), player)) {

					qualified = entityItem;
					break;
				}
			}

			if (qualified == null) return;

			if (!player.isCreative() && !player.isSpectator() && TFConfig.getPortalLockingAdvancement(player) != null) {
				AdvancementHolder requirement = PlayerHelper.getAdvancement(player, Objects.requireNonNull(TFConfig.getPortalLockingAdvancement(player)));
				if (requirement != null && !PlayerHelper.doesPlayerHaveRequiredAdvancement(player, requirement)) {
					player.sendOverlayMessage(TFPortalBlock.PORTAL_UNWORTHY);

					if (!TFPortalBlock.isPlayerNotifiedOfRequirement(player)) {
						// .doesPlayerHaveRequiredAdvancement null-checks already, so we can skip null-checking the `requirement`
						DisplayInfo info = requirement.value().display().orElse(null);
						PacketDistributor.sendToPlayer(player, info == null ? new MissingAdvancementToastPacket(net.minecraft.network.chat.Component.translatable("twilightforest.ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())) : new MissingAdvancementToastPacket(info.getTitle(), info.getIcon()));

						TFPortalBlock.playerNotifiedOfRequirement(player);
					}

					return; // Item qualifies, but the player doesn't
				}
			}

			Random rand = new Random();
			for (int i = 0; i < 2; i++) {
				double vx = rand.nextGaussian() * 0.02D;
				double vy = rand.nextGaussian() * 0.02D;
				double vz = rand.nextGaussian() * 0.02D;

				level.addParticle(ParticleTypes.EFFECT, qualified.getX(), qualified.getY() + 0.2, qualified.getZ(), vx, vy, vz);
			}

			if (TFBlocks.TWILIGHT_PORTAL.tryToCreatePortal(level, qualified.blockPosition(), qualified, player))
				TFAdvancements.MADE_TF_PORTAL.trigger(player);

		}
	}

	private static boolean isBlockProtectedFromInteraction(BlockGetter level, BlockPos pos) {
		return level.getBlockState(pos).is(TFBlockTags.STRUCTURE_BANNED_INTERACTIONS);
	}

	private static void sendStructureProtectionPacket(Player player, List<Pair<BoundingBox, Boolean>> sbbData) {
		if (player instanceof ServerPlayer sp) {
			PacketDistributor.sendToPlayer(sp, new StructureProtectionPacket(Optional.of(sbbData)));
		}
	}

	private static void sendAllClearPacket(Player player) {
		if (player instanceof ServerPlayer sp) {
			PacketDistributor.sendToPlayer(sp, new StructureProtectionPacket(Optional.empty()));
		}
	}
	 */
}