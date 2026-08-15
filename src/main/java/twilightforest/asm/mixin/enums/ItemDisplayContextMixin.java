package twilightforest.asm.mixin.enums;

import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemDisplayContext.class)
public enum ItemDisplayContextMixin {
	TWILIGHTFOREST_JARRED(-1, "twilightforest:jarred");

	@Shadow
	ItemDisplayContextMixin(int id, String name){
	}
}