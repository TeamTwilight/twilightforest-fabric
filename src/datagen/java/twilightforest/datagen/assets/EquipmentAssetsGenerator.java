package twilightforest.datagen.assets;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import twilightforest.init.TFEquipmentAssets;

import java.util.function.BiConsumer;

public class EquipmentAssetsGenerator extends EquipmentAssetProvider {
	public EquipmentAssetsGenerator(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void carminite$registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
		TFEquipmentAssets.bootstrap(output);
	}
}