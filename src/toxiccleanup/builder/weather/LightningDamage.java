package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.builder.Damage;

/**
 * Lightning Type Damage, can not deal damage to lightning rods specifically.
 */
public class LightningDamage extends Damage {

    public static final String TYPE = "lightning";

    /**
     * Constructs a new LightningDamage at the given position.
     *
     * @param position the position where the lightning damage occurs
     * @requires position != null
     * @ensures getType().equals(TYPE) and getX() == position.getX(), getY() == position.getY()
     */
    public LightningDamage(Positionable position) {
        super(position);
        this.setType(TYPE);
    }
}
