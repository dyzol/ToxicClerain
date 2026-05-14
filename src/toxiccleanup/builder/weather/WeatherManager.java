package toxiccleanup.builder.weather;

import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.engine.renderer.Renderable;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.machines.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The concrete implementation of {@link Weather} for the
 * {@link toxiccleanup.builder.ToxicCleanup} game. {@link WeatherManager} is responsible for:
 *
 * <ul>
 *   <li>Holding all {@link WeatherSpawnPoint}s for the game.</li>
 *   <li>Holding all Weather Phenomena {@link GameEntity}s for the game.</li>
 *   <li>Handling the interaction between {@link LightningRod} and {@link Lightning}.</li>
 *   <li>
 *       Answering requests about the overall state of the weather system by other systems
 *       <p>i.e</p>
 *       <ul>
 *           <li>> is a particular location obscured.</li>
 *           <li>> is a particular location currently receiving damage from weather phenomena.</li>
 *       </ul>
 *   </li>
 *   <li>Ticking forward the internal state of all {@link WeatherSpawnPoint}s. </li>
 *   <li>Ticking forward the internal state of all weather {@link GameEntity}s. </li>
 * </ul>
 */
public class WeatherManager implements Weather {
    private final List<GameEntity> phenomena = new ArrayList<>();
    private final WeatherSpawnManager spawnManager = new WeatherSpawnManager();
    private final WeatherQueryManager queryManager;
    private final LightningManager lightningManager;

    /** Constructor that initialises submanagers: queryManager and lightningManager
     * @ensures queryManager and lightningManager exist
     */
    public WeatherManager() {
        this.queryManager = new WeatherQueryManager(phenomena);
        this.lightningManager = new LightningManager(phenomena);
    }

    /**
     * Add the given spawnPoint to the weather manager for it to handle ticking it and
     * any other game logic.
     * @requires spawnPoint not null
     * @param spawnPoint - spawn point we wish top use
     */
    public void addSpawnPoint(WeatherSpawnPoint spawnPoint) {
        spawnManager.addSpawnPoint(spawnPoint);
    }

    /**
     * Adds a GameEntity to be managed by the WeatherManager.
     *
     * @param weather - GameEntity instance of a weather Phenomenon.
     */
    public void addWeather(GameEntity weather) {
        phenomena.add(weather);
    }

    /**
     * Return if the given title location should be currently obscured by the
     * internal weather system.
     *
     * @param dimensions - screen and tile dimensions
     * @param position   - position requesting for the obscured status of
     * @return if the given title location should be currently obscured by the
     * internal weather system.
     * @requires dimensions not null
     * @requires position not null
     */
    @Override
    public boolean isObscuring(Dimensions dimensions, Positionable position) {
        return queryManager.isObscuring(dimensions, position);
    }

    /**
     * Return {@link Damage} the given tile location is currently experiencing otherwise
     * returns null.
     *
     * @param dimensions - screen and tile dimensions
     * @param position   - position requesting for the damage status of
     * @return {@link Damage} the given tile location is currently experiencing otherwise
     * returns null.
     * @requires dimensions not null
     * @requires position not null
     */
    public Damage getDamage(Dimensions dimensions, Positionable position) {
        return queryManager.getDamage(dimensions, position);
    }

    /**
     * required by Damaging interface, kept as-is for compatibility with tests
     * @return null
     */
    @Override
    public Damage getDamage() {
        return null;
    }

    /**
     * Return if the given tile location is experiencing damaging conditions.
     *
     * @param dimensions - screen and tile dimensions
     * @param position   - position requesting for the damage status of
     * @return if the given tile location is experiencing damaging conditions.
     * @requires dimensions not null
     * @requires position not null
     */
    @Override
    public boolean isDamaging(Dimensions dimensions, Positionable position) {
        return queryManager.isDamaging(dimensions, position);
    }

    /**
     * Receives the position of a {@link LightningRod} and adjusts the weather system accordingly.
     * Moves any {@link Lightning} that are within the radius {@value LightningRod#RADIUS}
     * of the given position to the given position.
     * @requires position not null
     * @param position - position of the lightning rod that the weather should be adjusted for.
     */
    @Override
    public void applyLightningRod(Positionable position) {
        lightningManager.attractLightning(position);
    }

    /**
     * Advances component state by one game tick using engine and game context.
     *
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension. Useful for processing keyboard presses or mouse movement.
     * @param game  The state of the game, including the player and world. Can be used to query or
     *              update the game state.
     * @requires state not null
     * @requires game not null
     */
    @Override
    public void tick(EngineState state, GameState game) {
        spawnManager.tick(state, game);
        for (GameEntity weather : phenomena) {
            weather.tick(state, game);
        }
        cleanup();
    }

    /**
     * A collection of renderables that should each be displayed.
     *
     * @return A collection of renderables to display.
     */
    @Override
    public List<Renderable> render() {
        return new ArrayList<>(phenomena);
    }

    /**
     * Cleanup and removes any weather phenomena that have been marked for removal.
     */
    private void cleanup() {
        for (int i = phenomena.size() - 1; i >= 0; i -= 1) {
            if (phenomena.get(i).isMarkedForRemoval()) {
                phenomena.remove(i);
            }
        }
    }

    /**
     * Generates a simplified {@link String} representation of the WeatherManagers internal state.
     *
     * @return a simplified {@link String} representation of the WeatherManagers internal state.
     */
    @Override
    public String toString() {
        return "WeatherManager:[\n" + "Phenomena:" + phenomena.size() + "\n" + "SpawnPoints:"
                + spawnManager.getSpawnPoints().size() + "\n" + "]\n";
    }

}
