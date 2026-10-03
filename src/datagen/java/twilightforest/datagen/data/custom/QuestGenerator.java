package twilightforest.datagen.data.custom;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import twilightforest.TFCommon;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class QuestGenerator extends FabricCodecDataProvider<QuestingRamContext> {
	public QuestGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture, PackOutput.Target.DATA_PACK, "twilight/quests", QuestingRamContext.CODEC);
	}

	@Override
	protected void configure(BiConsumer<Identifier, QuestingRamContext> provider, HolderLookup.Provider registryLookup) {
		provider.accept(TFCommon.prefix("questing_ram"), QuestingRamContext.FALLBACK);
	}

	@Override
	public String getName() {
		return "Twilight Forest Quests";
	}
}
