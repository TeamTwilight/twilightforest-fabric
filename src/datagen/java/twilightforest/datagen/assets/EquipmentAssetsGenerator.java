package twilightforest.datagen.assets;

import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import twilightforest.init.TFEquipmentAssets;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EquipmentAssetsGenerator implements DataProvider {
	private final PackOutput.PathProvider pathProvider;

	public EquipmentAssetsGenerator(PackOutput output) {
		this.pathProvider = output.createPathProvider(
			PackOutput.Target.RESOURCE_PACK,
			"equipment"
		);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = new HashMap<>();
		BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output = (id, asset) -> {
			if (equipmentAssets.putIfAbsent(id, asset) != null) {
				throw new IllegalStateException(
					"Tried to register equipment asset twice for id: " + id
				);
			}
		};

		EquipmentAssetProvider.bootstrap(output);
		TFEquipmentAssets.bootstrap(output);

		return DataProvider.saveAll(
			cache,
			EquipmentClientInfo.CODEC,
			pathProvider::json,
			equipmentAssets
		);
	}

	@Override
	public String getName() {
		return "Equipment Asset Definitions";
	}
}