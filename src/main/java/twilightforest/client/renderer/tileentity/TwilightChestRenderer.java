package twilightforest.client.renderer.tileentity;

import com.google.common.collect.ImmutableMap;
import io.github.fabricators_of_create.porting_lib.util.MaterialChest;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.block.entity.TwilightChestEntity;

import java.util.EnumMap;
import java.util.Map;

public class TwilightChestRenderer<T extends TwilightChestEntity> extends ChestRenderer<T> implements MaterialChest {
    public static final Map<Block, EnumMap<ChestType, Material>> MATERIALS;

    static {
        ImmutableMap.Builder<Block, EnumMap<ChestType, Material>> builder = ImmutableMap.builder();

        builder.put(TFBlocks.TWILIGHT_OAK_CHEST.get(), chestMaterial("twilight"));
        builder.put(TFBlocks.CANOPY_CHEST.get(), chestMaterial("canopy"));
        builder.put(TFBlocks.MANGROVE_CHEST.get(), chestMaterial("mangrove"));
        builder.put(TFBlocks.DARK_CHEST.get(), chestMaterial("darkwood"));
        builder.put(TFBlocks.TIME_CHEST.get(), chestMaterial("time"));
        builder.put(TFBlocks.TRANSFORMATION_CHEST.get(), chestMaterial("trans"));
        builder.put(TFBlocks.MINING_CHEST.get(), chestMaterial("mining"));
        builder.put(TFBlocks.SORTING_CHEST.get(), chestMaterial("sort"));

        MATERIALS = builder.build();
    }

    public TwilightChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Material getMaterial(BlockEntity blockEntity, ChestType chestType) {
        EnumMap<ChestType, Material> b = MATERIALS.get(blockEntity.getBlockState().getBlock());

        if (b == null) return null;

        Material material = b.get(chestType);

        return material;
    }

    private static EnumMap<ChestType, Material> chestMaterial(String type) {
        EnumMap<ChestType, Material> map = new EnumMap<>(ChestType.class);

        map.put(ChestType.SINGLE, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("model/chest/" + type + "/" + type)));
        map.put(ChestType.LEFT, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("model/chest/" + type + "/left")));
        map.put(ChestType.RIGHT, new Material(Sheets.CHEST_SHEET, TwilightForestMod.prefix("model/chest/" + type + "/right")));

        return map;
    }
}
