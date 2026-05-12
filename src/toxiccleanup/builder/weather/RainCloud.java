package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.TickTimer;
import toxiccleanup.builder.SpriteGallery;


/**
 * <p> A {@link RainCloud} is a weather phenomena that will move to the left, over time. </p>
 * <p> It obscures {@link toxiccleanup.builder.machines.SolarPanel}s that
 * it is sharing a tile with. </p>
 * <p> Plays an animation loop endlessly using sprites from {@link SpriteGallery#raincloud}. </p>
 * <p> When it reaches the leftmost edge of the screen, it will mark itself for removal. </p>
 *
 * <p> Rendered using {@link SpriteGallery#raincloud}. </p>
 *
 */
public class RainCloud extends Cloudable {
    public static final int SPAWN_TIME = 300;
    /**
     * Constructs a new RainCloud at the given position.
     *
     * @param position the position to place the rain cloud at
     */
    public RainCloud(Positionable position) {
        super(position, SpriteGallery.raincloud);
    }

    /**
     * Constructs a new RainCloud with custom movement timer (for testing).
     *
     * @param position the position to place the rain cloud at
     * @param movementTimer custom movement timer
     */
    public RainCloud(Positionable position, TickTimer movementTimer) {
        super(position, SpriteGallery.raincloud, movementTimer);
    }
}
