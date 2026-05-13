package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.builder.Damage;

/** Represents damage caused by lightning strikes.
 *
 * <p>This damage type has a type identifier of {@value #TYPE}. Other classes
 * may use this identifier to distinguish lightning damage from other damage types.
 */
public class LightningDamage extends Damage {

    public static final String TYPE = "lightning";

    /**
     * Constructs a new LightningDamage at the given position.
     *
     * <p>The damage type is automatically set to "lightning" to allow
     * lightning rods to identify and ignore this damage type.
     *
     * @param position the position where the lightning damage occurs
     * @requires position not null
     * @ensures getType().equals(TYPE) and getX() == position.getX(), getY() == position.getY()
     */
    public LightningDamage(Positionable position) {
        super(position);
        this.setType(TYPE);
    }
}
