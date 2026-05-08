package com.codex.twilight.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.client.renderer.block.BrazierRenderer;
import twilightforest.client.renderer.block.CandelabraRenderer;
import twilightforest.client.renderer.block.CicadaRenderer;
import twilightforest.client.renderer.block.DryingRackRenderer;
import twilightforest.client.renderer.block.FireflyRenderer;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.client.renderer.block.KeepsakeCasketRenderer;
import twilightforest.client.renderer.block.MoonwormRenderer;
import twilightforest.client.renderer.block.OminousCandleRenderer;
import twilightforest.client.renderer.block.ReactorDebrisRenderer;
import twilightforest.client.renderer.block.RedThreadRenderer;
import twilightforest.client.renderer.block.SinisterSpawnerRenderer;
import twilightforest.client.renderer.block.SkullCandleRenderer;
import twilightforest.client.renderer.block.SkullChestRenderer;
import twilightforest.client.renderer.block.TFChestRenderer;
import twilightforest.client.renderer.block.TrophyRenderer;
import twilightforest.init.TFBlockEntities;

/**
 * F2.5 — paired client BlockEntityRenderer registration.
 *
 * <p>Workflow for the project owner: as each upstream Twilight Forest BER
 * is translated from NeoForge to Fabric and dropped into
 * {@code src/tfjava-client/twilightforest/client/renderer/block/}, wire the
 * matching {@code BlockEntityRendererRegistry.register(...)} line below.
 * The Gradle build then compiles the new file from the LGPL-licensed
 * derivative-work source dir alongside this bootstrap, so the mod jar ships
 * the BER as soon as the next {@code gradlew buildAndInstall} runs.
 *
 * <p>Until each line is enabled, the corresponding TF block entity falls
 * through to its legacy-disguised vanilla BER (chest, barrel, brewing stand,
 * candle, etc.). That's still functional — the block is visible and animated
 * for both vanilla and paired clients — just not pixel-identical to upstream.
 *
 * <p>Registration order is independent; pick whichever BER is cheapest to
 * port first. Suggested priority by visibility:
 * <ol>
 *   <li>{@code KEEPSAKE_CASKET} (player death-loot box, high-frequency)</li>
 *   <li>{@code BRAZIER} / {@code OMINOUS_CANDLE} (boss-room ambience)</li>
 *   <li>{@code MASON_JAR} / {@code AMBIENT_JAR} (utility blocks)</li>
 *   <li>{@code FIREFLY} / {@code CICADA} / {@code MOONWORM} (small ambient sprites)</li>
 *   <li>{@code TROPHY} / {@code SKULL_CHEST} / {@code TF_CHEST} (boss reward visuals)</li>
 *   <li>{@code DRYING_RACK} / {@code RED_THREAD} / {@code REACTOR_DEBRIS} (rare blocks)</li>
 *   <li>{@code SINISTER_SPAWNER} / {@code CANDELABRA} / {@code SKULL_CANDLE} (cosmetic)</li>
 * </ol>
 *
 * <p>Once a registration is enabled, you can verify the wire-up by:
 * <pre>
 *   ./gradlew compileClientJava   # confirms the upstream class compiles
 *   ./gradlew buildAndInstall     # rebuilds the mod jar
 *   # restart local server, /tp to a TF block, observe the rendering
 * </pre>
 */
public final class ClientBlockEntityRendererBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger("CodexTwilight/client/ber");

    private ClientBlockEntityRendererBootstrap() {
    }

    public static void bootstrap() {
        BlockEntityRendererRegistry.register(BlockEntityType.CHEST, TFChestRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityType.TRAPPED_CHEST, TFChestRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.MASON_JAR, JarRenderer.MasonJarRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.BRAZIER, BrazierRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.CANDELABRA, CandelabraRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.CICADA, CicadaRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.DRYING_RACK, DryingRackRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.FIREFLY, FireflyRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.KEEPSAKE_CASKET, KeepsakeCasketRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.MOONWORM, MoonwormRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.OMINOUS_CANDLE, OminousCandleRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.RED_THREAD, RedThreadRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.REACTOR_DEBRIS, ReactorDebrisRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.SINISTER_SPAWNER, SinisterSpawnerRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.SKULL_CANDLE, SkullCandleRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.SKULL_CHEST, SkullChestRenderer::new);
        BlockEntityRendererRegistry.register(TFBlockEntities.TROPHY, TrophyRenderer::new);

        LOGGER.debug("F2.5 BER scaffold loaded with TFChest, ambient critter, casket, trophy, spawner, DryingRack, Candelabra, Brazier, OminousCandle, RedThread, ReactorDebris, and SkullCandle renderers.");
    }
}
