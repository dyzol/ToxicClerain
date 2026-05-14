package toxiccleanup.builder.machines;

import toxiccleanup.builder.Damage;

/**
 * Indicates the implementing object has a damaged and undamaged state that can be changed.
 *
 * @invariant damaged state is a boolean (true for damaged, false undamaged)
 * @provided
 */
public interface Damageable {

    /**
     * Returns whether this object is currently in a damaged state.
     *
     * @return true if damaged, false if undamaged (otherwise)
     */
    boolean isDamaged();

    /**
     * Sets this object to a damaged state.
     * @requires dmg not null
     * @param dmg the damage object describing what caused the damage
     */
    void setDamage(Damage dmg);

    /**
     * Repairs this object, returning it to an undamaged state.
     * @ensures isDamaged() returns false
     */
    void repairDamage();
}
