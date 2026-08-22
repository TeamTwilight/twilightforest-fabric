package twilightforest.fabric.events.neo;

import com.google.common.base.Preconditions;
import net.fabricmc.api.EnvType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import twilightforest.fabric.events.TFEvents;
import twilightforest.fabric.events.internal.ICancellableEvent;

public abstract class PlayerInteractEvent extends PlayerEvent {
	private final InteractionHand hand;
	private final BlockPos pos;
	@Nullable
	private final Direction face;

	protected PlayerInteractEvent(Player player, InteractionHand hand, BlockPos pos, @Nullable Direction face) {
		super(Preconditions.checkNotNull(player, "Null player in PlayerInteractEvent!"));
		this.hand = Preconditions.checkNotNull(hand, "Null hand in PlayerInteractEvent!");
		this.pos = Preconditions.checkNotNull(pos, "Null position in PlayerInteractEvent!");
		this.face = face;
	}

	public static class RightClickBlock extends PlayerInteractEvent implements ICancellableEvent {
		private InteractionResult cancellationResult = InteractionResult.PASS;

		private TriState useBlock = TriState.DEFAULT;
		private TriState useItem = TriState.DEFAULT;
		private BlockHitResult hitVec;

		public RightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
			super(player, hand, pos, hitVec.getDirection());
			this.hitVec = hitVec;
		}

		public TriState getUseBlock() {
			return useBlock;
		}

		public TriState getUseItem() {
			return useItem;
		}

		public BlockHitResult getHitVec() {
			return hitVec;
		}

		public void setUseBlock(TriState triggerBlock) {
			this.useBlock = triggerBlock;
		}

		public void setUseItem(TriState triggerItem) {
			this.useItem = triggerItem;
		}

		@Override
		public void setCanceled(boolean canceled) {
			ICancellableEvent.super.setCanceled(canceled);
			if (canceled) {
				useBlock = TriState.FALSE;
				useItem = TriState.FALSE;
			}
		}

		public InteractionResult getCancellationResult() {
			return cancellationResult;
		}

		public void setCancellationResult(InteractionResult result) {
			this.cancellationResult = result;
		}

		@Override
		public RightClickBlock post() {
			TFEvents.RIGHT_CLICK_BLOCK.invoker().onRightClickBlock(this);
			return this;
		}
	}

	public static class LeftClickEmpty extends PlayerInteractEvent {
		public LeftClickEmpty(Player player) {
			super(player, InteractionHand.MAIN_HAND, player.blockPosition(), null);
		}

		@Override
		public LeftClickEmpty post() {
			TFEvents.LEFT_CLICK_EMPTY.invoker().onEmptyLeftClick(this);
			return this;
		}
	}

	public InteractionHand getHand() {
		return hand;
	}

	public ItemStack getItemStack() {
		return getEntity().getItemInHand(hand);
	}

	public BlockPos getPos() {
		return pos;
	}

	@Nullable
	public Direction getFace() {
		return face;
	}

	public Level getLevel() {
		return getEntity().level();
	}

	public EnvType getSide() {
		return getLevel().isClientSide() ? EnvType.CLIENT : EnvType.SERVER;
	}
}