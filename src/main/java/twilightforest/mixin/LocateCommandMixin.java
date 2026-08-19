package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.Field;
import java.util.Locale;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {

	@ModifyExpressionValue(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/commands/Commands;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;"
		)
	)
	private static RequiredArgumentBuilder<CommandSourceStack, ?> twilightforest$enableCrossNamespaceStructureSuggestions(RequiredArgumentBuilder<CommandSourceStack, ?> original) {
		if (!"structure".equals(twilightforest$getArgumentName(original))) {
			return original;
		}

		return original.suggests(SUGGEST_STRUCTURES);
	}

	@Unique
	private static String twilightforest$getArgumentName(RequiredArgumentBuilder<?, ?> builder) {
		try {
			Field nameField = RequiredArgumentBuilder.class.getDeclaredField("name");
			nameField.setAccessible(true);
			return (String) nameField.get(builder);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	@Unique
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_STRUCTURES = (context, builder) -> {
		Registry<Structure> registry = context.getSource().registryAccess().registryOrThrow(Registries.STRUCTURE);
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
		boolean hasColon = remaining.indexOf(':') != -1;

		for (ResourceLocation id : registry.keySet()) {
			// With a colon: substring-match the whole id. Without: substring-match the path
			// (across all namespaces) or prefix-match the namespace, so "li" finds
			// twilightforest:lich_tower without flooding every twilightforest structure.
			if (hasColon ? id.toString().contains(remaining) : id.getPath().contains(remaining) || id.getNamespace().startsWith(remaining)) {
				builder.suggest(id.toString());
			}
		}
		for (TagKey<Structure> tag : registry.getTagNames().toList()) {
			if (hasColon ? tag.location().toString().contains(remaining) : tag.location().getPath().contains(remaining) || tag.location().getNamespace().startsWith(remaining)) {
				builder.suggest("#" + tag.location());
			}
		}
		return builder.buildFuture();
	};
}