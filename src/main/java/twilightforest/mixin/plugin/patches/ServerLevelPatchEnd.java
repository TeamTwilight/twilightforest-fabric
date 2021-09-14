package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ServerLevelPatchEnd implements Patch {
    @Override
    public void applyMethod(MethodNode node) {
        node.instructions.insert(
                Patch.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "twilightforest/ASMHooks",
                                "trackingEnd",
                                FabricLoader.getInstance().isDevelopmentEnvironment() ? "(Lnet/minecraft/world/entity/Entity;)V" : "(Lnet/minecraft/class_1297;)V",
                                false
                        )
                )
        );
    }

    @Override
    public void applyClass(ClassNode classNode) {}

    @Override
    public String getMixinClass() {
        return FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_3218");
    }

    @Override
    public String getMethodName() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "onTrackingEnd";
        return "method_31437";
    }

    @Override
    public String getMethodDesc() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "(Lnet/minecraft/world/entity/Entity;)V";
        return "(Lnet/minecraft/class_1297;)V";
    }
}
