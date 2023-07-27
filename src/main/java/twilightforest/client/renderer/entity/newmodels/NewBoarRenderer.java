package twilightforest.client.renderer.entity.newmodels;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.newmodels.NewBoarModel;
import twilightforest.entity.passive.Boar;

//old renderer used to use PigModel, had to change it because of the texture size change
public class NewBoarRenderer extends MobRenderer<Boar, NewBoarModel<Boar>> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("wildboar.png");

	public NewBoarRenderer(EntityRendererProvider.Context manager, NewBoarModel<Boar> model) {
		super(manager, model, 0.7F);
	}

	@Override
	public ResourceLocation getTextureLocation(Boar entity) {
		return textureLoc;
	}
}
