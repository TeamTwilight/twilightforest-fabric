package twilightforest.entity;

/**
 * 1:1 port of upstream {@code twilightforest.entity.IHostileMount} — marker interface
 * for entities that can forcefully pick up the player (e.g. Pinch Beetle, Mosquito
 * Swarm). Used by player-side mixins to disable normal dismount paths while the
 * player is forcibly carried.
 */
public interface IHostileMount {
}
