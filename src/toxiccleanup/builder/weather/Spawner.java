package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.builder.entities.GameEntity;

/**
 * Functional interface for creating game entities at specified positions.
 * Specifically, this defines a lambda that takes a {@link Positionable}
 * and returns a {@link GameEntity}.
 * Intended for use in systems that wish to regularly spawn various kinds of new {@link GameEntity}s
 * at given positions.
 */
@FunctionalInterface
public interface Spawner {
    /**
     * Creates a new game entity at the specified position.
     *
     * @requires position not null
     * @ensures returns non-null GameEntity at given position
     * @param position the position where the new entity should be created
     * @return a newly created {@link GameEntity} instance at the specified position
     */
    public GameEntity spawn(Positionable position);
}
