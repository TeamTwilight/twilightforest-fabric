package twilightforest.mixin.plugin.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ServerLevelEntitycallbackPatch implements Patch {
    @Override
    public void applyMethod(MethodNode node) {
        node.instructions.insert(
                Patch.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "twilightforest/ASMHooks",
                                "trackingStart",
                                "(Lnet/minecraft/world/entity/Entity;)V",
                                false
                        )
                )
        );
    }

    @Override
    public void applyClass(ClassNode classNode) {
//        for(MethodNode node : classNode.methods) {
//            if(node.name.equals("onTrackingEnd")) {
//                node.instructions.insert(
//                        Patch.listOf(
//                                new VarInsnNode(Opcodes.ALOAD, 1),
//                                new MethodInsnNode(
//                                        Opcodes.INVOKESTATIC,
//                                        "twilightforest/ASMHooks",
//                                        "trackingEnd",
//                                        "(Lnet/minecraft/world/entity/Entity;)V",
//                                        false
//                                )
//                        )
//                );
//            }
//        }
    }


    @Override
    public String getMixinClass() {
        return "net.minecraft.server.level.ServerLevel$EntityCallbacks";
    }

    @Override
    public String getMethodName() {
        return "onTrackingStart";
    }

    @Override
    public String getMethodDesc() {
        return "(Lnet/minecraft/world/entity/Entity;)V";
    }
}
