package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.init.TFBlockEntities;
import twilightforest.tags.TFBlockEntityTypeTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BlockEntityTypeTagGenerator extends KeyTagProvider<BlockEntityType<?>> {

	public BlockEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BLOCK_ENTITY_TYPE, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFBlockEntityTypeTags.RELOCATION_NOT_SUPPORTED).addAll(List.of(
			TFBlockEntities.ANTIBUILDER.builtInRegistryHolder().key(),
			TFBlockEntities.BEANSTALK_GROWER.builtInRegistryHolder().key(),
			TFBlockEntities.NAGA_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.LICH_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.MINOSHROOM_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.HYDRA_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.UR_GHAST_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.ALPHA_YETI_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.SNOW_QUEEN_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.FINAL_BOSS_SPAWNER.builtInRegistryHolder().key()));

		this.tag(TFBlockEntityTypeTags.IMMOVABLE).addAll(List.of(
			TFBlockEntities.ANTIBUILDER.builtInRegistryHolder().key(),
			TFBlockEntities.BEANSTALK_GROWER.builtInRegistryHolder().key(),
			TFBlockEntities.NAGA_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.LICH_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.MINOSHROOM_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.HYDRA_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.UR_GHAST_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.ALPHA_YETI_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.SNOW_QUEEN_SPAWNER.builtInRegistryHolder().key(),
			TFBlockEntities.FINAL_BOSS_SPAWNER.builtInRegistryHolder().key()));
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Entity Tags";
	}
}
