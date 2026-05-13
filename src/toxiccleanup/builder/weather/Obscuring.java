package toxiccleanup.builder.weather;

/**
 * Marker interface (no methods) for weather phenomena that can obscure tiles.
 * Weather phenomena that implement this interface can block or reduce
 * sunlight reaching solar panels on the same tile. When a tile is obscured,
 * solar panels on that tile cannot generate power.
 * Purpose of this interface is to allow WeatherManager to identify which
 * weather phenomena affect visibility.
 */
public interface Obscuring { }
