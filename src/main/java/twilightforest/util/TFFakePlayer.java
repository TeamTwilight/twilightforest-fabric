package twilightforest.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Fabric 兼容的 FakePlayer 工具类。
 * 替代 NeoForge 的 FakePlayer / FakePlayerFactory。
 */
public class TFFakePlayer {

	private static final GameProfile TF_FAKE_PLAYER = new GameProfile(
		UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"),
		"[TwilightForest]"
	);

	/**
	 * 获取 FakePlayer 实例（用于服务端操作）。
	 */
	public static net.minecraft.server.level.ServerPlayer getFakePlayer(ServerLevel level) {
		return new net.minecraft.server.level.ServerPlayer(
			level.getServer(),
			level,
			TF_FAKE_PLAYER,
			new net.minecraft.server.level.ClientInformation(
			"en_us",
			8,
			net.minecraft.world.entity.player.ChatVisiblity.FULL,
			true,
			0,
			net.minecraft.world.entity.HumanoidArm.RIGHT,
			false,
			false
		)
		);
	}

	/**
	 * 获取 FakePlayer 实例（用于 Level）。
	 */
	public static net.minecraft.server.level.ServerPlayer getFakePlayer(Level level) {
		if (level instanceof ServerLevel serverLevel) {
			return getFakePlayer(serverLevel);
		}
		throw new IllegalArgumentException("Level must be ServerLevel");
	}
}