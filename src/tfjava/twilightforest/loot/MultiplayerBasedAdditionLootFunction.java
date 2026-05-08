package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.components.entity.MultiplayerInclusivityAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFLoot;

import java.util.List;

public class MultiplayerBasedAdditionLootFunction extends LootItemConditionalFunction {
    public static final MapCodec<MultiplayerBasedAdditionLootFunction> CODEC = RecordCodecBuilder.mapCodec(
        inst -> commonFields(inst)
            .and(NumberProviders.CODEC.fieldOf("extra_count_per_player").forGetter(o -> o.value))
            .apply(inst, MultiplayerBasedAdditionLootFunction::new)
    );

    private final NumberProvider value;

    public MultiplayerBasedAdditionLootFunction(List<LootItemCondition> predicates, NumberProvider value) {
        super(predicates);
        this.value = value;
    }

    public static Builder addForAllParticipatingPlayers(NumberProvider additionPerPlayer) {
        return new Builder(additionPerPlayer);
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return TFLoot.MULTIPLAYER_MULTIPLIER.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls() && context.hasParam(LootContextParams.THIS_ENTITY)) {
            MultiplayerInclusivityAttachment fight = TFDataAttachments.get(context.getParam(LootContextParams.THIS_ENTITY), TFDataAttachments.MULTIPLAYER_FIGHT);
            int qualifiedPlayers = fight.getQualifiedPlayers().size();
            if (qualifiedPlayers > 1) {
                int participatingPlayers = qualifiedPlayers - 1;
                int extraItems = this.value.getInt(context) * participatingPlayers;
                stack.setCount(Mth.clamp(stack.getCount() + extraItems, 0, stack.getMaxStackSize()));
                TwilightForestMod.LOGGER.debug("{} extra players participated in a fight against {}, dropping {} extra {} for a total of {}.",
                        participatingPlayers,
                        context.getParam(LootContextParams.THIS_ENTITY).getType().getDescription().getString(),
                        extraItems,
                        stack.getItem().getDescription().getString(),
                        stack.getCount());
            }
        }
        return stack;
    }

    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final NumberProvider count;

        public Builder(NumberProvider perPlayer) {
            this.count = perPlayer;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public MultiplayerBasedAdditionLootFunction build() {
            return new MultiplayerBasedAdditionLootFunction(this.getConditions(), this.count);
        }
    }
}
