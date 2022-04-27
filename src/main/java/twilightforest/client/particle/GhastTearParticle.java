package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.item.Item;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TFSounds;

@Environment(EnvType.CLIENT)
public class GhastTearParticle extends TextureSheetParticle {

	GhastTearParticle(ClientLevel world, double x, double y, double z, Item item) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.sprite = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(item).getParticleIcon();
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.quadSize = 2.0F;
		this.gravity = 0.6F;

		this.lifetime = 20 + random.nextInt(40);
		this.hasPhysics = true;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.TERRAIN_SHEET;
	}

	@Override
	public void tick() {
		if (this.onGround && random.nextBoolean()) {
			level.playSound(null, this.x, this.y + 1D, this.z, TFSounds.TEAR_BREAK, SoundSource.HOSTILE, 0.5F, 1.0F);

			ItemStack itemID = new ItemStack(Items.GHAST_TEAR);
			for (int i = 0; i < 20; ++i) {
				double gaussX = random.nextGaussian() * 0.1D;
				double gaussY = random.nextGaussian() * 0.2D;
				double gaussZ = random.nextGaussian() * 0.1D;

				level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemID), this.x + random.nextFloat() - random.nextFloat(), this.y + 0.5F, this.z + random.nextFloat(), gaussX, gaussY, gaussZ);
				level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);

			}
			this.remove();
		}
		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new GhastTearParticle(worldIn, x, y, z, Items.GHAST_TEAR);
		}
	}
}
