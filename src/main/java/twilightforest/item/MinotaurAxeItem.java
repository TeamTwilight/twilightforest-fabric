package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.List;

public class MinotaurAxeItem extends AxeItem {

	private static final int BONUS_CHARGING_DAMAGE = 7;

	protected MinotaurAxeItem(Tier material, Properties props) {
		super(material, 6F, material.getSpeed() * 0.05f - 3.4f, props);
	}

	public static float onAttack(DamageSource damageSource, LivingEntity target, float amount) {
		Entity source = damageSource.getDirectEntity();
		if (!target.level.isClientSide && source instanceof LivingEntity && source.isSprinting() && (damageSource.getMsgId().equals("player") || damageSource.getMsgId().equals("mob"))) {
			ItemStack weapon = ((LivingEntity) damageSource.getDirectEntity()).getMainHandItem();
			if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
				// enchantment attack sparkles
				((ServerLevel) target.level).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				return amount + BONUS_CHARGING_DAMAGE;
			}
		}
		return amount;
	}

	@Override
	public int getEnchantmentValue() {
		return Tiers.GOLD.getEnchantmentValue();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		tooltip.add(new TranslatableComponent("item.twilightforest.minotaur_axe.tooltip").withStyle(ChatFormatting.GRAY));
	}
}
