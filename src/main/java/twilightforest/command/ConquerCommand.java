package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.world.components.structures.start.TFStructureStart;

import java.util.Optional;

public final class ConquerCommand {
    private static final SimpleCommandExceptionType NOT_IN_STRUCTURE =
        new SimpleCommandExceptionType(Component.translatable("commands.tffeature.structure.required"));

    public LiteralArgumentBuilder<CommandSourceStack> register() {
        LiteralArgumentBuilder<CommandSourceStack> conquer = Commands.literal("conquer")
            .requires(source -> source.hasPermission(2))
            .executes(context -> changeStructureActivity(context.getSource(), true));
        return conquer.then(Commands.literal("reactivate")
            .requires(source -> source.hasPermission(2))
            .executes(context -> changeStructureActivity(context.getSource(), false)));
    }

    private static int changeStructureActivity(CommandSourceStack source, boolean flag) throws CommandSyntaxException {
        BlockPos pos = BlockPos.containing(source.getPosition());
        Optional<StructureStart> start = LandmarkUtil.locateNearestLandmarkStart(
            source.getLevel(),
            SectionPos.blockToSectionCoord(pos.getX()),
            SectionPos.blockToSectionCoord(pos.getZ()));

        if (start.isPresent() && start.get().getBoundingBox().isInside(pos) && start.get() instanceof TFStructureStart tfStart) {
            source.sendSuccess(() -> Component.translatable("commands.tffeature.structure.conquer.update", tfStart.isConquered(), flag), true);
            tfStart.setConquered(flag, source.getLevel());
            return Command.SINGLE_SUCCESS;
        }
        throw NOT_IN_STRUCTURE.create();
    }
}
