package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;

/**
 * Interface for weather phenomena that can be attracted by lightning rods.
 */
public interface Attractable {
    /**
     * Attracts this weather phenomenon to the given position.
     *
     * @param targetPosition the position to attract to
     */
    void attractTo(Positionable targetPosition);
}