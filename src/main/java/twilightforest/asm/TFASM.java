package twilightforest.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

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

        // maprendercontext.js
        // ItemInHandRenderer
        String itemStackClass = resolver.mapClassName("intermediary", "net.minecraft.class_1799").replace('.', '/');
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary", "net.minecraft.class_759"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(FabricLoader.getInstance().isDevelopmentEnvironment() ? "renderArmWithItem" : "method_3228"))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var i = 0;
                for (var index = instructions.size() - 1; index > 0; index--) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (i == 0 &&

                            node instanceof FieldInsnNode fieldInsnNode &&

                            node.getOpcode() == Opcodes.GETSTATIC &&

                            equate(fieldInsnNode.owner, resolver.mapClassName("intermediary", "net.minecraft.class_1802").replace('.', '/')) &&

                            equate(fieldInsnNode.name, FabricLoader.getInstance().isDevelopmentEnvironment() ? "FILLED_MAP" : "field_8204")

                    )
                        i = index + 1;

                }
                instructions.insert(
                        instructions.get(i),
                        ASM.listOf(
                                new VarInsnNode(Opcodes.ALOAD, 6),
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "shouldMapRender",
                                        "(ZL" + itemStackClass + ";)Z",
                                        false
                                )
                        )
                );
            });
        });
        String clientLevelName = resolver.mapClassName("intermediary", "net.minecraft.class_638").replace('.', '/');
        String minecraftClass = resolver.mapClassName("intermediary", "net.minecraft.class_310").replace('.', '/');
        String mapSaveDataClass = resolver.mapClassName("intermediary", "net.minecraft.class_22").replace('.', '/');
        String levelClass = resolver.mapClassName("intermediary", "net.minecraft.class_1937").replace('.', '/');
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary", "net.minecraft.class_759"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(FabricLoader.getInstance().isDevelopmentEnvironment() ? "renderMap" : "method_3223"))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                AbstractInsnNode insn = null;
                for (var index = 0; index < instructions.size() - 1; index++) {
                    var /*org.objectweb.asm.tree.VarInsnNode*/ node = instructions.get(index);
                    if (insn == null &&

                            node instanceof VarInsnNode varInsnNode &&

                            node.getOpcode() == Opcodes.ASTORE &&

                            varInsnNode.var == 6

                    )
                        insn = node;

                }
                instructions.insertBefore(
                        insn,
                        ASM.listOf(
                                new VarInsnNode(Opcodes.ALOAD, 4),
                                new VarInsnNode(Opcodes.ALOAD, 0),
                                new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/ItemInHandRenderer", FabricLoader.getInstance().isDevelopmentEnvironment() ? "minecraft" : "field_4050", "L" + minecraftClass +";"),
                                new FieldInsnNode(Opcodes.GETFIELD, minecraftClass, FabricLoader.getInstance().isDevelopmentEnvironment() ? "level" : "field_1687", "L" + clientLevelName + ";"),
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "renderMapData",
                                        "(L" + mapSaveDataClass + ";L" + itemStackClass + ";L" + levelClass + ";)L" + mapSaveDataClass + ";",
                                        false
                                )
                        )
                );
            });
        });
        // MapItem
        ClassTinkerers.addTransformation(resolver.mapClassName("intermediary", "net.minecraft.class_1806"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(FabricLoader.getInstance().isDevelopmentEnvironment() ? "appendHoverText" : "method_9568"))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                AbstractInsnNode insn = null;
                for (var index = 0; index < instructions.size() - 1; index++) {
                    var /*org.objectweb.asm.tree.VarInsnNode*/ node = instructions.get(index);
                    if (insn == null &&

                            node instanceof VarInsnNode varInsnNode &&

                            node.getOpcode() == Opcodes.ASTORE &&

                            varInsnNode.var == 6

                    )
                        insn = node;

                }
                instructions.insertBefore(
                        insn,
                        ASM.listOf(
                                new VarInsnNode(Opcodes.ALOAD, 1),
                                new VarInsnNode(Opcodes.ALOAD, 2),
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "renderMapData",
                                        "(L" + mapSaveDataClass +";L" + itemStackClass + ";L" + levelClass + ";)L" + mapSaveDataClass + ";",
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
