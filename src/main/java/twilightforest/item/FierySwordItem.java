package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;
import java.util.List;

public class FierySwordItem extends SwordItem implements CustomEnchantingBehaviorItem {

	public FierySwordItem(Tier toolMaterial, Properties props) {
		super(toolMaterial, 3, -2.4F, props);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment != Enchantments.FIRE_ASPECT;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return !EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.FIRE_ASPECT) && CustomEnchantingBehaviorItem.super.isBookEnchantable(stack, book);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);

		if (result && !target.getLevel().isClientSide() && !target.fireImmune()) {
			target.setSecondsOnFire(15);
		} else {
			for (int var1 = 0; var1 < 20; ++var1) {
				double px = target.getX() + target.getLevel().getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
				double py = target.getY() + target.getLevel().getRandom().nextFloat() * target.getBbHeight();
				double pz = target.getZ() + target.getLevel().getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
				target.getLevel().addParticle(ParticleTypes.FLAME, px, py, pz, 0.02, 0.02, 0.02);
			}
		}

		return result;
	}

	//we have to set the entity on fire early in order to actually cook the food
	public static InteractionResult setFireBeforeDeath(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
		if(entity instanceof LivingEntity living && living.getMainHandItem().is(TFItems.FIERY_SWORD.get()) && !living.fireImmune()) {
			living.setSecondsOnFire(1);
		}
		return InteractionResult.PASS;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
	}
}
