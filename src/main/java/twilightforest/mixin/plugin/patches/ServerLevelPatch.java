package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
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
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "net.minecraft.server.level.ServerLevel";
        return "net.minecraft.class_3218";
    }

    @Override
    public String getMethodName() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "getEntityOrPart";
        return "method_31424";
    }

    @Override
    public String getMethodDesc() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            return "(I)Lnet/minecraft/world/entity/Entity;";
        return "(I)Lnet/minecraft/class_1297;";
    }
}
