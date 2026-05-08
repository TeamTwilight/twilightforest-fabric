package twilightforest.init;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.TwilightForestMod;

import java.util.EnumMap;
import java.util.List;

public final class TFArmorMaterials {
    public static final Holder<ArmorMaterial> TRAVELLERS = register("travellers", new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 2);
        map.put(ArmorItem.Type.LEGGINGS, 3);
        map.put(ArmorItem.Type.CHESTPLATE, 4);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 4);
    }), 0, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(TFItems.TANNED_LEATHER.get()), List.of(new ArmorMaterial.Layer(TwilightForestMod.prefix("travellers"))), 0.0F, 0.0F));

    private TFArmorMaterials() {
    }

    private static Holder<ArmorMaterial> register(String path, ArmorMaterial material) {
        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, TwilightForestMod.prefix(path), material);
    }
}
