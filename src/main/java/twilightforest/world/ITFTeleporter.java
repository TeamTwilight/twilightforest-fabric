package twilightforest.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * {@link io.github.fabricators_of_create.porting_lib.entity.ITeleporter} but only with getPortalInfo.
 */
public interface ITFTeleporter {
	@Nullable
	default PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		return new PortalInfo(entity.position(), Vec3.ZERO, entity.getYRot(), entity.getXRot());
	}
}
