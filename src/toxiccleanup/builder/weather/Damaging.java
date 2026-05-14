package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.builder.Damage;

/**
 * Indicates weather phenomenon can deal/generate 'damage'.
 * <p>Implementing this interface means the weather entity (e.g., {@link AcidCloud},
 * {@link Lightning}) is capable of damaging machines on the same tile.
 */
public interface Damaging {
    /**
     * Returns the damage at the specified position.
     * @param dimensions screen and tile dimensions
     * @param position position the position to check for damage
     * @return a {@link Damage} object if damage is present, null otherwise
     * @requires dimensions not null
     * @requires position not null
     */
    public Damage getDamage(Dimensions dimensions, Positionable position);

    /**
     * Returns the current damage from this weather phenomenon.
     *
     * <p>Implementations may apply additional conditions before returning damage.
     * For example, {@link Lightning} only returns damage during animation frames
     * 5 and 6 when this method is called.
     *
     * @return {@link Damage} object if damage is currently being dealt, null otherwise
     */
    public Damage getDamage();
}