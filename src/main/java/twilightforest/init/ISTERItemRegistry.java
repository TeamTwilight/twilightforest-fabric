package twilightforest.init;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import twilightforest.client.ISTER;

public class ISTERItemRegistry {
    public static <T extends Item> T register(@NotNull T item) {
        BuiltinItemRendererRegistry.INSTANCE.register(item, new ISTER());
        return item;
    }
}