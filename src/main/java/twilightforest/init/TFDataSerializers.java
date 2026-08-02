package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import twilightforest.TFRegistries;
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
		EntityDataSerializers.registerSerializer(STRING_LIST);
		EntityDataSerializers.registerSerializer(DWARF_RABBIT_VARIANT);
		EntityDataSerializers.registerSerializer(TINY_BIRD_VARIANT);
		EntityDataSerializers.registerSerializer(MAGIC_PAINTING_VARIANT);
	}
}
