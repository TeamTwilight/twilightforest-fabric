package twilightforest.compat.rei.categories;

import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import twilightforest.TwilightForestMod;
import twilightforest.compat.rei.displays.REICrumbleHornDisplay;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.List;

public class REICrumbleHornCategory implements DisplayCategory<REICrumbleHornDisplay> {

	public static final CategoryIdentifier<REICrumbleHornDisplay> CRUMBLE_HORN = CategoryIdentifier.of(TwilightForestMod.ID, "crumble_horn");

	public static final int WIDTH = 116;
	public static final int HEIGHT = 54;

	private final Renderer icon;
	private final Component localizedName;

	public REICrumbleHornCategory() {
//        this.background = helper.createDrawable(location, 0, 0, WIDTH, HEIGHT);
		this.icon = EntryStacks.of(TFItems.CRUMBLE_HORN.get().getDefaultInstance());
		this.localizedName = Component.translatable("gui.crumble_horn_jei");
	}

	@Override
	public CategoryIdentifier<? extends REICrumbleHornDisplay> getCategoryIdentifier() {
		return CRUMBLE_HORN;
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public Renderer getIcon() {
		return icon;
	}

	@Override
	public int getDisplayWidth(REICrumbleHornDisplay display) {
		return WIDTH + 8;
	}

	@Override
	public int getDisplayHeight() {
		return HEIGHT + 8;
	}

	@Override
	public List<Widget> setupDisplay(REICrumbleHornDisplay display, Rectangle origin) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(origin));
		Rectangle bounds = origin.getBounds();
		bounds.translate(4, 4);

		widgets.add(Widgets.createTexturedWidget(TwilightForestMod.getGuiTexture("crumble_horn_jei.png"), new Rectangle(bounds.getX(), bounds.getY(), WIDTH, HEIGHT)));

		if (!display.isResultAir) {
			widgets.add(Widgets.createTexturedWidget(TwilightForestMod.getGuiTexture("crumble_horn_jei.png"), bounds.getX() + 76, bounds.getY() + 14, 116, 0, 26, 26));
		}

		widgets.add(
				Widgets.createSlot(offsetPoint(bounds, 19, 19))
						.markInput()
						.disableBackground()
						.entries(display.getInputEntries().get(0))
		);

		int size = !display.isResultAir ? 16 : 32;

		int x = !display.isResultAir ? 81 : 75;
		int y = !display.isResultAir ? 19 : 12;

		widgets.add(
				Widgets.createSlot(new Rectangle(offsetPoint(bounds, x, y), new Dimension(size, size)))
						.markOutput()
						.disableBackground()
						.entries(display.getOutputEntries().get(0))
		);

		return widgets;
	}

	public static Point offsetPoint(Rectangle bounds, int x, int y) {
		return new Point(bounds.getX() + x, bounds.getY() + y);
	}
}
