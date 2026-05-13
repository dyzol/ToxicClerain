package toxiccleanup.builder.world;

import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.engine.util.FileManager;
import toxiccleanup.builder.weather.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link WeatherBuilder} builds the weather system from a map file or string representation.
 * by parsing a weather map file containing symbols that represent spawn points,
 * creating {@link WeatherSpawnPoint} instances at the appropriate positions and
 * assembling a complete {@link Weather} system ready for gameplay
 *
 * <p>The game ends when either all toxic fields are cleared (win) or the player's HP reaches
 * zero (lose). Player HP is periodically reduced while any toxic fields remain.
 * @invariant static methods do not share state
 * @multistage
 */
public class WeatherBuilder {
    /**
     * Constructs a weather system from a string representation of a weather map.
     * The string must contain a square grid of characters. Each character is
     * interpreted as a symbol for a weather spawn point. The grid dimensions
     * must match the number of tiles calculated from the given dimensions.
     *
     * @requires dimensions not null
     * @requires text not null
     * @requires dimensions.windowSize() % dimensions.tileSize() == 0
     * @param dimensions the world dimensions (window size and tile size)
     * @param text the string representation of the weather map
     * @return a weather system with spawn points added
     * @throws WorldLoadException if the map has incorrect dimensions or contains invalid symbols
     */
    public static Weather fromString(Dimensions dimensions, String text)
            throws WorldLoadException {
        final Weather weather = new WeatherManager();
        int numberOfTiles = dimensions.windowSize() / dimensions.tileSize();
        String[] lines = text.split("\n");

        if (lines.length != numberOfTiles) {
            throw new WorldLoadException("Expected " + numberOfTiles
                    + " lines to match the given dimensions but got " + lines.length);
        }

        final List<WeatherSpawnPoint> spawnPoints = new ArrayList<>();
        for (int row = 0; row < numberOfTiles; row++) {
            char[] currentRow = lines[row].strip().toCharArray();

            if (currentRow.length != numberOfTiles) {
                throw new WorldLoadException("Expected " + numberOfTiles
                        + " characters to match the given dimensions but got "
                        + currentRow.length, row);
            }

            for (int col = 0; col < numberOfTiles; col++) {
                final int tileX = dimensions.tileToPixel(col);
                final int tileY = dimensions.tileToPixel(row);
                char symbol = currentRow[col];
                final Position position = new Position(tileX, tileY);
                WeatherSpawnPoint spawner;
                try {
                    spawner = SpawnerFactory.fromSymbol(position, symbol);
                } catch (IllegalArgumentException e) {
                    throw new WorldLoadException("Unknown symbol: '" + symbol + "'", row, col);
                }
                if (spawner != null) {
                    spawnPoints.add(spawner);
                }
            }
        }
        for (WeatherSpawnPoint spawnPoint : spawnPoints) {
            weather.addSpawnPoint(spawnPoint);

        }
        return weather;
    }

    /**
     * Takes the given dimensions and locations of a file and attempts to process said
     * file for {@link WeatherSpawnPoint} locations for our various weather Phenomena including:
     * <ul>
     *  <li> {@link Cloud} </li>
     *  <li> {@link AcidCloud} </li>
     *  <li> {@link RainCloud} </li>
     *  <li> {@link Lightning} </li>
     * </ul>
     *
     * @requires dimensions not null
     * @requires filepath not null or empty
     * @param dimensions - world dimensions, used for placing of spawners in relation to
     *                   said dimensions.
     * @param filepath   - path to a file holding the desired x,y locations and type for placing our
     *                   various {@link WeatherSpawnPoint}s
     * @return our newly Weather, with {@link WeatherSpawnPoint} added and ready for use in the
     * game.
     * @throws IOException if the file cannot be read or does not exist
     * @throws WorldLoadException if the map format is invalid
     */
    public static Weather fromFile(Dimensions dimensions, String filepath)
            throws IOException, WorldLoadException {
        final String text = new FileManager().readFile(filepath);
        return fromString(dimensions, text);
    }
}