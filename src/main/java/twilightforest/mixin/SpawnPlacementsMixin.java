package twilightforest.mixin;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.block.TFBlocks;
import twilightforest.util.TriPredicate;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

@Mixin(SpawnPlacements.Type.class)
public class SpawnPlacementsMixin {

    @Unique
    private TriPredicate<LevelReader, BlockPos, EntityType<?>> canSpawn;

    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    @Mutable
    private static SpawnPlacements.Type[] $VALUES;

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    public static SpawnPlacements.Type init(String name, int id) {
        throw new AssertionError();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectData(String enumName, int ordinal, CallbackInfo ci) {
        if(enumName.equals("TF_ON_ICE")) {
            canSpawn = (world, pos, entityType) -> {
                BlockState state = world.getBlockState(pos.below());
                Block block = state.getBlock();
                BlockPos up = pos.above();
                return (block == TFBlocks.wispy_cloud || block == TFBlocks.wispy_cloud) && block != Blocks.BEDROCK && block != Blocks.BARRIER && NaturalSpawner.isValidEmptySpawnBlock(world, pos, world.getBlockState(pos), world.getFluidState(pos), entityType) && NaturalSpawner.isValidEmptySpawnBlock(world, up, world.getBlockState(up), world.getFluidState(up), entityType);
            };
        } else if(enumName.equals("CLOUD_DWELLERS")) {
            canSpawn = (world, pos, entityType) -> {
                BlockState state = world.getBlockState(pos.below());
                Block block = state.getBlock();
                Material material = state.getMaterial();
                BlockPos up = pos.above();
                return (material == Material.ICE || material == Material.ICE_SOLID) && block != Blocks.BEDROCK && block != Blocks.BARRIER && NaturalSpawner.isValidEmptySpawnBlock(world, pos, world.getBlockState(pos), world.getFluidState(pos), entityType) && NaturalSpawner.isValidEmptySpawnBlock(world, up, world.getBlockState(up), world.getFluidState(up), entityType);
            };
        } else {
            canSpawn = (levelReader, blockPos, entityType) -> true;
        }
    }

    static {
        ArrayList<SpawnPlacements.Type> values =  new ArrayList<>(Arrays.asList($VALUES));
        SpawnPlacements.Type last = values.get(values.size() - 1);

        // add new value
        values.add(init("TF_ON_ICE", last.ordinal() + 1));
        values.add(init("CLOUD_DWELLERS", last.ordinal() + 1));

        $VALUES = values.toArray(new SpawnPlacements.Type[0]);
    }
}
