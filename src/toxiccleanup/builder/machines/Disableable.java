package toxiccleanup.builder.machines;

/**
 * Indicates the implementing object has a disabled and enabled state that can be changed.
 *
 * @provided
 */
public interface Disableable {
    /**
     * Returns whether this object is currently in a disabled state.
     *
     * @return true if disabled, false otherwise (enabled)
     */
    boolean isDisabled();

    /**
     * Sets the Disableable Object to disabled stated.
     * @ensures isDisabled returns true
     */
    void disable();

    /**
     * Sets the Disableable Object to enabled state.
     * @ensures isDisabled returns false
     */
    void enable();
}
