package twilightforest.asm.transformers.entity;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.EntityHooks#resetStuckUnrestrained}
 */
public class ResetStuckUnrestrainedTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext ctx) {
		InsnList insns = node.instructions;
		for (AbstractInsnNode p = insns.getFirst(); p != null; p = p.getNext()) {
			if (p.getOpcode() != Opcodes.GETFIELD) continue;
			FieldInsnNode f = (FieldInsnNode) p;
			if ("net/minecraft/world/entity/Entity".equals(f.owner)
				&& "stuckSpeedMultiplier".equals(f.name)
				&& "Lnet/minecraft/world/phys/Vec3;".equals(f.desc)) {
				InsnList hook = new InsnList();
				hook.add(new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"resetStuckUnrestrained",
					"(Lnet/minecraft/world/entity/Entity;)V"
				));
				hook.add(new VarInsnNode(Opcodes.ALOAD, 0));

				insns.insertBefore(f, hook);
			}
		}
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.entity.Entity",
			"move",
			"(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}

