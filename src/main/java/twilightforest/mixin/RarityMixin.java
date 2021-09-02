package twilightforest.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

@Mixin(Rarity.class)
public abstract class RarityMixin {
    @SuppressWarnings("ShadowTarget")
    @Shadow @Final @Mutable
    private static Rarity[] $VALUES;

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    public static Rarity init(String name, int id, ChatFormatting chatFormatting) {
        throw new AssertionError();
    }

    // add new property from the static constructor
    // static blocks are merged into the target class (at the end)
    static {
        ArrayList<Rarity> values =  new ArrayList<>(Arrays.asList($VALUES));
        Rarity last = values.get(values.size() - 1);

        // add new value
        values.add(init("TWILIGHT", last.ordinal() + 1, ChatFormatting.DARK_GREEN));

        $VALUES = values.toArray(new Rarity[0]);
    }
}
