package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class LevelRendererPatch implements Patch {
    @Override
    public void applyMethod(MethodNode methodNode) {
        MethodInsnNode lastInstruction = null;
        for (int index = methodNode.instructions.size() - 1; index > 0; index--) {
            var node = methodNode.instructions.get(index);
            if (lastInstruction == null &&

                    node instanceof MethodInsnNode &&

                    node.getOpcode() == Opcodes.INVOKEVIRTUAL &&

                    ((MethodInsnNode)node).owner.equals("net/minecraft/client/multiplayer/ClientLevel") &&

                    ((MethodInsnNode)node).name.equals("entitiesForRendering") &&

                    ((MethodInsnNode)node).desc.equals("()Ljava/lang/Iterable;")

            )
                lastInstruction = ((MethodInsnNode)node);

        }
        methodNode.instructions.insert(
                lastInstruction,
                Patch.listOf(
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "twilightforest/ASMHooks",
                                "renderMutiparts",
                                "(Ljava/lang/Iterable;)Ljava/lang/Iterable;",
                                false
                        )
                )
        );
    }

    @Override
    public void applyClass(ClassNode classNode) {

    }

    @Override
    public String getMixinClass() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "net.minecraft.client.renderer.LevelRenderer";
        return "net.minecraft.class_898";
    }

    @Override
    public String getMethodName() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "renderLevel";
        return "method_22710";
    }

    @Override
    public String getMethodDesc() {
        return "(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lcom/mojang/math/Matrix4f;)V";
    }
}
