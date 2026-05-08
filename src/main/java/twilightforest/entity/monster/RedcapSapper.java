package twilightforest.entity.monster;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItemVisuals;

public class RedcapSapper extends Redcap {
    public RedcapSapper(EntityType<? extends RedcapSapper> type, Level level) {
        super(type, level);
        this.heldPick = TFItemVisuals.withModel(new ItemStack(Items.DIAMOND_PICKAXE), TFItemVisuals.IRONWOOD_PICKAXE);
        this.tntLeft = 3;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Redcap.registerAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected int getDisplayModel() {
        return TFItemVisuals.REDCAP_SAPPER_DISPLAY;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(source, difficulty);
        this.setItemSlot(EquipmentSlot.FEET, TFItemVisuals.withModel(new ItemStack(Items.DIAMOND_BOOTS), TFItemVisuals.IRONWOOD_BOOTS));
    }
}