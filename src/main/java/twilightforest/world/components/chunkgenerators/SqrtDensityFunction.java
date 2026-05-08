package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public record SqrtDensityFunction(DensityFunction input) implements DensityFunction.SimpleFunction {
    public static final MapCodec<SqrtDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(SqrtDensityFunction::input)
    ).apply(instance, SqrtDensityFunction::new));
    public static final KeyDispatchDataCodec<SqrtDensityFunction> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

    @Override
    public double compute(FunctionContext context) {
        double v = this.input.compute(context);
        return v <= 0.0D ? 0.0D : Math.sqrt(v);
    }

    @Override
    public double minValue() {
        return 0.0D;
    }

    @Override
    public double maxValue() {
        double m = this.input.maxValue();
        return m <= 0.0D ? 0.0D : Math.sqrt(m);
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return KEY_CODEC;
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new SqrtDensityFunction(this.input.mapAll(visitor)));
    }
}
