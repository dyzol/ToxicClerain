package toxiccleanup.builder.weather;

import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.TickTimer;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.Tickable;

/**
 * A WeatherSpawnPoint is responsible for managing timed creation of preset weather Phenomena.
 * It takes a given {@link Positionable} position, everytime the given {@link TickTimer} finishes
 * it will call the given {@link Spawner} lambda
 */
public class WeatherSpawnPoint implements Tickable {

    private final TickTimer timer;
    private final Spawner spawner;
    private final Positionable position; // single position, not list

    /**
     * Constructs a new instance of {@link WeatherSpawnPoint}
     * using the given position, timer and spawner.
     *
     * @param position - position that will be passed to the {@link Spawner} when it fires.
     * @param timer    - the timer that will be used to determine when to call the {@link Spawner}.
     * @param spawner  - the {@link Spawner} we intend to call whenever the {@link TickTimer}
     *                 is finished.
     * @ensures getPosition() returns a copy of the given position
     * @requires position not null
     * @requires timer not null
     * @requires spawner not null
     */
    public WeatherSpawnPoint(Positionable position, TickTimer timer, Spawner spawner) {
        this.position = position;
        this.timer = timer;
        this.spawner = spawner;
    }

    /** Returns copy of stored position
     *
     * @ensures result.getX == this.position.getX and same for Y
     * @return the correctly stored position of the {@link WeatherSpawnPoint} in question.
     */
    public Positionable getPosition() {
        return new Position(position.getX(), position.getY());
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
        timer.tick();
        if (timer.isFinished()) {
            game.getWeather().addWeather(spawner.spawn(getPosition()));
        }
    }
}
