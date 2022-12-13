package twilightforest.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import twilightforest.TwilightForestMod;

public class TFEmiRecipeCategory extends EmiRecipeCategory {
	public final String name;
	public final Component title;

	public TFEmiRecipeCategory(String name, RegistryObject<? extends ItemLike> icon) {
		super(TwilightForestMod.prefix(name), EmiStack.of(icon.get()));
		this.name = name;
		this.title = Component.translatable("gui.%s_jei".formatted(name));
	}

	@Override
	public Component getName() {
		return title;
	}
}
