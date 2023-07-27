package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

// For making rectangular grids that are approximately-evenly spaced (floating-point -> integer rounding), even if re-sampled for a different chunk or general region
// Making a hexagonal pattern will require using two of these
// Positions are lazily generated, meaning no excess of positions are produced if terminated early
public class RectangleLatticeIterator<T> implements Iterator<T>, Iterable<T> {
    private final int yLevel, latticeStartX, latticeStartZ, latticeCountX, latticeCountZ;
    private final float xSpacing, zSpacing, xOffset, zOffset;
    private final TernaryIntegerFunction<T> converter;

    private int latticeX = 0, latticeZ = 0;

    public static RectangleLatticeIterator<BlockPos.MutableBlockPos> boundedGrid(BoundingBox chunkBounds, int yLevel, float xSpacing, float zSpacing, float xOffset, float zOffset) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        return new RectangleLatticeIterator<>(chunkBounds.minX(), chunkBounds.minZ(), chunkBounds.maxX(), chunkBounds.maxZ(), yLevel, xSpacing, zSpacing, xOffset, zOffset, mutableBlockPos::set);
    }

    public RectangleLatticeIterator(int minX, int minZ, int maxX, int maxZ, int yLevel, float xSpacing, float zSpacing, float xOffset, float zOffset, TernaryIntegerFunction<T> converter) {
        this.yLevel = yLevel;

        this.xSpacing = xSpacing;
        this.zSpacing = zSpacing;
        this.xOffset = xOffset;
        this.zOffset = zOffset;

        this.latticeStartX = getNearestStartLatticeIndex(this.xSpacing, minX - this.xOffset);
        this.latticeStartZ = getNearestStartLatticeIndex(this.zSpacing, minZ - this.zOffset);
        this.latticeCountX = getNearestEndLatticeIndex(this.xSpacing, (maxX + 0.999f) - this.xOffset) - this.latticeStartX + 1;
        this.latticeCountZ = getNearestEndLatticeIndex(this.zSpacing, (maxZ + 0.999f) - this.zOffset) - this.latticeStartZ + 1;

        this.converter = converter;
    }

    @Override
    public T next() {
        T ret = this.converter.apply(this.generateX(), this.yLevel, this.generateZ());

        // March downwards, then rightwards as each column is completed
        if (this.latticeZ + 1 < this.latticeCountZ) {
            this.latticeZ++;
        } else {
            this.latticeZ = 0;
            this.latticeX++;
        }

        return ret;
    }

    private int generateX() {
        return (int) (this.xOffset + (this.latticeStartX + this.latticeX) * this.xSpacing);
    }

    private int generateZ() {
        return (int) (this.zOffset + (this.latticeStartZ + this.latticeZ) * this.zSpacing);
    }

    @Override
    public boolean hasNext() {
        return this.latticeX < this.latticeCountX;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this;
    }

    private static int getNearestStartLatticeIndex(float latticeSpacing, float i) {
        return Mth.floor((i + Mth.positiveModulo(-i, latticeSpacing)) / latticeSpacing);
    }

    private static int getNearestEndLatticeIndex(float latticeSpacing, float i) {
        return Mth.floor((i - Mth.positiveModulo(i, latticeSpacing)) / latticeSpacing);
    }

    @FunctionalInterface
    public interface TernaryIntegerFunction<T> {
        T apply(int x, int y, int z);
    }
}
