package twilightforest.compat.tcon.traits;

import io.github.fabricators_of_create.porting_lib.event.common.PlayerBreakSpeedCallback;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class PrecipitateModifier extends NoLevelsModifier {

	@Override
	public void onBreakSpeed(IToolStackView tool, int level, PlayerBreakSpeedCallback.BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		event.newSpeed = event.newSpeed + (this.getBonusPercentage(event.player) * event.originalSpeed);
	}

	private float getBonusPercentage(LivingEntity entity) {
		float maxHealth = entity.getMaxHealth();
		return (maxHealth - entity.getHealth()) / maxHealth;
	}
}
