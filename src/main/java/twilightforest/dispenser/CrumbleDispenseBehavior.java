package twilightforest.dispenser;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.block.TFBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CrumbleDispenseBehavior extends DefaultDispenseItemBehavior {

    boolean fired = false;
    private final List<Pair<Predicate<BlockState>, UnaryOperator<BlockState>>> crumbleTransforms = new ArrayList<>();
    private final List<Predicate<BlockState>> harvestedStates = new ArrayList<>();

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        this.addCrumbleTransforms();
        boolean crumbled = false;
        boolean harvested = false;
        Level world = source.getLevel();
        BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        BlockState blockstate = world.getBlockState(blockpos);
        if(!world.isClientSide) {
            if (!(stack.getMaxDamage() == stack.getDamageValue() + 1)) {
                for (Pair<Predicate<BlockState>, UnaryOperator<BlockState>> transform : crumbleTransforms) {
                    if (transform.getLeft().test(blockstate)) {
                        world.setBlock(blockpos, transform.getRight().apply(blockstate), 3);
                        crumbled = true;
                    }
                }

                if (crumbled) {
                    world.levelEvent(2001, blockpos, Block.getId(blockstate));
                    if (stack.hurt(1, world.random, (ServerPlayer) null)) {
                        stack.setCount(0);
                    }
                    fired = true;
                }

                for (Predicate<BlockState> predicate : harvestedStates) {
                    if (predicate.test(blockstate)) {
                        world.destroyBlock(blockpos, true);
                        harvested = true;
                    }
                }

                if (harvested) {
                    if (stack.hurt(1, world.random, (ServerPlayer) null)) {
                        stack.setCount(0);
                    }
                    fired = true;
                }
                return stack;
            }
        }
        return stack;
    }

    @Override
    protected void playSound(BlockSource source) {
        if (fired) {
            super.playSound(source);
            fired = false;
        } else {
            source.getLevel().levelEvent(1001, source.getPos(), 0);
        }
    }

    private void addCrumbleTransforms() {
        addCrumble(() -> Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS::defaultBlockState);
        addCrumble(() -> Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS::defaultBlockState);
        addCrumble(() -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE::defaultBlockState);
        addCrumble(() -> Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS::defaultBlockState);
        addCrumble(() -> TFBlocks.maze_stone_brick, () -> TFBlocks.maze_stone_cracked.defaultBlockState());
        addCrumble(() -> TFBlocks.underbrick, () -> TFBlocks.underbrick_cracked.defaultBlockState());
        addCrumble(() -> TFBlocks.tower_wood, () -> TFBlocks.tower_wood_cracked.defaultBlockState());
        addCrumble(() -> TFBlocks.deadrock, () -> TFBlocks.deadrock_cracked.defaultBlockState());
        addCrumble(() -> TFBlocks.castle_brick, () -> TFBlocks.castle_brick_cracked.defaultBlockState());
        addCrumble(() -> TFBlocks.nagastone_pillar, () -> TFBlocks.nagastone_pillar_weathered.defaultBlockState());
        addCrumble(() -> TFBlocks.etched_nagastone, () -> TFBlocks.etched_nagastone_weathered.defaultBlockState());
        addCrumble(() -> Blocks.STONE, Blocks.COBBLESTONE::defaultBlockState);
        addCrumble(() -> Blocks.COBBLESTONE, Blocks.GRAVEL::defaultBlockState);
        addCrumble(() -> Blocks.SANDSTONE, Blocks.SAND::defaultBlockState);
        addCrumble(() -> Blocks.RED_SANDSTONE, Blocks.RED_SAND::defaultBlockState);
        addCrumble(() -> Blocks.GRASS_BLOCK, Blocks.DIRT::defaultBlockState);
        addCrumble(() -> Blocks.MYCELIUM, Blocks.DIRT::defaultBlockState);
        addCrumble(() -> Blocks.PODZOL, Blocks.DIRT::defaultBlockState);
        addCrumble(() -> Blocks.COARSE_DIRT, Blocks.DIRT::defaultBlockState);
        addCrumble(() -> Blocks.CRIMSON_NYLIUM, Blocks.NETHERRACK::defaultBlockState);
        addCrumble(() -> Blocks.WARPED_NYLIUM, Blocks.NETHERRACK::defaultBlockState);
        addCrumble(() -> Blocks.QUARTZ_BLOCK, Blocks.SAND::defaultBlockState);
        addHarvest(() -> Blocks.GRAVEL);
        addHarvest(() -> Blocks.DIRT);
        addHarvest(() -> Blocks.SAND);
        addHarvest(() -> Blocks.RED_SAND);
        addHarvest(() -> Blocks.CLAY);
        addHarvest(() -> Blocks.ANDESITE);
        addHarvest(() -> Blocks.GRANITE);
        addHarvest(() -> Blocks.DIORITE);
    }

    private void addCrumble(Supplier<Block> block, Supplier<BlockState> result) {
        addCrumble(state -> state.getBlock() == block, state -> result.get());
    }

    private void addCrumble(Predicate<BlockState> test, UnaryOperator<BlockState> transform) {
        crumbleTransforms.add(Pair.of(test, transform));
    }

    private void addHarvest(Supplier<Block> block) {
        addHarvest(state -> state.getBlock() == block);
    }

    private void addHarvest(Predicate<BlockState> test) {
        harvestedStates.add(test);
    }
}
