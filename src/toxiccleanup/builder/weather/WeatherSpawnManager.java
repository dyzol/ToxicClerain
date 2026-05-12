package toxiccleanup.builder.weather;

import toxiccleanup.builder.GameState;
import toxiccleanup.engine.EngineState;
import java.util.ArrayList;
import java.util.List;

public class WeatherSpawnManager {
    private final List<WeatherSpawnPoint> spawnPoints = new ArrayList<>();

    void addSpawnPoint(WeatherSpawnPoint spawnPoint) {
        spawnPoints.add(spawnPoint);
    }

    void tick(EngineState state, GameState game) {
        for (WeatherSpawnPoint spawnPoint : spawnPoints) {
            spawnPoint.tick(state, game);
        }
    }

    List<WeatherSpawnPoint> getSpawnPoints() {
        return new ArrayList<>(spawnPoints);
    }
}
