package twilightforest.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Locale;

public class TFASM implements Runnable {

    @Override
    public void run() {
        extendEnums();
        foliage();
        music();
        multipart();
        maprendercontext();
        mount();
        seed();
    }

    private static void extendEnums() {
        ClassTinkerers.enumBuilder(mapC("class_1886")) // EnchantmentCategory
                .addEnumSubclass("TWILIGHTFOREST_BLOCK_AND_CHAIN", "twilightforest.asm.EnchantmentCategoryBlockAndChain")
                .build();
        String grassColorClass = mapC("class_4763$class_5486"); // BiomeSpecialEffects.GrassColorModifier
        ClassTinkerers.enumBuilder(grassColorClass, String.class)
                .addEnumSubclass("TWILIGHTFOREST_ENCHANTED_FOREST", "twilightforest.asm.GrassColorModifierEnchantedForest", prefix("enchanted_forest"))
                .addEnumSubclass("TWILIGHTFOREST_SWAMP", "twilightforest.asm.GrassColorModifierSwamp", prefix("swamp"))
                .addEnumSubclass("TWILIGHTFOREST_DARK_FOREST", "twilightforest.asm.GrassColorModifierDarkForest", prefix("dark_forest"))
                .addEnumSubclass("TWILIGHTFOREST_DARK_FOREST_CENTER", "twilightforest.asm.GrassColorModifierDarkForestCenter", prefix("dark_forest_center"))
                .addEnumSubclass("TWILIGHTFOREST_SPOOKY_FOREST", "twilightforest.asm.GrassColorModifierSpookyForest", prefix("spooky_forest"))
                .build();
        ClassTinkerers.enumBuilder(mapC("class_1814"), "L" + mapC("class_124") + ";")  // Rarity // ChatFormatting
                .addEnum("TWILIGHT", () -> new Object[] { ChatFormatting.DARK_GREEN })
                .build();
    }

    private static void foliage() {
                            // Biome
        String biomeClass = mapC("class_1959").replace('.', '/');
                                            // BiomeColors.FOLIAGE_COLOR_RESOLVER lambda
        String foliageColorResolverLambda = mapM("class_1163.method_23791(Lnet/minecraft/class_1959;DD)I");
                                         // BiomeColors
        ClassTinkerers.addTransformation(mapC("class_1163"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if(!methodNode.name.equals(foliageColorResolverLambda)) return;
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
    }

    private static void music() {
                            // Music
        String musicClass = mapC("class_5195").replace('.', '/');
                                         // MusicManager
        ClassTinkerers.addTransformation(mapC("class_1142"), classNode -> {
                          // MusicManager.tick
            String tick = mapM("class_1142.method_18669()V");
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(tick))
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
    }

    private static void multipart() {
                             // Entity
        String entityClass = mapC("class_1297").replace('.', '/');
                                     // ServerEntity.sendDirtyEntityData
        String sendDirtyEntityData = mapM("class_3231.method_14306()V");
                                         // ServerEntity
        ClassTinkerers.addTransformation(mapC("class_3231"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(sendDirtyEntityData))
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
                                         // ResourceManagerReloadListener.onResourceManagerReload
        String onResourceManagerReload = mapM("class_4013.method_14491(Lnet/minecraft/class_3300;)V");
                              // EntityRenderDispatcher.Context
        String contextClass = mapC("class_5617$class_5618").replace('.', '/');
                                         // EntityRenderDispatcher
        ClassTinkerers.addTransformation(mapC("class_898"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(onResourceManagerReload))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                        ASM.findFirstInstruction(methodNode, Opcodes.INVOKESPECIAL),
                        ASM.listOf(
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "bakeMultipartRenders",
                                        "(L" + contextClass + ";)L" + contextClass + ";",
                                        false
                                )
                        )
                );
            });
        });

