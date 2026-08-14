package carminite.mixin;

import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(BundlePacket.class)
public class BundlePacketMixin {

	@ModifyVariable(
		method = "<init>(Ljava/lang/Iterable;)V",
		at = @At("HEAD"),
		argsOnly = true,
		name = "packets"
	)
	private static Iterable<Packet<?>> carminite$flattenPackets(Iterable<Packet<?>> packets) {
		List<Packet<?>> list = new ArrayList<>();
		carminite$recursivelyCollectBundledPackets(packets, list);
		return list;
	}

	@Unique
	private static void carminite$recursivelyCollectBundledPackets(
		Iterable<Packet<?>> packets,
		List<Packet<?>> list
	) {
		for (Packet<?> packet : packets) {
			if (packet instanceof BundlePacket<?> bundle) {
				carminite$recursivelyCollectBundledPackets((Iterable) bundle.subPackets(), list);
			} else {
				list.add(packet);
			}
		}
	}
}