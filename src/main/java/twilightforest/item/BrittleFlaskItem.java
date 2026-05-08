package twilightforest.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Fabric port of TF {@code BrittleFlaskItem} / {@code GreaterFlaskItem}.
 */
public class BrittleFlaskItem extends CodexItem {

    private final boolean greater;
    private final int maxDoses;

    public BrittleFlaskItem(Properties properties, Item fallback, boolean greater) {
        super(properties, fallback, -1);
        this.greater = greater;
        this.maxDoses = greater ? 4 : 3;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.set(TFDataComponents.POTION_FLASK_CONTENTS,
                this.greater ? PotionFlaskComponent.EMPTY_UNBREAKABLE : PotionFlaskComponent.EMPTY);
        return stack;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS,
                this.greater ? PotionFlaskComponent.EMPTY_UNBREAKABLE : PotionFlaskComponent.EMPTY).potion().potion().isPresent();
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.opaque(stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS,
                this.greater ? PotionFlaskComponent.EMPTY_UNBREAKABLE : PotionFlaskComponent.EMPTY).potion().getColor());
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return tryFill(stack, other, action, player);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        return tryFill(stack, slot.getItem(), action, player);
    }

    private boolean tryFill(ItemStack flaskStack, ItemStack other, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) return false;
        PotionContents bottlePotion = other.get(DataComponents.POTION_CONTENTS);
        if (bottlePotion == null) return false;

        PotionFlaskComponent flask = flaskStack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS,
                this.greater ? PotionFlaskComponent.EMPTY_UNBREAKABLE : PotionFlaskComponent.EMPTY);

        boolean canMix = flask.potion().potion().isEmpty() || flask.potion().equals(bottlePotion);
        boolean hasRoom = flask.doses() < this.maxDoses - flask.breakage();
        if (!canMix || !hasRoom) return false;

        if (!player.getAbilities().instabuild) {
            other.shrink(1);
            if (!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
                player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
            }
        }
        changeAndConsumeFlask(flaskStack, player, st ->
                st.set(TFDataComponents.POTION_FLASK_CONTENTS, flask.tryAddDose(bottlePotion)));
        player.level().playSound(null, player, TFSounds.FLASK_FILL, SoundSource.PLAYERS,
                (flask.doses() + 1) * 0.25F, player.level().getRandom().nextFloat() * 0.1F + 0.9F);
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        PotionFlaskComponent flask = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS,
                this.greater ? PotionFlaskComponent.EMPTY_UNBREAKABLE : PotionFlaskComponent.EMPTY);

        if (flask.potion() == PotionContents.EMPTY) {
            return InteractionResultHolder.fail(stack);
        }

        if (flask.doses() > 0) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        PotionFlaskComponent flask = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS,
                this.greater ? PotionFlaskComponent.EMPTY_UNBREAKABLE : PotionFlaskComponent.EMPTY);
        if (flask.potion() == PotionContents.EMPTY || !(entity instanceof Player player)) {
            return super.finishUsingItem(stack, level, entity);
        }

        if (!level.isClientSide()) {
            for (MobEffectInstance effect : flask.potion().getAllEffects()) {
                if (effect.is(MobEffects.HARM) != entity.isInvertedHealAndHarm() && effect.getAmplifier() > 0) {
                    entity.hurt(TFDamageTypes.source(level, TFDamageTypes.FAILED_CHALLENGE), (float) (6 << effect.getAmplifier()));
                } else if (effect.getEffect().value().isInstantenous()) {
                    effect.getEffect().value().applyInstantenousEffect(player, player, player, effect.getAmplifier(), 1.0D);
                } else {
                    player.addEffect(new MobEffectInstance(effect));
                }
            }
            if (!player.isCreative() && !player.isSpectator() && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                flask.potion().potion().ifPresent(potion -> TFDataAttachments.get(player, TFDataAttachments.FLASK_DOSES).trackDrink(potion, serverPlayer));
            }
            level.playSound(null, player, TFSounds.FLASK_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            changeAndConsumeFlask(stack, player, st -> {
                PotionFlaskComponent next = flask.removeDose();
                st.set(TFDataComponents.POTION_FLASK_CONTENTS, next);
                if (next.breakable() && next.breakage() >= this.maxDoses) {
                    st.shrink(1);
                    level.playSound(null, player, TFSounds.BRITTLE_FLASK_BREAK, SoundSource.PLAYERS, 1.5F, 0.7F);
                } else if (next.breakable()) {
                    level.playSound(null, player, TFSounds.BRITTLE_FLASK_CRACK, SoundSource.PLAYERS, 1.5F, 2.0F);
                }
            });
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new Tooltip(stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS,
                this.greater ? PotionFlaskComponent.EMPTY_UNBREAKABLE : PotionFlaskComponent.EMPTY), this.maxDoses));
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - Math.abs(stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS,
                this.greater ? PotionFlaskComponent.EMPTY_UNBREAKABLE : PotionFlaskComponent.EMPTY).doses() - this.maxDoses) * 13.0F / this.maxDoses);
    }

    private void changeAndConsumeFlask(ItemStack stack, Player player, Consumer<ItemStack> mutator) {
        if (stack.getCount() > 1) {
            ItemStack copy = stack.copyWithCount(1);
            stack.shrink(1);
            mutator.accept(copy);
            if (!player.getInventory().add(copy)) {
                player.drop(copy, false);
            }
        } else {
            mutator.accept(stack);
        }
    }

    public record Tooltip(PotionFlaskComponent component, int maxDoses) implements TooltipComponent {
    }
}
