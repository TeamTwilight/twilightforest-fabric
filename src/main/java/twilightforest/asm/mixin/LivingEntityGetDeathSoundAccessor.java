package twilightforest.asm.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityGetDeathSoundAccessor {
	@Invoker("getDeathSound")
	SoundEvent twilightforest$invokeGetDeathSound();
}
