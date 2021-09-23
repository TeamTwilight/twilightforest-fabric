package twilightforest.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.block.GiantBlock;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;
import twilightforest.entity.TFAttributes;

public class GiantPickItem extends PickaxeItem implements DynamicAttributeTool {

	protected GiantPickItem(Tier material, Properties props) {
		super(material, 8, -3.5F, props);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		// extra 64X strength vs giant obsidian
		destroySpeed *= (state.getBlock() == TFBlocks.giant_obsidian) ? 64 : 1;
		// 64x strength vs giant blocks
		return state.getBlock() instanceof GiantBlock ? destroySpeed * 64 : destroySpeed;
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
