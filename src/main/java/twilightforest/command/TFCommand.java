package twilightforest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class TFCommand {
    private static final twilightforest.util.DisplayUtil DISPLAY_UTIL = new twilightforest.util.DisplayUtil();
    private static final CenterCommand CENTER = new CenterCommand();
    private static final ConquerCommand CONQUER = new ConquerCommand();
    private static final ClearDisplayCommand CLEAR_DISPLAY = new ClearDisplayCommand(DISPLAY_UTIL);
    private static final CountLootCommand COUNT_LOOT = new CountLootCommand();
    private static final CountTemplateCommand COUNT_TEMPLATE = new CountTemplateCommand();
    private static final DisplayPiecesCommand DISPLAY_PIECES = new DisplayPiecesCommand(DISPLAY_UTIL);
    private static final GenerateBookCommand GENERATE_BOOK = new GenerateBookCommand();
    private static final InfoCommand INFO = new InfoCommand();
    private static final MapLocatorCommand MAP_LOCATOR = new MapLocatorCommand();
    private static final MapBiomesCommand MAP_BIOMES = new MapBiomesCommand();
    private static final ShieldCommand SHIELD = new ShieldCommand();
    private static final SinisterSpawnerCommand SINISTER_SPAWNER = new SinisterSpawnerCommand();
    private static final StructureDistanceCommand STRUCTURE_DISTANCE = new StructureDistanceCommand(DISPLAY_UTIL);
    private static final TFTeleportCommand TELEPORT = new TFTeleportCommand();
    private static final TravellersGearCommand TRAVELLERS_GEAR = new TravellersGearCommand();

    private TFCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
                                CommandBuildContext buildContext,
                                Commands.CommandSelection environment) {
        LiteralArgumentBuilder<CommandSourceStack> structureBranch = Commands.literal("structure_util")
            .then(DISPLAY_PIECES.register())
            .then(CLEAR_DISPLAY.register())
            .then(COUNT_LOOT.register())
            .then(COUNT_TEMPLATE.register())
            .then(STRUCTURE_DISTANCE.register());

        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("twilightforest")
            .executes(TFCommand::run)
            .then(TELEPORT.register())
            .then(CENTER.register())
            .then(MAP_LOCATOR.register())
            .then(CONQUER.register())
            .then(GENERATE_BOOK.register())
            .then(INFO.register())
            .then(MAP_BIOMES.register())
            .then(SHIELD.register())
            .then(SINISTER_SPAWNER.register(buildContext))
            .then(TRAVELLERS_GEAR.register())
            .then(structureBranch);
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(builder);
        dispatcher.register(Commands.literal("tf").executes(TFCommand::run).redirect(node));
        dispatcher.register(Commands.literal("tffeature").executes(TFCommand::run).redirect(node));
    }

    private static int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        throw new SimpleCommandExceptionType(Component.translatable("commands.tffeature.usage", context.getInput())).create();
    }
}
