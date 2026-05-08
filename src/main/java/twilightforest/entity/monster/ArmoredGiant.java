package twilightforest.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.SynchedEntityData;
import twilightforest.init.TFItemVisuals;

import java.util.List;

public class ArmoredGiant extends GiantMiner {
    public ArmoredGiant(EntityType<? extends ArmoredGiant> entityType, Level level) {
        super(entityType, level);
    }

}
