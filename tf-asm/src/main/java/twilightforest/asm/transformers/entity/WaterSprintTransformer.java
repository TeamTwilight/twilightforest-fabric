package twilightforest.asm.transformers.entity;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
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
			.forEach(m -> {
				VarInsnNode loadThis = new VarInsnNode(Opcodes.ALOAD, 0);
				MethodInsnNode patch = new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"unrestrainedSprintingInWater",
					"(ZLnet/minecraft/world/entity/LivingEntity;)Z",
					false
				);
				node.instructions.insert(m, patch);
				node.instructions.insert(m, loadThis);
			});
	}

	private static void injectIsInFluidType(MethodNode node) {
		ASMUtil.findInstructions(node, Opcodes.INVOKEVIRTUAL)
			.filter(insn -> insn instanceof MethodInsnNode)
			.map(insn -> (MethodInsnNode) insn)
			.filter(m -> "isInFluidType".equals(m.name) && "(Ljava/util/function/BiPredicate;)Z".equals(m.desc))
			.forEach(call -> {
				VarInsnNode loadThis = new VarInsnNode(Opcodes.ALOAD, 0);
				MethodInsnNode patch = new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"unrestrainedSwimPredicate",
					"(Ljava/util/function/BiPredicate;Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/function/BiPredicate;",
					false
				);
				node.instructions.insertBefore(call, loadThis);
				node.instructions.insert(loadThis, patch);
			});
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
