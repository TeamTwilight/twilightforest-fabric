package twilightforest.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import twilightforest.asm.ASM;

import java.util.List;
import java.util.Set;

public class TFMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override // We need to do this after mixin applies cause porting lib overwrites the method
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
		if (targetClassName.equals(resolver.mapClassName("intermediary", "net.minecraft.class_330$class_331"))) {
			targetClass.methods.forEach(methodNode -> {
				if (!methodNode.name.equals(FabricLoader.getInstance().isDevelopmentEnvironment() ? "draw" : "method_1777"))
					return;
				var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
				instructions.insertBefore(
						ASM.findFirstInstruction(methodNode, Opcodes.ISTORE),
						ASM.listOf(
								new VarInsnNode(Opcodes.ALOAD, 1),
								new VarInsnNode(Opcodes.ALOAD, 2),
								new VarInsnNode(Opcodes.ILOAD, 4),
								new MethodInsnNode(
										Opcodes.INVOKESTATIC,
										"twilightforest/ASMHooks",
										"mapRenderContext",
										"(L" + resolver.mapClassName("intermediary", "net.minecraft.class_4587").replace('.', '/') + ";L" + resolver.mapClassName("intermediary", "net.minecraft.class_4597").replace('.', '/') + ";I)V",
										false
								)
						)
				);
			});
		}
	}
}
