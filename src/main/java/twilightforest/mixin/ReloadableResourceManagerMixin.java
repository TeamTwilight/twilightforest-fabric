package twilightforest.mixin;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.client.JappaPackReloadListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {

	@Shadow
	@Final
	public List<PreparableReloadListener> listeners;

	@Inject(
		method = "createReload",
		at = @At("HEAD")
	)
	private void twilightforest$addFirstListener(
		Executor backgroundExecutor,
		Executor gameExecutor,
		CompletableFuture<Unit> waitingFor,
		List<PackResources> resourcePacks,
		CallbackInfoReturnable<ReloadInstance> cir
	) {
		List<PreparableReloadListener> listeners = this.listeners;
		listeners.remove(JappaPackReloadListener.INSTANCE);
		listeners.addFirst(JappaPackReloadListener.INSTANCE);
	}
}