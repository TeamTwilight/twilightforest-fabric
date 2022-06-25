package twilightforest.item;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

import javax.annotation.Nonnull;

public class MagicBeansItem extends Item {

	public MagicBeansItem(Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		Block blockAt = level.getBlockState(pos).getBlock();
		ItemStack stack = context.getItemInHand();

		int maxY = Math.max(pos.getY() + 100, 175);
		if (pos.getY() < maxY && blockAt == TFBlocks.UBEROUS_SOIL.get()) {
			if (!level.isClientSide()) {
				stack.shrink(1);
				level.setBlockAndUpdate(pos.above(), TFBlocks.BEANSTALK_GROWER.get().defaultBlockState());
				level.playSound(null, pos, TFSounds.BEANSTALK_GROWTH.get(), SoundSource.BLOCKS, 4.0F, 1.0F);
				if (player instanceof ServerPlayer) {
					player.awardStat(Stats.ITEM_USED.get(this));

					//fallback if the other part doesnt work since its inconsistent
					PlayerAdvancements advancements = ((ServerPlayer) player).getAdvancements();
					ServerAdvancementManager manager = ((ServerLevel) player.getCommandSenderWorld()).getServer().getAdvancements();
					Advancement advancement = manager.getAdvancement(TwilightForestMod.prefix("beanstalk"));
					if (advancement != null && !manager.getAllAdvancements().contains(advancement)) {
						advancements.award(advancement, "use_beans");
					}
				}
			}

			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}
}