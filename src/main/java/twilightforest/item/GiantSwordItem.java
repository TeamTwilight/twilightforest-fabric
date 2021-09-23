package twilightforest.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.TFAttributes;

public class GiantSwordItem extends SwordItem implements DynamicAttributeTool {

	public GiantSwordItem(Tier material, Properties props) {
		super(material, 10, -3.5F, props);
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack material) {
		return material.getItem() == TFItems.ironwood_ingot || super.isValidRepairItem(stack, material);
	}

	//TODO: Get this working by creating a mixin within the ServerPlayerGameMode and anywhere else forge dose it to add reach modifier
	@Override
	public Multimap<Attribute, AttributeModifier> getDynamicModifiers(EquipmentSlot slot, ItemStack stack, @org.jetbrains.annotations.Nullable LivingEntity user) {
		if ((stack == TFItems.giant_pickaxe.getDefaultInstance() || stack == TFItems.giant_sword.getDefaultInstance()) && slot.equals(EquipmentSlot.MAINHAND)) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(TFAttributes.REACH_DISTANCE, new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Tool modifier", 2.5, AttributeModifier.Operation.ADDITION));
			return builder.build();
		} else {
			return EMPTY;
		}
	}


}
