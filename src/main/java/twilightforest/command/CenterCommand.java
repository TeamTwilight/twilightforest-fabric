package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import twilightforest.init.TFLandmark;
import twilightforest.util.LegacyLandmarkPlacements;
import twilightforest.world.registration.TFGenerationSettings;

public class CenterCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("center").requires(cs -> cs.hasPermission(2)).executes(CenterCommand::run);
    }

    private static int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();

        if (!TFGenerationSettings.usesTwilightChunkGenerator(source.getLevel())) {
            throw TFCommand.NOT_IN_TF.create();
        }

        int dx = Mth.floor(source.getPosition().x());
        int dz = Mth.floor(source.getPosition().z());
        BlockPos cc = LegacyLandmarkPlacements.getNearestCenterXZ(dx >> 4, dz >> 4);
        TFLandmark closestFeature = LegacyLandmarkPlacements.pickLandmarkAtBlock(cc.getX(), cc.getZ(), source.getLevel());
        boolean fc = LegacyLandmarkPlacements.blockIsInLandmarkCenter(dx, dz);

        if(closestFeature == TFLandmark.NOTHING) {
            source.sendSuccess(Component.translatable("commands.tffeature.none_nearby").withStyle(ChatFormatting.RED), false);
        } else {
            String structurename = Component.translatable("structure." + closestFeature.name).withStyle(ChatFormatting.DARK_GREEN).getString();
            source.sendSuccess(Component.translatable("commands.tffeature.nearest", structurename), false);
            source.sendSuccess(Component.translatable("commands.tffeature.center", cc), false);
            source.sendSuccess(Component.translatable("commands.tffeature.chunk", fc), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
