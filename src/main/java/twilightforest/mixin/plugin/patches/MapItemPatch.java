package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class MapItemPatch implements Patch {

    @Override
    public void applyMethod(MethodNode methodNode) {
        AbstractInsnNode insn = null;
        for (var index = 0; index < methodNode.instructions.size() - 1; index++) {
            AbstractInsnNode node = methodNode.instructions.get(index);
            if (insn == null &&

                    node instanceof VarInsnNode varInsnNode &&

                    node.getOpcode() == Opcodes.ASTORE &&

                    varInsnNode.var == 6

            )
                insn = node;

        }
        methodNode.instructions.insertBefore(
                insn,
                Patch.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ALOAD, 2),
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "twilightforest/ASMHooks",
                                "renderMapData",
                                FabricLoader.getInstance().isDevelopmentEnvironment() ? "(Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;" : "(Lnet/minecraft/class_22;Lnet/minecraft/class_1799;Lnet/minecraft/world/level/Level;)Lnet/minecraft/class_22",
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
        return FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_1806");
    }

    @Override
    public String getMethodName() {
        return "appendHoverText";
    }

    @Override
    public String getMethodDesc() {
        return Patch.remapMethodDesc("(Lnet/minecraft/class_1799;Lnet/minecraft/class_1937;Ljava/util/List;Lnet/minecraft/class_1836;)V");
    }
}
