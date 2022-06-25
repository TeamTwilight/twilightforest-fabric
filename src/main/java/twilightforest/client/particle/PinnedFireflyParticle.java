package twilightforest.client.particle;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import twilightforest.client.particle.data.PinnedFireflyData;

import org.jetbrains.annotations.Nullable;

/**
 * Version of {@link FireflyParticle} where location is pinned to an entity
 */
public class PinnedFireflyParticle extends FireflyParticle {
	private final Entity follow;

	public PinnedFireflyParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, Entity entity) {
		super(level, x, y, z, vx, vy, vz);
		this.follow = entity;
	}

	@Override
	public void tick() {
		super.tick();
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.setPos(this.follow.getX(), this.follow.getY(), this.follow.getZ());
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<PinnedFireflyData> {

		@Nullable
		@Override
		public Particle createParticle(PinnedFireflyData data, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
			Entity e = level.getEntity(data.follow);
			if (e == null) {
				return null;
			} else {
				PinnedFireflyParticle ret = new PinnedFireflyParticle(level, x, y, z, vx, vy, vz, e);
				ret.pickSprite(sprite);
				return ret;
			}
		}
	}
}
