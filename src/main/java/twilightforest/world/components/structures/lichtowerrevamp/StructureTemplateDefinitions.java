package twilightforest.world.components.structures.lichtowerrevamp;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.SimpleWeightedRandomList;
import org.jetbrains.annotations.Nullable;
import twilightforest.world.components.structures.util.CodecResourceReloadListener;

import java.util.HashMap;
import java.util.Map;

public class StructureTemplateDefinitions extends CodecResourceReloadListener<StructureTemplateDefinition> {
	public static final StructureTemplateDefinitions INSTANCE = new StructureTemplateDefinitions(); // TODO Autowired

	private final Map<ResourceLocation, Map<ResourceLocation, Integer>> rawTemplatePools = new HashMap<>();
	private final Map<ResourceLocation, SimpleWeightedRandomList<ResourceLocation>> templatePools = new HashMap<>();

	public static final String DIRECTORY = "twilight/template_definition";

	private StructureTemplateDefinitions() {
		super(DIRECTORY, StructureTemplateDefinition.CODEC);
	}

	@Override
	protected void forLocation(ResourceManager manager, ResourceLocation templateName, StructureTemplateDefinition templateDefinition) {
		for(Map.Entry<ResourceLocation, Integer> poolToRegisterWeight : templateDefinition.poolWeights().entrySet()) {
			ResourceLocation templatePoolId = poolToRegisterWeight.getKey();
			Integer templateWeight = poolToRegisterWeight.getValue();

			Map<ResourceLocation, Integer> pool = this.rawTemplatePools.computeIfAbsent(templatePoolId, k -> new HashMap<>());

			pool.put(templateName, templateWeight);
		}
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
		this.rawTemplatePools.clear();
		this.templatePools.clear();

		super.apply(map, manager, profiler);

		for(Map.Entry<ResourceLocation, Map<ResourceLocation, Integer>> rawTemplatePool : this.rawTemplatePools.entrySet()) {
			SimpleWeightedRandomList.Builder<ResourceLocation> poolBuilder = SimpleWeightedRandomList.builder();

			for (Map.Entry<ResourceLocation, Integer> templateIdWeight : rawTemplatePool.getValue().entrySet()) {
				poolBuilder.add(templateIdWeight.getKey(), templateIdWeight.getValue());
			}

			ResourceLocation templatePoolId = rawTemplatePool.getKey();
			this.templatePools.put(templatePoolId, poolBuilder.build());
		}

		this.rawTemplatePools.clear();
	}

	@Nullable
	private ResourceLocation rollTemplatePool(RandomSource random, ResourceLocation templatePoolId) {
		SimpleWeightedRandomList<ResourceLocation> templatePool = this.templatePools.get(templatePoolId);
		return templatePool == null ? null : templatePool.getRandomValue(random).orElse(null);
	}

	@Nullable // TODO Autowired
	public static ResourceLocation getRandomTemplate(RandomSource random, ResourceLocation poolId) {
		return INSTANCE.rollTemplatePool(random, poolId);
	}
}
