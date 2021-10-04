package twilightforest.mixin.plugin.patches;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import twilightforest.TwilightForestMod;
import java.util.ArrayList;
import java.util.List;

public interface Patch {
    void applyMethod(MethodNode node);

    void applyClass(ClassNode classNode);

    String getMixinClass();

    String getMethodName();

    String getMethodDesc();

    static AbstractInsnNode findFirstInstructionAfter(MethodNode method, int opCode, int startIndex) {
        for (int i = Math.max(0, startIndex); i < method.instructions.size(); i++) {
            AbstractInsnNode ain = method.instructions.get(i);
            if (ain.getOpcode() == opCode) {
                return ain;
            }
        }
        return null;
    }

    /**
     * Builds a new {@link InsnList} out of the specified AbstractInsnNodes
     *
     * @param nodes The nodes you want to add
     * @return A new list with the nodes
     */
    static InsnList listOf(AbstractInsnNode... nodes) {
        InsnList list = new InsnList();
        for (AbstractInsnNode node : nodes)
            list.add(node);
        return list;
    }

    /**
     * Finds the first instruction with matching opcode
     *
     * @param method the method to search in
     * @param opCode the opcode to search for
     * @return the found instruction node or null if none matched
     */
    static AbstractInsnNode findFirstInstruction(MethodNode method, int opCode) {
        return findFirstInstructionAfter(method, opCode, 0);
    }

    static String remapMethodDesc(String desc) {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        StringBuilder builder = new StringBuilder("(");
        for (String test : desc.split(";")) {
            if (test.startsWith("(L") || test.startsWith("L")) {
                builder.append('L' + remapper.mapClassName("intermediary", test.replace("(L", "").replaceFirst("L", "").replace('/', '.')).replace('.', '/') + ';');
            } else if (test.contains(")")) {
                builder.append(')' + remapper.mapClassName("intermediary", test.replace(")", "").replaceFirst("L", "").replace('/','.')));
            }
        }
        return desc;
    }
}
