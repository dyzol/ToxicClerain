package toxiccleanup.builder.weather;

import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.machines.LightningRod;
import toxiccleanup.engine.game.Positionable;
import java.util.List;

/**
 * Manages lightning attraction for lightning rods.
 * <p>This class is responsible for moving lightning bolts toward lightning rods
 * when they are within attraction radius. It processes all active lightning
 * bolts and attracts any that are within range of a given rod position.
 * This class is package-private (default visibility) and is only used
 * internally by {@link WeatherManager}. It is not part of the public API.
 *
 * @invariant phenomena is never null
 * @invariant all elements in phenomena are non-null
 */
public class LightningManager {
    private final List<GameEntity> phenomena;

    /**
     * Constructs a new LightningManager.
     *
     * <p>The manager maintains a reference to the active weather phenomena
     * list but does not own it. The list must be managed externally.
     *
     * @requires phenomena not null
     * @param phenomena list of active weather phenomena to check for attraction
     */
    LightningManager(List<GameEntity> phenomena) {
        this.phenomena = phenomena;
    }

    /** Attracts any attractable weather phenomena within radius of the lightning rod.
     * Receives the position of a {@link LightningRod} and adjusts the weather system accordingly.
     * Moves any {@link Lightning} that are within the radius {@value LightningRod#RADIUS}
     * of the given position to the given position.
     *
     * @requires position not null, and phenomena list not null (ensured by constructor)
     * @ensures any attractable weather within radius has its position changed to rod position
     * @param position - position of the lightning rod that the weather should be adjusted for.
     */
    public void attractLightning(Positionable position) {
        for (GameEntity weather : phenomena) {
            if (!(weather instanceof Attractable)) {
                continue; // skip non-attractable weather
            }
            int deltaX = position.getX() - weather.getX();
            int deltaY = position.getY() - weather.getY();
            final int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance <= LightningRod.RADIUS) {
                ((Attractable) weather).attractTo(position);
            }
        }
    }
}