// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'cloud': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.Level',
                'methodName': ASM.mapMethod('m_46758_'), // isRainingAt
                'methodDesc': '(Lnet/minecraft/core/BlockPos;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var returns = [];
                for (var index = 0; index < instructions.size(); index++) {
                    var /*org.objectweb.asm.tree.MethodInsnNode*/ node = instructions.get(index);
                    if (node.getOpcode() === Opcodes.IRETURN) {
                        returns.push(node);
                    }
                }
                returns.forEach(function (/*org.objectweb.asm.tree.MethodInsnNode*/ value, index, array) {
                    instructions.insertBefore(
                        value,
                        ASM.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new VarInsnNode(Opcodes.ALOAD, 1),
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'cloud',
                                '(ZLnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z',
                                false
                                )
                            )
                        )
                });
                return methodNode;
            }
        }
    }
}