                                 // EntityRenderDispatcher.getRenderer
        String getRendererName = mapM("class_898.method_3953(Lnet/minecraft/class_1297;)Lnet/minecraft/class_897;");
                                     // EntityRenderer
        String entityRendererClass = mapC("class_897").replace('.', '/');
                                         // EntityRenderDispatcher
        ClassTinkerers.addTransformation(mapC("class_898"), classNode -> {
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
                             // LevelRenderer.renderLevel
        String renderLevel = mapM("class_761.method_22710(Lnet/minecraft/class_4587;FJZLnet/minecraft/class_4184;Lnet/minecraft/class_757;Lnet/minecraft/class_765;Lorg/joml/Matrix4f;)V");
                                      // entitiesForRendering
        String entitiesForRendering = mapM("class_638.method_18112()Ljava/lang/Iterable;");
                                         // LevelRenderer
        ClassTinkerers.addTransformation(mapC("class_761"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(renderLevel))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                AbstractInsnNode lastInstruction = null;
                for (var index = instructions.size() - 1; index > 0; index--) {
                    AbstractInsnNode node = instructions.get(index);
                    if (lastInstruction == null &&

                            node instanceof MethodInsnNode methodInsnNode &&

                            node.getOpcode() == Opcodes.INVOKEVIRTUAL &&
                                                         // ClientLevel
                            equate(methodInsnNode.owner, mapC("class_638").replace('.', '/')) &&

                            equate(methodInsnNode.name, entitiesForRendering) &&

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

    private static void maprendercontext() {
                                // ItemStack
        String itemStackClass = mapC("class_1799").replace('.', '/');
                                         // ItemInHandRenderer
        String itemInHandRendererClass = mapC("class_759").replace('.', '/');
                                   // ItemInHandRenderer.renderArmWithItem
        String renderArmWithItem = mapM("class_759.method_3228(Lnet/minecraft/class_742;FFLnet/minecraft/class_1268;FLnet/minecraft/class_1799;FLnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V");
                           // Items.FILLED_MAP
        String filledMap = mapF("class_1802.field_8204:Lnet/minecraft/class_1792;");
                                         // ItemInHandRenderer
        ClassTinkerers.addTransformation(mapC("class_759"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(renderArmWithItem))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var i = -1;
                for (var index = instructions.size() - 1; index > 0; index--) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (i == -1 &&

                            node instanceof FieldInsnNode fieldInsnNode &&

                            node.getOpcode() == Opcodes.GETSTATIC &&
                                                        // Items
                            equate(fieldInsnNode.owner, mapC("class_1802").replace('.', '/')) &&

                            equate(fieldInsnNode.name, filledMap)

                    )
                        i = index + 1;

                }

                if (i == -1) {
                    // Must be optifine... Optifine checks for instanceof MapItem, so this patch won't be needed anyway.
                    return;
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

                                // ItemFrame.getFramedMapId
        String getFramedMapId = mapM("class_1533.method_43272()Ljava/util/OptionalInt;");
                                         // ItemFrame
        ClassTinkerers.addTransformation(mapC("class_1533"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(getFramedMapId))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.IFEQ),
                        ASM.listOf(
                                new VarInsnNode(Opcodes.ALOAD, 1),
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

                                 // ClientLevel
        String clientLevelName = mapC("class_638").replace('.', '/');
                                // Minecraft
        String minecraftClass = mapC("class_310").replace('.', '/');
                                  // MapItemSavedData
        String mapSaveDataClass = mapC("class_22").replace('.', '/');
                            // Level
        String levelClass = mapC("class_1937").replace('.', '/');
                           // ItemInHandRenderer.renderMap
        String renderMap = mapM("class_759.method_3223(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_1799;)V");
                           // ItemInHandRenderer.minecraft
        String minecraft = mapF("class_759.field_4050:Lnet/minecraft/class_310;");
                       // ItemInHandRenderer.level
        String level = mapF("class_310.field_1687:Lnet/minecraft/class_638;");
                                         // ItemInHandRenderer
        ClassTinkerers.addTransformation(mapC("class_759"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(renderMap))
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
                                new FieldInsnNode(Opcodes.GETFIELD, itemInHandRendererClass, minecraft, "L" + minecraftClass +";"),
                                new FieldInsnNode(Opcodes.GETFIELD, minecraftClass, level, "L" + clientLevelName + ";"),
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
                                 // Block.appendHoverText
        String appendHoverText = mapM("class_2248.method_9568(Lnet/minecraft/class_1799;Lnet/minecraft/class_1922;Ljava/util/List;Lnet/minecraft/class_1836;)V");
                                         // MapItem
        ClassTinkerers.addTransformation(mapC("class_1806"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(appendHoverText))
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

    private static void mount() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                                // Input
            String inputClass = mapC("class_744").replace('.', '/');
                                  // Input.shiftKeyDown
            String shiftKeyDown = mapF("class_744.field_3903:Z");
                           // LocalPlayer.input
            String input = mapF("class_746.field_3913:Lnet/minecraft/class_744;");
                                      // LocalPlayer
            String localPlayerClass = mapC("class_746").replace('.', '/');
                                       // Player.wantsToStopRiding
            String wantsToStopRiding = mapM("class_1657.method_21824()Z");
                                 // Entity
            String entityClass = mapC("class_1297").replace('.', '/');
                              // Entity.rideTick
            String rideTick = mapM("class_1297.method_5842()V");
                                 // Entity.isPassenger
            String isPassenger = mapM("class_1297.method_5765()Z");

                                             // LocalPlayer
            ClassTinkerers.addTransformation(mapC("class_746"), classNode -> {
                classNode.methods.forEach(methodNode -> {
                    if (!methodNode.name.equals(rideTick))
                        return;
                    var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                    instructions.insert(
                            ASM.findFirstInstruction(methodNode, Opcodes.INVOKESPECIAL),
                            ASM.listOf(
                                    new VarInsnNode(Opcodes.ALOAD, 0),
                                    new FieldInsnNode(
                                            Opcodes.GETFIELD,
                                            localPlayerClass,
                                            input,
                                            "L" + inputClass + ";"
                                    ),
                                    new VarInsnNode(Opcodes.ALOAD, 0),
                                    new FieldInsnNode(
                                            Opcodes.GETFIELD,
                                            localPlayerClass,
                                            input,
                                            "L" + inputClass + ";"
                                    ),
                                    new FieldInsnNode(
                                            Opcodes.GETFIELD,
                                            inputClass,
                                            shiftKeyDown,
                                            "Z"
                                    ),
                                    new VarInsnNode(Opcodes.ALOAD, 0),
                                    new MethodInsnNode(
                                            Opcodes.INVOKEVIRTUAL,
                                            // Player
                                            mapC("class_1657").replace('.', '/'),
                                            wantsToStopRiding,
                                            "()Z",
                                            false
                                    ),
                                    new VarInsnNode(Opcodes.ALOAD, 0),
                                    new MethodInsnNode(
                                            Opcodes.INVOKEVIRTUAL,
                                            entityClass,
                                            isPassenger,
                                            "()Z",
                                            false
                                    ),
                                    new MethodInsnNode(
                                            Opcodes.INVOKESTATIC,
                                            "twilightforest/ASMHooks",
                                            "mountFix",
                                            "(ZZZ)Z",
                                            false
                                    ),
                                    new FieldInsnNode(
                                            Opcodes.PUTFIELD,
                                            inputClass,
                                            shiftKeyDown,
                                            "Z"
                                    )
                            )
                    );
                });
            });
        }
    }

    private static void seed() {
                                         // WorldGenSettings
        ClassTinkerers.addTransformation(mapC("class_5285"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if ((!methodNode.name.equals("<init>")))
                    return;
                if (!methodNode.desc.contains("Optional")) // instead of checking the whole desc we just see if it contains a Optional :troll:
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.PUTFIELD),
                        ASM.listOf(
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "seed",
                                        "(J)J",
                                        false
                                )
                        )
                );
            });
        });

                                      // LevelStorageSource.readWorldGenSettings
        String readWorldGenSettings = mapM("class_32.method_29010(Lcom/mojang/serialization/Dynamic;Lcom/mojang/datafixers/DataFixer;I)Lcom/mojang/datafixers/util/Pair;");
                                         // LevelStorageSource
        ClassTinkerers.addTransformation(mapC("class_32"), classNode -> {
            classNode.methods.forEach(methodNode -> {
                if (!methodNode.name.equals(readWorldGenSettings))
                    return;
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.ASTORE),
                        ASM.listOf(
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "twilightforest/ASMHooks",
                                        "seed",
                                        "(Lcom/mojang/serialization/Dynamic;)Lcom/mojang/serialization/Dynamic;",
                                        false
                                )
                        )
                );
            });
        });
    }

