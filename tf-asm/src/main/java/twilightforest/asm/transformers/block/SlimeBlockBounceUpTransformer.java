package twilightforest.asm.transformers.block;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.BlockHooks#stopBouncing}
 */
public final class SlimeBlockBounceUpTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		InsnList inject = new InsnList();
		inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		inject.add(new MethodInsnNode(
			Opcodes.INVOKESTATIC,
			"twilightforest/asmhooks/BlockHooks",
			"stopBouncing",
			"(Lnet/minecraft/world/entity/Entity;)V",
			false
		));
		AbstractInsnNode first = node.instructions.getFirst();
		node.instructions.insertBefore(first, inject);
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.level.block.SlimeBlock",
			"bounceUp",
			"(Lnet/minecraft/world/entity/Entity;)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}