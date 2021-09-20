package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

public class InfoCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("info").requires(cs -> cs.hasPermission(2)).executes(InfoCommand::run);
    }

    private static int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();

        if (!TFGenerationSettings.usesTwilightChunkGenerator(source.getLevel())) {
            throw TFCommand.NOT_IN_TF.create();
        }

        BlockPos pos = new BlockPos(source.getPosition());

        // nearest feature
        TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), source.getLevel());
        source.sendSuccess(new TranslatableComponent("commands.tffeature.nearest", nearbyFeature.name), false);

        // are you in a structure?
        ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(source.getLevel());
        if (chunkGenerator != null/* && chunkGenerator.isBlockInStructureBB(pos)*/) {
            source.sendSuccess(new TranslatableComponent("commands.tffeature.structure.inside"), false);

//            source.sendFeedback(new TranslationTextComponent("commands.tffeature.structure.conquer.status", chunkGenerator.isStructureConquered(pos)), false); TODO: Sorry...I got rid of the Chunk Generator's things
            // are you in a room?

            // what is the spawn list
//						List<SpawnListEntry> spawnList = chunkGenerator.getPossibleCreatures(EnumCreatureType.monster, dx, dy, dz);
//						sender.sendMessage(new TextComponentTranslation("Spawn list for the area is:"));
//						for (SpawnListEntry entry : spawnList) {
//							sender.sendMessage(new TextComponentTranslation(entry.toString()));
//						}
        } else {
            source.sendSuccess(new TranslatableComponent("commands.tffeature.structure.outside"), false);
        }

        return Command.SINGLE_SUCCESS;
    }

}