    public static boolean equate(Object a, Object b) {
        return a.equals(b);
    }

    public static String prefix(String name) {
        return "twilightforest:" + name.toLowerCase(Locale.ROOT);
    }

    /**
     * Remap a class name from intermediary to the runtime name
     * @param intermediaryName the intermediary name for the class alone, such as 'class_123'
     * @return the fully qualified remapped name, such as 'net.minecraft.thing.Thing',
     *         or the input with 'net.minecraft.' prepended if not found.
     */
    public static String mapC(String intermediaryName) {
        return FabricLoader.getInstance().getMappingResolver()
                .mapClassName("intermediary", "net.minecraft." + intermediaryName);
    }

    /**
     * Remap a method name from intermediary to the runtime name
     * @param name the intermediary name for the method, prefixed with its class, and suffixed with its descriptor in slash format.
     *             ex: 'class_123.method_456(Lnet/minecraft/class_1959;Ljava/util/Map;D)I'
     * @return the remapped name of the method. If not found, returns the method name alone.
     *             ex: 'method_456'
     */
    public static String mapM(String name) {
        int firstDot = name.indexOf('.');
        // not found          // last character
        if (firstDot == -1 || firstDot == name.length() - 1) {
            throw new IllegalStateException("Invalid method name: " + name);
        }
        String className = name.substring(0, firstDot);
        String methodAndDescriptor = name.substring(firstDot + 1);
        int openParenthesis = methodAndDescriptor.indexOf('(');
        if (openParenthesis == -1) {
            throw new IllegalStateException("descriptor not found: " + methodAndDescriptor);
        }
        String method = methodAndDescriptor.substring(0, openParenthesis);
        String descriptor = methodAndDescriptor.substring(openParenthesis);
        if (descriptor.contains(".")) {
            throw new IllegalStateException("descriptor should be in slash format");
        }
        return FabricLoader.getInstance().getMappingResolver()
                .mapMethodName("intermediary", "net.minecraft." + className, method, descriptor);
    }

    /**
     * Remap a field name from intermediary to the runtime name
     * @param name the intermediary name for the field, prefixed with its class, and suffixed with its descriptor in slash format.
     *             ex: 'class_123.field_456:Lnet/minecraft/class_789'
     * @return the remapped name of the field. If not found, returns the field name alone.
     *             ex: 'method_456'
     */
    public static String mapF(String name) {
        int firstDot = name.indexOf('.');
        // not found          // last character
        if (firstDot == -1 || firstDot == name.length() - 1) {
            throw new IllegalStateException("Invalid field name: " + name);
        }
        String className = name.substring(0, firstDot);
        String fieldAndDescriptor = name.substring(firstDot + 1);
        int colon = fieldAndDescriptor.indexOf(':');
        if (colon == -1) {
            throw new IllegalStateException("descriptor not found: " + fieldAndDescriptor);
        }
        String fieldName = fieldAndDescriptor.substring(0, colon);
        String descriptor = fieldAndDescriptor.substring(colon + 1);
        if (descriptor.contains(".")) {
            throw new IllegalStateException("descriptor should be in slash format");
        }
        return FabricLoader.getInstance().getMappingResolver()
                .mapFieldName("intermediary", "net.minecraft." + className, fieldName, descriptor);
    }
}
