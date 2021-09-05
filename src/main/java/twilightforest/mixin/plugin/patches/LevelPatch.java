package twilightforest.mixin.plugin.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class LevelPatch implements Patch {
    @Override
    public void applyMethod(MethodNode node) {
        node.instructions.insertBefore(
                Patch.findFirstInstruction(node, Opcodes.ARETURN),
                Patch.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ALOAD, 2),
                        new VarInsnNode(Opcodes.ALOAD, 3),
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "twilightforest/ASMHooks",
                                "multipartHitbox",
                                "(Ljava/util/List;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
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
        return "net.minecraft.world.level.Level";
    }

    @Override
    public String getMethodName() {
        return "getEntities";
    }

    @Override
    public String getMethodDesc() {
        return "(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate<-Lnet/minecraft/world/entity/Entity;>;)Ljava/util/List<Lnet/minecraft/world/entity/Entity;>;";
    }
}
