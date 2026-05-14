package toxiccleanup.builder.machines;

import toxiccleanup.builder.Damage;

/**
 * Class for managing a damaged state, currently wraps a basic boolean
 * but other implementations could wrap around a more sophisticated health systems in the future.
 *
 * @provided
 */
public class DamageHandler implements Damageable {
    private boolean damaged = false;

    /**
     * Constructs a new DamageHandler in an undamaged state.
     */
    public DamageHandler() {
        // empty so that damaged starts as false
    }

    /**
     * Returns whether this handler is in a damaged state.
     *
     * @return true if damaged, false if undamaged
     */
    @Override
    public boolean isDamaged() {
        return this.damaged;
    }

    /**
     * Sets the Damageable Object to it's damaged state.
     * @ensures isDamaged returns true
     * @requires dmg not null
     * @param dmg the damage object (ignored by this implementation)
     */
    @Override
    public void setDamage(Damage dmg) {
        this.damaged = true;
    }

    /**
     * Sets the Damageable Object to it's undamaged
     * @ensures isDamaged returns false
     */
    @Override
    public void repairDamage() {
        this.damaged = false;
    }
}
