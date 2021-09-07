package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class EntityRenderDispatcherPatch implements Patch {
    @Override
    public void applyMethod(MethodNode methodNode) {
        AbstractInsnNode lastInstruction = null;
        for (int index = methodNode.instructions.size() - 1; index > 0; index--) {
            AbstractInsnNode node = methodNode.instructions.get(index);
            if (lastInstruction == null &&

                    node instanceof InsnNode &&

                    node.getOpcode() == Opcodes.ARETURN

            )
                lastInstruction = node;

        }
        methodNode.instructions.insertBefore(
                lastInstruction,
                Patch.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "twilightforest/ASMHooks",
                                "getMultipartRenderer",
                                "(Lnet/minecraft/client/renderer/entity/EntityRenderer;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;",
                                false
                        )
                )
        );
    }

    @Override
    public void applyClass(ClassNode classNode) {
//        for(MethodNode methodNode : classNode.methods) {
//            if(methodNode.name.equals("onResourceManagerReload")) {
//                methodNode.instructions.insert(
//                        Patch.findFirstInstruction(methodNode, Opcodes.INVOKESPECIAL),
//                        Patch.listOf(
//                                new MethodInsnNode(
//                                        Opcodes.INVOKESTATIC,
//                                        "twilightforest/ASMHooks",
//                                        "bakeMultipartRenders",
//                                        "(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;",
//                                        false
//                                )
//                        )
//                );
//            }
//        }
    }

    @Override
    public String getMixinClass() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "net.minecraft.client.renderer.entity.EntityRenderDispatcher";
        return "net.minecraft.class_898";
    }

    @Override
    public String getMethodName() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "getRenderer";
        return "method_3550";
    }

    @Override
    public String getMethodDesc() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;";
        return "(Lnet/minecraft/class_1297;)Lnet/minecraft/class_897;";
    }
}
