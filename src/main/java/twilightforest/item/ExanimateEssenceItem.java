package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.block.OminousCandleBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

public class ExanimateEssenceItem extends CodexItem {

    public ExanimateEssenceItem(Properties properties, Item fallback) {
        super(properties, fallback, -1);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        boolean flag = false;
        BlockState state = level.getBlockState(blockPos);
        if (state.getBlock() instanceof CandleBlock candleBlock && OminousCandleBlock.CANDLE_MAP.containsKey(candleBlock) && state.getValue(CandleBlock.LIT)) {
            this.playSound(level, blockPos);
            level.setBlockAndUpdate(blockPos, OminousCandleBlock.CANDLE_MAP.get(candleBlock).get().defaultBlockState().setValue(OminousCandleBlock.CANDLES, state.getValue(CandleBlock.CANDLES)));
            level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, blockPos);
            OminousCandleBlock.eruptFlameParticles(level, blockPos, level.getBlockState(blockPos));
            flag = true;
        } else {
            blockPos = blockPos.relative(context.getClickedFace());
            state = level.getBlockState(blockPos);
            BlockState fireState = TFBlocks.OMINOUS_FIRE.get().defaultBlockState();

            if (state.canBeReplaced() && fireState.canSurvive(level, blockPos)) {
                this.playSound(level, blockPos);
                level.setBlock(blockPos, fireState, Block.UPDATE_ALL);
                level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, blockPos);
                flag = true;
            }
        }

        if (flag) {
            context.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.FAIL;
    }

    private void playSound(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        level.playSound(null, pos, net.minecraft.sounds.SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.5F, (random.nextFloat() - random.nextFloat()) * 0.2F + 0.75F);
    }
}
