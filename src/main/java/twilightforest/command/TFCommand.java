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

public class TFCommand {
	public static final TFCommand INSTANCE = new TFCommand();

	private final CenterCommand centerCommand = CenterCommand.INSTANCE;

	private final ConquerCommand conquerCommand = ConquerCommand.INSTANCE;

	private final GenerateBookCommand generateBookCommand = GenerateBookCommand.INSTANCE;

	private final InfoCommand infoCommand = InfoCommand.INSTANCE;

	private final MapBiomesCommand mapBiomesCommand = MapBiomesCommand.INSTANCE;

	private final MapLocatorCommand mapLocatorCommand = MapLocatorCommand.INSTANCE;

	private final ShieldCommand shieldCommand = ShieldCommand.INSTANCE;

	private final SinisterSpawnerCommand spawnerCommand = SinisterSpawnerCommand.INSTANCE;

	private final DisplayPiecesCommand displayPiecesCommand = DisplayPiecesCommand.INSTANCE;

	private final CountLootCommand countLootCommand = CountLootCommand.INSTANCE;

	private final CountTemplateCommand countTemplateCommand = CountTemplateCommand.INSTANCE;

	private final StructureDistanceCommand structureDistanceCommand = StructureDistanceCommand.INSTANCE;

	private final ClearDisplayCommand clearDisplayCommand = ClearDisplayCommand.INSTANCE;

	private final TravellersGearCommand travellersGearCommand = TravellersGearCommand.INSTANCE;

	private final TFTeleportCommand tfTeleportCommand = TFTeleportCommand.INSTANCE;

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
		LiteralArgumentBuilder<CommandSourceStack> structureBranch = Commands.literal("structure_util")
			.then(displayPiecesCommand.register())
			.then(clearDisplayCommand.register())
			.then(countLootCommand.register())
			.then(countTemplateCommand.register())
			.then(structureDistanceCommand.register());

		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("twilightforest")
			.executes(this::run)
			.then(tfTeleportCommand.register())
			.then(centerCommand.register())
			.then(mapLocatorCommand.register())
			.then(conquerCommand.register())
			.then(generateBookCommand.register())
			.then(infoCommand.register())
			.then(mapBiomesCommand.register())
			.then(shieldCommand.register())
			.then(spawnerCommand.register(buildContext))
			.then(travellersGearCommand.register())
			.then(structureBranch);
		LiteralCommandNode<CommandSourceStack> node = dispatcher.register(builder);
		dispatcher.register(Commands.literal("tf").executes(this::run).redirect(node));
		dispatcher.register(Commands.literal("tffeature").executes(this::run).redirect(node));
	}

	private int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		throw new SimpleCommandExceptionType(Component.translatable("commands.tffeature.usage", ctx.getInput())).create();
	}
}
