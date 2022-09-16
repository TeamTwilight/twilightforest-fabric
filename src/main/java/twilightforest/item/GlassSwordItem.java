package twilightforest.item;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.init.TFSounds;

public class GlassSwordItem extends SwordItem {

	public GlassSwordItem(Tier toolMaterial, Properties properties) {
		super(toolMaterial, 3, -2.4F, properties);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if(target.getLevel() instanceof ServerLevel server) {
			for (int i = 0; i < 20; i++) {
				double px = target.getX() + target.getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
				double py = target.getY() + target.getRandom().nextFloat() * target.getBbHeight();
				double pz = target.getZ() + target.getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
				server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_STAINED_GLASS.defaultBlockState()), px, py, pz, 1, 0, 0, 0, 0);
			}
		}

		stack.hurtAndBreak(stack.getMaxDamage() + 1, attacker, (user) -> {
			user.getLevel().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), TFSounds.GLASS_SWORD_BREAK.get(), attacker.getSoundSource(), 1F, 0.5F);
			user.broadcastBreakEvent(InteractionHand.MAIN_HAND);
			user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
		});
		return true;
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		super.fillItemCategory(tab, items);

		if (this.allowedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			CompoundTag tags = new CompoundTag();
			tags.putBoolean("Unbreakable", true);

			ListTag lore = new ListTag();
			lore.add(StringTag.valueOf("{\"translate\":\"item.twilightforest.glass_sword.tooltip.1\",\"italic\":false,\"color\":\"gray\"}"));
			// uncomment if someone asks if this will ever generate as loot
//			lore.add(StringTag.valueOf("{\"translate\":\"item.twilightforest.glass_sword.tooltip.2\",\"italic\":false,\"color\":\"gray\"}"));

			CompoundTag display = new CompoundTag();
			display.put("Lore", lore);

			tags.put("display", display);
			stack.setTag(tags);
			items.add(stack);
		}
	}
}