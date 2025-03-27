package twilightforest.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Transformation;
import com.mojang.serialization.DataResult;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.joml.Matrix4f;
import twilightforest.beans.Autowired;
import twilightforest.beans.Component;
import twilightforest.util.DisplayUtil;
import twilightforest.world.components.structures.util.ProgressionPiece;

import java.util.List;
import java.util.Optional;

@Component
public class DisplayPiecesCommand {
	@Autowired
	private DisplayUtil displayUtil;

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("display_pieces").requires(cs -> cs.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.argument("filter_structure", ResourceKeyArgument.key(Registries.STRUCTURE)).executes(this::debugDisplayPieces))
			.then(Commands.literal("clear").executes(this::clearDisplayPieces));
	}

	private int debugDisplayPieces(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Holder.Reference<Structure> structure = ResourceKeyArgument.getStructure(context, "filter_structure");

		if (!structure.isBound()) return 0;

		CommandSourceStack source = context.getSource();
		ServerLevel level = source.getLevel();
		BlockPos commandPos = BlockPos.containing(source.getPosition());

		StructureStart structureAt = level.structureManager().getStructureAt(commandPos, structure.value());

		BoundingBox structureBox = structureAt.getBoundingBox();
		int successes = this.displayUtil.spawnBlockDisplay(level, structureBox, Blocks.RED_STAINED_GLASS.defaultBlockState(), 0.01f) ? 1 : 0;

		List<StructurePiece> structurePieces = structureAt.getPieces();
		int maxPieces = structurePieces.size();
		for (StructurePiece piece : structurePieces) {
			BlockState displayState = piece instanceof ProgressionPiece shieldablePiece && shieldablePiece.isComponentProtected() ? Blocks.LIME_STAINED_GLASS.defaultBlockState() : Blocks.LIGHT_BLUE_STAINED_GLASS.defaultBlockState();
			ResourceLocation key = BuiltInRegistries.STRUCTURE_PIECE.getKey(piece.getType());
			float padding = Mth.lerp((float) successes / maxPieces, 0.003f, 0.025f);
			BoundingBox boundingBox = piece.getBoundingBox();
			if (this.displayUtil.spawnBlockDisplay(level, boundingBox, displayState, padding)) {
				MutableComponent nameLabel = net.minecraft.network.chat.Component.literal(key == null ? "missing key" : key.toString());
				this.displayUtil.setTextEntity(level, (boundingBox.minX() + boundingBox.maxX() + 1) * 0.5, boundingBox.minY() - padding, boundingBox.maxZ() + padding + 1, Display.BillboardConstraints.FIXED, nameLabel);

				successes++;
			}
		}

		return successes;
	}

	private int clearDisplayPieces(CommandContext<CommandSourceStack> context) {
		MinecraftServer server = context.getSource().getServer();

		// Extremely lazy way of clearing the entities without having to access the level entities with a scope, etc
		for (String command : List.of("kill @e[type=minecraft:text_display]", "kill @e[type=minecraft:block_display]")) {
			server.getCommands().performCommand(server.getCommands().getDispatcher().parse(command, context.getSource()), command);
		}

		return 0;
	}
}
