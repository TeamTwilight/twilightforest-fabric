package twilightforest.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class PlayerHelper {
	/**
	 * Fulfills all remaining criteria of the given advancement
	 */
	@Deprecated
	public static void grantAdvancement(ServerPlayer player, ResourceLocation id) {
		PlayerAdvancements advancements = player.getAdvancements();
		Advancement advancement = player.getServer().getAdvancements().getAdvancement(id);
		if (advancement != null) {
			for (String criterion : advancements.getOrStartProgress(advancement).getRemainingCriteria()) {
				advancements.award(advancement, criterion);
			}
		}
	}

	@Deprecated
	public static void grantCriterion(ServerPlayer player, ResourceLocation id, String criterion) {
		PlayerAdvancements advancements = player.getAdvancements();
		Advancement advancement = player.getServer().getAdvancements().getAdvancement(id);
		if (advancement != null) {
			advancements.award(advancement, criterion);
		}
	}

	@Nullable
	public static Advancement getAdvancement(Player player, ResourceLocation advancementLocation) {
		if (player.level().isClientSide() && player instanceof LocalPlayer localPlayer) {
			ClientAdvancements manager = localPlayer.connection.getAdvancements();
			return manager.getAdvancements().get(advancementLocation);
		} else if (player instanceof ServerPlayer serverPlayer) {
			ServerLevel world = (ServerLevel) serverPlayer.level();
			return world.getServer().getAdvancements().getAdvancement(advancementLocation);
		}

		return null;
	}

	public static boolean doesPlayerHaveRequiredAdvancement(Player player, Advancement advancement) {
		if (player.level().isClientSide()) {
			if (player instanceof LocalPlayer) {
				ClientAdvancements manager = ((LocalPlayer) player).connection.getAdvancements();
				if (advancement == null) return false;

				AdvancementProgress progress = manager.progress.get(advancement);
				return progress != null && progress.isDone();
			}
			return false;
		} else {
			if (player instanceof ServerPlayer) {
				return advancement != null && ((ServerPlayer) player).getAdvancements().getOrStartProgress(advancement).isDone();
			}
			return false;
		}
	}

	public static boolean doesPlayerHaveRequiredAdvancements(Player player, List<ResourceLocation> requiredAdvancements) {
		return PlayerHelper.playerHasRequiredAdvancements(player, requiredAdvancements);
	}

	public static boolean doesPlayerHaveRequiredAdvancements(Player player, ResourceLocation... requiredAdvancements) {
		return PlayerHelper.playerHasRequiredAdvancements(player, List.of(requiredAdvancements));
	}

	public static boolean playerHasRequiredAdvancements(Player player, Iterable<ResourceLocation> requiredAdvancements) {
		for (ResourceLocation advancementLocation : requiredAdvancements) {
			if (player.level().isClientSide()) {
				if (player instanceof LocalPlayer local) {
					ClientAdvancements manager = local.connection.getAdvancements();
					Advancement adv = manager.getAdvancements().get(advancementLocation);
					if (adv == null)
						return false;
					AdvancementProgress progress = manager.progress.get(adv);
					return progress != null && progress.isDone();
				}
			} else {
				if (player instanceof ServerPlayer sp) {
					ServerLevel world = (ServerLevel) player.level();
					Advancement adv = world.getServer().getAdvancements().getAdvancement(advancementLocation);
					return adv != null && sp.getAdvancements().getOrStartProgress(adv).isDone();
				}
			}
			return false;
		}
		return true;
	}
}
