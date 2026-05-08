package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.init.TFDataAttachments;

public final class ShieldCommand {
    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("shield")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("target", EntityArgument.entity())
                .then(Commands.literal("set")
                    .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(context -> set(EntityArgument.getEntity(context, "target"), IntegerArgumentType.getInteger(context, "amount"), true))
                        .then(Commands.argument("temp", BoolArgumentType.bool())
                            .executes(context -> set(EntityArgument.getEntity(context, "target"), IntegerArgumentType.getInteger(context, "amount"), BoolArgumentType.getBool(context, "temp"))))))
                .then(Commands.literal("add")
                    .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(context -> add(EntityArgument.getEntity(context, "target"), IntegerArgumentType.getInteger(context, "amount"), true))
                        .then(Commands.argument("temp", BoolArgumentType.bool())
                            .executes(context -> add(EntityArgument.getEntity(context, "target"), IntegerArgumentType.getInteger(context, "amount"), BoolArgumentType.getBool(context, "temp")))))));
    }

    private static int add(Entity entity, int amount, boolean temporary) {
        if (entity instanceof LivingEntity living) {
            TFDataAttachments.get(living, TFDataAttachments.FORTIFICATION_SHIELDS).addShields(living, amount, temporary);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int set(Entity entity, int amount, boolean temporary) {
        if (entity instanceof LivingEntity living) {
            TFDataAttachments.get(living, TFDataAttachments.FORTIFICATION_SHIELDS).setShields(living, amount, temporary);
        }
        return Command.SINGLE_SUCCESS;
    }
}
