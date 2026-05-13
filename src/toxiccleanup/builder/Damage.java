package toxiccleanup.builder;

import toxiccleanup.engine.game.Positionable;

/**
 * Represents damage that can be applied to machines and other game entities.
 * A Damage object contains an x-y position and type identifier. The type
 * allows different damage sources to be distinguished (eg. lightning vs acid damage)
 * @invariant x and y always positive and (including 0)
 * @invariant type never null
 */
public class Damage implements Positionable {
    private int damageX;
    private int damageY;
    public static final String UNTYPED = "untyped";
    private String type = UNTYPED;

    /**
     * Constructs a new Damage object at the given position.
     * The damage type is initially set to {@value #UNTYPED}. Subclasses
     * or callers may change it via {@link #setType(String)}.
     * @requires position not null
     * @param position the position where the damage occurs
     */
    public Damage(Positionable position) {
        this.damageX = position.getX();
        this.damageY = position.getY();
    }

    /**
     * Sets the type of this damage.
     * Damage types are used to identify the source of damage
     * so that certain machines can be immune to specific damage types.
     *
     * @param value the damage type identifier
     */
    public void setType(String value) {
        this.type = value;
    }

    /** Returns the type of this damage.
     * If not explicitly set, the type defaults to {@value #UNTYPED}.
     * @return the damage type identifier
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the horizontal (x-axis) coordinate of the component.
     *
     * @return The horizontal (x-axis) coordinate.
     * @ensures \result >= 0
     */
    @Override
    public int getX() {
        return damageX;
    }

    /**
     * Returns the vertical (y-axis) coordinate of the component.
     *
     * @return The vertical (y-axis) coordinate.
     * @ensures \result >= 0
     */
    @Override
    public int getY() {
        return damageY;
    }

    /**
     * Set the horizontal (x-axis) coordinate of the component.
     *
     * @param newDamageX The new horizontal coordinate for this component.
     * @requires damageX >= 0
     * @ensures getX() == damageX
     */
    @Override
    public void setX(int newDamageX) {
        this.damageX = newDamageX;
    }

    /**
     * Set the vertical (y-axis) coordinate of the component.
     *
     * @param newDamageY the new vertical coordinate for this component.
     * @requires damageY >= 0
     * @ensures getY() == damageY
     */
    @Override
    public void setY(int newDamageY) {
        this.damageY = newDamageY;
    }
}
