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
import twilightforest.util.TFBeanRegistry;

public class TFCommand {

	public static final TFCommand INSTANCE = new TFCommand();

	static {
		TFBeanRegistry.register(TFCommand.class, INSTANCE);
		TFBeanRegistry.addPostInit(INSTANCE::init);
	}

	private CenterCommand centerCommand;
	private ConquerCommand conquerCommand;
	private GenerateBookCommand generateBookCommand;
	private InfoCommand infoCommand;
	private MapBiomesCommand mapBiomesCommand;
	private MapLocatorCommand mapLocatorCommand;
	private ShieldCommand shieldCommand;
	private SinisterSpawnerCommand spawnerCommand;
	private DisplayPiecesCommand displayPiecesCommand;
	private CountLootCommand countLootCommand;
	private CountTemplateCommand countTemplateCommand;
	private StructureDistanceCommand structureDistanceCommand;
	private ClearDisplayCommand clearDisplayCommand;
	private TravellersGearCommand travellersGearCommand;
	private TFTeleportCommand tfTeleportCommand;

	private void init() {
		this.centerCommand = TFBeanRegistry.get(CenterCommand.class);
		this.conquerCommand = TFBeanRegistry.get(ConquerCommand.class);
		this.generateBookCommand = TFBeanRegistry.get(GenerateBookCommand.class);
		this.infoCommand = TFBeanRegistry.get(InfoCommand.class);
		this.mapBiomesCommand = TFBeanRegistry.get(MapBiomesCommand.class);
		this.mapLocatorCommand = TFBeanRegistry.get(MapLocatorCommand.class);
		this.shieldCommand = TFBeanRegistry.get(ShieldCommand.class);
		this.spawnerCommand = TFBeanRegistry.get(SinisterSpawnerCommand.class);
		this.displayPiecesCommand = TFBeanRegistry.get(DisplayPiecesCommand.class);
		this.countLootCommand = TFBeanRegistry.get(CountLootCommand.class);
		this.countTemplateCommand = TFBeanRegistry.get(CountTemplateCommand.class);
		this.structureDistanceCommand = TFBeanRegistry.get(StructureDistanceCommand.class);
		this.clearDisplayCommand = TFBeanRegistry.get(ClearDisplayCommand.class);
		this.travellersGearCommand = TFBeanRegistry.get(TravellersGearCommand.class);
		this.tfTeleportCommand = TFBeanRegistry.get(TFTeleportCommand.class);
	}

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
