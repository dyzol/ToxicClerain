package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.builder.Tickable;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.ui.RenderableGroup;

/**
 * Interface for managing weather phenomena that exist in this game.
 *
 * <p>The weather system is responsible for:
 * <ul>
 *   <li>Spawning weather phenomena at configured spawn points</li>
 *   <li>Managing active weather entities (clouds, lightning, etc.)</li>
 *   <li>Answering queries about weather effects at specific positions</li>
 *   <li>Handling lightning rod interactions</li>
 * </ul>
 *
 * <p><b>Class Invariant:</b> All implementations must maintain non-null
 * collections of spawn points and active weather phenomena.
 *
 * @provided
 */
public interface Weather extends Tickable, RenderableGroup, Damaging {

    /** Adds a spawn point to the weather system.
     *
     * <p>Spawn points are responsible for creating weather phenomena at
     * regular intervals. Once added, the spawn point will be ticked each
     * frame and may create new weather when its timer finishes.</p>
     *
     * @param spawnPoint the spawn point to add to weather system
     * @requires spawnPoint must not be null
     * @ensures spawnPoint is registered and will be ticked
     */
    void addSpawnPoint(WeatherSpawnPoint spawnPoint);

    /**
     * Adds an active weather phenomenon to the weather system.
     *
     * <p>Added weather entities will be ticked each frame, rendered,
     * and will affect the game world (e.g., obscuring solar panels,
     * damaging machines).
     *
     * @requires weather must not be null
     * @ensures weather is added to active phenomena
     *
     * @param weather the weather entity to add (e.g., Cloud, Lightning)
     * @throws NullPointerException if weather is null
     */
    void addWeather(GameEntity weather);

    /**
     * Returns whether the given tile position is obscured by weather.
     *
     * <p>A position is considered obscured if any weather phenomenon
     * that implements {@link Obscuring} occupies the same tile grid cell.
     * Obscured solar panels cannot generate power.
     *
     * @requires dimensions and position must not be null
     *
     * @param dimensions screen and tile dimensions for pixel-to-tile conversion
     * @param position the pixel position to check
     * @return true if the position is obscured by weather, false otherwise
     * @throws NullPointerException if dimensions or position is null
     */
    boolean isObscuring(Dimensions dimensions, Positionable position);

    /**
     * Returns whether the given tile position is experiencing damaging weather.
     *
     * <p>A position is damaging if any weather phenomenon that implements
     * {@link Damaging} occupies the same tile grid cell.
     *
     * @requires dimensions and position must not be null
     *
     * @param dimensions screen and tile dimensions for pixel-to-tile conversion
     * @param position the pixel position to check
     * @return true if damaging weather is present at the position, false otherwise
     * @throws NullPointerException if dimensions or position is null
     */
    boolean isDamaging(Dimensions dimensions, Positionable position);

    /**
     * Recieves the position of a lightning rod and adjusts the weather system accordingly.
     *
     * @requires position must not be null
     * @param position - position of the lightning rod that the weather should be adjusted for.
     * @throws NullPointerException if position is null
     */
    void applyLightningRod(Positionable position);
}