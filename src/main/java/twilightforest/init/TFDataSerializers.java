package twilightforest.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import twilightforest.TFCommon;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;

import java.util.List;

public class TFDataSerializers {
	public static final EntityDataSerializer<List<String>> STRING_LIST = EntityDataSerializer.forValueType(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()));
	public static final EntityDataSerializer<Holder<DwarfRabbitVariant>> DWARF_RABBIT_VARIANT = EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.DWARF_RABBIT_VARIANT));
	public static final EntityDataSerializer<Holder<TinyBirdVariant>> TINY_BIRD_VARIANT = EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.TINY_BIRD_VARIANT));
	public static final EntityDataSerializer<Holder<MagicPaintingVariant>> MAGIC_PAINTING_VARIANT = EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.MAGIC_PAINTINGS));

	public static void init() {
		TFCommon.LOGGER.info("Initializing entity data serializers...");
		FabricEntityDataRegistry.register(TFCommon.prefix("string_list"), STRING_LIST);
		FabricEntityDataRegistry.register(TFCommon.prefix("dwarf_rabbit_variant"), DWARF_RABBIT_VARIANT);
		FabricEntityDataRegistry.register(TFCommon.prefix("tiny_bird_variant"), TINY_BIRD_VARIANT);
		FabricEntityDataRegistry.register(TFCommon.prefix("magic_painting_variant"), MAGIC_PAINTING_VARIANT);
	}
}