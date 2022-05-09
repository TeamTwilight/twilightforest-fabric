package twilightforest.block;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import twilightforest.client.ISTER;

public class ISTERItemRegistry {
    public static void register(@NotNull ItemLike item, ResourceLocation typeId) {
        BuiltinItemRendererRegistry.INSTANCE.register(item, new ISTER(typeId));
    }
}
