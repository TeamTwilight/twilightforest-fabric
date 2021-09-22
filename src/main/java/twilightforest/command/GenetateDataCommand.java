package twilightforest.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.api.FabricLoader;
import twilightforest.data.DataGenerators;

import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

public class GenetateDataCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("gendata").requires(cs -> cs.hasPermission(2)).executes(GenetateDataCommand::run);
    }

    private static int run(CommandContext<CommandSourceStack> ctx) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment())
            throw new CommandRuntimeException(new TextComponent("This command is not supported!"));
        return DataGenerators.gatherData();
    }
}
