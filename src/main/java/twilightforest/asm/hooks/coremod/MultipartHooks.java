package twilightforest.asm.hooks.coremod;

import net.minecraft.world.entity.Entity;
import twilightforest.util.multiparts.MultipartEntityUtil;

public class MultipartHooks {
	private static final MultipartEntityUtil multipartEntityUtil = MultipartEntityUtil.INSTANCE;

	public static Entity sendDirtyEntityData(Entity entity) {
		return multipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}
}