package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDimension;

import java.util.Locale;

public final class TFTeleportCommand {
    private static final SimpleCommandExceptionType PLAYER_ONLY =
        new SimpleCommandExceptionType(Component.translatable("commands.tffeature.teleport.player_only"));
    private static final SimpleCommandExceptionType DIMENSION_MISSING =
        new SimpleCommandExceptionType(Component.translatable("commands.tffeature.teleport.dimension_missing"));
    private static final SimpleCommandExceptionType INVALID_POSITION =
        new SimpleCommandExceptionType(Component.translatable("commands.teleport.invalidPosition"));

    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("tp").requires(source -> source.hasPermission(2)).executes(this::run);
    }

    private int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            throw PLAYER_ONLY.create();
        }

        ServerLevel twilight = source.getServer().getLevel(TFDimension.DIMENSION_KEY);
        if (twilight == null) {
            throw DIMENSION_MISSING.create();
        }

        Vec3 pos = source.getPosition();
        Level level = player.level();
        double convertedY = (pos.y - level.getMinBuildHeight())
            / (level.getMaxBuildHeight() - level.getMinBuildHeight())
            * (twilight.getMaxBuildHeight() - twilight.getMinBuildHeight())
            + twilight.getMinBuildHeight();
        Vec3 target = new Vec3(pos.x, convertedY, pos.z);
        if (!twilight.isInWorldBounds(BlockPos.containing(target))) {
            throw INVALID_POSITION.create();
        }

        player.teleportTo(twilight, target.x(), target.y(), target.z(), player.getYRot(), player.getXRot());
        String x = String.format(Locale.ROOT, "%.1f", target.x());
        String y = String.format(Locale.ROOT, "%.1f", target.y());
        String z = String.format(Locale.ROOT, "%.1f", target.z());
        source.sendSuccess(() -> Component.translatable("commands.tffeature.teleport.success", x, y, z), false);
        return Command.SINGLE_SUCCESS;
    }
}
