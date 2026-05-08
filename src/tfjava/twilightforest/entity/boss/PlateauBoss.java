package twilightforest.entity.boss;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructures;

/**
 * 1:1 port of upstream {@code twilightforest.entity.boss.PlateauBoss} — final-castle
 * boss entity that returns the canonical home structure / death container /
 * boss-spawner block. Inherits all the home-restriction + boss-bar machinery from
 * {@link BaseTFBoss}.
 *
 * <p>Codex Fabric port note: codex's {@link BaseTFBoss#getBossBarColor()} returns
 * {@link BossEvent.BossBarColor} (vanilla enum), upstream returns an int. Upstream's
 * 0xFFFFFF maps to {@link BossEvent.BossBarColor#WHITE}. The other upstream
 * accessors ({@code getHomeRadius / getHomeStructure / getDeathContainer /
 * getBossSpawner}) aren't abstract on codex's BaseTFBoss — they're kept here as
 * 1:1 helpers for any caller that {@code instanceof PlateauBoss} dispatches.</p>
 */
public class PlateauBoss extends BaseTFBoss {

	public PlateauBoss(EntityType<? extends PlateauBoss> type, Level level) {
		super(type, level);
		this.xpReward = 647;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes();
	}

	public int getHomeRadius() {
		return 30;
	}

	public ResourceKey<Structure> getHomeStructure() {
		return TFStructures.FINAL_CASTLE;
	}

	public Block getDeathContainer(RandomSource random) {
		return TFBlocks.CANOPY_CHEST.get();
	}

	public Block getBossSpawner() {
		return TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get();
	}

	@Override
	protected BossEvent.BossBarColor getBossBarColor() {
		return BossEvent.BossBarColor.WHITE;
	}
}
