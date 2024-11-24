package twilightforest.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import twilightforest.item.GiantItem;

public class GiantItemRenderHelper {
	public static void handle(PoseStack poseStack) {
		poseStack.translate(-0.5, -0.5, -1.5);
		poseStack.scale(4, 4, 4);
	}
}
