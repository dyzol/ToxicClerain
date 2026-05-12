package toxiccleanup.builder.weather;

import toxiccleanup.builder.Damage;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import java.util.List;

public class WeatherQueryManager {
    private final List<GameEntity> phenomena;

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

    private boolean isAtPosition(GameEntity weather, Dimensions dimensions, int gridX, int gridY) {
        int weatherGridX = dimensions.pixelToTile(weather.getX());
        int weatherGridY = dimensions.pixelToTile(weather.getY());
        return gridX == weatherGridX && gridY == weatherGridY;
    }
}
