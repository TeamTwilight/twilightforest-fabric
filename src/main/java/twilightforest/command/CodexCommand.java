package twilightforest.command;

import net.minecraft.server.level.ServerPlayer;

import com.codex.twilight.network.CodexGogglesSurveyPayload;
import com.codex.twilight.network.CodexHitFlashPayload;
import com.codex.twilight.network.CodexNetworking;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

/**
 * Phase F1.4 — {@code /codex} ops command exposing paired-client status
 * counts. All sub-commands are read-only and purely additive.
 *
 * <ul>
 *   <li>{@code /codex status} — caller's own codex-twilight client channel status</li>
 *   <li>{@code /codex status <player>} — query another player's client channel status (op level 2)</li>
 *   <li>{@code /codex overlays} — report that legacy server-side visual overlays are disabled</li>
 *   <li>{@code /codex pack-uuid} — legacy compatibility note; paired client channels are authoritative</li>
 * </ul>
 */
public final class CodexCommand {

    private CodexCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
                                 net.minecraft.commands.CommandBuildContext registryAccess,
                                 Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("codex")
                .then(Commands.literal("status")
                        .executes(ctx -> statusSelf(ctx.getSource()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .requires(src -> src.hasPermission(2))
                                .executes(ctx -> statusOther(ctx.getSource(),
                                        EntityArgument.getPlayer(ctx, "player")))))
                .then(Commands.literal("overlays")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> overlays(ctx.getSource())))
                .then(Commands.literal("pack-uuid")
                        .executes(ctx -> packUuid(ctx.getSource())))
                .then(Commands.literal("test-place")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.literal("giant").executes(ctx -> testPlaceGiant(ctx.getSource())))
                        .then(Commands.literal("aurora").executes(ctx -> testPlaceAurora(ctx.getSource())))
                        .then(Commands.literal("patch").executes(ctx -> testPlacePatch(ctx.getSource())))
                        .then(Commands.literal("all").executes(ctx -> {
                            int n = 0;
                            n += testPlaceGiant(ctx.getSource());
                            n += testPlaceAurora(ctx.getSource());
                            n += testPlacePatch(ctx.getSource());
                            return n;
                        }))
                        .then(Commands.literal("clear").executes(ctx -> testClear(ctx.getSource()))))
        );
    }

    /**
     * Compute a position 2 blocks in front of the player at eye level minus a
     * block, so placed test blocks are at the player's feet level slightly
     * forward — easy to walk up and inspect.
     */
    private static net.minecraft.core.BlockPos frontOf(ServerPlayer player, int forward) {
        net.minecraft.world.phys.Vec3 dir = player.getLookAngle();
        // Flatten to horizontal plane so we don't drop blocks underground when
        // the player looks down.
        double dx = dir.x;
        double dz = dir.z;
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 1.0E-4) {
            dx = -Math.sin(Math.toRadians(player.getYRot()));
            dz = Math.cos(Math.toRadians(player.getYRot()));
            len = 1.0;
        }
        dx /= len;
        dz /= len;
        int x = (int) Math.floor(player.getX() + dx * forward);
        int z = (int) Math.floor(player.getZ() + dz * forward);
        int y = (int) Math.floor(player.getY());
        return new net.minecraft.core.BlockPos(x, y, z);
    }

    private static int testPlaceGiant(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("requires a player source"));
            return 0;
        }
        net.minecraft.core.BlockPos base = frontOf(player, 3);
        net.minecraft.server.level.ServerLevel level = player.serverLevel();
        // Place a row: giant_cobblestone, giant_log, giant_obsidian, giant_leaves
        net.minecraft.world.level.block.Block[] blocks = new net.minecraft.world.level.block.Block[]{
                twilightforest.init.TFBlocks.GIANT_COBBLESTONE.get(),
                twilightforest.init.TFBlocks.GIANT_LOG.get(),
                twilightforest.init.TFBlocks.GIANT_OBSIDIAN.get(),
                twilightforest.init.TFBlocks.GIANT_LEAVES.get(),
        };
        for (int i = 0; i < blocks.length; i++) {
            net.minecraft.core.BlockPos pos = base.east(i * 2);
            level.setBlock(pos, blocks[i].defaultBlockState(), 3);
        }
        source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                "[test-place] 4 giant blocks placed at " + base.getX() + "," + base.getY() + "," + base.getZ()
                        + " (paired clients render the real Twilight blocks directly)"), true);
        return 4;
    }

    private static int testPlaceAurora(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("requires a player source"));
            return 0;
        }
        net.minecraft.core.BlockPos base = frontOf(player, 5);
        net.minecraft.server.level.ServerLevel level = player.serverLevel();
        net.minecraft.world.level.block.state.BlockState aurora = twilightforest.init.TFBlocks.AURORA_BLOCK.get().defaultBlockState();
        // 4×4 horizontal grid so noise variation is visible
        for (int dx = 0; dx < 4; dx++) {
            for (int dz = 0; dz < 4; dz++) {
                level.setBlock(base.offset(dx, 0, dz), aurora, 3);
            }
        }
        source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                "[test-place] 4x4 aurora_block grid placed at " + base.getX() + "," + base.getY() + "," + base.getZ()), true);
        return 16;
    }

    private static int testPlacePatch(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("requires a player source"));
            return 0;
        }
        net.minecraft.core.BlockPos base = frontOf(player, 3);
        net.minecraft.server.level.ServerLevel level = player.serverLevel();
        level.setBlock(base, twilightforest.init.TFBlocks.CLOVER_PATCH.get().defaultBlockState(), 3);
        level.setBlock(base.east(2), twilightforest.init.TFBlocks.MOSS_PATCH.get().defaultBlockState(), 3);
        source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                "[test-place] clover_patch + moss_patch placed at " + base.getX() + "," + base.getY() + "," + base.getZ()), true);
        return 2;
    }

    private static int testClear(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(net.minecraft.network.chat.Component.literal("requires a player source"));
            return 0;
        }
        net.minecraft.core.BlockPos centre = frontOf(player, 4);
        net.minecraft.server.level.ServerLevel level = player.serverLevel();
        net.minecraft.world.level.block.state.BlockState air = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        int cleared = 0;
        for (int dx = -8; dx <= 8; dx++) {
            for (int dz = -8; dz <= 8; dz++) {
                for (int dy = -1; dy <= 4; dy++) {
                    net.minecraft.core.BlockPos p = centre.offset(dx, dy, dz);
                    net.minecraft.world.level.block.state.BlockState st = level.getBlockState(p);
                    net.minecraft.world.level.block.Block b = st.getBlock();
                    if (b == twilightforest.init.TFBlocks.GIANT_COBBLESTONE.get()
                            || b == twilightforest.init.TFBlocks.GIANT_LOG.get()
                            || b == twilightforest.init.TFBlocks.GIANT_OBSIDIAN.get()
                            || b == twilightforest.init.TFBlocks.GIANT_LEAVES.get()
                            || b == twilightforest.init.TFBlocks.AURORA_BLOCK.get()
                            || b == twilightforest.init.TFBlocks.CLOVER_PATCH.get()
                            || b == twilightforest.init.TFBlocks.MOSS_PATCH.get()) {
                        level.setBlock(p, air, 3);
                        cleared++;
                    }
                }
            }
        }
        final int finalCleared = cleared;
        source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                "[test-place] cleared " + finalCleared + " test blocks"), true);
        return cleared;
    }

    private static int statusSelf(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("/codex status without args requires a player source. Use /codex status <player> from console."));
            return 0;
        }
        return printStatusFor(source, player);
    }

    private static int statusOther(CommandSourceStack source, ServerPlayer target) {
        return printStatusFor(source, target);
    }

    private static int printStatusFor(CommandSourceStack source, ServerPlayer player) {
        boolean paired = CodexNetworking.canSendPairedClientPayload(player);
        boolean hitFlash = canSend(player, CodexHitFlashPayload.TYPE);
        boolean goggles = canSend(player, CodexGogglesSurveyPayload.TYPE);

        source.sendSuccess(() -> Component.literal("§6[Codex Client Status]"), false);
        source.sendSuccess(() -> Component.literal("  player           : " + player.getName().getString()), false);
        source.sendSuccess(() -> Component.literal("  paired client    : " + paired), false);
        source.sendSuccess(() -> Component.literal("  hit_flash        : " + hitFlash), false);
        source.sendSuccess(() -> Component.literal("  goggles_survey   : " + goggles), false);
        return 1;
    }

    private static int overlays(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("§6[Codex Twilight Overlays]"), false);
        source.sendSuccess(() -> Component.literal("  legacy server-side display overlays: disabled"), false);
        source.sendSuccess(() -> Component.literal("  paired clients render Twilight visuals locally"), false);
        return 1;
    }

    private static int packUuid(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("legacy pack UUID is unused; paired client payload channels are authoritative"), false);
        return 1;
    }

    private static boolean canSend(ServerPlayer player, net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<?> type) {
        try {
            return ServerPlayNetworking.canSend(player, type);
        } catch (Throwable ignored) {
            return false;
        }
    }

}
