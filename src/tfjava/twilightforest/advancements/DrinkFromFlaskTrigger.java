package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.alchemy.Potion;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

/**
 * Fabric port of upstream DrinkFromFlaskTrigger — DI-free.
 * Codec lives directly on TriggerInstance; no inner Factory + Beanification.
 */
public class DrinkFromFlaskTrigger extends SimpleCriterionTrigger<DrinkFromFlaskTrigger.TriggerInstance> {

    @Override
    public Codec<DrinkFromFlaskTrigger.TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, int doses, int seconds, Holder<Potion> potion) {
        this.trigger(player, (instance) -> instance.matches(doses, seconds, potion));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints doses,
                                  MinMaxBounds.Ints seconds, Holder<Potion> potion) implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("doses", MinMaxBounds.Ints.between(0, 4)).forGetter(TriggerInstance::doses),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("seconds", MinMaxBounds.Ints.exactly(8)).forGetter(TriggerInstance::seconds),
                BuiltInRegistries.POTION.holderByNameCodec().fieldOf("potion").forGetter(TriggerInstance::potion))
            .apply(instance, TriggerInstance::new));

        public boolean matches(int doses, int seconds, Holder<Potion> potion) {
            return this.doses.matches(doses) && this.seconds.matches(seconds)
                    && this.potion.is(potion.unwrapKey().orElseThrow());
        }

        public static Criterion<TriggerInstance> drankPotion(int doses, MinMaxBounds.Ints seconds, Holder<Potion> potion) {
            return TFAdvancements.DRINK_FROM_FLASK.get().createCriterion(
                    new TriggerInstance(Optional.empty(), MinMaxBounds.Ints.exactly(doses), seconds, potion));
        }

        public static Criterion<TriggerInstance> drankPotion(MinMaxBounds.Ints doses, MinMaxBounds.Ints seconds, Holder<Potion> potion) {
            return TFAdvancements.DRINK_FROM_FLASK.get().createCriterion(
                    new TriggerInstance(Optional.empty(), doses, seconds, potion));
        }
    }
}
