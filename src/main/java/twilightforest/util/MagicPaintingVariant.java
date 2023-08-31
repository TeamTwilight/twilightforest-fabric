package twilightforest.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public record MagicPaintingVariant(int width, int height, List<Layer> layers) {
    public static final Codec<MagicPaintingVariant> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(MagicPaintingVariant::width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(MagicPaintingVariant::height),
            ExtraCodecs.nonEmptyList(Layer.CODEC.listOf()).fieldOf("layers").forGetter(MagicPaintingVariant::layers)
    ).apply(recordCodecBuilder, MagicPaintingVariant::new));

    public record Layer(String path, @Nullable Parallax parallax, @Nullable OpacityModifier opacityModifier, boolean fullbright) {
        public static final Codec<Layer> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
                ExtraCodecs.NON_EMPTY_STRING.fieldOf("path").forGetter(Layer::path),
                Parallax.CODEC.optionalFieldOf("parallax").forGetter((layer) -> Optional.ofNullable(layer.parallax())),
                OpacityModifier.CODEC.optionalFieldOf("opacity_modifier").forGetter((layer) -> Optional.ofNullable(layer.opacityModifier())),
                Codec.BOOL.fieldOf("fullbright").forGetter(Layer::fullbright)
        ).apply(recordCodecBuilder, Layer::create));

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this too
        private static Layer create(String path, Optional<Parallax> parallax, Optional<OpacityModifier> opacityModifier, boolean fullbright) {
            return new Layer(path, parallax.orElse(null), opacityModifier.orElse(null), fullbright);
        }

        public record Parallax(Type type, float multiplier, int width, int height) {
            public static final Codec<Parallax> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
                    Type.CODEC.fieldOf("type").forGetter(Parallax::type),
                    Codec.FLOAT.fieldOf("multiplier").forGetter(Parallax::multiplier),
                    ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(Parallax::width),
                    ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(Parallax::height)
            ).apply(recordCodecBuilder, Parallax::new));

            public enum Type implements StringRepresentable {
                VIEW_ANGLE("view_angle"),
                LINEAR_TIME("linear_time"),
                SINE_TIME("sine_time");

                static final Codec<Parallax.Type> CODEC = StringRepresentable.fromEnum(Parallax.Type::values);
                private final String name;

                Type(String pName) {
                    this.name = pName;
                }

                @Override
                public String getSerializedName() {
                    return this.name;
                }
            }
        }

        public record OpacityModifier(Type type, float multiplier, boolean invert) {
            public static final Codec<OpacityModifier> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
                    OpacityModifier.Type.CODEC.fieldOf("type").forGetter(OpacityModifier::type),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("multiplier").forGetter(OpacityModifier::multiplier),
                    Codec.BOOL.fieldOf("invert").forGetter(OpacityModifier::invert)
            ).apply(recordCodecBuilder, OpacityModifier::new));

            public enum Type implements StringRepresentable {
                DISTANCE("distance"),
                WEATHER("weather"),
                LIGHTNING("lightning"),
                DAY_TIME("day_time"),
                DAY_TIME_SHARP("day_time_sharp"),
                SINE_TIME("sine_time");

                static final Codec<OpacityModifier.Type> CODEC = StringRepresentable.fromEnum(OpacityModifier.Type::values);
                private final String name;

                Type(String pName) {
                    this.name = pName;
                }

                @Override
                public String getSerializedName() {
                    return this.name;
                }
            }
        }
    }
}
