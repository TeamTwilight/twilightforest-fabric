package twilightforest.asm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.entity.PartEntity;
import twilightforest.fabric.interfaces.extension.ILevelExtension;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Level.class)
public class LevelMixin implements ILevelExtension {

	@Unique
	final Int2ObjectMap<PartEntity<?>> twilightforest$multiparts = new Int2ObjectOpenHashMap<>();

	@Inject(
		method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
		at = @At("TAIL")
	)
	private void twilightforest$appendPartEntities(
		@Nullable Entity except,
		AABB bb,
		Predicate<? super Entity> selector,
		CallbackInfoReturnable<List<Entity>> cir,
		@Local(name = "output") List<Entity> output
	) {
		for (PartEntity<?> partEntity : this.twilightforest$getPartEntities()) {
			if (partEntity != except && partEntity.getBoundingBox().intersects(bb) && selector.test(partEntity)) {
				output.add(partEntity);
			}
		}
	}

	@Inject(
		method = "getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
		at = @At("TAIL")
	)
	private <T extends Entity> void twilightforest$appendPartEntitiesTypeTest(
		EntityTypeTest<Entity, T> type,
		AABB bb,
		Predicate<? super T> selector,
		CallbackInfoReturnable<List<T>> cir,
		@Local(name = "output") List<Entity> output
	) {
		for (PartEntity<?> partEntity : this.twilightforest$getPartEntities()) {
			T t = type.tryCast(partEntity);
			if (t != null && t.getBoundingBox().intersects(bb) && selector.test(t)) {
				output.add(t);
			}
		}
	}

	@Override
	public @NonNull Int2ObjectMap<PartEntity<?>> twilightforest$getPartEntityMap() {
		return twilightforest$multiparts;
	}
}