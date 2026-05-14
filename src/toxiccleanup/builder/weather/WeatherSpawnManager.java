package toxiccleanup.builder.weather;

import toxiccleanup.builder.GameState;
import toxiccleanup.engine.EngineState;
import java.util.ArrayList;
import java.util.List;

/** Manages all weather spawn points in the game.
 * Only responsible for:
 *      Storing all registered {@link WeatherSpawnPoint} instances
 *      Ticking each spawn point every frame to trigger weather creation
 *      Providing access to the collection of spawn points
 * This class is package-private (default visibility) and is only used
 * internally by {@link WeatherManager}. It is not part of the public API.
 */
public class WeatherSpawnManager {
    private final List<WeatherSpawnPoint> spawnPoints = new ArrayList<>();

    /** Adds a spawn point to the manager.
     * @requires spawnPoint not null
     * @ensures spawnPoint gets added to internal collection
     * @param spawnPoint the spawn point to add
     */
    void addSpawnPoint(WeatherSpawnPoint spawnPoint) {
        spawnPoints.add(spawnPoint);
    }

    /** Advances all spawn points by one tick
     *
     * @param state current engine state (mouse, keyboard, dimensions)
     * @param game current game state (world, weather, machines)
     * @requires state not null
     * @requires game not null
     * @ensures spawnPoints are ticked by one tick
     */
    void tick(EngineState state, GameState game) {
        for (WeatherSpawnPoint spawnPoint : spawnPoints) {
            spawnPoint.tick(state, game);
        }
    }

    /** Grabs a copy of list containing all spawn points
     * New list returned to prevent hackers from modifying internal collection
     * @return new list contining registered spawn points
     */
    List<WeatherSpawnPoint> getSpawnPoints() {
        return new ArrayList<>(spawnPoints);
    }
}
