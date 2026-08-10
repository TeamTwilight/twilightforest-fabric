package twilightforest.client.renderer.block;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TFMain;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.client.model.TFModelLayers;

import java.util.List;

public class KeepsakeCasketRenderer<T extends BlockEntity & LidBlockEntity> extends SkullChestRenderer<T> {
	private static final List<Identifier> CASKET_TEXTURES = List.of(
		TFMain.getModelTexture("casket/keepsake_casket_0.png"),
		TFMain.getModelTexture("casket/keepsake_casket_1.png"),
		TFMain.getModelTexture("casket/keepsake_casket_2.png")
	);

	public KeepsakeCasketRenderer(BlockEntityRendererProvider.Context context) {
		super(context, TFModelLayers.KEEPSAKE_CASKET);
	}

	@Override
	protected Identifier getTextureLocation(BlockState blockstate) {
		return getTextureLocation(blockstate.getValue(KeepsakeCasketBlock.BREAKAGE));
	}

	public static Identifier getTextureLocation(int damage) {
		return CASKET_TEXTURES.get(Mth.clamp(damage, 0, CASKET_TEXTURES.size() - 1));
	}
}
