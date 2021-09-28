package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.ProtectionBox;

public class AreaProtectionPacket extends ISimplePacket {

	private final BoundingBox sbb;
	private final BlockPos pos;

	public AreaProtectionPacket(BoundingBox sbb, BlockPos pos) {
		this.sbb = sbb;
		this.pos = pos;
	}

	public AreaProtectionPacket(FriendlyByteBuf buf) {
		sbb = new BoundingBox(
				buf.readInt(), buf.readInt(), buf.readInt(),
				buf.readInt(), buf.readInt(), buf.readInt()
		);
		pos = buf.readBlockPos();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(sbb.minX());
		buf.writeInt(sbb.minY());
		buf.writeInt(sbb.minZ());
		buf.writeInt(sbb.maxX());
		buf.writeInt(sbb.maxY());
		buf.writeInt(sbb.maxZ());
		buf.writeLong(pos.asLong());
	}

	@Override
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {

		public static boolean onMessage(AreaProtectionPacket message) {
			Minecraft.getInstance().execute(() -> {

				ClientLevel world = Minecraft.getInstance().level;
				addProtectionBox(world, message.sbb);

				for (int i = 0; i < 20; i++) {

					double vx = world.random.nextGaussian() * 0.02D;
					double vy = world.random.nextGaussian() * 0.02D;
					double vz = world.random.nextGaussian() * 0.02D;

					double x = message.pos.getX() + 0.5D + world.random.nextFloat() - world.random.nextFloat();
					double y = message.pos.getY() + 0.5D + world.random.nextFloat() - world.random.nextFloat();
					double z = message.pos.getZ() + 0.5D + world.random.nextFloat() - world.random.nextFloat();

					world.addParticle(TFParticleType.PROTECTION, x, y, z, vx, vy, vz);
				}
			});
			return true;
		}

		static void addProtectionBox(ClientLevel world, BoundingBox sbb) {

			for (Entity entity : world.entitiesForRendering()) {
				if (entity instanceof ProtectionBox) {
					ProtectionBox protectionBox = (ProtectionBox) entity;
					if (protectionBox.lifeTime > 0 && protectionBox.matches(sbb)) {
						protectionBox.resetLifetime();
						return;
					}
				}
			}

			world.addFreshEntity(new ProtectionBox(world, sbb));
		}
	}
}
