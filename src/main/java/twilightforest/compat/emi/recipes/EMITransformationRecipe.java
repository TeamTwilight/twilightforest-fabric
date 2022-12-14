package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.compat.emi.EMICompat;
import twilightforest.compat.emi.EMIEntityWidget;
import twilightforest.compat.emi.InvisibleItemEmiStack;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.TransformPowderRecipe;

import java.util.List;

public class EMITransformationRecipe extends TFEmiRecipe<TransformPowderRecipe> {
	public static final int WIDTH = 116;
	public static final int HEIGHT = 54;
	public static final EmiStack POWDER = new InvisibleItemEmiStack(TFItems.TRANSFORMATION_POWDER);
	public static final ResourceLocation TEXTURES = TwilightForestMod.getGuiTexture("transformation_jei.png");
	public static final EmiTexture BACKGROUND = new EmiTexture(TEXTURES, 0, 0, WIDTH, HEIGHT);
	public static final EmiTexture DOUBLE_ARROW = new EmiTexture(TEXTURES, 116, 16, 23, 23);

	public final EmiTexture arrow;

	public EMITransformationRecipe(TransformPowderRecipe recipe) {
		super(EMICompat.TRANSFORMATION, recipe, WIDTH, HEIGHT);
		this.arrow = recipe.isReversible() ? DOUBLE_ARROW : EmiTexture.EMPTY_ARROW;
	}

	@Override
	protected void addInputs(List<EmiIngredient> inputs) {
	}

	@Override
	protected void addOutputs(List<EmiStack> outputs) {
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(BACKGROUND, 0, 0);
		widgets.add(new EMIEntityWidget(recipe.input(), 8, 11, 32));
		widgets.addSlot(POWDER, 48, 2).drawBack(false);
		widgets.addTexture(arrow, 46, 19);
		widgets.add(new EMIEntityWidget(recipe.result(), 76, 11, 32));
	}
}
