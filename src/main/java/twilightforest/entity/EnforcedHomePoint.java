package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

public interface EnforcedHomePoint {
    default void saveHomePointToNbt(CompoundTag tag) {
        GlobalPos home = this.getRestrictionPoint();
        if (home != null) {
            GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, home)
                    .resultOrPartial(TwilightForestMod.LOGGER::error)
                    .ifPresent(encoded -> tag.put("HomePos", encoded));
        }
    }

    default void loadHomePointFromNbt(CompoundTag tag) {
        if (tag.contains("Home", Tag.TAG_LIST) && this instanceof Entity entity) {
            ListTag home = tag.getList("Home", Tag.TAG_DOUBLE);
            this.setRestrictionPoint(GlobalPos.of(entity.level().dimension(),
                    BlockPos.containing(home.getDouble(0), home.getDouble(1), home.getDouble(2))));
            return;
        }
        if (tag.contains("HomePos")) {
            this.setRestrictionPoint(GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("HomePos"))
                    .resultOrPartial(TwilightForestMod.LOGGER::error)
                    .orElse(null));
        }
    }

    default boolean isMobWithinHomeArea(Entity entity) {
        if (!this.isRestrictionPointValid(entity.level().dimension())) {
            return true;
        }
        return this.getRestrictionPoint().pos().distSqr(entity.blockPosition()) < (double) (this.getHomeRadius() * this.getHomeRadius());
    }

    default boolean isRestrictionPointValid(ResourceKey<Level> currentLevel) {
        GlobalPos home = this.getRestrictionPoint();
        return home != null && home.dimension().equals(currentLevel);
    }

    @Nullable
    default GlobalPos getRestrictionPoint() {
        return null;
    }

    void setRestrictionPoint(@Nullable GlobalPos pos);

    default int getHomeRadius() {
        return 16;
    }
}
