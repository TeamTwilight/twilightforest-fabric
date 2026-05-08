package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import twilightforest.world.components.structures.util.StructureHints;

public final class GenerateBookCommand {
    private static final SimpleCommandExceptionType ERROR_NOT_RUN_BY_PLAYER =
        new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_player"));

    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("genbook")
            .requires(source -> source.hasPermission(3))
            .executes(context -> generateBook(context.getSource(), null))
            .then(Commands.argument("structure", ResourceKeyArgument.key(Registries.STRUCTURE))
                .executes(context -> generateBook(context.getSource(), ResourceKeyArgument.getStructure(context, "structure"))));
    }

    private static int generateBook(CommandSourceStack source, @Nullable Holder.Reference<Structure> structureKey) throws CommandSyntaxException {
        if (!(source.getEntity() instanceof Player player)) {
            throw ERROR_NOT_RUN_BY_PLAYER.create();
        }

        if (structureKey == null) {
            for (Structure structure : source.getLevel().registryAccess().registryOrThrow(Registries.STRUCTURE).stream().toList()) {
                if (structure instanceof StructureHints hints) {
                    giveBook(player, hints.createHintBook(source.registryAccess()));
                }
            }
        } else if (source.getLevel().registryAccess().registryOrThrow(Registries.STRUCTURE).get(structureKey.key()) instanceof StructureHints hints) {
            giveBook(player, hints.createHintBook(source.registryAccess()));
        } else {
            giveBook(player, StructureHints.HintConfig.defaultBook());
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void giveBook(Player player, ItemStack book) {
        if (!book.isEmpty() && !player.addItem(book)) {
            player.drop(book, true);
        }
    }
}
