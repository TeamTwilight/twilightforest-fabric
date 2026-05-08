package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFRegistries;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.function.Function;

public final class TravellersGearCommand {
    private static final DynamicCommandExceptionType ERROR_INVALID_MODIFIER =
        new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.tffeature.invalid_modifier", value));
    private static final SimpleCommandExceptionType ERROR_NOT_RUN_BY_PLAYER =
        new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_player"));
    private static final SimpleCommandExceptionType ERROR_NOT_HOLDING_GEAR =
        new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_travellers_gear"));
    private static final SimpleCommandExceptionType ERROR_TOO_MANY_MODIFIERS =
        new SimpleCommandExceptionType(Component.translatable("commands.tffeature.too_many_modifiers"));
    private static final Function<Component, SimpleCommandExceptionType> ERROR_NO_MODIFIER =
        component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.no_modifier", component));
    private static final Function<Component, SimpleCommandExceptionType> ERROR_HAS_MODIFIER =
        component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.has_modifier", component));
    private static final Function<Component, SimpleCommandExceptionType> ERROR_WRONG_SLOT =
        component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.wrong_modifier_slot", component));
    private static final SimpleCommandExceptionType ERROR_ABILITY =
        new SimpleCommandExceptionType(Component.translatable("commands.tffeature.ability_modifier"));

    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("travellers_gear").requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .then(Commands.literal("add_modifier")
                .then(Commands.argument("modifier", ResourceKeyArgument.key(TFRegistries.Keys.TRAVELLERS_MODIFIERS))
                    .executes(context -> addModifier(context.getSource(), getModifier(context.getSource(), context.getArgument("modifier", ResourceKey.class))))))
            .then(Commands.literal("remove_modifier")
                .then(Commands.argument("modifier", ResourceKeyArgument.key(TFRegistries.Keys.TRAVELLERS_MODIFIERS))
                    .executes(context -> removeModifier(context.getSource(), getModifier(context.getSource(), context.getArgument("modifier", ResourceKey.class))))));
    }

    @SuppressWarnings("unchecked")
    private static Holder.Reference<TravellersModifier> getModifier(CommandSourceStack source, ResourceKey<?> key) throws CommandSyntaxException {
        ResourceKey<TravellersModifier> modifierKey = (ResourceKey<TravellersModifier>) key;
        return source.registryAccess().lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).get(modifierKey)
            .orElseThrow(() -> ERROR_INVALID_MODIFIER.create(modifierKey.location()));
    }

    private static int addModifier(CommandSourceStack source, Holder.Reference<TravellersModifier> modifier) throws CommandSyntaxException {
        Context context = validate(source, modifier);
        if (TravellersModifiersManager.countInsertableModifiers(source.registryAccess(), context.stack()) >= context.item().getModifierSlots()) {
            throw ERROR_TOO_MANY_MODIFIERS.create();
        }
        if (TravellersModifiersManager.hasTravellersModifier(source.registryAccess(), context.stack(), modifier.key())) {
            throw ERROR_HAS_MODIFIER.apply(context.modKey()).create();
        }
        if (!modifier.value().group().test(context.player().getEquipmentSlotForItem(context.stack()))) {
            throw ERROR_WRONG_SLOT.apply(context.modKey()).create();
        }

        TravellersModifiersManager.addModifier(source.registryAccess(), context.stack(), modifier.key());
        source.sendSuccess(() -> Component.translatable("commands.tffeature.added_modifier", context.modKey(), context.stack().getHoverName()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int removeModifier(CommandSourceStack source, Holder.Reference<TravellersModifier> modifier) throws CommandSyntaxException {
        Context context = validate(source, modifier);
        if (!TravellersModifiersManager.hasTravellersModifier(source.registryAccess(), context.stack(), modifier.key())) {
            throw ERROR_NO_MODIFIER.apply(context.modKey()).create();
        }

        ((InsertableTravellersModifier) modifier.value()).removeModifier(context.stack());
        source.sendSuccess(() -> Component.translatable("commands.tffeature.removed_modifier", context.modKey(), context.stack().getHoverName()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static Context validate(CommandSourceStack source, Holder.Reference<TravellersModifier> modifier) throws CommandSyntaxException {
        if (!(source.getEntity() instanceof Player player)) {
            throw ERROR_NOT_RUN_BY_PLAYER.create();
        }
        if (!(player.getMainHandItem().getItem() instanceof TravellersModifiable modifiable)) {
            throw ERROR_NOT_HOLDING_GEAR.create();
        }
        if (modifier.value().isAbility()) {
            throw ERROR_ABILITY.create();
        }
        Component modKey = TravellersModifiersManager.getModifierTooltipComponent(modifier);
        return new Context(player, player.getMainHandItem(), modifiable, modKey);
    }

    private record Context(Player player, ItemStack stack, TravellersModifiable item, Component modKey) {
    }
}
