package twilightforest.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;

@Mixin(TntBlock.class)
public interface TntBlockAccessor {
    @Invoker("explode")
    static void explode(Level level, BlockPos pos, @Nullable LivingEntity entity) {

    }
}
