package twilightforest.util;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class PlayerHelper {
    private PlayerHelper() {
    }

    @Nullable
    public static AdvancementHolder getAdvancement(Player player, ResourceLocation advancementLocation) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel world = serverPlayer.serverLevel();
            return world.getServer().getAdvancements().get(advancementLocation);
        }

        return null;
    }

    public static boolean doesPlayerHaveRequiredAdvancement(Player player, @Nullable AdvancementHolder holder) {
        if (player instanceof ServerPlayer serverPlayer) {
            return holder != null && serverPlayer.getAdvancements().getOrStartProgress(holder).isDone();
        }

        return false;
    }

    public static boolean doesPlayerHaveRequiredAdvancements(Player player, List<ResourceLocation> requiredAdvancements) {
        return playerHasRequiredAdvancements(player, requiredAdvancements);
    }

    public static boolean doesPlayerHaveRequiredAdvancements(Player player, ResourceLocation... requiredAdvancements) {
        return playerHasRequiredAdvancements(player, List.of(requiredAdvancements));
    }

    public static boolean playerHasRequiredAdvancements(Player player, Iterable<ResourceLocation> advancements) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }

        ServerLevel world = serverPlayer.serverLevel();
        for (ResourceLocation advancementLocation : advancements) {
            AdvancementHolder advancement = world.getServer().getAdvancements().get(advancementLocation);
            if (advancement == null) {
                return false;
            }
            AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
            if (!progress.isDone()) {
                return false;
            }
        }
        return true;
    }
}
