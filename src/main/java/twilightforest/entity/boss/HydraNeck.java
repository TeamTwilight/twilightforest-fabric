package twilightforest.entity.boss;

import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TFConstants;

public class HydraNeck extends HydraPart {

	public static final ResourceLocation RENDERER = TFConstants.prefix("hydra_neck");

	public final HydraHead head;

	public HydraNeck(HydraHead head) {
		super(head.getParent(), 2F, 2F);
		this.head = head;
	}

	@Environment(EnvType.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}
}
