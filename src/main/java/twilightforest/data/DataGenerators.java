package twilightforest.data;

import com.mojang.brigadier.Command;
import twilightforest.TwilightForestMod;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.server.Bootstrap;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

public class DataGenerators {
	public static int gatherData() {
		DataGenerator generator = new DataGenerator(Paths.get("../src/generated/resources"), Collections.emptyList());

		generator.addProvider(new AdvancementProvider(generator));
		//generator.addProvider(new BlockstateGenerator(generator));
		//generator.addProvider(new ItemModelGenerator(generator));
		BlockTagsProvider blocktags = new BlockTagGenerator(generator);
		generator.addProvider(blocktags);
		generator.addProvider(new FluidTagGenerator(generator));
		generator.addProvider(new ItemTagGenerator(generator, blocktags));
		generator.addProvider(new EntityTagGenerator(generator));
		//generator.addProvider(new CustomTagGenerator.EnchantmentTagGenerator(generator));
		generator.addProvider(new LootGenerator(generator));
		generator.addProvider(new StonecuttingGenerator(generator));
		generator.addProvider(new CraftingGenerator(generator));
		generator.addProvider(new TwilightWorldDataCompiler(generator));
		try {
			generator.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Command.SINGLE_SUCCESS;
	}
}
