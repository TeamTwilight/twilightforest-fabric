package twilightforest.entity.boss;

import net.minecraft.resources.ResourceLocation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;

public class HydraNeckEntity extends HydraPartEntity {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("hydra_neck");

	public final HydraHeadEntity head;

	public HydraNeckEntity(HydraHeadEntity head) {
		super(head.getParent(), 2F, 2F);
		this.head = head;
	}

	@Environment(EnvType.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}
}
