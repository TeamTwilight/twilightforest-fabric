package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
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
                                FabricLoader.getInstance().isDevelopmentEnvironment() ? "(Ljava/util/List;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;" : "(Ljava/util/List;Lnet/minecraft/class_1937;Lnet/minecraft/class_1297;Lnet/minecraft/class_238;Ljava/util/function/Predicate;)Ljava/util/List;",
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
        return FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_1937");
    }

    @Override
    public String getMethodName() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "getEntities";
        return "method_31592";
    }

    @Override
    public String getMethodDesc() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;";
        return "(Lnet/minecraft/class_1297;Lnet/minecraft/class_238;Ljava/util/function/Predicate;)Ljava/util/List;";
    }
}