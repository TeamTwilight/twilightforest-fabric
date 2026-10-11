package twilightforest.item;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import twilightforest.network.ParticlePacket;

import java.util.function.Consumer;

public class FierySwordItem extends Item {

	public FierySwordItem(Properties properties) {
		super(properties);
	}

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (target.level().isClientSide() || target.fireImmune())
			return;

		target.igniteForSeconds(15);
		ParticlePacket packet = new ParticlePacket();
		for (int var1 = 0; var1 < 20; ++var1) {
			double px = target.getX() + target.level().getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
			double py = target.getY() + target.level().getRandom().nextFloat() * target.getBbHeight();
			double pz = target.getZ() + target.level().getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
			packet.queueParticle(ParticleTypes.FLAME, false, false, px, py, pz, 0.02, 0.02, 0.02);
		}
		for (ServerPlayer serverPlayer : PlayerLookup.tracking(target)) {
			ServerPlayNetworking.send(serverPlayer, packet);
		}
		if (target instanceof ServerPlayer serverPlayer) {
			ServerPlayNetworking.send(serverPlayer, packet);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, builder, flag);
		builder.accept(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}
}