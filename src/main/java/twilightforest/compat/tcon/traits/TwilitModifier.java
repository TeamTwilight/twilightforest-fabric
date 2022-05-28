package twilightforest.compat.tcon.traits;

import io.github.fabricators_of_create.porting_lib.event.common.PlayerBreakSpeedCallback;
import net.minecraft.core.Direction;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import twilightforest.world.registration.TFGenerationSettings;

public class TwilitModifier extends NoLevelsModifier {
	@Override
	public float getEntityDamage(IToolStackView tool, int level, ToolAttackContext context, float baseDamage, float damage) {
		return !context.getAttacker().level.dimension().equals(TFGenerationSettings.DIMENSION_KEY) ? damage + 2.0F : damage;
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, int level, PlayerBreakSpeedCallback.BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		if(event.player.level.dimension().equals(TFGenerationSettings.DIMENSION_KEY)) {
			event.newSpeed = event.newSpeed + 2.0F;
		}
	}
}
