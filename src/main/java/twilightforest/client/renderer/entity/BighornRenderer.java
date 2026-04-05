package twilightforest.client.renderer.entity;

import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.Sheep;
import twilightforest.TwilightForestMod;

public class BighornRenderer extends SheepRenderer {

	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("bighorn.png");

	@SuppressWarnings("unchecked")
	public BighornRenderer(EntityRendererProvider.Context context, SheepModel<? extends Sheep> baseModel, float shadowSize) {
		super(context);
		this.shadowRadius = shadowSize;
		this.model = (SheepModel<Sheep>) baseModel;
		this.addLayer(new SheepFurLayer(this, context.getModelSet()));
	}

	@Override
	public Identifier getTextureLocation(Sheep entity) {
		return TEXTURE;
	}
}
