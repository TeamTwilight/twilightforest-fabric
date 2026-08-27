package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class NoopModel<T extends HumanoidRenderState> extends HumanoidModel<T> {

	public NoopModel(ModelPart part) {
		super(part);
	}
}
