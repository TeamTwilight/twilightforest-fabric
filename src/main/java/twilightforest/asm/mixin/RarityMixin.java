package twilightforest.asm.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Rarity.class)
public enum RarityMixin {
	TWILIGHTFOREST_TWILIGHT(-1, "twilight", ChatFormatting.DARK_GREEN)
	;

	@Shadow
	RarityMixin(final int id, final String name, final ChatFormatting color){
	}
}