package twilightforest.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import twilightforest.client.particle.data.LeafParticleData;

import java.util.Random;
import java.util.concurrent.Executor;

public class SpawnFallenLeafFromPacket implements S2CPacket {

	private final BlockPos pos;
	private final Vec3 motion;

	public SpawnFallenLeafFromPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		motion = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public SpawnFallenLeafFromPacket(BlockPos pos, Vec3 motion) {
		this.pos = pos;
		this.motion = motion;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeDouble(motion.x);
		buf.writeDouble(motion.y);
		buf.writeDouble(motion.z);
	}

	@Override
	public void handle(Minecraft client, ClientPacketListener handler, PacketSender sender, SimpleChannel responseTarget) {
		Handler.onMessage(this, client);
	}

	public static class Handler {
		public static boolean onMessage(SpawnFallenLeafFromPacket message, Executor ctx) {
			ctx.execute(new Runnable() {
				@Override
				public void run() {
					Random rand = new Random();
					Level world = Minecraft.getInstance().level;
					int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), world, message.pos, 0);
					int r = Mth.clamp(((color >> 16) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
					int g = Mth.clamp(((color >> 8) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
					int b = Mth.clamp((color & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
					world.addParticle(new LeafParticleData(r, g, b),
							message.pos.getX() + world.random.nextFloat(),
							message.pos.getY(),
							message.pos.getZ() + world.random.nextFloat(),

							(world.random.nextFloat() * -0.5F) * message.motion.x(),
							world.random.nextFloat() * 0.5F + 0.25F,
							(world.random.nextFloat() * -0.5F) * message.motion.z()
					);
				}
			});
			return true;
		}
	}
}
