package toxiccleanup.builder.weather;

import toxiccleanup.builder.Damage;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import java.util.List;

/** Handles queries about weather effects at specific positions
 * This class is responsible for answering questions about whether a
 * given tile position is obscured by clouds or affected by damaging weather.
 * This class is package-private (default visibility) and is only used
 * internally by {@link WeatherManager}. It is not part of the public API.
 */
public class WeatherQueryManager {
    private final List<GameEntity> phenomena;

    /** Constructs a new WeatherQueryManager
     * The manager maintains a reference to the active weather phenomena
     * list but does not own it. The list must be managed externally.
     * @param phenomena list of active weather phenomena to query
     */
    WeatherQueryManager(List<GameEntity> phenomena) {
        this.phenomena = phenomena;
    }

    /**
     * Return if the given title location should be currently obscured by the
     * internal weather system.
     *
     * @param dimensions - screen and tile dimensions
     * @param position   - position requesting for the obscured status of
     * @return if the given title location should be currently obscured by the
     * internal weather system.
     */
    public boolean isObscuring(Dimensions dimensions, Positionable position) {
        //work out the grid we are checking against
        int gridX = dimensions.pixelToTile(position.getX());
        int gridY = dimensions.pixelToTile(position.getY());

        for (GameEntity weather : phenomena) {
            final int weatherGridX = dimensions.pixelToTile(weather.getX());
            final int weatherGridY = dimensions.pixelToTile(weather.getY());

            if (gridX == weatherGridX && gridY == weatherGridY && weather instanceof Obscuring) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return if the given tile location is experiencing damaging conditions.
     *
     * @param dimensions - screen and tile dimensions
     * @param position   - position requesting for the damage status of
     * @return if the given tile location is experiencing damaging conditions.
     */
    public boolean isDamaging(Dimensions dimensions, Positionable position) {
        //work out the grid we are checking against
        int gridX = dimensions.pixelToTile(position.getX());
        int gridY = dimensions.pixelToTile(position.getY());

        for (GameEntity weather : phenomena) {
            final int weatherGridX = dimensions.pixelToTile(weather.getX());
            final int weatherGridY = dimensions.pixelToTile(weather.getY());

            if (gridX == weatherGridX && gridY == weatherGridY && weather instanceof Damaging) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return {@link Damage} the given tile location is currently experiencing otherwise
     * returns null.
     *
     * @param dimensions - screen and tile dimensions
     * @param position   - position requesting for the damage status of
     * @return {@link Damage} the given tile location is currently experiencing otherwise
     * returns null.
     */
    public Damage getDamage(Dimensions dimensions, Positionable position) {
        //work out the grid we are checking against
        int gridX = dimensions.pixelToTile(position.getX());
        int gridY = dimensions.pixelToTile(position.getY());
        for (GameEntity weather : phenomena) {
            final int weatherGridX = dimensions.pixelToTile(weather.getX());
            final int weatherGridY = dimensions.pixelToTile(weather.getY());

            if (gridX == weatherGridX && gridY == weatherGridY && weather instanceof Damaging) {
                return ((Damaging) weather).getDamage();
            }
        }
        return null;
    }
}
