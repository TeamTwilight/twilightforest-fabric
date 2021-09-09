package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ItemInHandRenderPatch implements Patch {
    @Override
    public void applyMethod(MethodNode methodNode) {
        VarInsnNode insn = null;
        for (var index = 0; index < methodNode.instructions.size() - 1; index++) {
            AbstractInsnNode node = methodNode.instructions.get(index);
            if (insn == null &&

                    node instanceof VarInsnNode &&

                    node.getOpcode() == Opcodes.ASTORE &&

                    ((VarInsnNode) node).var == 6

            )
                insn = (VarInsnNode) node;

        }
        methodNode.instructions.insertBefore(
                insn,
                Patch.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 4),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new FieldInsnNode(Opcodes.GETFIELD, FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_759").replace('.', '/'), FabricLoader.getInstance().getMappingResolver().mapFieldName("intermediary", "net.minecraft.class_759", "field_4050", "Lnet/minecraft/class_310;"), "L"+FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary","net.minecraft.class_310")+";"),
                        new FieldInsnNode(Opcodes.GETFIELD, FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_310").replace('.', '/'), FabricLoader.getInstance().getMappingResolver().mapFieldName("intermediary", "net.minecraft.class_310", "field_1687", "Lnet/minecraft/class_638;"), "L"+FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary","net.minecraft.class_638")+";"),
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "twilightforest/ASMHooks",
                                "renderMapData",
                                "(Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;",
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
        return FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_759");
    }

    @Override
    public String getMethodName() {
        return FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_759", "method_3223", "(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_1799;)V");
    }

    @Override
    public String getMethodDesc() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            return "(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ItemStack;)V";
        return "(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_1799;)V";
    }
}
