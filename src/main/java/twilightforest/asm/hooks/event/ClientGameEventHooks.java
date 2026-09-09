package twilightforest.asm.hooks.event;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.LivingEntity;

public final class ClientGameEventHooks {
	/*private void setMusicInDimension(SelectMusicEvent event) {
		Music music = event.getOriginalMusic();
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == Musics.CREATIVE || music == Musics.UNDER_WATER) && TFDimension.isTwilightWorldOnClient(Minecraft.getInstance().level)) {
			event.setMusic(Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition()).value().getBackgroundMusic().orElse(Musics.GAME));
		}
	}*/

	/*private void renderAurora(RenderLevelStageEvent.AfterWeather event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;

		if (aurora > 0 || lastAurora > 0) {
			BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

			final float scale = 2048F * (mc.options.getEffectiveRenderDistance() * 16F / 32F);
			Vec3 pos = event.getLevelRenderState().cameraRenderState.pos;
			float y = (float) (256F - pos.y());
			buffer.addVertex(-scale, y, scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(-scale, y, -scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(scale, y, -scale).setColor(1F, 1F, 1F, 1F);
			buffer.addVertex(scale, y, scale).setColor(1F, 1F, 1F, 1F);

			float alpha = Mth.lerp(mc.getDeltaTracker().getGameTimeDeltaTicks(), lastAurora, aurora) / 60F * 0.5F;
			auroraRenderer.draw(
				buffer.buildOrThrow(),
				alpha,
				Mth.abs((int) mc.level.getBiomeManager().biomeZoomSeed),
				(float) pos.x(),
				(float) pos.y(),
				(float) pos.z()
			);
		}
	}*/

	private boolean areTrinketsEquipped(LivingEntity entity) {
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			//return CuriosCompat.isCurioEquippedAndVisible(entity, stack -> stack.getItem() instanceof TrophyItem);
		}
		return false;
	}
}
