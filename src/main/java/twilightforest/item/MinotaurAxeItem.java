package twilightforest.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class MinotaurAxeItem extends AxeItem {

	private static final int BONUS_CHARGING_DAMAGE = 7;

	protected MinotaurAxeItem(Tier material, Properties props) {
		super(material, 6F, material.getSpeed() * 0.05f - 3.4f, props);
	}

	@SubscribeEvent
	public static void onAttack(LivingHurtEvent evt) {
		LivingEntity target = evt.getEntityLiving();
		Entity source = evt.getSource().getDirectEntity();
		if (!target.level.isClientSide && source instanceof LivingEntity && source.isSprinting() && (evt.getSource().getMsgId().equals("player") || evt.getSource().getMsgId().equals("mob"))) {
			ItemStack weapon = ((LivingEntity) evt.getSource().getDirectEntity()).getMainHandItem();
			if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
				evt.setAmount(evt.getAmount() + BONUS_CHARGING_DAMAGE);
				// enchantment attack sparkles
				((ServerLevel) target.level).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
			}
		}
	}

	@Override
	public int getEnchantmentValue() {
		return Tiers.GOLD.getEnchantmentValue();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip"));
	}
}
