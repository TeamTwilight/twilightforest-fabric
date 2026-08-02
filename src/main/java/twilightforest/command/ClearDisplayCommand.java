package twilightforest.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import twilightforest.util.DisplayUtil;

import java.util.List;

public class ClearDisplayCommand {
	public static final ClearDisplayCommand INSTANCE = new ClearDisplayCommand();

	private final DisplayUtil displayUtil = DisplayUtil.INSTANCE;

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("clear_display").executes(this::clearDisplayPieces);
	}

	private int clearDisplayPieces(CommandContext<CommandSourceStack> context) {
		MinecraftServer server = context.getSource().getServer();

		// Extremely lazy way of clearing the entities without having to access the level entities with a scope, etc
		List<String> commands = List.of(
			"kill @e[type=minecraft:text_display,tag=" + this.displayUtil.tag + "]",
			"kill @e[type=minecraft:block_display,tag=" + this.displayUtil.tag + "]"
		);
		for (String command : commands) {
			server.getCommands().performCommand(server.getCommands().getDispatcher().parse(command, context.getSource()), command);
		}

		return 0;
	}
}
