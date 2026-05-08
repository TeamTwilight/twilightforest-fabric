package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

public final class CenterCommand {
    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("center").requires(source -> source.hasPermission(2)).executes(this::run);
    }

    private int run(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        int x = Mth.floor(source.getPosition().x());
        int z = Mth.floor(source.getPosition().z());
        BlockPos center = LegacyLandmarkPlacements.getNearestCenterXZ(x >> 4, z >> 4);
        var landmark = LegacyLandmarkPlacements.pickLandmarkAtBlock(center.getX(), center.getZ(), source.getLevel()).location();
        boolean inCenter = LegacyLandmarkPlacements.blockIsInLandmarkCenter(x, z);

        String name = Component.translatable(landmark.toLanguageKey("structure")).withStyle(ChatFormatting.DARK_GREEN).getString();
        source.sendSuccess(() -> Component.translatable("commands.tffeature.nearest", name), false);
        source.sendSuccess(() -> Component.translatable("commands.tffeature.center", center.toShortString()), false);
        source.sendSuccess(() -> Component.translatable("commands.tffeature.chunk", inCenter), false);
        return Command.SINGLE_SUCCESS;
    }
}
