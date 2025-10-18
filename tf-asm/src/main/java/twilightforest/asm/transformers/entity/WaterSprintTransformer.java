package twilightforest.asm.transformers.entity;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

/**
 * {@link twilightforest.asmhooks.EntityHooks#unrestrainedSprintingInWater}
 * {@link twilightforest.asmhooks.EntityHooks#unrestrainedSwimPredicate}
 */
public class WaterSprintTransformer implements ITransformer<MethodNode> {

	private static void injectIsInWater(MethodNode node) {
		ASMUtil.findInstructions(node, Opcodes.INVOKEVIRTUAL)
			.filter(insn -> insn instanceof MethodInsnNode)
			.map(insn -> (MethodInsnNode) insn)
			.filter(m -> "isInWater".equals(m.name) && "()Z".equals(m.desc))
			.filter(m -> {
				AbstractInsnNode prev = previousRealInsn(m);
				return prev instanceof VarInsnNode vin &&
					vin.getOpcode() == Opcodes.ALOAD &&
					vin.var == 0;
			})
			.forEach(m -> {
				InsnList patch = ASMAPI.listOf(
					new InsnNode(Opcodes.DUP),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/EntityHooks",
						"unrestrainedSprintingInWater",
						"(Lnet/minecraft/world/entity/LivingEntity;Z)Z",
						false
					)
				);
				node.instructions.insertBefore(m, patch.getFirst());
				node.instructions.insert(m, patch.getLast());
			});
	}

	private static void injectIsInFluidType(MethodNode node) {
		ASMUtil.findInstructions(node, Opcodes.INVOKEVIRTUAL)
			.filter(insn -> insn instanceof MethodInsnNode)
			.map(insn -> (MethodInsnNode) insn)
			.filter(m -> "isInFluidType".equals(m.name) && "(Ljava/util/function/BiPredicate;)Z".equals(m.desc))
			.forEach(call -> {
				AbstractInsnNode indy = previousRealInsn(call);
				if (!(indy instanceof InvokeDynamicInsnNode)) return;
				AbstractInsnNode capturedThis = previousRealInsn(indy);
				if (!(capturedThis instanceof VarInsnNode vin &&
					vin.getOpcode() == Opcodes.ALOAD && vin.var == 0)) {
					return;
				}
				node.instructions.insert(capturedThis, new InsnNode(Opcodes.DUP));
				MethodInsnNode wrap = new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"unrestrainedSwimPredicate",
					"(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/BiPredicate;)Ljava/util/function/BiPredicate;",
					false
				);
				node.instructions.insert(indy, wrap);
			});
	}

	private static AbstractInsnNode previousRealInsn(AbstractInsnNode n) {
		AbstractInsnNode p = n.getPrevious();
		while (p instanceof LabelNode || p instanceof LineNumberNode || p instanceof FrameNode) {
			p = p.getPrevious();
		}
		return p;
	}

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		injectIsInWater(node);
		injectIsInFluidType(node);
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull java.util.Set<Target<MethodNode>> targets() {
		return java.util.Set.of(Target.targetMethod(
			"net.minecraft.client.player.LocalPlayer",
			"aiStep",
			"()V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}
