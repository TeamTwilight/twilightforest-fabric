package twilightforest.mixin.plugin.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ServerLevelPatch implements Patch {
    @Override
    public void applyMethod(MethodNode node) {
        node.instructions.insertBefore(
                Patch.findFirstInstruction(node, Opcodes.ARETURN),
                Patch.listOf(
                        new VarInsnNode(Opcodes.ILOAD, 1),
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "twilightforest/ASMHooks",
                                "multipartFromID",
                                "(Lnet/minecraft/world/entity/Entity;I)Lnet/minecraft/world/entity/Entity;",
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
        return "net.minecraft.server.level.ServerLevel";
    }

    @Override
    public String getMethodName() {
        return "getEntityOrPart";
    }

    @Override
    public String getMethodDesc() {
        return "(I)Lnet/minecraft/world/entity/Entity;";
    }
}
