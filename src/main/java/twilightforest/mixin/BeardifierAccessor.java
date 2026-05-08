package twilightforest.mixin;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Beardifier.class)
public interface BeardifierAccessor {
	@Accessor("pieceIterator")
	ObjectListIterator<Beardifier.Rigid> codexTwilight$getPieceIterator();

	@Accessor("junctionIterator")
	ObjectListIterator<JigsawJunction> codexTwilight$getJunctionIterator();

	@Invoker("<init>")
	static Beardifier codexTwilight$create(ObjectListIterator<Beardifier.Rigid> pieceIterator, ObjectListIterator<JigsawJunction> junctionIterator) {
		throw new AssertionError();
	}
}
