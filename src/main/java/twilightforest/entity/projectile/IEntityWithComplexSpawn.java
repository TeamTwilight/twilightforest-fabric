package twilightforest.entity.projectile;

import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Fabric-compatible replacement for NeoForge's IEntityWithComplexSpawn.
 * Allows entities to write custom spawn data to the network buffer.
 */
public interface IEntityWithComplexSpawn {

	void writeSpawnData(RegistryFriendlyByteBuf buffer);

	void readSpawnData(RegistryFriendlyByteBuf buf);
}