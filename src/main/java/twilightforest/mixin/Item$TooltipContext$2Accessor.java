package twilightforest.mixin;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import twilightforest.asmhooks.Item$TooltipContext$2Duck;

@Mixin(targets = "net.minecraft.world.item.Item$TooltipContext$2")
public interface Item$TooltipContext$2Accessor extends Item$TooltipContext$2Duck {

	@Accessor("val$level")
	Level twilightforest$getLevel();
}