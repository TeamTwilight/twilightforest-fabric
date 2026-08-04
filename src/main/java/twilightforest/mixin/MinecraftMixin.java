package twilightforest.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import twilightforest.client.MagicPaintingTextureManager;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

	@Shadow
	@Final
	private ReloadableResourceManager resourceManager;

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resources/PaintingTextureManager;<init>(Lnet/minecraft/client/renderer/texture/TextureManager;)V"
		)
	)
	private void twilightforest$initMagicPaintingTextureManager(
		GameConfig gameConfig,
		CallbackInfo ci
	) {
		MagicPaintingTextureManager.instance = new MagicPaintingTextureManager(Minecraft.getInstance().getTextureManager());
		this.resourceManager.registerReloadListener(MagicPaintingTextureManager.instance);
	}
}