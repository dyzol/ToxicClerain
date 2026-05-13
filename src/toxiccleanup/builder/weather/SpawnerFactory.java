package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.RepeatingTimer;

/**
 * <p> Creates {@link WeatherSpawnPoint}s at a given position
 * constructed varies based on the symbol given.</p>
 * <ul>
 * <li>c and C are for clouds</li>
 * <li>a and A are for acidclouds</li>
 * <li>r and R are for rainclouds</li>
 * <li>l and L are for lightning</li>
 * </ul>
 * <p>
 * The use of lower and upper case changes the SPAWN_TIME fed to its RepeatingTimer
 * Specifically, lowercase letters use the static SPAWN_TIME
 * as the duration to give a {@link RepeatingTimer}
 * </p>
 * <p>
 *  Whereas uppercase letters use the static SPAWN_TIME * 5.5
 *  as the duration to give a {@link RepeatingTimer}
 * </p>
 */
public class SpawnerFactory {

    private static final double SPAWN_MULTIPLIER = 5.5;

    /**
     * Constructs a new {@link WeatherSpawnPoint} based on the symbol at the given position.
     *
     * @param position the position to place the spawn point at
     * @param symbol   the map symbol (c, C, r, R, a, A, l, L, or _)
     * @return a new WeatherSpawnPoint, or null if symbol is '_'
     * @throws IllegalArgumentException if symbol is not recognized
     */
    public static WeatherSpawnPoint fromSymbol(Positionable position, char symbol) {
        if (symbol == '_') {
            return null;
        }

        // Determine if uppercase (slower spawn rate)
        boolean isUppercase = Character.isUpperCase(symbol);
        char lowerSymbol = Character.toLowerCase(symbol);

        // Base spawn time and spawner (for normal rate)
        int baseSpawnTime;
        Spawner spawner = switch (lowerSymbol) {
            case 'c' -> {
                baseSpawnTime = Cloud.SPAWN_TIME;
                yield Cloud::new;
            }
            case 'r' -> {
                baseSpawnTime = RainCloud.SPAWN_TIME;
                yield RainCloud::new;
            }
            case 'a' -> {
                baseSpawnTime = AcidCloud.SPAWN_TIME;
                yield AcidCloud::new;
            }
            case 'l' -> {
                baseSpawnTime = Lightning.SPAWN_TIME;
                yield Lightning::new;
            }
            default -> throw new IllegalArgumentException("Unknown symbol: '" + symbol + "'");
        };

        // Multiply spawn time if uppercase
        int finalSpawnTime = isUppercase
                ? (int) (baseSpawnTime * SPAWN_MULTIPLIER)
                : baseSpawnTime;

        return new WeatherSpawnPoint(position, new RepeatingTimer(finalSpawnTime), spawner);
    }
}
