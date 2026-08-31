package twilightforest.asm.hooks.event;

// TODO [Fabric] : Integrate these hooks into mixins and validate each one of them once the project compiles
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
	*/
}