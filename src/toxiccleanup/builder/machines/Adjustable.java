package toxiccleanup.builder.machines;

/**
 * Indicates the implementing object has a key adjustable value, used for things such as power,
 * hp etc.
 *
 * <p>This interface is used for components that have a numeric value that
 * can be increased or decreased, such as:
 * <ul>
 *   <li>Power systems ({@link Machines}) – gain or spend power</li>
 *   <li>Player health ({@link toxiccleanup.builder.player.Harmable}) – take damage or heal</li>
 *   <li>Toxic fields ({@link toxiccleanup.builder.entities.tiles.ToxicField}) – reduce toxicity</li>
 * </ul>
 *
 * See also {@link Machines}, {@link toxiccleanup.builder.player.Harmable} for examples of intended use
 * in other interfaces.
 *
 * @provided
 */
public interface Adjustable {

    /**
     * Adjusts a key internal adjustable value by the given amount.
     * Each implementation may decide how it intends to use the amount for its adjustment.
     * @requires amount not null
     * @param amount amount we wish to adjust value by (positive is increase, negative decrease)
     */
    void adjust(int amount);
}
