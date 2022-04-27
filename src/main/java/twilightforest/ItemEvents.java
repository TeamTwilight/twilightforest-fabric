package twilightforest;

import io.github.fabricators_of_create.porting_lib.event.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.ProjectileImpactCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.item.*;

public class ItemEvents {
    public static void init() {
        LivingEntityEvents.ACTUALLY_HURT.register(KnightmetalSwordItem::onDamage);
        LivingEntityEvents.ACTUALLY_HURT.register(MinotaurAxeItem::onAttack);
        AttackEntityCallback.EVENT.register(FierySwordItem::setFireBeforeDeath);
        ProjectileImpactCallback.EVENT.register(EnderBowItem::onHit);
        OreMagnetItem.buildOreMagnetCache();

        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> Storage.empty(), TFBlockEntities.KEEPSAKE_CASKET.get());
    }
}
