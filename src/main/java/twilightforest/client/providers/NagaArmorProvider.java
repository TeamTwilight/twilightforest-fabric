package twilightforest.client.providers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import twilightforest.TwilightForestMod;

@Environment(EnvType.CLIENT)
public class NagaArmorProvider implements ArmorRenderingRegistry.TextureProvider{
    @Override
    public @NotNull ResourceLocation getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot, boolean secondLayer, @Nullable String suffix, ResourceLocation defaultTexture) {
        if (slot == EquipmentSlot.LEGS) {
            return new ResourceLocation(TwilightForestMod.ARMOR_DIR + "naga_scale_2.png");
        } else {
            return new ResourceLocation(TwilightForestMod.ARMOR_DIR + "naga_scale_1.png");
        }
    }
}
