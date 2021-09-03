package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class KnightmetalSwordItem extends SwordItem {

	private static final int BONUS_DAMAGE = 2;

	public KnightmetalSwordItem(Tier material, Properties props) {
		super(material, 3, -2.4F, props);
	}

	@SubscribeEvent
	public static void onDamage(LivingAttackEvent evt) {
		LivingEntity target = evt.getEntityLiving();

		if (!target.level.isClientSide && evt.getSource().getDirectEntity() instanceof LivingEntity) {
			ItemStack weapon = ((LivingEntity) evt.getSource().getDirectEntity()).getMainHandItem();

			if (!weapon.isEmpty() && ((target.getArmorValue() > 0 && (weapon.getItem() == TFItems.knightmetal_pickaxe.get() || weapon.getItem() == TFItems.knightmetal_sword.get())) || (target.getArmorValue() == 0 && weapon.getItem() == TFItems.knightmetal_axe.get()))) {
				// TODO scale bonus dmg with the amount of armor?
				target.hurt(DamageSource.MAGIC, BONUS_DAMAGE);
				// don't prevent main damage from applying
				target.invulnerableTime = 0;
				// enchantment attack sparkles
				((ServerLevel) target.level).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flags) {
		super.appendHoverText(stack, world, list, flags);
		list.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
	}
}
