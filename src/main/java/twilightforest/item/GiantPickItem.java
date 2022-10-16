package twilightforest.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.GiantBlock;
import twilightforest.capabilities.CapabilityList;
import twilightforest.init.TFBlocks;

import java.util.List;
import java.util.UUID;

public class GiantPickItem extends PickaxeItem {

	public static final UUID GIANT_REACH_MODIFIER = UUID.fromString("7f10172d-de69-49d7-81bd-9594286a6827");
	public static final UUID GIANT_RANGE_MODIFIER = UUID.fromString("cafc02ed-392f-4bf4-b745-42beff107ec4");

	public GiantPickItem(Tier material, Properties properties) {
		super(material, 8, -3.5F, properties);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, level, tooltip, flags);
		tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
		attributeBuilder.putAll(super.getDefaultAttributeModifiers(slot));
		attributeBuilder.put(ReachEntityAttributes.REACH, new AttributeModifier(GIANT_REACH_MODIFIER, "Reach modifier", 2.5, AttributeModifier.Operation.ADDITION));
		attributeBuilder.put(ReachEntityAttributes.ATTACK_RANGE, new AttributeModifier(GIANT_RANGE_MODIFIER, "Range modifier", 2.5, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.MAINHAND ? attributeBuilder.build() : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		// extra 64X strength vs giant obsidian
		destroySpeed *= (state.getBlock() == TFBlocks.GIANT_OBSIDIAN.get()) ? 64 : 1;
		// 64x strength vs giant blocks
		return state.getBlock() instanceof GiantBlock ? destroySpeed * 64 : destroySpeed;
	}

	@Override
	public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
		ItemStack stack = pPlayer.getMainHandItem();
		if (stack.is(this)) {
			CapabilityList.GIANT_PICK_MINE.maybeGet(pPlayer).ifPresent(cap -> cap.setMining(pLevel.getGameTime()));
		}
		return super.canAttackBlock(pState, pLevel, pPos, pPlayer);
	}
}