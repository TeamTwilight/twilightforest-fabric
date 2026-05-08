package twilightforest.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public class OptifineWarningScreen extends Screen {
	private final Screen lastScreen;
	private int ticksUntilEnable = 20 * 10;
	private MultiLineLabel message = MultiLineLabel.EMPTY;
	private MultiLineLabel suggestions = MultiLineLabel.EMPTY;
	private static final Component TEXT = Component.translatable("gui.twilightforest.optifine.message");
	private static final MutableComponent URL = Component.translatable("gui.twilightforest.optifine.suggestions").withStyle(style ->
			style.withColor(ChatFormatting.GREEN)
					.applyFormat(ChatFormatting.UNDERLINE)
					.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/NordicGamerFE/usefulmods")));
	private Button exitButton;

	public OptifineWarningScreen(Screen screen) {
		super(Component.translatable("gui.twilightforest.optifine.title"));
		this.lastScreen = screen;
	}

	@Override
	public Component getNarrationMessage() {
		return CommonComponents.joinForNarration(super.getNarrationMessage(), TEXT);
	}

	@Override
	protected void init() {
		super.init();
		this.exitButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_PROCEED, button -> Minecraft.getInstance().setScreen(this.lastScreen))
				.bounds(this.width / 2 - 75, this.height * 3 / 4, 150, 20)
				.build());
		this.exitButton.active = false;

		this.message = MultiLineLabel.create(this.font, TEXT, this.width - 50);
		this.suggestions = MultiLineLabel.create(this.font, URL, this.width - 50);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics, mouseX, mouseY, partialTicks);
		graphics.drawCenteredString(this.font, this.title, this.width / 2, 30, 16777215);
		this.message.renderCentered(graphics, this.width / 2, 70);
		this.suggestions.renderCentered(graphics, this.width / 2, 160);
		super.render(graphics, mouseX, mouseY, partialTicks);

		this.exitButton.render(graphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void tick() {
		super.tick();
		if (--this.ticksUntilEnable <= 0) {
			this.exitButton.active = true;
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return this.ticksUntilEnable <= 0;
	}

	@Override
	public void onClose() {
		Minecraft.getInstance().setScreen(this.lastScreen);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (mouseY > 160 && mouseY < 170) {
			Style style = this.getClickedComponentStyleAt((int) mouseX);
			if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
				this.handleComponentClicked(style);
				return false;
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Nullable
	private Style getClickedComponentStyleAt(int xPos) {
		int width = Minecraft.getInstance().font.width(URL);
		int left = this.width / 2 - width / 2;
		int right = this.width / 2 + width / 2;
		return xPos >= left && xPos <= right ? Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(URL, xPos - left) : null;
	}
}
