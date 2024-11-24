package twilightforest.compat.emi;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.ItemLike;

public class InvisibleItemEmiStack extends ItemEmiStack {
	public InvisibleItemEmiStack(RegistryObject<? extends ItemLike> item) {
		super(item.get().asItem().getDefaultInstance());
	}

	@Override
	public void render(GuiGraphics graphics, int x, int y, float delta, int flags) {
		// no
	}
}
