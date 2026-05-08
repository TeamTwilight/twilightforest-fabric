package twilightforest.util.entities;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class EntityExcludedDamageSource extends DamageSource {
    private final List<EntityType<?>> entities;

    public EntityExcludedDamageSource(Holder<DamageType> type, @Nullable Entity directEntity, @Nullable Entity causingEntity, EntityType<?>... entities) {
        super(type, directEntity, causingEntity);
        this.entities = Arrays.stream(entities).toList();
    }

    public EntityExcludedDamageSource(Holder<DamageType> type, EntityType<?>... entities) {
        super(type);
        this.entities = Arrays.stream(entities).toList();
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity living) {
        LivingEntity killCredit = living.getKillCredit();
        String key = "death.attack." + this.type().msgId();
        if (killCredit != null) {
            for (EntityType<?> entity : this.entities) {
                if (killCredit.getType() == entity) {
                    return Component.translatable(key, living.getDisplayName());
                }
            }
            return Component.translatable(key + ".player", living.getDisplayName(), killCredit.getDisplayName());
        }
        return Component.translatable(key, living.getDisplayName());
    }
}
