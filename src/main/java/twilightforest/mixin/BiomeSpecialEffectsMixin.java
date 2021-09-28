package twilightforest.mixin;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TFConstants;

import java.util.Arrays;
import java.util.List;

import net.minecraft.world.level.biome.BiomeSpecialEffects;

@Mixin(BiomeSpecialEffects.GrassColorModifier.class)
public abstract class BiomeSpecialEffectsMixin {
    @Unique
    private static boolean start = false;
    @Shadow
    public static BiomeSpecialEffects.GrassColorModifier valueOf(String name) {
        return null;
    }

    @Inject(method = "values", at = @At("RETURN"), cancellable = true)
    private static void addExtra(CallbackInfoReturnable<BiomeSpecialEffects.GrassColorModifier[]> cir) {
        if(!start) return;
        List<BiomeSpecialEffects.GrassColorModifier> colorModifiers = Arrays.asList(cir.getReturnValue());
        colorModifiers.add(valueOf("ENCHANTED_FOREST"));
        colorModifiers.add(valueOf("SWAMP"));
        colorModifiers.add(valueOf("DARK_FOREST"));
        colorModifiers.add(valueOf("DARK_FOREST_CENTER"));
        cir.setReturnValue(colorModifiers.toArray(new BiomeSpecialEffects.GrassColorModifier[0]));
    }

    @Shadow @Final private String name;


    @Shadow
    public static BiomeSpecialEffects.GrassColorModifier[] values() {
        return new BiomeSpecialEffects.GrassColorModifier[0];
    }

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    public static BiomeSpecialEffects.GrassColorModifier init(String name, int id, String name2) {
        throw new AssertionError();
    }

    {
        // add new value
        init("ENCHANTED_FOREST",values().length + 1, TFConstants.prefix("enchanted_forest").toString());
        init("SWAMP", values().length + 2, TFConstants.prefix("swamp").toString());
        init("DARK_FOREST", values().length + 3, TFConstants.prefix("dark_forest").toString());
        init("DARK_FOREST_CENTER",values().length + 4, TFConstants.prefix("dark_forest_center").toString());
        start = true;

    }

    /*@Inject(method = "modifyColor", at = @At("RETURN"), cancellable = true)
    public void insertNewColor(double x, double z, int color, CallbackInfoReturnable<Integer> cir) {
        if(this.name.equals(TwilightForestMod.prefix("swamp").toString())) {
            cir.setReturnValue(((GrassColor.get(0.8F, 0.9F) & 0xFEFEFE) + 0x4E0E4E) / 2);
        } else if(this.name.equals(TwilightForestMod.prefix("dark_forest").toString())) {
            cir.setReturnValue((GrassColor.get(0.7F, 0.8F) & 0xFEFEFE) + 0x1E0E4E / 2);
        } else if(this.name.equals(TwilightForestMod.prefix("dark_forest_center").toString())) {
            double d0 = Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false); //TODO: Check
            cir.setReturnValue(d0 < -0.2D ? 0x667540 : 0x554114);
        } else if(this.name.equals(TwilightForestMod.prefix("enchanted_forest").toString())) {
            cir.setReturnValue((color & 0xFFFF00) + BiomeGrassColors.getEnchantedColor((int) x, (int) z)); //TODO
        }
    }*/
}
