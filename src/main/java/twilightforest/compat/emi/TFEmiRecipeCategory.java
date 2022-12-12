package twilightforest.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import twilightforest.TwilightForestMod;

public class TFEmiRecipeCategory extends EmiRecipeCategory {
	public final Component title;

	public TFEmiRecipeCategory(String type, RegistryObject<? extends ItemLike> icon) {
		super(TwilightForestMod.prefix(type), EmiStack.of(icon.get()));
		this.title = Component.translatable("gui.%s_jei".formatted(type));
	}

	@Override
	public Component getName() {
		return title;
	}
}
