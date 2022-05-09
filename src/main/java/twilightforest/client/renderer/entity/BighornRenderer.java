package twilightforest.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

@Environment(EnvType.CLIENT)
public class BighornRenderer extends SheepRenderer {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("bighorn.png");

	@SuppressWarnings("unchecked")
	public BighornRenderer(EntityRendererProvider.Context manager, SheepModel<? extends Sheep> baseModel, EntityModel<?> coatModel, float shadowSize) {
		super(manager);
		this.shadowRadius = shadowSize;
		this.model = (SheepModel<Sheep>) baseModel;
		this.addLayer(new SheepFurLayer(this, manager.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(Sheep ent) {
		return textureLoc;
	}
}
