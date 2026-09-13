package twilightforest;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class TFMixinPlugin implements IMixinConfigPlugin {
	private static final FabricLoader LOADER = FabricLoader.getInstance();

	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {
		return "";
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return switch (mixinClassName) {
			case "twilightforest.mixin.UtilMixin",
				 "twilightforest.mixin.BoatRendererMixin"
				-> !LOADER.isModLoaded("kilt");

			case "twilightforest.mixin.compat.KiltBoatRendererMixin",
				 "twilightforest.mixin.compat.KiltBoatRendererCompatibilityMixin"
				-> LOADER.isModLoaded("kilt");

			default -> true;
		};
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {
		return List.of();
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}