package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import twilightforest.TFRegistries;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;

public final class TFDataSerializers {
        private static boolean bootstrapped;

    public static final EntityDataSerializer<Holder<TinyBirdVariant>> TINY_BIRD_VARIANT =
            EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.TINY_BIRD_VARIANT));
    public static final EntityDataSerializer<Holder<DwarfRabbitVariant>> DWARF_RABBIT_VARIANT =
            EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.DWARF_RABBIT_VARIANT));
    public static final EntityDataSerializer<Holder<twilightforest.entity.MagicPaintingVariant>> MAGIC_PAINTING_VARIANT =
            EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.MAGIC_PAINTINGS));

    /**
     * P5.d.4b: Hydra head-name list (length-prefixed UTF-8 strings × MAX_HEADS=7).
     * Upstream {@code TFDataSerializers.STRING_LIST} is wrapped in a NeoForge
     * DeferredHolder; codex stores the {@link EntityDataSerializer} directly so
     * call sites use {@code TFDataSerializers.STRING_LIST} (no {@code .get()}).
     */
    public static final EntityDataSerializer<java.util.List<String>> STRING_LIST =
            EntityDataSerializer.forValueType(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()));

        public static void bootstrap() {
                if (bootstrapped) {
                        return;
                }
                EntityDataSerializers.registerSerializer(TINY_BIRD_VARIANT);
                EntityDataSerializers.registerSerializer(DWARF_RABBIT_VARIANT);
                EntityDataSerializers.registerSerializer(MAGIC_PAINTING_VARIANT);
                EntityDataSerializers.registerSerializer(STRING_LIST);
                bootstrapped = true;
        }

    private TFDataSerializers() {
    }
}