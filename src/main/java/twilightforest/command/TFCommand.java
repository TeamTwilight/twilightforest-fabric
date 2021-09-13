package twilightforest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import twilightforest.data.DataGenerators;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;

public class TFCommand {
    public static final SimpleCommandExceptionType NOT_IN_TF = new SimpleCommandExceptionType(new TranslatableComponent("commands.tffeature.not_in_twilight_forest"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("twilightforest")
                .then(CenterCommand.register())
                .then(ConquerCommand.register())
                .then(InfoCommand.register())
                //.then(TestCommand.register())
                .then(ShieldCommand.register());
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(builder);
        dispatcher.register(Commands.literal("tf").redirect(node));
        dispatcher.register(Commands.literal("tffeature").redirect(node));

        dispatcher.register(MapBiomesCommand.register());
        dispatcher.register(Commands.literal("gendata").executes((source) -> DataGenerators.run()));
    }
}
