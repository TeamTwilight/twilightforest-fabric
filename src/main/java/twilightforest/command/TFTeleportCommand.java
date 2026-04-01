package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFDimension;

import java.util.Locale;

@tamaized.beanification.Component
public class TFTeleportCommand {
	private static final SimpleCommandExceptionType PLAYER_ONLY = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.teleport.player_only"));
	private static final SimpleCommandExceptionType DIMENSION_MISSING = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.teleport.dimension_missing"));
	private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(Component.translatable("commands.teleport.invalidPosition"));

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("tp")
			.requires(cs -> cs.hasPermission(2))
			.executes(this::run);
	}

	private int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		CommandSourceStack source = ctx.getSource();
		if (!(source.getEntity() instanceof ServerPlayer player)) {
			throw PLAYER_ONLY.create();
		}

		ServerLevel twilight = source.getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (twilight == null) {
			throw DIMENSION_MISSING.create();  // should never happen
		}

		Vec3 pos = player.position();
		twilight.getChunk(player.blockPosition());
		double y = twilight.getHeight(Heightmap.Types.MOTION_BLOCKING, player.blockPosition().getX(), player.blockPosition().getZ());
		Vec3 teleportPos = new Vec3(pos.x(), y, pos.z());
		if (!twilight.isInWorldBounds(BlockPos.containing(teleportPos)))
			throw INVALID_POSITION.create();
		player.teleportTo(twilight, teleportPos.x(), teleportPos.y(), teleportPos.z(), player.getYRot(), player.getXRot());
		String formattedX = String.format(Locale.ROOT, "%.1f", pos.x());
		String formattedY = String.format(Locale.ROOT, "%.1f", y);
		String formattedZ = String.format(Locale.ROOT, "%.1f", pos.z());
		source.sendSuccess(() -> Component.translatable("commands.tffeature.teleport.success", formattedX, formattedY, formattedZ), false);
		return Command.SINGLE_SUCCESS;
	}
}
