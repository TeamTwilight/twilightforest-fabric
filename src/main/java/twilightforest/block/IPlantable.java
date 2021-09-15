package twilightforest.block;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlantable {
    default PlantType getPlantType(BlockGetter world, BlockPos pos) {
        if (this instanceof CropBlock) return PlantType.CROP;
        if (this instanceof SaplingBlock) return PlantType.PLAINS;
        if (this instanceof FlowerBlock) return PlantType.PLAINS;
        if (this == Blocks.DEAD_BUSH)      return PlantType.DESERT;
        if (this == Blocks.LILY_PAD)       return PlantType.WATER;
        if (this == Blocks.RED_MUSHROOM)   return PlantType.CAVE;
        if (this == Blocks.BROWN_MUSHROOM) return PlantType.CAVE;
        if (this == Blocks.NETHER_WART)    return PlantType.NETHER;
        if (this == Blocks.TALL_GRASS)      return PlantType.PLAINS;
        return PlantType.PLAINS;
    }

    BlockState getPlant(BlockGetter world, BlockPos pos);

    final class PlantType
    {
        private static final Pattern INVALID_CHARACTERS = Pattern.compile("[^a-z_]"); //Only a-z and _ are allowed, meaning names must be lower case. And use _ to separate words.
        private static final Map<String, PlantType> VALUES = new ConcurrentHashMap<>();

        public static final PlantType PLAINS = get("plains");
        public static final PlantType DESERT = get("desert");
        public static final PlantType BEACH = get("beach");
        public static final PlantType CAVE = get("cave");
        public static final PlantType WATER = get("water");
        public static final PlantType NETHER = get("nether");
        public static final PlantType CROP = get("crop");

        /**
         * Getting a custom {@link PlantType}, or an existing one if it has the same name as that one. Your plant should implement {@link IPlantable}
         * and return this custom type in {@link IPlantable#getPlantType(BlockGetter, BlockPos)}.
         *
         * <p>If your new plant grows on blocks like any one of them above, never create a new {@link PlantType}.
         * This Type is only functioning in
         * {@link twilightforest.extensions.IBlockMethods#canSustainPlant(BlockState, BlockGetter, BlockPos, net.minecraft.core.Direction, IPlantable)},
         * which you are supposed to override this function in your new block and create a new plant type to grow on that block.
         *
         * This method can be called during parallel loading
         * @param name the name of the type of plant, you had better follow the style above
         * @return the acquired {@link PlantType}, a new one if not found.
         */
        public static PlantType get(String name)
        {
            return VALUES.computeIfAbsent(name, e ->
            {
                if (INVALID_CHARACTERS.matcher(e).find())
                    throw new IllegalArgumentException("PlantType.get() called with invalid name: " + name);
                return new PlantType(e);
            });
        }

        private final String name;

        private PlantType(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }
}
