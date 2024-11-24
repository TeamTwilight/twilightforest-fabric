package twilightforest.compat.rei.categories;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import twilightforest.TwilightForestMod;
import twilightforest.compat.rei.displays.REITransformationPowderDisplay;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.List;

public class REITransformationPowderCategory implements DisplayCategory<REITransformationPowderDisplay> {

	public static final CategoryIdentifier<REITransformationPowderDisplay> TRANSFORMATION = CategoryIdentifier.of(TwilightForestMod.ID, "transformation");

	public static final int WIDTH = 116;
	public static final int HEIGHT = 54;

	private final Renderer icon;
	private final Component localizedName;

	public REITransformationPowderCategory() {
		this.icon = EntryStacks.of(TFItems.TRANSFORMATION_POWDER.get().getDefaultInstance());
		this.localizedName = Component.translatable("gui.transformation_jei");
	}

	@Override
	public CategoryIdentifier<? extends REITransformationPowderDisplay> getCategoryIdentifier() {
		return TRANSFORMATION;
	}

	@Override
	public Component getTitle() {
		return this.localizedName;
	}

	@Override
	public Renderer getIcon() {
		return this.icon;
	}

	@Override
	public int getDisplayHeight() {
		return 54 + 8;
	}

	@Override
	public int getDisplayWidth(REITransformationPowderDisplay display) {
		return 116 + 8;
	}

	@Override
	public List<Widget> setupDisplay(REITransformationPowderDisplay display, Rectangle origin) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(origin));
		Rectangle bounds = origin.getBounds();
		bounds.translate(4, 4);

		widgets.add(Widgets.createTexturedWidget(TwilightForestMod.getGuiTexture("transformation_jei.png"), new Rectangle(bounds.getX(), bounds.getY(), WIDTH, HEIGHT)));

		if (display.isReversible) {
			widgets.add(Widgets.createTexturedWidget(TwilightForestMod.getGuiTexture("transformation_jei.png"), bounds.getX() + 46, bounds.getY() + 19, 116, 16, 23, 15));
		} else {
			widgets.add(Widgets.createTexturedWidget(TwilightForestMod.getGuiTexture("transformation_jei.png"), bounds.getX() + 46, bounds.getY() + 19, 116, 0, 23, 15));
		}

		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 8, bounds.getY() + 11, 32, 32))
				.markInput()
				.entries(display.getInputEntries().get(0))
				.disableBackground()
		);

		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 76, bounds.getY() + 11, 32, 32))
				.markInput()
				.entries(display.getOutputEntries().get(0))
				.disableBackground()
		);

		return widgets;
	}
}
