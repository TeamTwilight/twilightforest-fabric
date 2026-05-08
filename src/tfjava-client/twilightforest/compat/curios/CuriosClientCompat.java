package twilightforest.compat.curios;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.util.FastColor;
import twilightforest.compat.curios.renderer.CharmOfKeepingRenderer;
import twilightforest.compat.curios.renderer.CharmOfLifeNecklaceRenderer;
import twilightforest.compat.curios.renderer.CurioHeadRenderer;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public final class CuriosClientCompat {
	private static boolean bootstrapped;

	private CuriosClientCompat() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_LIFE_1.get(), new CharmOfLifeNecklaceRenderer(FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.5F, 0.5F)));
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_LIFE_2.get(), new CharmOfLifeNecklaceRenderer(FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.9F, 0.0F)));
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_1.get(), new CharmOfKeepingRenderer());
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_2.get(), new CharmOfKeepingRenderer());
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_3.get(), new CharmOfKeepingRenderer());

		TrinketRendererRegistry.registerRenderer(TFBlocks.NAGA_TROPHY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.LICH_TROPHY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.MINOSHROOM_TROPHY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.HYDRA_TROPHY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.UR_GHAST_TROPHY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.ALPHA_YETI_TROPHY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.SNOW_QUEEN_TROPHY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.QUEST_RAM_TROPHY.get().asItem(), new CurioHeadRenderer());

		TrinketRendererRegistry.registerRenderer(TFBlocks.CICADA.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.FIREFLY.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.MOONWORM.get().asItem(), new CurioHeadRenderer());

		TrinketRendererRegistry.registerRenderer(TFBlocks.CREEPER_SKULL_CANDLE.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.PIGLIN_SKULL_CANDLE.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.PLAYER_SKULL_CANDLE.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.SKELETON_SKULL_CANDLE.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.WITHER_SKELETON_SKULL_CANDLE.get().asItem(), new CurioHeadRenderer());
		TrinketRendererRegistry.registerRenderer(TFBlocks.ZOMBIE_SKULL_CANDLE.get().asItem(), new CurioHeadRenderer());
	}
}
