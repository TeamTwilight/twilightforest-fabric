package twilightforest.command;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.Collection;
import java.util.Collections;

public final class MapLocatorCommand {
    private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID =
        new DynamicCommandExceptionType(info -> Component.translatableEscape("commands.locate.structure.invalid", info));
    private static final DynamicCommandExceptionType ERROR_STRUCTURE_NOT_FOUND =
        new DynamicCommandExceptionType(info -> Component.translatableEscape("commands.locate.structure.not_found", info));

    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("map_locator").requires(source -> source.hasPermission(2))
            .then(Commands.argument("structure", ResourceOrTagKeyArgument.resourceOrTagKey(Registries.STRUCTURE))
                .executes(context -> run(context, Collections.singleton(context.getSource().getPlayerOrException()), false))
                .then(Commands.argument("player", EntityArgument.players())
                    .executes(context -> run(context, EntityArgument.getPlayers(context, "player"), false))
                    .then(Commands.argument("skip_known_structures", BoolArgumentType.bool())
                        .executes(context -> run(context, EntityArgument.getPlayers(context, "player"), BoolArgumentType.getBool(context, "skip_known_structures"))))));
    }

    private static int run(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, boolean skipKnownStructures) throws CommandSyntaxException {
        var structure = ResourceOrTagKeyArgument.getResourceOrTagKey(context, "structure", Registries.STRUCTURE, ERROR_STRUCTURE_INVALID);
        CommandSourceStack source = context.getSource();
        Registry<Structure> registry = source.getLevel().registryAccess().registryOrThrow(Registries.STRUCTURE);
        HolderSet<Structure> holders = getHolders(structure, registry);
        BlockPos origin = BlockPos.containing(source.getPosition());
        ServerLevel level = source.getLevel();
        Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
        Pair<BlockPos, Holder<Structure>> found = level.getChunkSource().getGenerator()
            .findNearestMapStructure(level, holders, origin, 100, skipKnownStructures);
        stopwatch.stop();

        if (found == null) {
            throw ERROR_STRUCTURE_NOT_FOUND.create(structure.asPrintable());
        }

        BlockPos foundPos = found.getFirst();
        ItemStack map = MapItem.create(level, foundPos.getX(), foundPos.getZ(), (byte) 4, true, true);
        MapItemSavedData.addTargetDecoration(map, foundPos, "+", MapDecorationTypes.RED_X);
        for (ServerPlayer player : players) {
            player.getInventory().add(map.copy());
        }
        return LocateCommand.showLocateResult(source, structure, foundPos, found, "commands.locate.structure.success", false, stopwatch.elapsed());
    }

    private static HolderSet<Structure> getHolders(ResourceOrTagKeyArgument.Result<Structure> structure,
                                                   Registry<Structure> registry) throws CommandSyntaxException {
        var unwrapped = structure.unwrap();
        if (unwrapped.left().isPresent()) {
            ResourceKey<Structure> key = unwrapped.left().get();
            return HolderSet.direct(registry.getHolderOrThrow(key));
        }
        TagKey<Structure> tag = unwrapped.right().orElseThrow(() -> ERROR_STRUCTURE_INVALID.create(structure.asPrintable()));
        return registry.getTag(tag).orElseThrow(() -> ERROR_STRUCTURE_INVALID.create(structure.asPrintable()));
    }
}
