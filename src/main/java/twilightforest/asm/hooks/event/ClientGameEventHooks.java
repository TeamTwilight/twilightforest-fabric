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

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	/*private void updateBowFOV(ComputeFovModifierEvent event) {
		Player player = event.getPlayer();
		if (player.isUsingItem()) {
			Item useItem = player.getUseItem().getItem();
			if (useItem instanceof TripleBowItem || useItem instanceof EnderBowItem || useItem instanceof IceBowItem || useItem instanceof SeekerBowItem) {
				float f = player.getTicksUsingItem() / 20.0F;
				f = f > 1.0F ? 1.0F : f * f;
				event.setNewFovModifier((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, (event.getFovModifier() * (1.0F - f * 0.15F))));
			}
		}
	}*/

	/*private void unrenderHeadWithTrophies(RenderLivingEvent.Pre<?, ?> event) {
		ItemStack stack = event.getEntity().getItemBySlot(EquipmentSlot.HEAD);
		boolean visible = !(stack.getItem() instanceof TrophyItem) && !areCuriosEquipped(event.getEntity());
		boolean isPlayer = event.getEntity() instanceof Player;
		if (event.getRenderer().getModel() instanceof HeadedModel headedModel) {
			headedModel.getHead().visible = visible && (!isPlayer || headedModel.getHead().visible);  // some mods like Better Combat can move player's head and hide it in the first person view
			if (event.getRenderer().getModel() instanceof HumanoidModel<?> humanoidModel) {
				humanoidModel.hat.visible = visible && (!isPlayer || humanoidModel.hat.visible);
			}
		}
	}*/

	/*private void renderCustomBossbars(CustomizeGuiOverlayEvent.BossEventProgress event) {
		if (event.getBossEvent() instanceof ClientTFBossBar bossEvent) {
			event.setCanceled(true);
			bossEvent.renderBossBar(event.getGuiGraphics(), event.getX(), event.getY());
		}
	}*/

	private boolean areTrinketsEquipped(LivingEntity entity) {
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			//return CuriosCompat.isCurioEquippedAndVisible(entity, stack -> stack.getItem() instanceof TrophyItem);
		}
		return false;
	}
}
