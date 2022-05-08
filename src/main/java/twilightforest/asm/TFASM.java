package twilightforest.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TFASM implements Runnable {
    @Override
    public void run() {
        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
        // foliage.js
        String biomeClass = resolver.mapClassName("intermediary","net.minecraft.class_1959").replace('.', '/');
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary","net.minecraft.class_1163"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if(!methodNode.name.equals("lambda$static$0")) return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.IRETURN),
                        ASM.listOf(
                                new VarInsnNode(Opcodes.ALOAD, 0),
                                new VarInsnNode(Opcodes.DLOAD, 1),
                                new VarInsnNode(Opcodes.DLOAD, 3),
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "foliage",
                                        "(IL" + biomeClass + ";DD)I",
                                        false
                                )
                        )
                );
            });
        });

        // music.js
        String musicClass = resolver.mapClassName("intermediary", "net.minecraft.class_5195").replace('.', '/');
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary", "net.minecraft.class_1142"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(resolver.mapMethodName("intermediary", "net.minecraft.class_1142", "method_18669", "()V")))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                        ASM.findFirstInstruction(methodNode, Opcodes.INVOKEVIRTUAL),
                        ASM.listOf(
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "music",
                                        "(L" + musicClass + ";)L" + musicClass +";",
                                        false
                                )
                        )
                );
            });
        });

        // multipart.js
        // ServerEntity
        String entityClass = resolver.mapClassName("intermediary", "net.minecraft.class_1297").replace('.', '/');
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary", "net.minecraft.class_3231"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(resolver.mapMethodName("intermediary", "net.minecraft.class_3231", "method_14306", "()V")))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                        ASM.findFirstInstruction(methodNode, Opcodes.GETFIELD),
                        ASM.listOf(
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "updateMultiparts",
                                        "(L" + entityClass + ";)L" + entityClass +";",
                                        false
                                )
                        )
                );
            });
        });
        String resourceManagerClass = resolver.mapClassName("intermediary", "net.minecraft.class_3300").replace('.', '/');
        String reloadName = FabricLoader.getInstance().isDevelopmentEnvironment() ? "onResourceManagerReload" : "method_14491";
        String providerContext = resolver.mapClassName("intermediary", "net.minecraft.class_5617$class_5618").replace('.', '/');
        // EntityRenderDispatcher
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary", "net.minecraft.class_898"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(reloadName))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                        ASM.findFirstInstruction(methodNode, Opcodes.INVOKESPECIAL),
                        ASM.listOf(
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "bakeMultipartRenders",
                                        "(L" + providerContext + ";)L" + providerContext + ";",
                                        false
                                )
                        )
                );
            });
        });
        // EntityRenderDispatcher
        String getRendererName = FabricLoader.getInstance().isDevelopmentEnvironment() ? "getRenderer" : "method_3953";
        String entityRendererClass = resolver.mapClassName("intermediary", "net.minecraft.class_897").replace('.', '/');
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary", "net.minecraft.class_898"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(getRendererName))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                AbstractInsnNode lastInstruction = null;
                for (var index = instructions.size() - 1; index > 0; index--) {
                    var /*org.objectweb.asm.tree.AbstractInsnNode*/ node = instructions.get(index);
                    if (lastInstruction == null &&

                            node instanceof InsnNode &&

                            node.getOpcode() == Opcodes.ARETURN

                    )
                        lastInstruction = node;

                }
                instructions.insertBefore(
                        lastInstruction,
                        ASM.listOf(
                                new VarInsnNode(Opcodes.ALOAD, 1),
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "getMultipartRenderer",
                                        "(L" + entityRendererClass + ";L" + entityClass + ";)L" + entityRendererClass + ";",
                                        false
                                )
                        )
                );
            });
        });

        String renderLevelName = FabricLoader.getInstance().isDevelopmentEnvironment() ? "renderLevel" : "method_22710";
        // LevelRenderer
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary", "net.minecraft.class_761"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(renderLevelName))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                AbstractInsnNode lastInstruction = null;
                for (var index = instructions.size() - 1; index > 0; index--) {
                    AbstractInsnNode node = instructions.get(index);
                    if (lastInstruction == null &&

                            node instanceof MethodInsnNode methodInsnNode &&

                            node.getOpcode() == Opcodes.INVOKEVIRTUAL &&

                            equate(methodInsnNode.owner, resolver.mapClassName("intermediary", "net.minecraft.class_638").replace('.', '/')) &&

                            equate(methodInsnNode.name, FabricLoader.getInstance().isDevelopmentEnvironment() ? "entitiesForRendering" : "method_18112") &&

                            equate(methodInsnNode.desc, "()Ljava/lang/Iterable;")

                    )
                        lastInstruction = node;

                }
                instructions.insert(
                        lastInstruction,
                        ASM.listOf(
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "renderMutiparts",
                                        "(Ljava/lang/Iterable;)Ljava/lang/Iterable;",
                                        false
                                )
                        )
                );
            });
        });
    }

    public boolean equate(Object a, Object b) {
        return a.equals(b);
    }
}
