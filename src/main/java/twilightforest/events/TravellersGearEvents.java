package twilightforest.events;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import twilightforest.init.*;

import java.util.*;

public class TravellersGearEvents {
	public static final TravellersGearEvents INSTANCE = new TravellersGearEvents();

	private static final List<AttachmentType<?>> ATTACHMENTS_TO_PRESERVE_ON_DEATH = List.of(
		TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION
	);

	public static void init() {
		ServerPlayerEvents.COPY_FROM.register(INSTANCE::keepAttachmentsOnDeath);
	}

	public void keepAttachmentsOnDeath(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
		if (!alive) {
			for (AttachmentType<?> attachment : ATTACHMENTS_TO_PRESERVE_ON_DEATH) {
				copyAttachmentData(oldPlayer, newPlayer, attachment);
			}
		}
	}

	private <T> void copyAttachmentData(Player source, Player target, AttachmentType<T> type) {
		if (source.hasAttached(type)) {
			target.setAttached(type, source.getAttached(type));
		}
	}
}