package toxiccleanup.builder.weather;

import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.machines.LightningRod;
import toxiccleanup.engine.game.Positionable;
import java.util.List;

public class LightningManager {
    private final List<GameEntity> phenomena;

    LightningManager(List<GameEntity> phenomena) {
        this.phenomena = phenomena;
    }

    /**
     * Receives the position of a {@link LightningRod} and adjusts the weather system accordingly.
     * Moves any {@link Lightning} that are within the radius {@value LightningRod#RADIUS}
     * of the given position to the given position.
     *
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