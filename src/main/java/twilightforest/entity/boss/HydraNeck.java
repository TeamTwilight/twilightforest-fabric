package twilightforest.entity.boss;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import twilightforest.TwilightForestMod;

/**
 * 1:1 port of upstream {@code twilightforest.entity.boss.HydraNeck} — neck
 * segment hitbox between the body and a {@link HydraHead}. Forwards
 * right-click interaction to the head so name-tagging works on any segment.
 */
public class HydraNeck extends HydraPart {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("hydra_neck");

	public final HydraHead head;

	public HydraNeck(HydraHead head) {
		super(head.getParent(), 2F, 2F);
		this.head = head;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return this.head.interact(player, hand);
	}

	public ResourceLocation renderer() {
		return RENDERER;
	}
}
