package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.builder.SpriteGallery;

/**
 * <p> A {@link Cloud} is a weather phenomena that will move to the left, over time. </p>
 * <p> It obscures {@link toxiccleanup.builder.machines.SolarPanel}s that
 * it is sharing a tile with. </p>
 * <p> When it reaches the leftmost edge of the screen, it will mark itself for removal. </p>
 *
 * <p> Rendered using {@link SpriteGallery#cloud}. </p>
 *
 * @provided
 */
public class Cloud extends Cloudable {
    public static final int SPAWN_TIME = 300;

    /**
     * Constructs a new Cloud at the given position.
     * @requires position not null
     * @param position the position to place the cloud at
     */
    public Cloud(Positionable position) {
        super(position, SpriteGallery.cloud);
    }
}
